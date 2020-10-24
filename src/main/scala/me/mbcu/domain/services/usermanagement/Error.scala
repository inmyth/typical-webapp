package me.mbcu.domain.services.usermanagement

sealed trait Error extends Throwable
private[usermanagement] object Error {
  final case object Exists                       extends Error
  final case object NotFound                     extends Error
  final case object Active                       extends Error
  final case object Deleted                      extends Error
  final case object Blocked                      extends Error
  final case object NotAuthorized                extends Error
  final case object BadToken                     extends Error
  final case object BadParam                     extends Error
  final case object NoResources                  extends Error
  final case object BadUserName                  extends Error
  final case object BadEmail                     extends Error
  final case object BadRole                      extends Error
  final case object BadUserId                    extends Error
  final case class System(underlying: Throwable) extends Error
}
