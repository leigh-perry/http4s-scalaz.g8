package $package$.service

import $package$.shared.testsupport.UtestScalaCheck
import org.scalacheck.Prop.forAll
import utest._

object DummyTest extends TestSuite with UtestScalaCheck {
  val tests =
    Tests {
      "Service tests" - {
        "substring" -
          forAll {
            (a: String, b: String, c: String) => (a + b + c).substring(a.length, a.length + b.length) == b
          }.check()
      }
    }
}
