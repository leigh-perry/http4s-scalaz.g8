package $package$.extapi.store

import $package$.extapi.Config
import $package$.extapi.model.Model.HourlyAggregatedFullness
import $package$.shared.tagger._
import doobie.hikari.hikaritransactor.HikariTransactor

import scalaz.concurrent.Task

class SqlStore(cfg: Config) extends Store[Long, HourlyAggregatedFullness] {
  private val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(getClass)

  //import $package$.extapi.model.doobieSupport._
  import doobie.imports._

  val xa: HikariTransactor[Task] = {
    val ht =
      for {
        hxa <-
          HikariTransactor[Task](
            cfg.db.driver.unwrap,
            cfg.db.url.unwrap,
            cfg.db.user.unwrap,
            cfg.db.password.unwrap
          )
        _ <- hxa.configure {
          ds => {
            log.info(s"Configuring HikariTransactor max pool size \${cfg.db.maxPoolSize.unwrap}")
            ds.setMaximumPoolSize(cfg.db.maxPoolSize.unwrap)
            //ds.setMinimumIdle(minIdle)
          }
        }
        _ = log.info("Created HikariTransactor")
      } yield hxa

    ht.unsafePerformSync
  }

  override def read(id: Long): Task[Seq[ValueType]] = {
    // Latest record for each bin type for specified gateway
    val query =
      sql"""
        SELECT f.GATEWAY_ID, f.TOTAL_FULLNESS, f.BIN_TYPE_ID, f.LAT, f.LNG, f.EVENT_TS_MAX, f.REPORT_TIME, f.FLOOR, f.LOCATION
        FROM(
              SELECT f.GATEWAY_ID, f.TOTAL_FULLNESS, f.BIN_TYPE_ID, f.LAT, f.LNG, f.EVENT_TS_MAX, f.REPORT_TIME, f.FLOOR, f.LOCATION, ROW_NUMBER ()
              OVER(
              PARTITION BY f.GATEWAY_ID, f.BIN_TYPE_ID
                ORDER BY f.REPORT_TIME DESC
                ) AS N
              FROM HOURLY_AGGREGATED_FULLNESS f
            ) f
        WHERE f.N = 1
           AND gateway_id = \$id
      """.query[HourlyAggregatedFullness]

    query.list.transact(xa)
  }

  override def readAll(): Task[Seq[ValueType]] = {
    val query =
      sql"""
        SELECT gateway_id, total_fullness, BIN_TYPE_ID, lat, lng, event_ts_max, report_time, floor, location
        FROM hourly_aggregated_fullness
        ORDER BY gateway_id, BIN_TYPE_ID, report_time
      """.query[HourlyAggregatedFullness]

    query.list.transact(xa)
  }
}
