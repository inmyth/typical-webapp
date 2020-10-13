package me.mbcu.repos

import org.scalatest._

class RepoTest extends AsyncFlatSpec {
  import mbcu.me.config.Config.{Config, EnvConfig, ExecutorsConfig, Repositories, Services}
  import mbcu.me.config.Config.ExecutorsConfig.ComputationScheduler
  import mbcu.me.domain.User

  val config      = Config(EnvConfig(failureProbability = 0.5), ExecutorsConfig(ComputationScheduler(1)), Services.UNSTABLE)
  implicit val ec = config.executorsConfig.computationScheduler.ec

  val repo        = Repositories.fromConfig.run(config)
  val userId      = User.Id(1)
  val userName    = User.UserName("martin")
  val user        = User(userId, userName)
  val newUserName = User.UserName("utama")

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
