package mbcu.me.services.usermanagement

import mbcu.me.config.Config.EnvConfig
import mbcu.me.domain.User.UserName
import mbcu.me.domain.{Done, User}
import mbcu.me.repos.UserRepository
import mbcu.me.services.usermanagement.Unstable.failWithProbability
import monix.eval.Task
import monix.execution.Scheduler

import scala.util.Random

object Implementations {

  def default(userRepository: UserRepository)(implicit ec: Scheduler): Service[Task] = new Default(userRepository)

  def unstable(underlying: Service[Task], failureProbability: Double)(implicit ec: Scheduler) =
    new Unstable(underlying, failureProbability)
}

final class Default private[usermanagement] (userRepository: UserRepository)(implicit ec: Scheduler)
    extends Service[Task] {

  override def get(id: User.Id): Task[User] =
    for {
      maybeUser <- userRepository.get(id)
      user <- maybeUser match {
        case Some(value) => Task.now(value)
        case None        => Task.raiseError(Error.NotFound)
      }
    } yield user

  override def changeUserName(id: User.Id, newName: User.UserName): Task[Done] =
    for {
      user  <- get(id)
      clone <- Task.now(user.copy(id, userName = newName))
      _     <- userRepository.insert(clone)
    } yield Done

}

object Unstable {

  private def failWithProbability[A](probability: Double)(f: Task[A]) =
    if (Random.nextDouble < probability) Task.raiseError(new Exception) else f

}

final class Unstable private[usermanagement] (underlying: Service[Task], failureProbability: Double)(implicit
  ec: Scheduler
) extends Service[Task] {

  private def fail[A](f: Task[A]): Task[A] = failWithProbability(failureProbability)(f)

  override def get(id: User.Id): Task[User] = fail(underlying.get(id))

  override def changeUserName(id: User.Id, newName: UserName): Task[Done] = fail(underlying.changeUserName(id, newName))

}
