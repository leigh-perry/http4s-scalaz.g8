package $package$.shared

import $package$.shared.testsupport.UtestScalaCheck
import org.scalacheck.Prop.forAll
import utest._

object UtestScalaCheckTest extends TestSuite with UtestScalaCheck {
  val tests =
    Tests {
      "Utest support for ScalaCheck" - {
        "success case" - {
          forAll {
            (v: Int) => {
              v == v
            }
          }.check()
        }

        "failure case" - {
          //  forAll {
          //    (v: Int) => {
          //      v == v + 1
          //    }
          //  }.check()
        }
      }
    }
}
