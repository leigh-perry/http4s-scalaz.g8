package com.organisation.$package$.testsupport

import org.scalacheck.util.Pretty
import org.scalacheck.{Prop, Test}
import utest.assert

trait UtestScalaCheck {
  protected[this] object UtestReporter extends Test.TestCallback {
    override def onTestResult(name: String, res: org.scalacheck.Test.Result) = {
      val scalaCheckResult = if (res.passed) "" else Pretty.pretty(res, Pretty.defaultParams)
      assert(scalaCheckResult.isEmpty)
    }
  }

  implicit protected[this] class PropWrapper(prop: Prop) {
    def check(): Unit = prop.check(Test.Parameters.default.withTestCallback(UtestReporter))
  }

}
