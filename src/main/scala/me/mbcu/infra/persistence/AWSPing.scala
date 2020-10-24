package me.mbcu.infra.persistence

import me.mbcu.domain.shared.Done

trait AWSPing[F[_]] {

  def testAccessAndIAMPermission(): F[Done]

}
