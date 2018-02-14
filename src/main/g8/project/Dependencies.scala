import sbt._

object Dependencies {
  private object Version {
    val scalaz = "7.2.19"
    val logback = "1.2.3"
    val slf4j = "1.7.25"

    val http4s = "0.16.5a"
    val argonaut = "6.2"
    val doobie = "0.4.4"

    val utest = "0.6.0"
    val scalacheck = "1.13.5"
  }

  val scalazConcurrent = "org.scalaz" %% "scalaz-concurrent" % Version.scalaz
  val scalazCore = "org.scalaz" %% "scalaz-core" % Version.scalaz
  val scalazEffect = "org.scalaz" %% "scalaz-effect" % Version.scalaz
  val logback = "ch.qos.logback" % "logback-classic" % Version.logback
  val slf4j = "org.slf4j" % "slf4j-api" % Version.slf4j

  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % Version.http4s
  val http4sDsl = "org.http4s" %% "http4s-dsl" % Version.http4s
  val argonaut = "io.argonaut" %% "argonaut" % Version.argonaut
  val argonautScalaz = "io.argonaut" %% "argonaut-scalaz" % Version.argonaut
  val http4sArgonaut = "org.http4s" %% "http4s-argonaut" % Version.http4s

  val doobieCore = "org.tpolecat" %% "doobie-core" % Version.doobie
  val doobieHikari = "org.tpolecat" %% "doobie-hikari" % Version.doobie

  val utest = "com.lihaoyi" %% "utest" % Version.utest
  val scalacheck = "org.scalacheck" %% "scalacheck" % Version.scalacheck
}
