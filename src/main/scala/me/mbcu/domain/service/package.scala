package me.mbcu.domain

package object service {

  type CertivManagement[F[_]] = me.mbcu.domain.service.certivmanagement.Service[F]
  val CertivManagement: certivmanagement.Implementations.type = certivmanagement.Implementations

  type CertivManagementError = me.mbcu.domain.service.certivmanagement.Error

}
