package mbcu.me

import mbcu.me.services.usermanagement.Implementations

package object services {

  type UserManagement[F[_]] = usermanagement.Service[F]
  val UserManagement: Implementations.type = usermanagement.Implementations
}
