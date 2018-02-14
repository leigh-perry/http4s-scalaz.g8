package $package$.shared

trait AppFailure extends Exception

object AppFailure {
  final case class InvalidConfig(reason: String) extends AppFailure
}
