package me.mbcu.domain.services.certivmanagement

import me.mbcu.domain.shared.Done

trait AWSPing[F[_]] {

  def testAccessAndIAMPermission(): F[Done]

}
