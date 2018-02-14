package $package$.extapi.testsupport

import java.sql.Timestamp

import $package$.extapi.model.Model._
import $package$.extapi.store.Store

import scalaz.concurrent.Task

class StubStore(recordCount: Int) extends Store[Long, HourlyAggregatedFullness] {

  private val fixedTimestamp = new Timestamp(1516693100000L)

  private val contents: Map[KeyType, ValueType] =
    (0 until recordCount)
      .map(
        i => {
          val l = i.asInstanceOf[KeyType]
          val d = i.asInstanceOf[Double]

          l -> HourlyAggregatedFullness(l, d, s"binTypeId\$l", d, d, fixedTimestamp, fixedTimestamp, l, s"location\$l")
        }
      ).toMap

  override def read(id: KeyType): Task[Seq[HourlyAggregatedFullness]] = {
    contents.get(id) match {
      case Some(viewsState) => Task.now(Seq(viewsState))
      case None => Task.fail(RowNotFound(id))
    }
  }

  override def readAll(): Task[Seq[HourlyAggregatedFullness]] = {
    Task.now(
      contents
        .values
        .toList
        .sortBy(_.gatewayId)
    )
  }

}
