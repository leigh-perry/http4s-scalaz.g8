import sbt._

object Dependencies {
  private object Version {
    val argonaut    = "6.2"
    val fs2Scalaz   = "0.2.0"
    val scalaz      = "7.2.18"
    val utest       = "0.6.0"
    val scalacheck  = "1.13.5"
  }

  val argonaut          = "io.argonaut"     %% "argonaut"           % Version.argonaut
  val fs2Scalaz         = "co.fs2"          %% "fs2-scalaz"         % Version.fs2Scalaz
  val scalazConcurrent  = "org.scalaz"      %% "scalaz-concurrent"  % Version.scalaz
  val scalazCore        = "org.scalaz"      %% "scalaz-core"        % Version.scalaz
  val scalazEffect      = "org.scalaz"      %% "scalaz-effect"      % Version.scalaz
  val utest             = "com.lihaoyi"     %% "utest"              % Version.utest
  val scalacheck        = "org.scalacheck"  %% "scalacheck"         % Version.scalacheck
}
