package $package$.shared

import $package$.shared.testsupport.UtestScalaCheck
import $package$.shared.tagger.{Tagger, TaggerOps}
import org.scalacheck.Prop.forAll
import utest._

object TaggerTest extends TestSuite with UtestScalaCheck {
  object NewTypes {
    type TaggedString = TaggedString.Type
    object TaggedString extends Tagger[String]

    type TaggedInt = TaggedInt.Type
    object TaggedInt extends Tagger[Int]
  }

  import NewTypes._

  val tests =
    Tests {
      "Tagger tests" - {

        "tagged string wrap/unwrap" - {
          forAll {
            (v: String) => {
              TaggedString(v).unwrap == v
            }
          }.check()
        }

        "tagged int wrap/unwrap" - {
          forAll {
            (v: Int) => {
              TaggedInt(v).unwrap == v
            }
          }.check()
        }

      }
    }
}
