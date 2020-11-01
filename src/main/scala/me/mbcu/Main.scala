package me.mbcu

import cats.data.EitherT
import com.github.plokhotnyuk.jsoniter_scala.core._
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router
import me.mbcu.config.Config.Config
import monix.eval.Task
import pureconfig.ConfigSource
import sttp.tapir.json.jsoniter.jsonBody
import sttp.tapir.server.vertx.{VertxEndpoint, VertxEndpointOptions}
import sttp.tapir.{Codec, CodecFormat, Validator, endpoint, header, query}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try
import pureconfig._
import pureconfig.generic.auto._
import me.mbcu.config.ConfUtils._
object Main extends App {
  val config      = ConfigSource.default.loadOrThrow[Config]
  implicit val ec = config.executorsConfig.computationScheduler.ec
//  val x      = Repositories.fromConfig(config)
//  val y      = x.testIAMPermissionAndAccess().runToFuture(ec)
//  Await.result(y, DurationInt(3).second)

//  import com.dslplatform.json._ // import pimping
//  lazy implicit val dslJson = new DslJson[Any]()
//
//  final case class MyId(name: Option[String], id: Int)
//  val os = new ByteArrayOutputStream()
//  dslJson.encode(MyId(None, 3), os)
//  println(os.toString)

  case class MyId(id: String)
  case class MyToken(token: String)
  case class MyOut(myId: MyId, myToken: MyToken)
  sealed trait ErrorInfo
  case class NotFound(what: String)  extends ErrorInfo
  case class AuthError(what: String) extends ErrorInfo

  implicit val myIdCodec: Codec[String, MyId, CodecFormat.TextPlain] = Codec.string
    .map(MyId)(_.id)
    .validate(Validator.pattern("^[A-Z].*").contramap(_.id))

  implicit val codec: JsonValueCodec[MyId]       = JsonCodecMaker.make
  implicit val eCodec: JsonValueCodec[ErrorInfo] = JsonCodecMaker.make
  implicit val eCodec2: JsonValueCodec[MyToken]  = JsonCodecMaker.make
  implicit val aaaa: JsonValueCodec[MyOut]       = JsonCodecMaker.make

  val anEndpoint =
    endpoint
      .in(header[String]("X-AUTH-TOKEN"))
      .get
      .in("hello")
      .in(query[MyId]("name"))
      .errorOut(jsonBody[ErrorInfo])
      .out(jsonBody[MyOut])

//  anEndpoint.route(p => p)
  def jwt(token: String): Task[Either[ErrorInfo, MyToken]] =
    Task {
      token match {
        case "secret" => Right(MyToken("secret"))
        case _        => Left(AuthError("token not ok"))
      }
    }

  def getMyId(myId: MyId, myToken: MyToken): Task[Either[ErrorInfo, MyOut]] =
    Task {
      myId match {
        case x if x.id == "ABC" => Right(MyOut(myId, myToken))
        case _                  => Left(NotFound("this user"))
      }
    }

  import cats.implicits._

  def logic(myId: MyId)(implicit token: Task[Either[ErrorInfo, MyToken]]) = {

    val x = for {
      a <- EitherT(token)
      b <- EitherT(getMyId(myId, a))
    } yield b
    x.value.runToFuture
  }

  implicit val options: VertxEndpointOptions = VertxEndpointOptions()
  val vertx                                  = Vertx.vertx()
  val server                                 = vertx.createHttpServer()
  val router                                 = Router.router(vertx)

  val attach = anEndpoint.route(p => {
    logic(p._2)(jwt(p._1))
  })

  attach(router)
  server.requestHandler(router).listenFuture(9000)
}
