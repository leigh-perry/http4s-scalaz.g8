package $package$.extapi.model

import java.sql.Timestamp
import java.util.Date

import argonaut.Argonaut.casecodec9
import argonaut.CodecJson
import $package$.shared.AppFailure
import org.http4s.argonaut._
import org.http4s.{EntityDecoder, EntityEncoder}

object Model {

  final case class HourlyAggregatedFullness(
    gatewayId: Long,
    totalFullness: Double,
    binTypeId: String,
    lat: Double,
    lng: Double,
    eventTsMax: Timestamp,
    reportTime: Timestamp,
    floor: Long,
    location: String
  )

  object HourlyAggregatedFullness {

    private val undefinedTimestamp = new Timestamp(new Date().getTime)
    val undefined = HourlyAggregatedFullness(0, 0.0, "", 0.0, 0.0, undefinedTimestamp, undefinedTimestamp, 0L, "")

    import $package$.extapi.model.argonautSupport._

    implicit val codec: CodecJson[HourlyAggregatedFullness] =
      casecodec9(
        HourlyAggregatedFullness.apply,
        HourlyAggregatedFullness.unapply
      )(
        "gatewayId",
        "totalFullness",
        "binTypeId",
        "lat",
        "lng",
        "eventTsMax",
        "reportTime",
        "floor",
        "location"
      )
    implicit val decoder: EntityDecoder[HourlyAggregatedFullness] = jsonOf[HourlyAggregatedFullness]
    implicit val encoder: EntityEncoder[HourlyAggregatedFullness] = jsonEncoderOf[HourlyAggregatedFullness]
  }

  final case class RowNotFound[K](id: K) extends AppFailure
  final case class RowAlreadyExists[K](id: K) extends AppFailure
  final case class ApiNotImplemented(method: String) extends AppFailure
  final case class ApiInternalError(message: String) extends AppFailure

}
