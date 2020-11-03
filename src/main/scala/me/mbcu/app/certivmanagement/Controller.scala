package me.mbcu.app.certivmanagement

import cats.data.EitherT
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import io.vertx.scala.ext.web.{Route, Router}
import me.mbcu.app.shared.{BadUserName, BaseHandler, BaseOut, ErrorInfo}
import me.mbcu.config.Config.{ExecutorsConfig, Services}
import monix.eval.Task
import monix.execution.Scheduler
import sttp.tapir.json.jsoniter.jsonBody
import sttp.tapir.server.vertx.{VertxEndpoint, VertxEndpointOptions}
import sttp.tapir.{Codec, CodecFormat, Validator, endpoint, header, query}
import me.mbcu.app.shared.Error._

private[certivmanagement] object Controller {}

private[certivmanagement] class Controller(options: VertxEndpointOptions, services: Services) {
  implicit val scheduler = ExecutorsConfig.ecIO

  case class MyName(name: String)

  implicit val myIdCodec: Codec[String, MyName, CodecFormat.TextPlain] = Codec.string
    .map(MyName)(_.name)
    .validate(Validator.pattern("^[a-z].*").contramap(_.name))

  implicit val myNameCodec: JsonValueCodec[MyName]             = JsonCodecMaker.make
  implicit val outMyNameCodec: JsonValueCodec[BaseOut[MyName]] = JsonCodecMaker.make

  def parseMyName(myName: MyName): Task[Either[ErrorInfo, BaseOut[MyName]]] =
    Task {
      myName match {
        case x if x.name == "abc" => Right(BaseOut[MyName](myName))
        case _                    => Left(BadUserName())
      }
    }

  case class InsertNameLogic(token: String, myName: MyName) extends BaseHandler {
    def logic: EitherT[Task, ErrorInfo, BaseOut[MyName]] =
      for {
        _ <- EitherT(procHeader(token))
        b <- EitherT(parseMyName(myName))
      } yield b
  }

  val insertNameEndpoint: Router => Route =
    endpoint
      .in(header[String]("X-AUTH-TOKEN"))
      .get
      .in("hello")
      .in(query[MyName]("name"))
      .errorOut(jsonBody[ErrorInfo])
      .out(jsonBody[BaseOut[MyName]])
      .route(InsertNameLogic.tupled(_).logic.value.runToFuture)(
        options
      ) // this has to be placed after Vertx stuff Router.router otherwise 500

  val endpoints = Vector(insertNameEndpoint)
}
