package $package$.extapi.model

import java.sql.Timestamp

import argonaut.Argonaut._
import argonaut._
import org.http4s._
import org.http4s.argonaut._

object argonautSupport extends ArgonautOps

trait ArgonautOps {

  implicit def timestampEncode(implicit element: EncodeJson[String]): EncodeJson[Timestamp] = {
    EncodeJson(ts => element(Option(ts).map(_.toString).getOrElse("")))
  }

  implicit def timestampDecode(implicit element: DecodeJson[String]): DecodeJson[Timestamp] = {
    DecodeJson(
      cursor =>
        element(cursor)
          .map(ts => Timestamp.valueOf(ts))
    )
  }


  implicit def entityEncoderForAnyA[A: EncodeJson]: EntityEncoder[A] = jsonEncoderOf[A]

  /** Support encoding of collections for any encodable type */
  implicit def entityEncoderForAnySequenceOfA[A: EncodeJson]: EncodeJson[Seq[A]] = {
    EncodeJson[Seq[A]](s => Json.array(s.map(_.asJson): _*))
  }

}
