package mbcu.me.repos.user

import mbcu.me.domain.{Done, User}
import mbcu.me.domain.User.UserName
import monix.eval.Task

private[repos] trait Repo {
  def insert(user: User): Task[Done]
  def get(id: User.Id): Task[Option[User]]
  def getByUserName(userName: UserName): Task[Option[User]]
}

object Repo {
  def inMemory(): Repo = new InMemoryDB()
}
