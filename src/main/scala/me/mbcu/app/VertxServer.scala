package me.mbcu.app

import cats.data.Reader
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.CorsHandler
import me.mbcu.app.certivmanagement.CertivController
import me.mbcu.config.Config.{EnvConfig, Services}
import sttp.tapir.server.vertx.VertxEndpointOptions

object VertxServer {
  val options: VertxEndpointOptions = VertxEndpointOptions()
  val vertx                         = Vertx.vertx()
  val server                        = vertx.createHttpServer()
  val router                        = Router.router(vertx)

  private val servicesReader: Reader[Services, Services] = Reader(p => p)
  val setupServer: Reader[Services, Unit] =
    for {
      services <- servicesReader
      _      = setupCORS(services.config.envConfig)
      certiv = new CertivController(options, services)
      _      = setupEndpoints(certiv)
      _      = startServer(services.config.envConfig)
    } yield Unit

  def setupCORS(envConfig: EnvConfig): Unit = {
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

  def setupEndpoints(certivController: CertivController): Unit = {
    certivController.endpoints.foreach(p => p(router))
  }

  def startServer(envConfig: EnvConfig): Unit = server.requestHandler(router).listen(envConfig.backendPort)

}
