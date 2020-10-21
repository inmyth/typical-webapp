package mbcu.me.infra.persistence

import java.io.File

import awscala.s3.S3
import mbcu.me.config.Config.S3Config
import mbcu.me.domain.services.CertivFileRepository
import mbcu.me.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

private[persistence] object CertivS3Impl {}

private[persistence] class CertivS3Impl(scheduler: Scheduler, s3: S3, s3Config: S3Config)
    extends CertivFileRepository(scheduler) {

  override def putDocument(target: Path, file: File): Task[Done] = ???
}
