package me.mbcu.domain.model.certivmanagement

case class User(id: User.MyId, userName: Option[User.Name])

object User {

  final case class MyId(value: String) extends AnyVal
  final case class Name(value: String) extends AnyVal
  final case class City(value: String) extends AnyVal

}
