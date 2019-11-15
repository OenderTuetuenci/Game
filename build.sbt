name := "Game"

version := "0.1"

scalaVersion := "2.13.1"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
libraryDependencies += "org.scalatest" % "scalatest_2.13" % "3.0.8" % "test"
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")
addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.2.7")
