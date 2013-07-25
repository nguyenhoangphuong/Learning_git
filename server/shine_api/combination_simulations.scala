package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import bootstrap._
import com.excilys.ebi.gatling.core.structure._
import com.excilys.ebi.gatling.http.request.builder._

import Shine.Predef._
import Shine.SignupScenarios._
import Shine.TimelineScenarios._
import Shine.GraphItemsScenarios._

/** This class contains simulations of APIs combination
  */
class CombinationSimulations extends Simulation {

	val signup = SignupScenarios.feedNewUserThenSignup(Predef.newUser)

	val itemCount = 20

	val insertTimelineItems = TimelineScenarios.insertTimelineItems(
		"${auth_token}",
		itemCount)

	val insertGraphItems = GraphItemsScenarios.insertGraphItems(
		"${auth_token}",
		itemCount)

	val pushSyncLog = feed(Predef.newSyncLog)
		.exec({
			var headers = Predef.apiKey
			
			headers += "auth_token" -> "${auth_token}"
			headers += "Content-Type" -> "application/json"

			http("Push Sync Log Request")
			.post(apiUrl("synclog"))
			.headers(headers)
			.body("${log}")
		})

	def apiCombinationScenario(duration: Int = 0): ScenarioBuilder = {
		var scenarioName: String = "Signup, timeline & graph items, sync log"

		scenarioName += " - Run ID: " + Helper.randomNumericString(1)
		scenario(scenarioName).exec(
			during(duration) {
				exec(
					signup,
					insertTimelineItems,
					insertGraphItems,
					pushSyncLog
				)
			}
		)
	}

	def doApiCombinationConcurrently(users: Int = 0,
		duration: Int = 0,
		rampUp: Int = 10,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		delay: Int = 0) {
		val httpConf = httpConfig.baseURL(baseUrl)
			.responseInfoExtractor((response: Response) => {
				List[String](response.getStatusCode.toString())
			})
		val scenario = apiCombinationScenario(duration)

		setUp(scenario.users(users).ramp(rampUp).delay(delay)
			.protocolConfig(httpConf))
	}

	def doApiCombinationWithConcurrencyLevels(
		configs: List[Map[String, String]]) {
		var i: Int = 0
		var size: Int = configs.size
		var x: Map[String, String] = Map("" -> "")
		var delay: Int = 0

		for (i <- 0 until size) {
			x = configs(i)
			doApiCombinationConcurrently(x("users").toInt,
				x("duration").toInt,
				x("rampUp").toInt,
				x("baseUrl"),
				delay)
			delay += x("duration").toInt
		}
	}

	doApiCombinationWithConcurrencyLevels(Predef.configs())

}
