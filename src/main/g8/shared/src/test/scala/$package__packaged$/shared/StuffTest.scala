package com.organisation.$package$.shared

import com.organisation.$package$.testsupport.UtestScalaCheck
import org.scalacheck.Prop.forAll
import utest._

@SuppressWarnings(Array("org.wartremover.warts.Nothing"))
object StuffTest extends TestSuite with UtestScalaCheck {
  val tests =
    Tests {
      "Shared tests" - {
        "startsWith" -
          forAll {
            (a: String, b: String) => (a + b).startsWith(a)
          }.check()

        "concatenate" -
          forAll {
            (a: String, b: String) => (a + b).length >= a.length && (a + b).length >= b.length
          }.check()
      }
    }
}
