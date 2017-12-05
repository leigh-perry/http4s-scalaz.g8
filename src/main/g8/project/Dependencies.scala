import sbt._

object Dependencies {
  private object Version {
    val argonaut         = "6.2"
    val fs2Scalaz        = "0.2.0"
    val scalaz           = "7.2.17"
    val specs2           = "4.0.2"
  }

  val argonaut              = "io.argonaut"              %% "argonaut"              % Version.argonaut
  val fs2Scalaz             = "co.fs2"                   %% "fs2-scalaz"            % Version.fs2Scalaz
  val scalazConcurrent      = "org.scalaz"               %% "scalaz-concurrent"     % Version.scalaz
  val scalazCore            = "org.scalaz"               %% "scalaz-core"           % Version.scalaz
  val scalazEffect          = "org.scalaz"               %% "scalaz-effect"         % Version.scalaz
  val specs2Core            = "org.specs2"               %% "specs2-core"           % Version.specs2
  val specs2Matchers        = "org.specs2"               %% "specs2-matcher-extra"  % Version.specs2
  val specs2ScalaCheck      = "org.specs2"               %% "specs2-scalacheck"     % Version.specs2
  val specs2Scalaz          = "org.specs2"               %% "specs2-scalaz"         % Version.specs2
}
