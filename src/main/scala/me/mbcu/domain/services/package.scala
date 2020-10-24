package me.mbcu.domain

package object services {

  type CertivManagement[F[_]] = me.mbcu.domain.services.certivmanagement.Service[F]
  val CertivManagement: certivmanagement.Implementations.type = certivmanagement.Implementations

  type CertivManagementError = me.mbcu.domain.services.certivmanagement.Error

  type CertivDynamoRepository = certivmanagement.DynamoRepo
  val CertivDynamoRepository = certivmanagement.DynamoRepo

  type CertivFileRepository = certivmanagement.FileRepo
  val CertivFileRepository = certivmanagement.FileRepo

}
