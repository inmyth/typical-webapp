package me.mbcu.domain.repository

package object certivmanagement {

  type CertivDynamoInMem = certivmanagement.CertivDynamoInMemImp
  val CertivDynamoInMem = certivmanagement.CertivDynamoInMemImp

  type CertivDynamo = certivmanagement.CertivDynamoImp
  val certivDynamo = certivmanagement.CertivDynamoImp

  type CertivFileStorage = certivmanagement.CertivS3Imp
  val CertivFileStorage = certivmanagement.CertivS3Imp

  type CertivDynamoRepository = DynamoRepo
  val CertivDynamoRepository = certivmanagement.DynamoRepo

  type CertivFileRepository = FileRepo
  val CertivFileRepository = certivmanagement.FileRepo

}
