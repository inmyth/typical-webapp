package mbcu.me.config

import java.util.concurrent.ForkJoinPool

import mbcu.me.repos.UserRepository

import scala.concurrent.ExecutionContext

object Config {

  final case class ExecutorsConfig(parallelismConfig: ExecutorsConfig.ParallelismConfig)

  object ExecutorsConfig {

    final case class ParallelismConfig(parallellism: Int) {
      val serviceExecutor: ExecutionContext = ExecutionContext.fromExecutor(new ForkJoinPool(parallellism))
    }

  }

  final case class ServiceConfig(uncertaintyConfig: ServiceConfig.UncertaintyConfig)

  object ServiceConfig {

    final case class UncertaintyConfig(failureProbability: Double, timeoutProbability: Double)

  }

  object Repositories {}

  final case class Repositories(val userRepo: UserRepository = UserRepository.inMemory())
}
