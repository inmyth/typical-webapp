package mbcu.me.domain.services.certivmanagement

import mbcu.me.domain.models.usermanagement.User
import mbcu.me.domain.shared.Done

trait Service[F[A]] {

  def insert(user: User): F[Done]

  def get(id: User.Id): F[User]

  def changeUserName(id: User.Id, newName: User.UserName): F[Done]

}
