package $package$.shared

import org.specs2.Specification
import org.specs2.matcher.ShouldThrownExpectations
import org.specs2.specification.core.SpecStructure

class MoreStuffTest extends Specification with ShouldThrownExpectations {

  override def is: SpecStructure = {
    s2"""
      More stuff
        should work \$dummy
    """
  }

  private def dummy = {
    123 must be_===(123)
  }

}
