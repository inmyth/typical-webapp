package mbcu.me.domain.services.certivmanagement

import mbcu.me.domain.models.usermanagement.User
import mbcu.me.domain.models.usermanagement.User.UserName
import mbcu.me.domain.shared.Done
import mbcu.me.infra.persistence.CertivDynamoInMem
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
