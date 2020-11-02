package me.mbcu.domain.service.certivmanagement

import me.mbcu.domain.model.certivmanagement.User
import me.mbcu.domain.shared.Done

trait Service[F[_]] {

  def insert(user: User): F[Done]

}
