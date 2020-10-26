name := "typical-webapp"

version := "0.1"

scalaVersion := "2.12.12"

// Task
libraryDependencies += "io.monix" %% "monix" % "3.2.2"
// Conf reader and converter
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.14.0"
// AWS SDK for Scala
libraryDependencies += "com.github.seratch" %% "awscala-s3"       % "0.8.+"
libraryDependencies += "com.github.seratch" %% "awscala-dynamodb" % "0.8.+"
// Server + Typed endpoints
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-core"         % "0.17.0-M5"
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-vertx-server" % "0.17.0-M5"
// Tests
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test
