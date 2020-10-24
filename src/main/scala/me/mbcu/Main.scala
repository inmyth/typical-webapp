package me.mbcu

import me.mbcu.config.Config.ExecutorsConfig.ComputationScheduler
import me.mbcu.config.Config._

object Main extends App {

  val config = Config(EnvConfig(failureProbability = 0.5), ExecutorsConfig(ComputationScheduler(1)), Services.UNSTABLE)
  val app    = Application.fromConfig.run(config)

}
