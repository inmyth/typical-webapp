package me.mbcu.domain.services.certivmanagement

import me.mbcu.domain.models.usermanagement.User
import me.mbcu.domain.models.usermanagement.User.UserName
import me.mbcu.domain.shared.Done
import me.mbcu.infra.persistence.CertivDynamoInMem
import monix.eval.Task
import monix.execution.Scheduler

private[services] abstract class DynamoRepo(scheduler: Scheduler) {
  def insert(user: User): Task[Done]
  def get(id: User.Id): Task[Option[User]]
  def getByUserName(userName: UserName): Task[Option[User]]
}

object DynamoRepo {
  def inMem(scheduler: Scheduler): DynamoRepo = new CertivDynamoInMem(scheduler)
}
