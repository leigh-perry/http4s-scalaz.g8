package $package$.shared.testsupport.test

import java.util.concurrent.atomic.AtomicInteger

import $package$.shared.testsupport.TaskTestSupport
import utest._

import scalaz.concurrent.Task

object TaskTestSupportTest extends TestSuite {

  val tests =
    Tests {
      "TaskTestSupport tests" - {

        "test awaitResult success" - {
          val counter = new AtomicInteger()
          val f: () => Task[Int] = () => Task.now(counter.incrementAndGet())
          assert(TaskTestSupport.awaitResult(f, 3, TaskTestSupport.taskMatchPauseMillis * 20L))
        }

        "test awaitResult timeout" - {
          val counter = new AtomicInteger()
          val f: () => Task[Int] = () => Task.now(counter.incrementAndGet())
          assert(!TaskTestSupport.awaitResult(f, 30, TaskTestSupport.taskMatchPauseMillis * 4L))
        }
      }
    }

}
