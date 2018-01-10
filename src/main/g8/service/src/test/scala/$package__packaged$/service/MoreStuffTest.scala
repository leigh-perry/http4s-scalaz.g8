package $package$.service

import $package$.testsupport.UtestScalaCheck
import org.scalacheck.Prop.forAll
import utest._

@SuppressWarnings(Array("org.wartremover.warts.Nothing", "org.wartremover.warts.Any"))
object MoreStuffTest extends TestSuite with UtestScalaCheck {
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
