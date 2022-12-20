val scala212 = "2.12.17"
val scala213 = "2.13.8"

ThisBuild / scalaVersion := scala212
ThisBuild / version := "1.0"
ThisBuild / name := "sbt-multi-module-project"
ThisBuild / organization := "com.devtokki"

// add external resolver
// resolvers += Resolver.url("my-test-repo", url("https://devtokki.com/repository/..."))

// add Maven local repo
//resolver += Resolver.mavenLocal

// custom tasks
lazy val printerTask = taskKey[Unit]("Custom Printer task")
printerTask := { // binding code to task
  val uuidTask = uuidStringTask.value
  println(s"Generated uuid from task: $uuidTask")

  val uuidSetting = uuidStringSetting.value
  println(s"Generated setting from task: $uuidSetting")
  CustomTaskPrinter.print()
}

lazy val uuidStringTask = taskKey[String]("Random UUID generator")
uuidStringTask := {
  StringTask.strTask()
}


// custom settings
lazy val uuidStringSetting = settingKey[String]("Random UUID setting")
uuidStringSetting := {
  val uuid = StringTask.strTask()
  // add some more code
  uuid
}

// command aliases
addCommandAlias("ci", "compile;test;assembly")

lazy val core = (project in file("core")).settings(
  assembly / mainClass := Some("com.devtokki.CoreApp"),
  libraryDependencies += Constants.rootPackage %% "cats-effect" % "3.3.0",
  crossScalaVersions := List(scala212, scala213)
)
lazy val server = (project in file("server")).dependsOn(core)

lazy val root = (project in file(".")).aggregate(core, server)
