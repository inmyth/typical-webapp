package me.mbcu.domain.repository.certivmanagement

import awscala.dynamodbv2.DynamoDB
import me.mbcu.config.Config.RepositoryConfig.DynamoConfig
import me.mbcu.domain.model.certivmanagement.User
import me.mbcu.domain.repository.AWSPing
import me.mbcu.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

private[certivmanagement] abstract class DynamoRepo(scheduler: Scheduler, dynamoConfig: DynamoConfig)
    extends AWSPing[Task] {

  def put(user: User): Task[Done]

}

private[certivmanagement] object DynamoRepo {
  def inMem(scheduler: Scheduler, dynamoConfig: DynamoConfig): DynamoRepo =
    new CertivDynamoInMem(dynamoConfig)(scheduler)

  def ddb(scheduler: Scheduler, db: DynamoDB, dynamoConfig: DynamoConfig): DynamoRepo =
    new CertivDynamo(db, dynamoConfig)(scheduler)

}
