package $package$.shared

import $package$.shared.tagger.Tagger

object Config {
  type HostName = HostName.Type
  object HostName extends Tagger[String]

  type PortNumber = PortNumber.Type
  object PortNumber extends Tagger[Int]

  final case class Endpoint(host: HostName, port: PortNumber)
}

final case class Config(endpoint: Config.Endpoint)
