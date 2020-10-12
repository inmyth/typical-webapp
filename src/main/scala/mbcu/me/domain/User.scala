package mbcu.me.domain

import mbcu.me.domain.User.UserName

case class User(id: User.Id, userName: UserName)

object User {

  final case class Id(value: Int)          extends AnyVal
  final case class UserName(value: String) extends AnyVal

}
