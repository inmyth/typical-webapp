package me.mbcu.domain

package object services {

  type UserManagement[F[_]] = me.mbcu.domain.services.usermanagement.Service[F]
  val UserManagement: usermanagement.Implementations.type = usermanagement.Implementations

  type UserManagementError = me.mbcu.domain.services.usermanagement.Error

  type UserRepository = usermanagement.Repo
  val UserRepository = usermanagement.Repo

}
