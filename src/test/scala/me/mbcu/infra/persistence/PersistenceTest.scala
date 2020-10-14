package me.mbcu.infra.persistence

import mbcu.me.domain.models.usermanagement
import org.scalatest._

class PersistenceTest extends AsyncFlatSpec {
  import mbcu.me.config.Config.ExecutorsConfig.ComputationScheduler
  import mbcu.me.config.Config._
  import mbcu.me.domain.models.usermanagement.User

  val config      = Config(EnvConfig(failureProbability = 0.5), ExecutorsConfig(ComputationScheduler(1)), Services.UNSTABLE)
  implicit val ec = config.executorsConfig.computationScheduler.ec

  val repo     = Repositories.fromConfig.run(config)
  val userId   = User.Id(1)
  val userName = User.UserName("martin")
  val user     = usermanagement.User(userId, userName)

  behavior of "Repository"
  it should "save and retrieve new user" in {
    repo.userRepo.insert(user)
    val task = repo.userRepo.get(userId)
    val z    = task.runToFuture
    z map { p => assert(p.get.userName === userName) }
  }

  it should "find by username" in {
    val task = repo.userRepo.getByUserName(userName)
    val z    = task.runToFuture
    z map { p => assert(p.get.userName === userName) }
  }

}
