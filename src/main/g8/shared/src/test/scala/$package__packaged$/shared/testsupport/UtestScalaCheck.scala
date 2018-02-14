package $package$.shared.testsupport

import org.scalacheck.util.Pretty
import org.scalacheck.{Prop, Test}
import utest.assert

trait UtestScalaCheck {
  protected[this] object UtestReporter extends Test.TestCallback {
    override def onTestResult(name: String, res: org.scalacheck.Test.Result): Unit = {
      val passed = res.passed
      if (!passed) {
        println(s"UtestScalaCheck check failed: \${Pretty.pretty(res, Pretty.defaultParams)}")
      }
      assert(passed)
    }
  }

  implicit protected[this] class PropWrapper(prop: Prop) {
    def check(): Unit = prop.check(Test.Parameters.default.withTestCallback(UtestReporter))
  }

}
