package $package$.extapi.model

import java.time.temporal.ChronoUnit
import java.time.{LocalDateTime, OffsetDateTime, ZoneOffset}

import doobie.enum.jdbctype.{JavaObject, Other}
import doobie.imports.Meta

import scalaz.NonEmptyList

object doobieSupport extends DoobieMeta

trait DoobieMeta {

  implicit val dateTimeMeta: Meta[OffsetDateTime] =
    Meta.advanced[OffsetDateTime](
      NonEmptyList(Other, JavaObject),
      NonEmptyList("timestamp"),
      _.getObject(_, classOf[OffsetDateTime]) match {
        case null => null
        case a => a.asInstanceOf[OffsetDateTime]
      },
      (ps, i, o) => ps.setObject(i, normaliseDateTime(o)),
      (rs, i, o) => rs.updateObject(i, normaliseDateTime(o))
    )

  def normaliseDateTime(dt: OffsetDateTime): OffsetDateTime = {
    dt.withOffsetSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS)
  }

  implicit val localDateTimeMeta: Meta[LocalDateTime] =
    Meta.advanced[LocalDateTime](
      NonEmptyList(Other, JavaObject),
      NonEmptyList("timestamp"),
      _.getObject(_, classOf[LocalDateTime]) match {
        case null => null
        case a => a.asInstanceOf[LocalDateTime]
      },
      (ps, i, o) => ps.setObject(i, o),
      (rs, i, o) => rs.updateObject(i, o)
    )

}
