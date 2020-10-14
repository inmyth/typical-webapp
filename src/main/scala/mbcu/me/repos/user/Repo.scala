package mbcu.me.repos.user

import mbcu.me.domain.User.UserName
import mbcu.me.domain.{Done, User}
import monix.eval.Task
import monix.execution.Scheduler

private[repos] abstract class Repo(scheduler: Scheduler) {
  def insert(user: User): Task[Done]
  def get(id: User.Id): Task[Option[User]]
  def getByUserName(userName: UserName): Task[Option[User]]
}

object Repo {
  def inMemory(scheduler: Scheduler): Repo = new InMemoryDB(scheduler)
}
