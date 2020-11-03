package me.mbcu.app

import cats.data.Reader
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.CorsHandler
import me.mbcu.app.certivmanagement.CertivController
import me.mbcu.config.Config.{Application, EnvConfig}

object VertxServer {
  val vertx  = Vertx.vertx()
  val server = vertx.createHttpServer()
  val router = Router.router(vertx)

  private val applicationReader: Reader[Application, Application] = Reader(p => p)

  private def setupCORS(envConfig: EnvConfig): Unit = {
    router
      .route()
      .handler(
        CorsHandler
          .create(s"http://localhost:${envConfig.frontendPort}") //  frontend SPA
          .allowedMethod(io.vertx.core.http.HttpMethod.GET)
          .allowedMethod(io.vertx.core.http.HttpMethod.POST)
          .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
          .allowCredentials(true)
          .allowedHeader("Authorization") // this is a must
          .allowedHeader("X-AUTH-TOKEN")
          .allowedHeader("Access-Control-Allow-Method")
          .allowedHeader("Access-Control-Allow-Origin")
          .allowedHeader("Access-Control-Allow-Credentials")
          .allowedHeader("Content-Type")
      )
  }

  private def setupEndpoints(certivController: CertivController): Unit =
    certivController.endpoints.foreach(p => p(router))

  private def startServer(envConfig: EnvConfig): Unit =
    server.requestHandler(router).listen(envConfig.backendPort)

  val setupServer: Reader[Application, Unit] =
    for {
      app <- applicationReader
      _      = setupCORS(app.services.config.envConfig)
      certiv = app.controllers.certivController
      _      = setupEndpoints(certiv)
      _      = startServer(app.services.config.envConfig)
    } yield ()

}
