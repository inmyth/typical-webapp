package me.mbcu.domain.repository.certivmanagement

import awscala.s3.S3
import me.mbcu.config.Config.RepositoryConfig.S3Config
import me.mbcu.domain.repository.AWSPing
import me.mbcu.domain.repository.RepoHelper.S3Path
import me.mbcu.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

private[certivmanagement] abstract class FileRepo(scheduler: Scheduler) extends AWSPing[Task] {

  def put(target: S3Path, file: java.io.File): Task[Done]

}

private[certivmanagement] object FileRepo {

  def s3(scheduler: Scheduler, s3: S3, s3Config: S3Config): FileRepo = new CertivFileStorage(s3, s3Config)(scheduler)

}
