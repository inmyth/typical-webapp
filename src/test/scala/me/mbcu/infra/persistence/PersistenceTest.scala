package me.mbcu.infra.persistence

import me.mbcu.domain.models.usermanagement
import me.mbcu.TestConfig
import me.mbcu.config.Config.Repositories
import me.mbcu.domain.models.usermanagement.User
import me.mbcu.domain.shared.Done
import org.scalatest._

class PersistenceTest extends AsyncFlatSpec with TestConfig {

  implicit val ec = config.executorsConfig.computationScheduler.ec

  val repo     = Repositories.fromConfig(config).certivDynamo
  val userId   = User.MyId("a")
  val userName = Some(User.Name("martin"))
  val user     = usermanagement.User(userId, userName)

  behavior of "Certiv Dynamo Repository"

  it should "be able test IAM permissions" in {
    val f = repo.testAccessAndIAMPermission().runToFuture
    f map { p => assert(p === Done) }
  }

}
