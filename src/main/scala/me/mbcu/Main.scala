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
import sttp.tapir._
import sttp.tapir.server.vertx._
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main extends App {
//  val config = ConfigSource.default.loadOrThrow[Config]
//  val ec = config.executorsConfig.computationScheduler.ec
//  val x = Repositories.fromConfig(config)
//  val y = x.testIAMPermissionAndAccess().runToFuture(ec)
//  Await.result(y, DurationInt(3).second)

  implicit val options: VertxEndpointOptions = VertxEndpointOptions()
  val vertx                                  = Vertx.vertx()
  val server                                 = vertx.createHttpServer()
  val router                                 = Router.router(vertx)
  val anEndpoint: Endpoint[(String), Unit, String, Any] =
    endpoint.get.in("hello").in(query[String]("name")).out(stringBody) // your definition here
  def logic(s: String): Future[Either[Unit, String]] = Future(Right(s)) // your logic here
  val attach                                         = anEndpoint.route(logic)
  attach(router) // your endpoint is now attached to the router, and the route has been created
  server.requestHandler(router).listenFuture(9000)

}
