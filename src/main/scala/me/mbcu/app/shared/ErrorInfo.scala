package me.mbcu.app.shared

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

sealed trait ErrorInfo
private[app] case object Exists        extends ErrorInfo
private[app] case object NotFound      extends ErrorInfo
private[app] case object Active        extends ErrorInfo
private[app] case object Deleted       extends ErrorInfo
private[app] case object Blocked       extends ErrorInfo
private[app] case object NotAuthorized extends ErrorInfo
private[app] case object BadToken      extends ErrorInfo
private[app] case object BadParam      extends ErrorInfo
private[app] case object NoResources   extends ErrorInfo
private[app] case class BadUserName()  extends ErrorInfo
private[app] case object BadEmail      extends ErrorInfo
private[app] case object BadRole       extends ErrorInfo
private[app] case object BadUserId     extends ErrorInfo

private[app] object Error {
  implicit val errorInfoCodec: JsonValueCodec[ErrorInfo] = JsonCodecMaker.make

}
