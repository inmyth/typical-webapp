package mbcu.me.infra

package object persistence {

  type CertivDynamoInMem = persistence.CertivDynamoInMemImpl
  val CertivDynamoInMem = persistence.CertivDynamoInMemImpl

  type CertivFileStorage = persistence.CertivS3Impl
  val CertivFileStorage = persistence.CertivS3Impl

}
