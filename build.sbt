name := "Game"

version := "0.1"

scalaVersion := "2.13.0"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
libraryDependencies += "org.scalatest" % "scalatest_2.13" % "3.0.8" % "test"
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"


//libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.144-R12"
//// Add dependency on JavaFX library based on JAVA_HOME variable
//unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/lib/scalafx_2.13-12.0.2-R18.jar"))

// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "12.0.2-R18"
resolvers += Resolver.sonatypeRepo("snapshots")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xcheckinit", "-encoding", "utf8", "-feature")

// Fork a new JVM for 'run' and 'test:run', to avoid JavaFX double initialization problems
fork := true

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add JavaFX dependencies
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m=>
  "org.openjfx" % s"javafx-$m" % "12.0.2" classifier osName
)