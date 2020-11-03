package me.mbcu.domain.service.certivmanagement

import me.mbcu.TestConfig
import me.mbcu.config.Config.Services
import me.mbcu.domain.model.certivmanagement.User
import me.mbcu.domain.shared.Done
import org.scalatest.flatspec.AsyncFlatSpec

class ServicesTest extends AsyncFlatSpec with TestConfig {

  implicit val ec = config.executorsConfig.computationScheduler.ec

  val services = Services.fromConfig.run(config)
  val userId   = User.MyId("a")
  val userName = Some(User.Name("martin"))
  val user     = User(userId, userName)

  behavior of "Default Repository"

  it should "insert returns Done" in {
    val task = services.certivManagement.insert(user)
    task.runToFuture.map(p => assert(p === Done))
  }

}