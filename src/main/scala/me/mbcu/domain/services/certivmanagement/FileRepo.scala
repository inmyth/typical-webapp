package me.mbcu.domain.services.certivmanagement

import awscala.s3.S3
import me.mbcu.domain.services.certivmanagement.FileRepo.S3Path
import me.mbcu.config.Config.RepositoryConfig.S3Config
import me.mbcu.domain.shared.Done
import me.mbcu.infra.persistence.CertivFileStorage
import monix.eval.Task
import monix.execution.Scheduler

private[services] abstract class FileRepo(scheduler: Scheduler) {

  def put(target: S3Path, file: java.io.File): Task[Done]

}

object FileRepo {

  final case class S3Path(folders: Option[Seq[String]], file: String) {

    override def toString: String =
      folders match {
        case Some(value) => s"""${(value :+ "").mkString("/")}$file"""
        case _           => file
      }
  }

  def s3(scheduler: Scheduler, s3: S3, s3Config: S3Config): FileRepo = new CertivFileStorage(s3, s3Config)(scheduler)

}
