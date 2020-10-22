package mbcu.me

import mbcu.me.config.Config.Config
import mbcu.me.config.Config.RepositoryConfig.S3Config

object Main extends App {
  //  val config = Config(EnvConfig(failureProbability = 0.5), ExecutorsConfig(ComputationScheduler(1)), Services.UNSTABLE)
//  val app    = Application.fromConfig.run(config)

  import mbcu.me.config.ConfUtils._
  import pureconfig._
  import pureconfig.generic.auto._

  val x = ConfigSource.default.load[Config]

  println(x)

}
/*

env-config {
  repo-mode {
          type : "in-mem"

  }
}
executors-config {
    computation-scheduler {
        parallellism : 1
    }
}
repository-config {
    s3-config {
        region : "us-east-1"
        bucket : "dev-certiv"
    }
    dynamo-config {
        region : "us-east-1"
    }
    sqlconfig {
        region : "us-east-1"
    }
    in-mem-config{
        failure-probability : 0.5
    }
}
 */
