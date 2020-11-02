package me.mbcu.domain.repository.certivmanagement

import me.mbcu.config.Config.RepositoryConfig.DynamoConfig
import me.mbcu.domain.model.certivmanagement.User
import me.mbcu.domain.model.certivmanagement.User.MyId
import me.mbcu.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

import scala.collection.concurrent.TrieMap

private[certivmanagement] object CertivDynamoInMemImp {
  private val db: TrieMap[User.MyId, User] = TrieMap.empty
}

private[certivmanagement] class CertivDynamoInMemImp(dynamoConfig: DynamoConfig)(implicit scheduler: Scheduler)
    extends CertivDynamoRepository(scheduler, dynamoConfig) {
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
