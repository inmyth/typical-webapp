package mbcu.me.domain.services.usermanagement

import mbcu.me.domain.shared.Done
import monix.eval.Task
import monix.execution.Scheduler

private[services] abstract class FileRepo(scheduler: Scheduler) {

  final case class Path(folder: String, file: String)

  def put(target: Path, file: java.io.File): Task[Done]

}

object FileRepo {}
