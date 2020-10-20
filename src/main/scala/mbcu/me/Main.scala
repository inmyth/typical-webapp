package mbcu.me

import mbcu.me.config.Config.ExecutorsConfig.ComputationScheduler
import mbcu.me.config.Config.Services.ServiceMode
import mbcu.me.config.Config._
import pureconfig.ConfigSource

object Main extends App {

  val config = Config(EnvConfig(failureProbability = 0.5), ExecutorsConfig(ComputationScheduler(1)), Services.UNSTABLE)
  val app    = Application.fromConfig.run(config)
  import pureconfig.generic.auto._
  val x = ConfigSource.default.load[ServiceMode]
  print(x)

}
