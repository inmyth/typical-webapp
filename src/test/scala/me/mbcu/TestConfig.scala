package me.mbcu

import mbcu.me.config.Config.Config

trait TestConfig {

  import pureconfig._
  import pureconfig.generic.auto._
  import mbcu.me.config.ConfUtils._

  val testConf =
    """
      |env-config {
      |  repo-mode {
      |      type : "in-mem"
      |  }
      |}
      |
      |executors-config {
      |    computation-scheduler {
      |        parallellism : 1
      |    }
      |}
      |
      |repository-config {
      |    s-3-config {
      |        region : "us-east-1"
      |        bucket : "dev-certiv"
      |    }
      |    dynamo-config {
      |        region : "us-east-1"
      |    }
      |    sql-config {
      |        region : "us-east-1"
      |    }
      |    in-mem-config{
      |        failure-probability : 0.5
      |    }
      |}
      |""".stripMargin

  val config = ConfigSource.string(testConf).loadOrThrow[Config]

}