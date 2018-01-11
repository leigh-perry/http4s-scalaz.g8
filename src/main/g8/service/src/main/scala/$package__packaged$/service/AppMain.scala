package $package$.service

import $package$.BuildInfo
import $package$.shared.Config.{Endpoint, HostName, PortNumber}
import $package$.shared.{AppFailure, Apps, Config}

import scalaz.\/
import scalaz.syntax.either._

object AppMain {

  import org.slf4j.{Logger, LoggerFactory}

  private val log: Logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]): Unit = {
    val banner =
      Apps.className(this) + " process version: " + BuildInfo.version +
        "\n  scala-version: " + BuildInfo.scalaVersion +
        "\n  sbt-version:   " + BuildInfo.sbtVersion +
        "\n  build-time:    " + BuildInfo.buildTime +
        "\n  git-commit:    " + BuildInfo.gitCommitIdentifier +
        "\n  libraries:     " + BuildInfo.libraryDependencies
    log.info(banner)
    Apps.logEnvironment()

    val envName = if (args.length == 0) "dev" else args(0)
    val outcome =
      for {
        c <- getConfig(envName)
        r <- runApp(c)
      } yield r

    outcome.fold(
      appFailure => log.error("Failed: " + appFailure),
      _ => log.info("Completed successfully")
    )
  }

  private def runApp(c: Config): AppFailure \/ Unit = {
    log.info(c.toString)
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
