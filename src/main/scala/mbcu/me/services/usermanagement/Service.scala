package mbcu.me.services.usermanagement

import mbcu.me.domain.{Done, User}
import mbcu.me.domain.User.UserName

trait Service[F[_]] {

  def get(id: User.Id): F[User]

  def changeUserName(id: User.Id, newName: UserName): F[Done]

}
