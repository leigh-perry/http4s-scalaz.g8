package $package$.extapi

import $package$.BuildInfo
import $package$.extapi.model.Model.HourlyAggregatedFullness
import $package$.extapi.service.ReadService
import $package$.extapi.store.SqlStore
import $package$.shared.Apps.{overridable, overridableInt}
import $package$.shared.Newtype._
import $package$.shared.tagger._
import $package$.shared.{AppFailure, Apps}
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.ProcessApp

import scalaz.\/
import scalaz.concurrent.Task
import scalaz.stream.Process
import scalaz.syntax.either._

object AppMain extends ProcessApp {
  private val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(getClass)

  override def process(args: List[String]): Process[Task, Nothing] = {
    val banner =
      s"""\${Apps.className(this)} process version: \${BuildInfo.version}
         |  scala-version: \${BuildInfo.scalaVersion}
         |  sbt-version:   \${BuildInfo.sbtVersion}
         |  build-time:    \${BuildInfo.buildTime}
         |  git-commit:    \${BuildInfo.gitCommitIdentifier}
         |  library-dependencies: \${BuildInfo.libraryDependencies}"""
        .stripMargin
    log.info(banner)
    Apps.logEnvironment()

    val envName = overridable("DXRES_ENV", "dev")
    val outcome =
      for {
        cfg <- configFor(envName)
        _ = log.info(s"Using [\$envName] configuration: \${cfg.toString}")
        r <- Apps.supervise(runApp(cfg), HttpFailure)
      } yield r

    outcome.fold(
      appFailure => {
        val message = "Failed: " + appFailure
        log.error(message)
        throw new RuntimeException(message)
      },
      identity
    )
  }

  private def runApp(cfg: Config): Process[Task, Nothing] = {
    val store = new SqlStore(cfg)
    val viewService = new ReadService[Long, HourlyAggregatedFullness](cfg, store)
    val controller = new ApiHttpController(cfg, viewService)

    BlazeBuilder
      .bindHttp(cfg.restHttpPort.unwrap, "0.0.0.0")
      .mountService(controller.service)
      .serve
  }

  /** Scala as configuration DSL */
  private def configFor(envName: String): AppFailure \/ Config = {

    import Config._

    envName match {
      case "dev" =>
        val url = "jdbc:sqlserver://ddd.xxx.yyy:1234;database=nnnn"
        val databaseConfig =
          baseConfig.db.copy(
            url = DbUrl(url),
            user = DbUser("someusername"),
            password = DbPassword("PPPassword@123")
          )

        // TODO: why won't read under Azure?   val port = PortNumber(overridableInt("DXRES_HTTPPORT", baseConfig.restHttpPort.unwrap))
        val port = PortNumber(overridableInt("server.port", baseConfig.restHttpPort.unwrap))
        baseConfig.copy(db = databaseConfig, restHttpPort = port).right

      case "uat" =>
        baseConfig.right

      case "prod" =>
        baseConfig.right

      case _ =>
        AppFailure.InvalidConfig("Unknown environment: " + envName).left
    }
  }

  final case class HttpFailure(e: Throwable) extends AppFailure
}
