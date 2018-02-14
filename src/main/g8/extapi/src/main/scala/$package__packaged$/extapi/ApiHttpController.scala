package $package$.extapi

import java.lang.{Long => JLong}
import java.time.LocalDateTime

import $package$.extapi.model.Model.HourlyAggregatedFullness
import $package$.extapi.service.CrudService
import org.http4s._
import org.http4s.dsl._
import org.slf4j.Logger

import scalaz.concurrent.Task

class ApiHttpController(cfg: Config, viewService: CrudService[Long, HourlyAggregatedFullness]) {
  private val log: Logger = org.slf4j.LoggerFactory.getLogger(getClass)

  import $package$.extapi.model.Model.HourlyAggregatedFullness._
  import $package$.extapi.model.Model._
  import $package$.extapi.model.argonautSupport._

  private val errorHandler: PartialFunction[Throwable, Task[Response]] = {
    case RowNotFound(name) => BadRequest(s"Row \$name not found")
    case RowAlreadyExists(name) => Conflict(s"Row \$name already exists")
    case ApiNotImplemented(method) => NotImplemented(s"API method \$method is not implemented")
  }

  // TODO swagger via https://github.com/http4s/rho

  val routes: HttpService =
    HttpService {

      case GET -> Root / "test" =>
        log.info("GET test message")
        Ok(Task.now(s"Test string from \${this.getClass.getCanonicalName} at \${LocalDateTime.now}"))
          .handleWith(errorHandler)

      case GET -> Root / "hourly" =>
        log.info("GET all")
        Ok(viewService.readAll())
          .handleWith(errorHandler)

      case GET -> Root / "hourly" / idString => {
        log.info(s"GET \$idString")
        val id = JLong.valueOf(idString)

        Ok(viewService.read(id))
          .handleWith(errorHandler)
      }

      case req @ POST -> Root / "hourly" =>
        req.decode[HourlyAggregatedFullness] {
          row =>
            viewService.create(row)
              .flatMap(v => Created(row))
              .handleWith(errorHandler)
        }

      case req @ PUT -> Root / "hourly" / name =>
        req.decode[HourlyAggregatedFullness] {
          row =>
            viewService.update(row)
              .flatMap(_ => Accepted())
              .handleWith(errorHandler)
        }

      case DELETE -> Root / "hourly" / id =>
        viewService.delete(id)
          .flatMap(_ => Ok())
          .handleWith(errorHandler)
    }

  import org.http4s.server.middleware.CORS

  // Add CORS support
  val service = CORS(routes)

}
