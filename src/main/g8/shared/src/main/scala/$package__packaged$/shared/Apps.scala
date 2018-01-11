package $package$.shared

import java.io.{PrintWriter, StringWriter, Writer}

import org.slf4j.{Logger, LoggerFactory}

import scalaz.\/
import scalaz.syntax.either._

object Apps {
  private val log: Logger = LoggerFactory.getLogger(getClass)

  def using[A <: AutoCloseable, B](resource: => A, onFailure: Throwable => AppFailure)(f: A => B): AppFailure \/ B = {
    try {
      f(resource).right
    } catch {
      case e: Throwable => onFailure(e).left
    } finally {
      resource.close()
    }
  }

  def supervise[A](resource: => A, onFailure: Throwable => AppFailure): AppFailure \/ A = {
    try {
      resource.right
    } catch {
      case e: Throwable => onFailure(e).left
    }
  }

  def logEnvironment(): Unit = {
    import scala.collection.JavaConverters._

    val separator = "================================================================================"

    log.info(separator)
    log.info("System properties")
    log.info(separator)
    for ((k, v) <- System.getProperties.asScala) {
      log.info(k + "=" + v)
    }

    log.info(separator)
    log.info("Environment variables")
    log.info(separator)
    for ((k, v) <- System.getenv.asScala) {
      log.info(k + "=" + v)
    }

    log.info(separator)
  }

  def className(o: AnyRef): String = {
    o.getClass.getSimpleName.replaceAll("\\\\\$", "")
  }

  def exceptionDetails(e: Throwable): String = {
    "Exception details:\n  " + stackTraceOf(e)
  }

  def stackTraceOf(e: Throwable): String = {
    val sw: Writer = new StringWriter
    e.printStackTrace(new PrintWriter(sw))
    sw.toString
  }

  object extensions {

    implicit class RichLogger(val log: Logger) extends AnyVal {
      def exception(e: Throwable): Unit = log.error(exceptionDetails(e))
    }

  }
}
