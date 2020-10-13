package mbcu.me.repos.user

import mbcu.me.domain.{Done, User}
import monix.eval.Task
import monix.execution.Scheduler

import scala.collection.concurrent.TrieMap

private[user] object InMemoryDB {
  private val db: TrieMap[User.Id, User] = TrieMap.empty
}

private[user] class InMemoryDB(scheduler: Scheduler) extends Repo(scheduler) {
  import InMemoryDB._

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
