package mbcu.me.infra.persistence

import mbcu.me.domain.models.usermanagement.User
import mbcu.me.domain.services.UserRepository
import mbcu.me.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

import scala.collection.concurrent.TrieMap

private[persistence] object UserInMemoryDB {
  private val db: TrieMap[User.Id, User] = TrieMap.empty
}

private[persistence] class UserInMemoryDB(scheduler: Scheduler) extends UserRepository(scheduler) {
  import UserInMemoryDB._

  override def insert(user: User): Task[Done] =
    Task.now {
      db.put(user.id, user)
      Done
    }

  override def get(id: User.Id): Task[Option[User]] = Task.now(db.get(id))

  override def getByUserName(userName: User.UserName): Task[Option[User]] =
    Task.now(db.collectFirst { case (_, user) if user.userName == userName ⇒ user })

  def all(): Task[List[User]] = Task.now(db.values.toList)
}
