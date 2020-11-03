package me.mbcu.domain.repository.certivmanagement

import java.io.File

import awscala.s3.{Bucket, S3}
import com.amazonaws.services.s3.model.DeleteObjectRequest
import me.mbcu.config.Config.RepositoryConfig.S3Config
import me.mbcu.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler
import me.mbcu.domain.repository.RepoHelper.S3Path

private[certivmanagement] object CertivS3Imp {}

private[certivmanagement] class CertivS3Imp(s3: S3, s3Config: S3Config)(implicit scheduler: Scheduler)
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