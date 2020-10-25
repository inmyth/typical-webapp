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
import me.mbcu.infra.persistence.AWSPing
import monix.eval.Task

object Main extends App {

  val config = ConfigSource.default.load[Config]

  config match {
    case Right(value) => {
      val ec = value.executorsConfig.computationScheduler.ec

      val x = Repositories.fromConfig(value)
      val y = x.certivDynamo.asInstanceOf[AWSPing[Task]]
      val z = y.testAccessAndIAMPermission().runToFuture(ec)
      Await.result(z, DurationInt(3).second)

    }
    case Left(value) => println(value)
  }
}
