package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._

import Shine.Predef._
import Shine.HelloScenarios._

/** This class contains simulations of hello world
  */
class HelloSimulations extends Simulation {

	/** Hello concurrently with different concurrency levels.
	  * Concurrency levels are stored in a list
	  * @param configs list of scenarios' configurations. Each list member is a
	  * 	Map[Sring, String] which stores:
	  * 		"users": number of users (concurrency level)
	  * 		"duration": duration for that concurrency level
	  * 		"baseUrl"
	  * 		"rampUp": ramp up period for this concurrency level
	  */
	def helloWithConcurrencyLevels(
		configs: List[Map[String, String]]) {
		var i: Int = 0
		var size: Int = configs.size
		var x: Map[String, String] = Map("" -> "")
		var delay: Int = 0

		for (i <- 0 until size) {
			x = configs(i)
			helloConcurrentlyWithDuration(x("users").toInt,
				x("duration").toInt,
				x("baseUrl"),
				x("rampUp").toInt,
				delay)
			delay += x("duration").toInt
		}
	}

	/** Hello concurrently with durations
	  * @param users number of users
	  * @param duration duration for each user
	  * @param baseUrl
	  * @param rampUp ramp up period
	  * @param delay delay before starting scenario
	  */
	def helloConcurrentlyWithDuration(users: Int = 0,
		duration: Int = 0,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		rampUp: Int = 10,
		delay: Int = 0) {
		helloConcurrently(users, duration, true, rampUp, baseUrl, delay)
	}

	/** Hello concurrently with looping times
	  * @param users number of users
	  * @param loopingTimes looping times of each user
  	  * @param baseUrl
	  * @param rampUp ramp up period
	  * @param delay delay before starting scenario
	  */
	def helloConcurrentlyWithLoopTimes(users: Int = 0,
		loopingTimes: Int = 0,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		rampUp: Int = 10,
		delay: Int = 0) {
		helloConcurrently(users, loopingTimes, false, rampUp, baseUrl, delay)
	}


	/** Hello concurrently
	  * @param users number of users
	  * @param loop looping duration/times (depends on loopIsDuration)
	  * @param loopIsDuration decides if loop is duration or repeating times
	  * @param rampUp ramp up period
	  * @param baseUrl
	  * @param delay delay before starting scenario
	  */
	def helloConcurrently(users: Int = 0,
		loop: Int = 0,
		loopIsDuration: Boolean = true,
		rampUp: Int = 10,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		delay: Int = 0) {
		val httpConf = httpConfig.baseURL(baseUrl)
			.responseInfoExtractor((response: Response) => {
				List[String](response.getStatusCode.toString())
			})
		val scenario = helloMultipleTimesScenario(loop, loopIsDuration)

		setUp(scenario.users(users).ramp(rampUp).delay(delay)
			.protocolConfig(httpConf))
	}

	helloWithConcurrencyLevels(Predef.configs())

}
