package mbcu.me

import java.io.File

import mbcu.me.config.Config.{Config, ExecutorsConfig, Repositories}
import mbcu.me.domain.services.certivmanagement.FileRepo.S3Path
import pureconfig._
import pureconfig.generic.auto._
import mbcu.me.config.ConfUtils._
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object Main extends App {

  val config = ConfigSource.default.load[Config]

  config match {
    case Right(value) => {
      val x = Repositories.fromConfig(value)

//      val z = x.certivStorage.put(S3Path("aaa", "b.txt"), new File("README.md")).runToFuture(ExecutorsConfig.ecIO)
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
