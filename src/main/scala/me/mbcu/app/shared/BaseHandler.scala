package me.mbcu.app.shared

import monix.eval.Task

abstract class BaseHandler {

  def procHeader(token: String): Task[Either[ErrorInfo, Token]] =
    Task {
      token match {
        case "secret" => Right(Token("secret"))
        case _        => Left(BadToken)
      }
    }

}
