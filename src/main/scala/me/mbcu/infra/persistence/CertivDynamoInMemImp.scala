package me.mbcu.infra.persistence

import me.mbcu.config.Config.RepositoryConfig.DynamoConfig
import me.mbcu.domain.models.usermanagement.User
import me.mbcu.domain.models.usermanagement.User.MyId
import me.mbcu.domain.services.CertivDynamoRepository
import me.mbcu.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

import scala.collection.concurrent.TrieMap

private[persistence] object CertivDynamoInMemImp {
  private val db: TrieMap[User.MyId, User] = TrieMap.empty
}

private[persistence] class CertivDynamoInMemImp(dynamoConfig: DynamoConfig)(implicit scheduler: Scheduler)
    extends CertivDynamoRepository(scheduler, dynamoConfig)
    with AWSPing[Task] {
  import CertivDynamoInMemImp._

  override def put(user: User): Task[Done] =
    Task.now {
      db.put(user.id, user)
      Done
    }

  override def testAccessAndIAMPermission(): Task[Done] = {
    val testId = MyId(dynamoConfig.iamTestKey)
    for {
      _ <- put(User(testId, None))
      _ <- Task.now { db -= testId }
    } yield Done
  }
}
