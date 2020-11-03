package me.mbcu

import me.mbcu.config.Config.{Application, Config}
import pureconfig.ConfigSource
import pureconfig._
import pureconfig.generic.auto._
import me.mbcu.config.ConfUtils._

object Main extends App {
  val config = ConfigSource.default.loadOrThrow[Config]
  val app    = Application.fromConfig(config)
  Application.start(app)

}
