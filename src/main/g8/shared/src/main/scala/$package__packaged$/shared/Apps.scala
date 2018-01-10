package $package$.shared

import java.io.{PrintWriter, StringWriter, Writer}

object Apps {
  def logEnvironment(): Unit = {
    import scala.collection.JavaConverters._

    val separator = "================================================================================"

    // TODO logging
    println(separator)
    println("System properties")
    println(separator)
    for ((k, v) <- System.getProperties.asScala) {
      println(k + "=" + v)
    }

    println(separator)
    println("Environment variables")
    println(separator)
    for ((k, v) <- System.getenv.asScala) {
      println(k + "=" + v)
    }

    println(separator)
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
}
