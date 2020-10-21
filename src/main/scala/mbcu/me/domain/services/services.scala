package mbcu.me.domain.services

package object services {

  type UserManagement[F[_]] = mbcu.me.domain.services.usermanagement.Service[F]
  val UserManagement: usermanagement.Implementations.type = usermanagement.Implementations

  type UserManagementError = mbcu.me.domain.services.usermanagement.Error

  type UserRepository = usermanagement.Repo
  val UserRepository = usermanagement.Repo

  type FileRepository = usermanagement.FileRepo
  val FileRepository = usermanagement.FileRepo

}