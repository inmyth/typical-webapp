package me.mbcu.config

import awscala.Region
import awscala.dynamodbv2.DynamoDB
import awscala.s3.{Bucket, S3}
import cats.data.Reader
import me.mbcu.domain.services.certivmanagement.FileRepo.S3Path
import me.mbcu.config.Config.EnvConfig.{InMem, Real, RepoMode}
import me.mbcu.config.Config.RepositoryConfig.{DynamoConfig, InMemConfig, S3Config, SQLConfig}
import me.mbcu.domain.services.certivmanagement.FileRepo.S3Path
import me.mbcu.domain.services.{CertivDynamoRepository, CertivFileRepository, CertivManagement}
import me.mbcu.domain.shared.Done
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

    final case class S3Config(region: Region, bucket: Bucket, iamTestTargetKey: String, iamTestFile: java.io.File) {
      val iamTestFilePath = S3Path(None, iamTestTargetKey)
    }

    final case class DynamoConfig(region: Region, tableName: String, iamTestKey: String)

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

    val certivDynamo: CertivDynamoRepository = {
      val dynamoConfig = config.repositoryConfig.dynamoConfig
      config.envConfig.repoMode match {
        case InMem =>
          val ec = config.executorsConfig.computationScheduler.ec
          CertivDynamoRepository.inMem(ec, dynamoConfig)
        case Real =>
          val ddb = DynamoDB.at(dynamoConfig.region)
          val ec  = ExecutorsConfig.ecIO
          CertivDynamoRepository.ddb(ec, ddb, dynamoConfig)
      }
    }

    val certivStorage: CertivFileRepository = {
      val ec     = ExecutorsConfig.ecIO
      val region = config.repositoryConfig.s3Config.region
      val s3     = S3.at(region)
      CertivFileRepository.s3(ec, s3, config.repositoryConfig.s3Config)
    }

    def testIAMPermissionAndAccess(): Task[Done] =
      for {
        _ <- certivDynamo.testAccessAndIAMPermission()
        _ <- certivStorage.testAccessAndIAMPermission()
      } yield Done

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