package me.mbcu.infra

package object persistence {

  type CertivDynamoInMem = persistence.CertivDynamoInMemImp
  val CertivDynamoInMem = persistence.CertivDynamoInMemImp

  type CertivDynamo = persistence.CertivDynamoImp
  val certivDynamo = persistence.CertivDynamoImp

  type CertivFileStorage = persistence.CertivS3Imp
  val CertivFileStorage = persistence.CertivS3Imp

}
