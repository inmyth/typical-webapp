package me.mbcu.infra.persistence

import mbcu.me.domain.models.usermanagement
import me.mbcu.TestConfig
import org.scalatest._

class PersistenceTest extends AsyncFlatSpec with TestConfig {
  import mbcu.me.config.Config._
  import mbcu.me.domain.models.usermanagement.User

  implicit val ec = config.executorsConfig.computationScheduler.ec

  val repo     = Repositories.fromConfig(config).certivDynamo
  val userId   = User.Id(1)
  val userName = User.UserName("martin")
  val user     = usermanagement.User(userId, userName)

  behavior of "Certiv Dynamo Repository"
  it should "be able to save and retrieve new user" in {
    repo.insert(user)
    val task = repo.get(userId)
    val z    = task.runToFuture
    z map { p => assert(p.get.userName === userName) }
  }

  it should "be able to find user by username" in {
    val task = repo.getByUserName(userName)
    val z    = task.runToFuture
    z map { p => assert(p.get.userName === userName) }
  }

}
