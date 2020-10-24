package me.mbcu.infra.persistence

import me.mbcu.config.Config.RepositoryConfig.DynamoConfig
import me.mbcu.domain.models.usermanagement.User
import me.mbcu.domain.models.usermanagement.User.MyId
import me.mbcu.domain.services.CertivDynamoRepository
import me.mbcu.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

import scala.collection.concurrent.TrieMap

private[persistence] object CertivDynamoInMemImpl {
  private val db: TrieMap[User.MyId, User] = TrieMap.empty
}

private[persistence] class CertivDynamoInMemImpl(dynamoConfig: DynamoConfig)(implicit scheduler: Scheduler)
    extends CertivDynamoRepository(scheduler, dynamoConfig)
    with AWSPing[Task] {
  import CertivDynamoInMemImpl._

  override def insert(user: User): Task[Done] =
    Task.now {
      db.put(user.id, user)
      Done
    }

  override def get(id: User.MyId): Task[Option[User]] = Task.now(db.get(id))

  override def getByUserName(userName: User.Name): Task[Option[User]] =
    Task.now(db.collectFirst { case (_, user) if user.userName.nonEmpty && user.userName.get == userName ⇒ user })

  def all(): Task[List[User]] = Task.now(db.values.toList)

  override def testAccessAndIAMPermission(): Task[Done] = {
    val testId = MyId(dynamoConfig.testKey)
    for {
      _ <- insert(User(testId, None))
      _ <- Task.now { db -= testId }
    } yield Done
  }
}
