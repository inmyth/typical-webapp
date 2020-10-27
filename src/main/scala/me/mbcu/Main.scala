package me.mbcu

import java.io.File

import awscala.Region
import awscala.dynamodbv2.{DynamoDB, Table}
import com.amazonaws.services.dynamodbv2.model.{AttributeValue, DeleteItemRequest}
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
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
import sttp.tapir.CodecFormat.TextPlain

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import sttp.tapir.json.jsoniter._
import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.github.plokhotnyuk.jsoniter_scala.core._
import sttp.tapir.server.{PartialServerEndpoint, ServerEndpoint}
object Main extends App {
//  val config = ConfigSource.default.loadOrThrow[Config]
//  val ec = config.executorsConfig.computationScheduler.ec
//  val x = Repositories.fromConfig(config)
//  val y = x.testIAMPermissionAndAccess().runToFuture(ec)
//  Await.result(y, DurationInt(3).second)

  case class AAAUser(name: String)
  val AuthenticationErrorCode = 1001

  case class MyId(id: String)

  sealed trait ErrorInfo
  case class NotFound(what: String)  extends ErrorInfo
  case class AuthError(what: String) extends ErrorInfo

  def auth(token: String): Future[Either[AuthError, AAAUser]] =
    Future {
      if (token == "secret") Right(AAAUser("Spock"))
      else Left(AuthError("1001"))
    }

  implicit val codec: JsonValueCodec[MyId]       = JsonCodecMaker.make
  implicit val eCodec: JsonValueCodec[ErrorInfo] = JsonCodecMaker.make

  implicit val myIdCodec: Codec[String, MyId, TextPlain] = Codec.string
    .map(MyId)(_.id)
    .validate(Validator.pattern("^[A-Z].*").contramap(_.id))

  implicit val options: VertxEndpointOptions = VertxEndpointOptions()
  val vertx                                  = Vertx.vertx()
  val server                                 = vertx.createHttpServer()
  val router                                 = Router.router(vertx)

  val secureEndpoint: PartialServerEndpoint[AAAUser, Unit, ErrorInfo, Unit, Any, Future] = endpoint
    .in(header[String]("X-AUTH-TOKEN"))
    .errorOut(jsonBody[ErrorInfo])
    .serverLogicForCurrent(auth)

  val anEndpoint: Endpoint[MyId, ErrorInfo, MyId, Any] =
    secureEndpoint.endpoint.get
      .in("hello")
      .in(query[MyId]("name"))
      .errorOut(jsonBody[ErrorInfo])
      .out(jsonBody[MyId])
      .serverLogic {
        case (t, unit, id) => Future.successful[Either[(ErrorInfo, ErrorInfo), MyId]](Right(MyId(id)))
      }

  def logic(s: MyId): Future[Either[ErrorInfo, MyId]] =
    Future {
      s match {
        case x if x.id == "abc" => Right(MyId("abc"))
        case _                  => Left(NotFound("this user"))
      }
    }

  val attach = anEndpoint.route(logic)

  // extend the base endpoint to define (potentially multiple) proper endpoints, define the rest of the server logic
  val secureHelloWorld1WithLogic = secureEndpoint.get
    .in("hello1")
    .in(query[String]("salutation"))
    .out(stringBody)
    .serverLogic {
      case (user, salutation) => {
        println("asadadsa")
        Future(Right(s"$salutation, ${user.name}!"))
      }
    }

  val at2 = secureHelloWorld1WithLogic.endpoint.route {
    case (u, salutation) => {
      println(u)
      println(salutation)
      Future(Right(s"$salutation"))
    }
  }

  attach(router) // your endpoint is now attached to the router, and the route has been created
//  at2(router)
  server.requestHandler(router).listenFuture(9000)

}
