package default

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class RecordedSimulation extends Simulation {

  val httpConf = http
    .baseURL("###TOKEN_VALID_URL###")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader(
      "Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  private def byId = s"""{"id":"A00001U"}"""
  val execById = exec(
      http("request_byId")
        .post("/###MICROSERVICE_CONTEXT###" + "/afiliat/aseguradoById/")
        .header("Content-Type", "application/json")
        .body(StringBody(byId))
        .asJSON)

  private def afiliatById = s"""{"id":"A00003S"}"""
  val execByKeyAndLang = exec(
      http("request_byKeyAndLang")
        .post("/###MICROSERVICE_CONTEXT###" + "/afiliat/administrativoById/")
        .header("Content-Type", "application/json")
        .body(StringBody(afiliatById))
        .asJSON)

  val scn = scenario("RecordedSimulation")
    .exec(
      execById,
      execByKeyAndLang
    )
    .pause(3)

  setUp(scn.inject(rampUsers(100) over (10 seconds)))
    .protocols(httpConf)
    .assertions(global.responseTime.max.lessThan(###TOKEN_RESPONSE_TIME###))
}
