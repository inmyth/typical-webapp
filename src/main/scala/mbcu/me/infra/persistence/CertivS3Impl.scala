package mbcu.me.infra.persistence

import java.io.File

import awscala.s3.{Bucket, S3}
import mbcu.me.config.Config.RepositoryConfig.S3Config
import mbcu.me.domain.services.CertivFileRepository
import mbcu.me.domain.services.certivmanagement.FileRepo.S3Path
import mbcu.me.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

private[persistence] object CertivS3Impl {}

private[persistence] class CertivS3Impl(s3: S3, s3Config: S3Config)(implicit scheduler: Scheduler)
    extends CertivFileRepository(scheduler) {

  val bucket: Bucket = s3Config.bucket

  override def put(target: S3Path, file: File): Task[Done] =
    for {
      _ <- Task { s3.put(bucket, target.toString, file) }
    } yield Done

}
