package mbcu.me.config

import awscala.Region
import awscala.s3.{Bucket, S3}
import cats.data.Reader
import mbcu.me.config.Config.EnvConfig.RepoMode
import mbcu.me.config.Config.RepositoryConfig.{DynamoConfig, InMemConfig, S3Config, SQLConfig}
import mbcu.me.domain.services.certivmanagement.FileRepo.S3Path
import mbcu.me.domain.services.{CertivDynamoRepository, CertivFileRepository, CertivManagement}
import monix.eval.Task
import monix.execution.ExecutionModel.AlwaysAsyncExecution
import monix.execution.Scheduler

object Config {

  final case class ExecutorsConfig(computationScheduler: ExecutorsConfig.ComputationScheduler)

  object ExecutorsConfig {

    final case class ComputationScheduler(parallellism: Int) {
      val ec: Scheduler =
        Scheduler.computation(parallelism = parallellism, executionModel = AlwaysAsyncExecution)
    }

    lazy val ecIO: Scheduler =
      Scheduler.io(name = "my-io-java-backed-cached-pool", executionModel = AlwaysAsyncExecution)

  }

  object EnvConfig {

    sealed trait RepoMode
    final object InMem extends RepoMode
    final object Real  extends RepoMode
  }

  final case class EnvConfig(repoMode: RepoMode)

  object RepositoryConfig {

    final case class S3Config(region: Region, bucket: Bucket, iamTestFileName: String) {
      val iamTestFilePath = S3Path(None, iamTestFileName)
    }

    final case class DynamoConfig(region: Region)

    final case class SQLConfig(region: Region)

    final case class InMemConfig(failureProbability: Double)
  }

  final case class RepositoryConfig(
    s3Config: S3Config,
    dynamoConfig: DynamoConfig,
    sqlConfig: SQLConfig,
    inMemConfig: InMemConfig
  )

  final case class Config(envConfig: EnvConfig, executorsConfig: ExecutorsConfig, repositoryConfig: RepositoryConfig)

  object Repositories {
    val fromConfig: Reader[Config, Repositories] = Reader(Repositories(_))
  }

  final case class Repositories(config: Config) {

    val certivDynamo: CertivDynamoRepository =
      CertivDynamoRepository.inMem(config.executorsConfig.computationScheduler.ec)

    val certivStorage: CertivFileRepository = {
      val ec     = ExecutorsConfig.ecIO
      val region = config.repositoryConfig.s3Config.region
      val s3     = S3.at(region)
      CertivFileRepository.s3(ec, s3, config.repositoryConfig.s3Config)
    }

  }

  object Services {
    val configReader: Reader[Config, Config] = Reader(p => p)

    def fromConfig: Reader[Config, Services] =
      for {
        config <- configReader
        repo   <- Repositories.fromConfig
      } yield Services(config, repo)
  }

  final case class Services(config: Config, repositories: Repositories) {
    private val ecComp: Scheduler = config.executorsConfig.computationScheduler.ec

    val certivManagement: CertivManagement[Task] =
      CertivManagement.default(repositories.certivDynamo, repositories.certivStorage)(ecComp)
  }

  object Application {

    val reader: Reader[Services, Application] = Reader(Application.apply)

    val fromConfig: Reader[Config, Application] = Services.fromConfig andThen reader

  }

  final case class Application(services: Services)

}
