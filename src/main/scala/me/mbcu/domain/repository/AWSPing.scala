package me.mbcu.domain.repository

import me.mbcu.domain.shared.Done

trait AWSPing[F[_]] {

  def testAccessAndIAMPermission(): F[Done]

}
