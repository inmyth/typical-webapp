package mbcu.me.services.usermanagement

import mbcu.me.domain.User.UserName
import mbcu.me.domain.{Done, User}
import mbcu.me.repos.UserRepository
import mbcu.me.services.usermanagement.Unstable.failWithProbability
import monix.eval.Task
import monix.execution.Scheduler

import scala.util.Random

object Implementations {

  def default(userRepository: UserRepository)(implicit ec: Scheduler): Service[Task] = new Default(userRepository)

  def unstable(underlying: Service[Task], failureProbability: Double)(implicit ec: Scheduler): Service[Task] =
    new Unstable(underlying, failureProbability)
}

final class Default private[usermanagement] (userRepository: UserRepository)(implicit ec: Scheduler)
    extends Service[Task] {

  override def insert(user: User): Task[Done] = userRepository.insert(user)

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

  private def failWithProbability[A](probability: Double)(f: Task[A]): Task[A] =
    if (Random.nextDouble < probability) Task.raiseError(Error.System(new Throwable("#failWithProbability"))) else f
}

final class Unstable private[usermanagement] (underlying: Service[Task], failureProbability: Double)(implicit
  ec: Scheduler
) extends Service[Task] {

  private def mayFail[A](f: Task[A]) = failWithProbability(failureProbability)(f)

  override def get(id: User.Id): Task[User] = mayFail(underlying.get(id))

  override def changeUserName(id: User.Id, newName: UserName): Task[Done] =
    mayFail(underlying.changeUserName(id, newName))

  override def insert(user: User): Task[Done] = mayFail(underlying.insert(user))
}
