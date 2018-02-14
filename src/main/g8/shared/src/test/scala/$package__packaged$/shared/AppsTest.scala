package $package$.shared

import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}

import $package$.shared.testsupport.UtestScalaCheck
import utest._

import scalaz.\/
import scalaz.syntax.either._

object AppsTest extends TestSuite with UtestScalaCheck {
  final case class TestFailure(e: Throwable) extends AppFailure
  final case class CloseableResource() extends AutoCloseable {
    val isClosed = new AtomicBoolean
    override def close(): Unit = isClosed.set(true)
  }

  // To track dereferencing of by-name resource
  val instanceCount = new AtomicInteger
  final case class CountedResource() extends AutoCloseable {
    instanceCount.incrementAndGet()
    override def close(): Unit = ()
  }

  val tests =
    Tests {
      "Disjunction exceptional code" - {
        "success case" - assert(Apps.supervise("asdf", TestFailure, silent = true) == "asdf".right)
        "exception case" - assert(Apps.supervise(exceptionalString, TestFailure, silent = true).isLeft)
      }

      "Disjunction / close exceptional code" - {
        "success case" - {
          val resource = new CloseableResource
          val value: AppFailure \/ String = Apps.using(resource, TestFailure, silent = true)(_ => "asdf")
          assert(
            value == "asdf".right,
            resource.isClosed.get
          )
        }
        "resource should instantiate once only" - {
          val value: AppFailure \/ String = Apps.using(new CountedResource, TestFailure, silent = true)(_ => "asdf")
          assert(
            value == "asdf".right,
            instanceCount.get() == 1
          )
        }
        "exception case" - {
          val resource = new CloseableResource
          val value: AppFailure \/ String = Apps.using(resource, TestFailure, silent = true)(_ => exceptionalString)
          assert(
            value.isLeft,
            resource.isClosed.get
          )
        }

      }
    }

  private def exceptionalString: String = {
    throw new RuntimeException("intentional")
  }
}
