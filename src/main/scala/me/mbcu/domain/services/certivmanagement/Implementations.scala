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

  override def insert(user: User): Task[Done] = dynamoRepo.insert(user)

  override def get(id: User.Id): Task[User] =
    for {
      maybeUser <- dynamoRepo.get(id)
      user <- maybeUser match {
        case Some(value) => Task.now(value)
        case None        => Task.raiseError(Error.NotFound)
      }
    } yield user

  override def changeUserName(id: User.Id, newName: User.UserName): Task[Done] =
    for {
      user  <- get(id)
      clone <- Task.now(user.copy(id, userName = newName))
      _     <- dynamoRepo.insert(clone)
    } yield Done

}
