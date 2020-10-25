package me.mbcu.domain.services.certivmanagement

import me.mbcu.domain.models.usermanagement.User
import me.mbcu.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

object Implementations {

  def default(dynamoRepo: DynamoRepo, fileRepo: FileRepo)(implicit ec: Scheduler): Service[Task] =
    new Default(dynamoRepo, fileRepo)

}

final class Default private[certivmanagement] (dynamoRepo: DynamoRepo, fileRepo: FileRepo)(implicit ec: Scheduler)
    extends Service[Task] {

  override def insert(user: User): Task[Done] = dynamoRepo.put(user)

}
