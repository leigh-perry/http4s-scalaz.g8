import sbt._

object Dependencies {
  private object Version {
    val argonaut    = "6.2"
    val scalaz      = "7.2.18"
    val logback     = "1.2.3"
    val slf4j       = "1.7.25"

    val utest       = "0.6.0"
    val scalacheck  = "1.13.5"
  }

  val argonaut          = "io.argonaut"     %% "argonaut"           % Version.argonaut
  val scalazConcurrent  = "org.scalaz"      %% "scalaz-concurrent"  % Version.scalaz
  val scalazCore        = "org.scalaz"      %% "scalaz-core"        % Version.scalaz
  val scalazEffect      = "org.scalaz"      %% "scalaz-effect"      % Version.scalaz
  val logback           = "ch.qos.logback"  % "logback-classic"     % Version.logback
  val slf4j             = "org.slf4j"       % "slf4j-api"           % Version.slf4j
  val utest             = "com.lihaoyi"     %% "utest"              % Version.utest
  val scalacheck        = "org.scalacheck"  %% "scalacheck"         % Version.scalacheck
}
