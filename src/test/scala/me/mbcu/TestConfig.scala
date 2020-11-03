package me.mbcu

import me.mbcu.config.Config.Config

trait TestConfig {

  import pureconfig._
  import pureconfig.generic.auto._
  import me.mbcu.config.ConfUtils._

  val testConf =
    """
      |env-config {
      |  repo-mode {
      |      type : "in-mem"
      |  }
      |  frontend-port : 8080
      |  backend-port : 9000
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
      |        iam-test-target-key : "thisisiamtestfiletocreateanddelete.txt"
      |        iam-test-file: "README.md"
      |    }
      |    dynamo-config {
      |        region : "us-east-1"
      |        table-name : "proto-lambda-scalajs"
      |        iam-test-key : "thisisiamtestitem"
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
