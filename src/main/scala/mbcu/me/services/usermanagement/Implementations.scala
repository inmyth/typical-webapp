package mbcu.me.services.usermanagement

import cats.data.EitherT
import mbcu.me.domain.{Done, User}
import mbcu.me.repos.UserRepository
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.ExecutionContext

object Implementations {}

final class Default private[usermanagement] (userRepository: UserRepository)(implicit ec: Scheduler) extends Service[Task] {

  override def get(id: User.Id): Task[Either[Error, User]] =
    for {
      maybeUser <- userRepository.get(id)
      res = maybeUser.toRight(Error.NotFound)
    } yield res

  override def changeUserName(id: User.Id, oldName: User.UserName, newName: User.UserName): Task[Either[Error, Done]] =
    for {
      maybeUser <- get(id)
      r <- maybeUser.

    }

}
