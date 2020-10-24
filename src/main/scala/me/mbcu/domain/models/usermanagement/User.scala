package me.mbcu.domain.models.usermanagement

case class User(id: User.Id, userName: User.UserName)

object User {

  final case class Id(value: Int)          extends AnyVal
  final case class UserName(value: String) extends AnyVal

}
