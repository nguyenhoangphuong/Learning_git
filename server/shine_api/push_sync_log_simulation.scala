package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._

import Shine.APIKey._
import Shine.APIURL._
import Shine.NewSyncLog._
import Shine.NewUserInfo._

class PushSyncLogSimulation extends Simulation {
	def pushSyncLogWithLoopingTimes(users: Int, repeat: Int = 0, rampUp: Int = 10) {
		val httpConf = httpConfig.baseURL(apiUrl("baseUrl"))
		var header = Map("api_key" -> "76801581",
						"auth_token" -> "3816438795046890631-fqyBQ8AaybpGzxpygkk5",
						"Content-Type" -> "application/json")
		
		val scn = scenario("Push Sync Log API")
			.repeat(repeat) {
				feed(newSyncLog)
				.exec(
					http("Push Sync Log Request")
						.post(apiUrl("synclog"))
						.headers(header)
						.body("${log}").asJSON.check(status.is(200))
						
				)
			}


		setUp(scn.users(users).ramp(rampUp).protocolConfig(httpConf))
	}

	def signUpThenPushLog(users: Int, repeat: Int = 0, rampUp: Int = 10) {
		val httpConf = httpConfig.baseURL(apiUrl("baseUrl")).responseInfoExtractor((response: Response) => {
  								List[String](response.getResponseBody())
		})

		val signUp = exec({
			http("Signup request")
				.post(apiUrl("signup"))
				.headers(apiKey)
				.param("email", "${email}")
				.param("password", "${password}")
				.param("udid", "${udid}")
				.check(jsonPath("auth_token").find.saveAs("token"))
		})

		val pushSyncLog = 
			exec({
				var header = Map("api_key" -> "76801581",
				"auth_token" -> "${token}",
				"Content-Type" -> "application/json")

				 http("Push Sync Log Request")
				.post(apiUrl("synclog"))
				.headers(header)
				.body("${log}").asJSON.check(status.is(200))
			})

		val scn = scenario("Signup API")
			.repeat(repeat) {
				feed(newUserInfo).feed(newSyncLog)
					.exec(
						signUp, pushSyncLog
					)
		}
		setUp(scn.users(users).ramp(rampUp).protocolConfig(httpConf))
	}


	signUpThenPushLog(1, 1, 0)
}