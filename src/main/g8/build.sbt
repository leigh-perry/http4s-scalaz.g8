import sbt.Project
import Dependencies._

val projectName = "\$projectName-\$id"

lazy val commonSettings =
  ProjectDefaults.settings ++ Seq(
    name := projectName,
    organization := "$organisation$",
    scalaVersion := "$scala_version$"
  )

val tests = "compile->compile;test->test"

lazy val shared =
  module(
    id = "shared",
    deps = Seq(
      specs2Core % "test",
      specs2ScalaCheck % "test"
    )
  )

lazy val service =
  module(
    id = "service",
    deps = Seq(specs2Scalaz % "test")
  ).dependsOn(shared % tests)

lazy val root = (project in file("."))
  .aggregate(shared, service)
  .dependsOn(service)
  .settings(commonSettings)
  .settings(
    aggregate in update := false,
    updateOptions := updateOptions.value.withCachedResolution(true),
    mainClass in Compile := (mainClass in `service` in Compile).value,
    fullClasspath in Runtime ++= (fullClasspath in `service` in Runtime).value
  )

def module(id: String, settings: Seq[Def.Setting[_]] = commonSettings, deps: Seq[ModuleID] = Vector()): Project = {
  Project(id = id, base = file(id), settings = settings).settings(
    name := s"\$projectName-\$id",
    libraryDependencies ++= deps
  )
}
