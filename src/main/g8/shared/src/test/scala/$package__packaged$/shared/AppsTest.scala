package $package$.shared

import java.util.concurrent.atomic.AtomicBoolean

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

  val tests =
    Tests {
      "Disjunction exceptional code" - {
        "success case" - assert(Apps.supervise("asdf", t => TestFailure(t)) == "asdf".right)
        "exception case" - {
          assert(Apps.supervise(exceptionalString, t => TestFailure(t)).isLeft)
        }
      }

      "Disjunction / close exceptional code" - {
        "success case" - {
          val resource = new CloseableResource
          val value: AppFailure \/ String = Apps.using(resource, t => TestFailure(t))(_ => "asdf")
          assert(
            value == "asdf".right,
            resource.isClosed.get
          )
        }
        "exception case" - {
          val resource = new CloseableResource
          val value: AppFailure \/ String = Apps.using(resource, t => TestFailure(t))(_ => exceptionalString)
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
