package $package$.shared

import scalaz.{@@, Tag}

package object tagger {
  implicit class TaggerOps[A, T](val tag: A @@ T) {
    def unwrap: A = Tag.unwrap(tag)
  }
}
