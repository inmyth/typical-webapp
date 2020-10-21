name := "typical-webapp"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies += "io.monix"              %% "monix"      % "3.2.2"
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.14.0"
libraryDependencies += "com.github.seratch"    %% "awscala-s3" % "0.8.+"
libraryDependencies += "org.scalatest"         %% "scalatest"  % "3.1.0" % Test
