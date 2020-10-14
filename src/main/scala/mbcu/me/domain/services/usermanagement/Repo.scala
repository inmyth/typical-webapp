package mbcu.me.domain.services.usermanagement

import mbcu.me.domain.models.usermanagement.User
import mbcu.me.domain.models.usermanagement.User.UserName
import mbcu.me.domain.shared.Done
import mbcu.me.infra.persistence.FakeUserDb
import monix.eval.Task
import monix.execution.Scheduler

private[services] abstract class Repo(scheduler: Scheduler) {
  def insert(user: User): Task[Done]
  def get(id: User.Id): Task[Option[User]]
  def getByUserName(userName: UserName): Task[Option[User]]
}

object Repo {
  def inMemory(scheduler: Scheduler): Repo = new FakeUserDb(scheduler)
}
