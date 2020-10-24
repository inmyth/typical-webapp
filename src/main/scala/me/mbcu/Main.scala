package me.mbcu

import java.io.File

import awscala.Region
import awscala.dynamodbv2.{DynamoDB, Table}
import com.amazonaws.services.dynamodbv2.model.{AttributeValue, DeleteItemRequest}
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
//      val x = Repositories.fromConfig(value)
//
////      val z =
////        x.certivStorage.put(S3Path(Some(Seq("aaa")), "b.txt"), new File("README.md")).runToFuture(ExecutorsConfig.ecIO)
//      val z = x.certivStorage
//        .putDeleteIAMTestFile(value.repositoryConfig.s3Config.iamTestFilePath)
//        .runToFuture(ExecutorsConfig.ecIO)
//      Await.result(z, 3.seconds)
////      x.certivStorage.Application.fromConfig.run(value)
//
//      print("aaaaa")

      // DunamoDb
      implicit val ddb = DynamoDB.at(Region.US_EAST_1)
//        .table(value.repositoryConfig.dynamoConfig.tableName)
      import scala.jdk.CollectionConverters._
      ddb.putItem(value.repositoryConfig.dynamoConfig.tableName, "myId" -> "thisisiamtestitem")

      val table = ddb.table(value.repositoryConfig.dynamoConfig.tableName)
//      ddb.deleteItem(table.get, "thisisiamtestitem")
//      ddb.deleteItem("", )
      ddb.deleteItem(
        new DeleteItemRequest()
          .withTableName(value.repositoryConfig.dynamoConfig.tableName)
          .withKey(Map("myId" -> new AttributeValue("thisisiamtestitem")).asJava)
      )

    }
    case Left(value) => println(value)
  }
}
