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
import me.mbcu.domain.services.certivmanagement.AWSPing
import monix.eval.Task

object Main extends App {

  val config = ConfigSource.default.loadOrThrow[Config]
  val ec     = config.executorsConfig.computationScheduler.ec
  val x      = Repositories.fromConfig(config)
  val y      = x.testIAMPermissionAndAccess().runToFuture(ec)
  Await.result(y, DurationInt(3).second)

}
