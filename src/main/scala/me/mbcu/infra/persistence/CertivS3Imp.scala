package me.mbcu.infra.persistence

import java.io.File

import awscala.s3.{Bucket, S3, S3Object}
import com.amazonaws.services.s3.model.DeleteObjectRequest
import me.mbcu.config.Config.RepositoryConfig.S3Config
import me.mbcu.domain.services.CertivFileRepository
import me.mbcu.domain.services.certivmanagement.FileRepo.S3Path
import me.mbcu.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

private[persistence] object CertivS3Imp {}

private[persistence] class CertivS3Imp(s3: S3, s3Config: S3Config)(implicit scheduler: Scheduler)
    extends CertivFileRepository(scheduler) {

  val bucket: Bucket = s3Config.bucket

  override def put(target: S3Path, file: File): Task[Done] =
    for {
      _ <- Task { s3.put(bucket, target.toString, file) }
    } yield Done

  override def testAccessAndIAMPermission(): Task[Done] = {
    for {
      _ <- put(s3Config.iamTestFilePath, s3Config.iamTestFile)
      - <- Task {
        s3.deleteObject(new DeleteObjectRequest(bucket.name, s3Config.iamTestFilePath.toString))
      }
    } yield Done
  }

}
