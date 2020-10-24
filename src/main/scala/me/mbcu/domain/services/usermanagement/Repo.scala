package me.mbcu.domain.services.usermanagement

import me.mbcu.domain.models.usermanagement.User
import me.mbcu.domain.models.usermanagement.User.UserName
import me.mbcu.domain.shared.Done
import me.mbcu.infra.persistence.FakeUserDb
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
