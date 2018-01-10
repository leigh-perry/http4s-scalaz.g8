import sbt.Project
import Dependencies._

val projectName = "\$projectName-\$id"

lazy val gitCommitIdentifier: String = {
  import sys.process._

  val stdout = new StringBuilder
  val stderr = new StringBuilder
  val status = "git rev-parse HEAD" ! ProcessLogger(stdout.append(_), stderr.append(_))

  val s = stdout.toString
  if (status == 0) {
    val trimmed = s.trim
    if (trimmed.length == 0) "(none)" else trimmed
  } else {
    "(error retrieving git commit identifier)"
  }
}

lazy val commonSettings =
  ProjectDefaults.settings ++
    Seq(
      name := projectName,
      organization := "$organisation$",
      scalaVersion := "$scala_version$"
    )

val tests = "compile->compile;test->test"

lazy val shared =
  module(id = "shared", deps = Seq(utest % "test", scalacheck % "test"))
    .enablePlugins(BuildInfoPlugin)
    .settings(
      buildInfoKeys :=
        Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion) ++
          Seq[BuildInfoKey](
            libraryDependencies,
            BuildInfoKey.action("buildTime")(java.time.LocalDateTime.now),
            BuildInfoKey.action("gitCommitIdentifier")(gitCommitIdentifier)
          ),
      buildInfoPackage := "$package$"
    )

lazy val service = module(id = "service", deps = Seq()).dependsOn(shared % tests)

lazy val root =
  (project in file("."))
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
  Project(id = id, base = file(id), settings = settings)
    .settings(
      name := s"\$projectName-\$id",
      libraryDependencies ++= deps
    )
}
