import sbt._
import sbt.Keys._
import sbt.Resolver

import Dependencies._

object ProjectDefaults {
  private val scalacOptionsWarnings =
    Set(
      //"-Xfatal-warnings",
      "-Xlint",
      "-Yno-adapted-args",
      "-Yrangepos",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused-import",
      "-Ywarn-value-discard"
    )

  val settings =
    Seq(
      scalaVersion := "$scala_version$",

      scalacOptions ++=
        Seq(
          "-deprecation",
          "-encoding", "UTF-8",
          "-feature",
          "-language:existentials",
          "-language:higherKinds",
          "-language:implicitConversions",
          "-unchecked",
          "-Xfuture",
          "-target:jvm-1.8"
        ) ++ scalacOptionsWarnings,

      fork in Test := true,
      fork in run := true,

      testFrameworks += new TestFramework("utest.runner.Framework"),

        javaOptions ++=
        Seq(
          "-Xms1G",
          "-Xmx1G",
          "-XX:MaxMetaspaceSize=512M",
          "-XX:+HeapDumpOnOutOfMemoryError",
          "-XX:HeapDumpPath=./heap-dump.hprof"
        ),

      // Disable warnings in console
      scalacOptions in(Compile, console) ~= { _ filterNot scalacOptionsWarnings.apply },
      scalacOptions in(Test, console) ~= { _ filterNot scalacOptionsWarnings.apply },

      resolvers ++=
        Seq(
          Resolver.mavenLocal,
          Resolver.typesafeRepo("releases")
        ),

      //Stops the auto creation of java / scala-2.11 directories
      unmanagedSourceDirectories in Compile ~= { _.filter(_.exists) },
      unmanagedSourceDirectories in Test ~= { _.filter(_.exists) },

      // All subprojects to use
      libraryDependencies ++=
        Seq(
          slf4j,
          logback,
          scalazCore,
          scalazConcurrent
        ),

      addCompilerPlugin("org.spire-math" % "kind-projector" % "0.9.5" cross CrossVersion.binary)
    )

}
