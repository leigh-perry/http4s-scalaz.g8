package $package$.extapi.testsupport

import java.nio.charset.CharacterCodingException

import $package$.shared.Apps
import org.http4s.{EntityBody, HttpService, InvalidMessageBodyFailure, MessageFailure, Method, ParseFailure, ParseResult, Request, Response, Status, Uri}

import scalaz.concurrent.Task
import scalaz.syntax.either._
import scalaz.{\/, stream}

trait Http4sTestSupport {

  def testRequestResponseBody(
    service: HttpService,
    method: Method,
    uriPath: String,
    expectedStatus: Status,
    expectedResponseBodyString: String
  ): Unit = {
    import utest._

    val result: (MessageFailure \/ Response) =
      for {
        request <- requestOf(method, uriPath, None)
        response <- responseOf(service, request)
      } yield response

    result.fold(
      l => {
        println(s"Failure \${Apps.exceptionDetails(l)}")
        assert(false)
        ()
      },
      response => {
        println(s"Expected status \$expectedStatus: got \${response.status}")

        val body: Option[String] = bodyStringOf(response)
        val expectedBody = Some(expectedResponseBodyString)
        println(s"Expected body\n  \$expectedBody\ngot\n  \${body}")

        assert(
          response.status == expectedStatus,
          body == expectedBody
        )
      }
    )

  }

  def requestOf(
    method: Method,
    uriPath: String,
    requestBodyString: Option[String]
  ): ParseFailure \/ Task[Request] = {
    val parsedServiceUri: ParseResult[Uri] = Uri.fromString(uriPath)

    parsedServiceUri.map {
      serviceUri =>
        Request(method, serviceUri)
          .withBody(requestBodyString.getOrElse(""))
    }
  }

  def responseOf(service: HttpService, request: Task[Request]): InvalidMessageBodyFailure \/ Response = {
    request
      .flatMap(service.run)
      .unsafePerformSync
      .cata(_.right, InvalidMessageBodyFailure("could not get response").left)
  }

  private def bodyStringOf(response: Response) = {
    stringOf(response.body)
  }

  def stringOf(body: EntityBody): Option[String] = {
    val process: stream.Process[Task, Either[CharacterCodingException, String]] = body.map(bytes => bytes.decodeAscii)

    import scalaz.Scalaz._

    val containedValues: Seq[Either[CharacterCodingException, String]] = process.unemit._1
    val list: List[Either[CharacterCodingException, String]] = containedValues.toList
    val strings: Either[CharacterCodingException, List[String]] = list.sequenceU

    strings match {
      case Right(List(e)) => e.some
      case _ => None
    }
  }

}
