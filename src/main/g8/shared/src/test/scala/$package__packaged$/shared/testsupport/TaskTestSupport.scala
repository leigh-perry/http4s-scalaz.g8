package $package$.shared.testsupport

import java.time.Duration
import java.util.concurrent.TimeUnit

import scalaz.\/
import scalaz.concurrent.Task
import scalaz.syntax.either._

object TaskTestSupport {
  private val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(getClass)

  val taskMatchPauseMillis = 100L

  def awaitResult[T](f: () => Task[T], expected: T, limitMs: Long): Boolean = awaitResultF[T](f, _ == expected, limitMs)

  def awaitResultF[T](f: () => Task[T], matcher: T => Boolean, limitMs: Long): Boolean = {

    val limitNs = TimeUnit.MILLISECONDS.toNanos(limitMs)
    val nsStart = System.nanoTime

    @annotation.tailrec
    def go(): String \/ Boolean = {
      val v = f().unsafePerformSync
      if (matcher(v)) {
        true.right
      } else {
        val ns = System.nanoTime - nsStart
        if (ns > limitNs) {
          val message = s"awaitResultF timed out after \${Duration.ofNanos(ns)} with value \$v"
          log.error(message)
          message.left
        } else {
          Thread.sleep(taskMatchPauseMillis)
          go()
        }
      }
    }

    val result = go()
    result.isRight
  }

}
