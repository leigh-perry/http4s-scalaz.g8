package $package$.shared

import $package$.shared.tagger.Tagger

object Newtype {
  type AmqpEndpoint = AmqpEndpoint.Type
  object AmqpEndpoint extends Tagger[String]

  type UserName = UserName.Type
  object UserName extends Tagger[String]

  type Password = Password.Type
  object Password extends Tagger[String]

  type TopicName = TopicName.Type
  object TopicName extends Tagger[String]

  type PortNumber = PortNumber.Type
  object PortNumber extends Tagger[Int]

  type DbDriver = DbDriver.Type
  object DbDriver extends Tagger[String]

  type DbUrl = DbUrl.Type
  object DbUrl extends Tagger[String]

  type DbUser = DbUser.Type
  object DbUser extends Tagger[String]

  type DbPassword = DbPassword.Type
  object DbPassword extends Tagger[String]

  type DbMaxPoolSize = DbMaxPoolSize.Type
  object DbMaxPoolSize extends Tagger[Int]
}
