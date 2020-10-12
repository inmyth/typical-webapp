package mbcu.me.services.usermanagement

import mbcu.me.domain.{Done, User}
import mbcu.me.domain.User.UserName

private[usermanagement] trait Service[F[_]] {

  def get(id: User.Id): F[Error Either User]

  def changeUserName(id: User.Id, oldName: UserName, newName: UserName): F[Error Either Done]

}
