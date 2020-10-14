package mbcu.me.helpers

import cats.data.WriterT
import monix.eval.Task

object Definitions {

  type Logged[A] = WriterT[Task, Vector[String], A]

}
