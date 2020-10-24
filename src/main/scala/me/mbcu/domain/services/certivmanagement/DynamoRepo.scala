package me.mbcu.domain.services.certivmanagement

import me.mbcu.config.Config.RepositoryConfig.DynamoConfig
import me.mbcu.domain.models.usermanagement.User
import me.mbcu.domain.shared.Done
import me.mbcu.infra.persistence.CertivDynamoInMem
import monix.eval.Task
import monix.execution.Scheduler

private[services] abstract class DynamoRepo(scheduler: Scheduler, dynamoConfig: DynamoConfig) {
  def insert(user: User): Task[Done]
  def get(id: User.MyId): Task[Option[User]]
  def getByUserName(userName: User.Name): Task[Option[User]]

}

object DynamoRepo {
  def inMem(scheduler: Scheduler, dynamoConfig: DynamoConfig): DynamoRepo =
    new CertivDynamoInMem(dynamoConfig)(scheduler)
}
