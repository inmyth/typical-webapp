package me.mbcu.domain.services

import me.mbcu.config.Config.ExecutorsConfig.ComputationScheduler
import me.mbcu.config.Config.{Config, EnvConfig, ExecutorsConfig, Services}
import me.mbcu.domain.models.usermanagement
import me.mbcu.domain.models.usermanagement.User
import me.mbcu.domain.services.UserManagementError
import org.scalatest.AsyncFlatSpec

class ServicesTest extends AsyncFlatSpec {

  val config      = Config(EnvConfig(failureProbability = 1.0), ExecutorsConfig(ComputationScheduler(1)), Services.UNSTABLE)
  implicit val ec = config.executorsConfig.computationScheduler.ec

  val services = Services.fromConfig.run(config)
  val userId   = User.Id(1)
  val userName = User.UserName("martin")
  val user     = usermanagement.User(userId, userName)

  behavior of "Unstable Repository"

  it should "totally fail when failure probability is 100%" in {
    val task = services.userManagement.insert(user)
    recoverToSucceededIf[UserManagementError] {
      task.runToFuture
    }
  }

}
