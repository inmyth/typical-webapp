package me.mbcu.domain.repository

object RepoHelper {

  final case class S3Path(folders: Option[Seq[String]], file: String) {

    override def toString: String =
      folders match {
        case Some(value) => s"""${(value :+ "").mkString("/")}$file"""
        case _           => file
      }
  }
}
