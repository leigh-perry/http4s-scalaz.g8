import sbt.Project
import Dependencies._

val projectName = "$name$"

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

lazy val extapi =
  module(
    id = "extapi",
    deps =
      Seq(
        http4sDsl,
        http4sBlazeServer,
        http4sArgonaut,
        argonaut,
        argonautScalaz,
        doobieCore,
        doobieHikari
      )
  ).dependsOn(shared, shared % tests)

lazy val root =
  (project in file("."))
    .aggregate(shared, extapi)
    .dependsOn(extapi)
    .settings(commonSettings)
    .settings(
      aggregate in update := false,
      updateOptions := updateOptions.value.withCachedResolution(true),
      mainClass in Compile := (mainClass in `extapi` in Compile).value,
      fullClasspath in Runtime ++= (fullClasspath in `extapi` in Runtime).value
    )

// Prevent clash in sbt assembly
assemblyExcludedJars in assembly := {
  val cp = (fullClasspath in assembly).value
  cp.filter(_.data.getName.contains("log4j"))
}

assemblyMergeStrategy in assembly := {
//  case PathList(ps @ _*) if ps.exists(_.contains("io.netty.versions.properties")) => MergeStrategy.first
//  case PathList(ps @ _*) if ps.exists(_.contains("publicsuffix")) => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

// Create assembly only for top level project
aggregate in assembly := false

// support docker task ("sbt docker" command)
enablePlugins(DockerPlugin)

dockerfile in docker := {
  // The assembly task generates a fat JAR file
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/\${artifact.name}"

  new Dockerfile {
    from("azul/zulu-openjdk:9")
    add(new File("docker-components/run_app.sh"), "/app/run_app.sh")
    add(artifact, artifactTargetPath)
    entryPoint("/app/run_app.sh", artifactTargetPath)
  }
}

buildOptions in docker := BuildOptions(cache = false)

imageNames in docker :=
  Seq(
    // Sets the latest tag
    ImageName(s"\${organization.value}/$name;format="normalize"$:latest")
  )


def module(id: String, settings: Seq[Def.Setting[_]] = commonSettings, deps: Seq[ModuleID] = Vector()): Project = {
  Project(id = id, base = file(id), settings = settings)
    .settings(
      name := s"\$projectName-\$id",
      libraryDependencies ++= deps ++ Seq("org.scala-lang" % "scala-reflect" % "$scala_version$")
    )
}
