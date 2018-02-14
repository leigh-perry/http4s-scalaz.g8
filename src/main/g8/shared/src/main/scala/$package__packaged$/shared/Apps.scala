package $package$.shared

import java.io.{PrintWriter, StringWriter, Writer}

import scalaz.\/
import scalaz.concurrent.Task
import scalaz.syntax.either._

object Apps {
  private val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(getClass)

  import Apps.extensions._

  import scala.collection.JavaConverters._

  val systemProperties: Map[String, String] = System.getProperties.asScala.toMap
  val environmentVariables: Map[String, String] = System.getenv.asScala.toMap

  def using[A <: AutoCloseable, B](resource: => A, onFailure: Throwable => AppFailure, silent: Boolean = false)
    (f: A => B): AppFailure \/ B = {
    val instantiated = resource // deref by-name resource only once
    try {
      f(instantiated).right
    } catch {
      case e: Throwable =>
        if (!silent) {
          log.exception(e)
        }
        onFailure(e).left
    } finally {
      instantiated.close()
    }
  }

  def supervise[A](resource: => A, onFailure: Throwable => AppFailure, silent: Boolean = false): AppFailure \/ A = {
    try {
      resource.right
    } catch {
      case e: Throwable =>
        if (!silent) {
          log.exception(e)
        }
        onFailure(e).left
    }
  }

  def deferred[E <: Throwable, T](value: => E \/ T): Task[T] = {
    Task.suspend(Task.fromDisjunction(value))
    }

  def overridable(name: String, defaultValue: String): String = {
    systemProperties.getOrElse(name, environmentVariables.getOrElse(name, defaultValue))
  }

  def overridableInt(name: String, defaultValue: Int): Int = {
    val s = overridable(name, "")
    if (s.isEmpty) defaultValue else s.toInt
  }

  def logEnvironment(): Unit = {
    logContents("System properties", systemProperties)
    logContents("Environment variables", environmentVariables)
  }

  private def logContents(title: String, map: Map[String, String]): Unit = {
    val separator = "================================================================================"

    log.info(separator)
    log.info(title)
    log.info(separator)

    val sorted = map.toSeq.sortBy(_._1)
    for ((k, value) <- sorted) {
      val v =
        value
          .replaceAll("\n", """\\n""")
          .replaceAll("\r", """\\r""")
          .replaceAll("\t", """\\t""")

      log.info(s"\$k=\$v")
    }
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

  def runScript(filepath: String): Unit = {
    println(s"Running \$filepath from \${System.getProperty("user.dir")}")

    import scala.language.postfixOps
    import sys.process._
    //noinspection ScalaUnnecessaryParentheses
    val output = (filepath !!)

    val s = output.trim
    println(
      s match {
        case "" => "(no script output)"
        case _ =>
          s"""
             |-----------------script output---------------------
             |\$s
             |---------------------------------------------------
           """.stripMargin.trim
      }
    )
  }

  object extensions {

    import org.slf4j.Logger

    implicit class RichLogger(val log: Logger) extends AnyVal {
      def exception(e: Throwable): Unit = log.error(exceptionDetails(e))
    }

  }
}
