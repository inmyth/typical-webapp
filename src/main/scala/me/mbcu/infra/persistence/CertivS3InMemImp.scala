package me.mbcu.infra.persistence

import java.io.File

import me.mbcu.config.Config.RepositoryConfig.S3Config
import me.mbcu.domain.services.CertivFileRepository
import me.mbcu.domain.services.certivmanagement.FileRepo
import me.mbcu.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

import scala.collection.concurrent.TrieMap

private[persistence] object CertivS3InMemImp {
  private val storage: TrieMap[String, File] = TrieMap.empty
}

private[persistence] class CertivS3InMemImp(s3Config: S3Config)(implicit scheduler: Scheduler)
    extends CertivFileRepository(scheduler) {

  import me.mbcu.infra.persistence.CertivS3InMemImp._

  override def put(target: FileRepo.S3Path, file: File): Task[Done] =
    Task {
      storage.put(target.toString, file)
      Done
    }

  override def testAccessAndIAMPermission(): Task[Done] =
    for {
      _ <- put(s3Config.iamTestFilePath, s3Config.iamTestFile)
      - <- Task(storage.remove(s3Config.iamTestFilePath.toString))
    } yield Done
}
