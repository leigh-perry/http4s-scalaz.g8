package $package$.shared.tagger

import scalaz.syntax.std.option._
import scalaz.{@@, Tag}

trait Tagger[A] {
  sealed trait Marker
  final type Type = A @@ Marker

  def apply(a: A): Type = Tag[A, Marker](a)
  def unapply(tagged: Type): Option[A] = unwrapped(tagged).some
  def unwrapped(tagged: Type): A = Tag.unwrap(tagged)
}
