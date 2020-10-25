package me.mbcu.domain.services.certivmanagement

import me.mbcu.domain.models.usermanagement.User
import me.mbcu.domain.shared.Done

trait Service[F[_]] {

  def insert(user: User): F[Done]

}
