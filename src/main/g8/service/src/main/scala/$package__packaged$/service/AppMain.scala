package $package$.service

import $package$.shared.Apps
import $package$.shared.Config.{Endpoint, HostName, PortNumber}
import $package$.shared.{AppFailure, Config}
import $package$.BuildInfo

import scalaz.\/
import scalaz.syntax.either._

object AppMain {
  def main(args: Array[String]): Unit = {
    val banner =
      Apps.className(this) + " process version: " + BuildInfo.version +
        "\n  scala-version: " + BuildInfo.scalaVersion +
        "\n  sbt-version: " + BuildInfo.sbtVersion +
        "\n  library-dependencies: " + BuildInfo.libraryDependencies +
        "\n  build-time: " + BuildInfo.buildTime +
        "\n  git-commit-identifier: " + BuildInfo.gitCommitIdentifier
    println(banner)
    Apps.logEnvironment()

    val envName = if (args.length == 0) "dev" else args(0)
    val outcome =
      for {
        c <- getConfig(envName)
        r <- runApp(c)
      } yield r

    // TODO logging
    outcome.fold(
      appFailure => println("Failed: " + appFailure),
      _ => println("Completed successfully")
    )
  }

  private def runApp(c: Config): AppFailure \/ Unit = {
    println(c) // TODO log config etc
    ().right
  }

  private def getConfig(envName: String): AppFailure \/ Config = {
    envName match {
      case "dev" => configOf("dev.appserver.com", 1234).right
      case "uat" => configOf("uat.appserver.com", 1234).right
      case "prod" => configOf("prod.appserver.com", 1234).right
      case _ => AppFailure.InvalidConfig("Unknown config: " + envName).left
    }
  }

  private def configOf(host: String, port: Int) = {
    Config(Endpoint(HostName(host), PortNumber(port)))
  }
}
