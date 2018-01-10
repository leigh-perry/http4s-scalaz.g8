package $package$.shared

trait AppFailure

object AppFailure {
  final case class InvalidConfig(reason: String) extends AppFailure
}
