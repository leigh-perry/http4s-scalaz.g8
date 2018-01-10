package $package$.service

import $package$.shared.Config.{Endpoint, HostName, PortNumber}
import $package$.shared.{AppFailure, Config}

import scalaz.\/
import scalaz.syntax.either._

object AppMain {
  def main(args: Array[String]): Unit = {
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
