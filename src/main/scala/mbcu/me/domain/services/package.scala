package mbcu.me.domain

package object services {

  type CertivManagement[F[_]] = mbcu.me.domain.services.certivmanagement.Service[F]
  val CertivManagement: certivmanagement.Implementations.type = certivmanagement.Implementations

  type CertivManagementError = mbcu.me.domain.services.certivmanagement.Error

  type CertivDynamoRepository = certivmanagement.DynamoRepo
  val CertivDynamoRepository = certivmanagement.DynamoRepo

  type CertivFileRepository = certivmanagement.FileRepo
  val CertivFileRepository = certivmanagement.FileRepo

}
