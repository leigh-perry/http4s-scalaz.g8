package $package$.extapi

import $package$.extapi.model.Model.HourlyAggregatedFullness
import $package$.extapi.service.ReadService
import $package$.extapi.testsupport.{Http4sTestSupport, StubStore}
import org.http4s.{Method, Status}
import utest._

object ApiHttpControllerTest extends TestSuite with Http4sTestSupport {

  private val uriPath = "hourly"

  val tests =
    Tests {
      "ApiHttpController tests" - {
        "read empty table" - checkReadAll(apiHttpController(0), "[]")
        "read non-empty table" - {
          checkReadAll(
            apiHttpController(3),
            """[{"binTypeId":"binTypeId0","lng":0.0,"totalFullness":0.0,"location":"location0","gatewayId":0,"reportTime":"2018-01-23 18:38:20.0","floor":0,"lat":0.0,"eventTsMax":"2018-01-23 18:38:20.0"},{"binTypeId":"binTypeId1","lng":1.0,"totalFullness":1.0,"location":"location1","gatewayId":1,"reportTime":"2018-01-23 18:38:20.0","floor":1,"lat":1.0,"eventTsMax":"2018-01-23 18:38:20.0"},{"binTypeId":"binTypeId2","lng":2.0,"totalFullness":2.0,"location":"location2","gatewayId":2,"reportTime":"2018-01-23 18:38:20.0","floor":2,"lat":2.0,"eventTsMax":"2018-01-23 18:38:20.0"}]"""
          )
        }
        "read existing row" - {
          checkRead(
            apiHttpController(3),
            1,
            """[{"binTypeId":"binTypeId1","lng":1.0,"totalFullness":1.0,"location":"location1","gatewayId":1,"reportTime":"2018-01-23 18:38:20.0","floor":1,"lat":1.0,"eventTsMax":"2018-01-23 18:38:20.0"}]"""
          )
        }
      }
    }

  private def checkReadAll(controller: ApiHttpController, expectedContents: String): Unit = {
    testRequestResponseBody(controller.service, Method.GET, uriPath, Status.Ok, expectedContents)
  }

  private def checkRead(controller: ApiHttpController, i: Int, expectedContents: String): Unit = {
    testRequestResponseBody(controller.service, Method.GET, s"\$uriPath/\$i", Status.Ok, expectedContents)
  }

  private def apiHttpController(recordCount: Int): ApiHttpController = {
    val cfg = Config.baseConfig
    new ApiHttpController(cfg, new ReadService[Long, HourlyAggregatedFullness](cfg, new StubStore(recordCount)))
  }
}
