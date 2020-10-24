package me.mbcu.config

import cats.data.Reader
import me.mbcu.config.Config.Services.{DEFAULT, ServiceMode, UNSTABLE}
import me.mbcu.domain.services.{UserManagement, UserRepository}
import monix.eval.Task
import monix.execution.ExecutionModel.AlwaysAsyncExecution
import monix.execution.Scheduler

object Config {

  final case class Config(envConfig: EnvConfig, executorsConfig: ExecutorsConfig, serviceMode: ServiceMode)

  final case class ExecutorsConfig(computationScheduler: ExecutorsConfig.ComputationScheduler)

  object ExecutorsConfig {

    final case class ComputationScheduler(parallellism: Int) {
      val ec: Scheduler =
        Scheduler.computation(parallelism = parallellism, executionModel = AlwaysAsyncExecution)
    }

    lazy val ecIO: Scheduler =
      Scheduler.io(name = "my-io-java-backed-cached-pool", executionModel = AlwaysAsyncExecution)

  }

  final case class EnvConfig(failureProbability: Double)

  object Repositories {
    val fromConfig: Reader[Config, Repositories] = Reader(p => Repositories(p.executorsConfig))
  }

  final case class Repositories(executorsConfig: ExecutorsConfig) {
    val userRepo: UserRepository = UserRepository.inMemory(executorsConfig.computationScheduler.ec)
  }

  object Services {
    val configReader: Reader[Config, Config] = Reader(p => p)

    def fromConfig: Reader[Config, Services] =
      for {
        repo   <- Repositories.fromConfig
        config <- configReader
      } yield Services(config, repo)

    sealed trait ServiceMode
    case object DEFAULT  extends ServiceMode
    case object UNSTABLE extends ServiceMode
  }

  final case class Services(config: Config, repositories: Repositories) {
    private val ecComp: Scheduler = config.executorsConfig.computationScheduler.ec

    val userManagement: UserManagement[Task] = config.serviceMode match {
      case DEFAULT => UserManagement.default(repositories.userRepo)(ecComp)
      case UNSTABLE =>
        UserManagement.unstable(
          UserManagement.default(repositories.userRepo)(ecComp),
          config.envConfig.failureProbability
        )(ecComp)
    }

  }

  object Application {

    val reader: Reader[Services, Application] = Reader(Application.apply)

    val fromConfig: Reader[Config, Application] = Services.fromConfig andThen reader

  }

  final case class Application(services: Services)

}
