package mbcu.me.domain.services.certivmanagement

import awscala.s3.S3
import mbcu.me.config.Config.RepositoryConfig.S3Config
import mbcu.me.domain.services.certivmanagement.FileRepo.S3Path
import mbcu.me.domain.shared.Done
import mbcu.me.infra.persistence.CertivFileStorage
import monix.eval.Task
import monix.execution.Scheduler

private[services] abstract class FileRepo(scheduler: Scheduler) {

  def put(target: S3Path, file: java.io.File): Task[Done]

}

object FileRepo {

  final case class S3Path(folder: String, file: String) {
    override def toString: String = s"$folder/$file"
  }

  def s3(scheduler: Scheduler, s3: S3, s3Config: S3Config): FileRepo = new CertivFileStorage(s3, s3Config)(scheduler)

}
