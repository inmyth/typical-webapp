package me.mbcu.domain.services

import mbcu.me.config.Config.ExecutorsConfig.ComputationScheduler
import mbcu.me.config.Config.{Config, EnvConfig, ExecutorsConfig, Services}
import mbcu.me.domain.models.usermanagement
import mbcu.me.domain.models.usermanagement.User
import mbcu.me.domain.shared.Done
import me.mbcu.TestConfig
import org.scalatest.flatspec.AsyncFlatSpec

class ServicesTest extends AsyncFlatSpec with TestConfig {

  implicit val ec = config.executorsConfig.computationScheduler.ec

  val services = Services.fromConfig.run(config)
  val userId   = User.Id(1)
  val userName = User.UserName("martin")
  val user     = usermanagement.User(userId, userName)

  behavior of "Default Repository"

  it should "insert returns Done" in {
    val task = services.certivManagement.insert(user)
    task.runToFuture.map(p => assert(p === Done))
  }

}
