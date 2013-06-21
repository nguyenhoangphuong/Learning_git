package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._

import Shine.APIKey._
import Shine.APIURL._
import Shine.NewUserInfo._

class SignupSimulation extends Simulation {
	def signUpForADuration(users: Int, duration: Int = 0, rampUp: Int = 10) {
		val httpConf = httpConfig.baseURL(apiUrl("baseUrl"))
		val scn = scenario("Signup API")
			.during(duration) {
				feed(newUserInfo)
				.exec(
					http("Signup request")
						.post(apiUrl("signup"))
						.headers(apiKey)
						.param("email", "${email}")
						.param("password", "${password}")
						.param("udid", "${udid}")
				)
			}

		setUp(scn.users(users).ramp(rampUp).protocolConfig(httpConf))
	}

	def signUpWithLoopingTimes(users: Int, repeat: Int = 0, rampUp: Int = 10) {
		val httpConf = httpConfig.baseURL(apiUrl("baseUrl")).responseInfoExtractor((response: Response) => {
  								List[String](response.getResponseBody())
		})
		val scn = scenario("Signup API")
			.repeat(repeat) {
				feed(newUserInfo)
				.exec(
					http("Signup request")
						.post(apiUrl("signup"))
						.headers(apiKey)
						.param("email", "${email}")
						.param("password", "${password}")
						.param("udid", "${udid}").check(status.is(200))
				)
			}
		setUp(scn.users(users).ramp(rampUp).protocolConfig(httpConf))
	}

	// signUpForADuration(5, 5, 0)
	signUpWithLoopingTimes(1, 1, 0)
}
