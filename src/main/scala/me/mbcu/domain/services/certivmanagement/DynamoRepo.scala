package me.mbcu.domain.services.certivmanagement

import awscala.dynamodbv2.DynamoDB
import me.mbcu.config.Config.RepositoryConfig.DynamoConfig
import me.mbcu.domain.models.usermanagement.User
import me.mbcu.domain.shared.Done
import me.mbcu.infra.persistence.{CertivDynamo, CertivDynamoInMem}
import monix.eval.Task
import monix.execution.Scheduler

private[services] abstract class DynamoRepo(scheduler: Scheduler, dynamoConfig: DynamoConfig) {
  def put(user: User): Task[Done]

}

object DynamoRepo {
  def inMem(scheduler: Scheduler, dynamoConfig: DynamoConfig): DynamoRepo =
    new CertivDynamoInMem(dynamoConfig)(scheduler)

  def ddb(scheduler: Scheduler, db: DynamoDB, dynamoConfig: DynamoConfig): DynamoRepo =
    new CertivDynamo(db, dynamoConfig)(scheduler)

}
