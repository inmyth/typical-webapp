package me.mbcu.domain.service.certivmanagement

import me.mbcu.domain.model.certivmanagement.User
import me.mbcu.domain.repository.certivmanagement.{CertivDynamoRepository, CertivFileRepository, DynamoRepo, FileRepo}
import me.mbcu.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

object Implementations {

  def default(dynamoRepo: CertivDynamoRepository, fileRepo: CertivFileRepository)(implicit
    ec: Scheduler
  ): Service[Task] =
    new Default(dynamoRepo, fileRepo)

}

final class Default private[certivmanagement] (dynamoRepo: CertivDynamoRepository, fileRepo: CertivFileRepository)(
  implicit ec: Scheduler
) extends Service[Task] {

  override def insert(user: User): Task[Done] = dynamoRepo.put(user)

}
