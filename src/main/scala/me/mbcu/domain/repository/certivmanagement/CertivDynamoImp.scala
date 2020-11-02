package me.mbcu.domain.repository.certivmanagement

import awscala.dynamodbv2.DynamoDB
import com.amazonaws.services.dynamodbv2.model.{AttributeValue, DeleteItemRequest}
import me.mbcu.config.Config.RepositoryConfig.DynamoConfig
import me.mbcu.domain.model.certivmanagement.User
import me.mbcu.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

import scala.collection.JavaConverters._

private[certivmanagement] object CertivDynamoImp {}

private[certivmanagement] class CertivDynamoImp(db: DynamoDB, dynamoConfig: DynamoConfig)(implicit scheduler: Scheduler)
    extends CertivDynamoRepository(scheduler, dynamoConfig) {

  val tableName: String = dynamoConfig.tableName
  val key: String       = "myId"

  override def put(user: User): Task[Done] =
    Task {
      val a = collection.mutable.Map(key -> user.id.value)
      if (user.userName.nonEmpty) a += "name" -> user.userName.get.value
      db.putItem(tableName, a.toSeq: _*)
      Done
    }

  override def testAccessAndIAMPermission(): Task[Done] =
    for {
      _ <- Task(db.putItem(tableName, key -> dynamoConfig.iamTestKey))
      _ <- Task {
        db.deleteItem(
          new DeleteItemRequest()
            .withTableName(tableName)
            .withKey(Map(key -> new AttributeValue(dynamoConfig.iamTestKey)).asJava)
        )
      }

    } yield Done

}
