package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._

class SignupSimulation extends Simulation {
	import Shine.APIKey._
	import Shine.APIURL._
	import Shine.NewUserInfo._

	val httpConf = httpConfig.baseURL(apiUrl("baseUrl"))
	val scn = scenario("Signup API")
		//.during(600) {
		.repeat(1) {
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

	setUp(scn.users(10).ramp(0).protocolConfig(httpConf))
}
