package mbcu.me.services.usermanagement

import mbcu.me.domain.{Done, User}
import mbcu.me.domain.User.UserName

trait Service[F[A]] {

  def insert(user: User): F[Done]

  def get(id: User.Id): F[User]

  def changeUserName(id: User.Id, newName: UserName): F[Done]

}
