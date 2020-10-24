package me.mbcu

import java.io.File

import pureconfig._
import pureconfig.generic.auto._

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import me.mbcu.config.Config.ExecutorsConfig.ComputationScheduler
import me.mbcu.config.ConfUtils._
import me.mbcu.config.Config._

object Main extends App {

  val config = ConfigSource.default.load[Config]

  config match {
    case Right(value) => {
      val x = Repositories.fromConfig(value)

//      val z =
//        x.certivStorage.put(S3Path(Some(Seq("aaa")), "b.txt"), new File("README.md")).runToFuture(ExecutorsConfig.ecIO)
      val z = x.certivStorage
        .putDeleteIAMTestFile(value.repositoryConfig.s3Config.iamTestFilePath)
        .runToFuture(ExecutorsConfig.ecIO)
      Await.result(z, 3.seconds)
//      x.certivStorage.Application.fromConfig.run(value)

      print("aaaaa")
    }
    case Left(value) => println(value)
  }
}
