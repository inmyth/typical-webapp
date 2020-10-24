name := "typical-webapp"

version := "0.1"

scalaVersion := "2.13.3"

// Task
libraryDependencies += "io.monix" %% "monix" % "3.2.2"
// Conf reader and converter
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.14.0"
// AWS SDK for Scala
libraryDependencies += "com.github.seratch" %% "awscala-s3"       % "0.8.+"
libraryDependencies += "com.github.seratch" %% "awscala-dynamodb" % "0.8.+"

// Tests
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test
