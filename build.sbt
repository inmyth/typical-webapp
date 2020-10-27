name := "typical-webapp"

version := "0.1"

scalaVersion := "2.12.12"
val tapir = "0.17.0-M5"

// Task
libraryDependencies += "io.monix" %% "monix" % "3.2.2"
// Conf reader and converter
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.14.0"
// AWS SDK for Scala
libraryDependencies += "com.github.seratch" %% "awscala-s3"       % "0.8.+"
libraryDependencies += "com.github.seratch" %% "awscala-dynamodb" % "0.8.+"
// Server + Typed endpoints
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-core"           % tapir
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-vertx-server"   % tapir
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-jsoniter-scala" % tapir
//libraryDependencies += "com.softwaremill.sttp.tapir"   %% "tapir-akka-http-server" % tapir
//libraryDependencies += "com.softwaremill.sttp.client3" %% "core"                   % "3.0.0-RC6"
libraryDependencies ++= Seq(
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core"   % "2.6.2",
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.6.2" % "provided"
)
// Tests
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test
