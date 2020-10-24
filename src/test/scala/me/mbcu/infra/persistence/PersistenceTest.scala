package me.mbcu.infra.persistence

import me.mbcu.domain.models.usermanagement
import me.mbcu.TestConfig
import me.mbcu.config.Config.Repositories
import me.mbcu.domain.models.usermanagement.User
import org.scalatest._

class PersistenceTest extends AsyncFlatSpec with TestConfig {

  implicit val ec = config.executorsConfig.computationScheduler.ec

  val repo     = Repositories.fromConfig(config).certivDynamo
  val userId   = User.MyId("a")
  val userName = Some(User.Name("martin"))
  val user     = usermanagement.User(userId, userName)

  behavior of "Certiv Dynamo Repository"
  it should "be able to save and retrieve new user" in {
    repo.insert(user)
    val task = repo.get(userId)
    val z    = task.runToFuture
    z map { p => assert(p.get.userName === userName) }
  }

  it should "be able to find user by username" in {
    val task = repo.getByUserName(userName.get)
    val z    = task.runToFuture
    z map { p => assert(p.get.userName === userName) }
  }

}
