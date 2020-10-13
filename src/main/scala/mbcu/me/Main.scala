package mbcu.me

import mbcu.me.config.Config.ExecutorsConfig.ComputationScheduler
import mbcu.me.config.Config._

object Main extends App {

  val config = Config(EnvConfig(failureProbability = 0.5), ExecutorsConfig(ComputationScheduler(1)), Services.UNSTABLE)
  val app    = Application.fromConfig.run(config)

}
