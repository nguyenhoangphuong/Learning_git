package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._

import Shine.Predef._
import Shine.GraphItemsScenarios._

/** This class contains simulations of graph items
  */
class GraphItemsSimulations extends Simulation {

	val itemCount: Int = 20

	def insertGraphItemsWithConcurrencyLevels(
		configs: List[Map[String, String]]) {
		var i: Int = 0
		var size: Int = configs.size
		var x: Map[String, String] = Map("" -> "")
		var delay: Int = 0

		for (i <- 0 until size) {
			x = configs(i)
			insertGraphItemsConcurrentlyWithDuration(x("users").toInt,
				itemCount,
				x("duration").toInt,
				x("baseUrl"),
				x("rampUp").toInt,
				delay)
			delay += x("duration").toInt
		}
	}

	def insertGraphItemsConcurrentlyWithDuration(users: Int = 0,
		itemCount: Int = 0,
		duration: Int = 0,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		rampUp: Int = 10,
		delay: Int = 0) {
		insertGraphItemsConcurrently(users, itemCount, duration, true,
			rampUp, baseUrl, delay)
	}

	def insertGraphItemsConcurrentlyWithLoopTimes(users: Int = 0,
		itemCount: Int = 0,
		loop: Int = 0,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		rampUp: Int = 10,
		delay: Int = 0) {
		insertGraphItemsConcurrently(users, itemCount, loop, false,
			rampUp, baseUrl, delay)
	}

	// /** Signup concurrently
	//   * @param users number of users
	//   * @param loop looping duration/times (depends on loopIsDuration)
	//   * @param loopIsDuration decides if loop is duration or repeating times
	//   * @param rampUp ramp up period
	//   * @param baseUrl
	//   * @param delay delay before starting scenario
	//   */
	def insertGraphItemsConcurrently(users: Int = 0,
		itemCount: Int = 0,
		loop: Int = 0,
		loopIsDuration: Boolean = true,
		rampUp: Int = 10,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		delay: Int = 0) {
		val httpConf = httpConfig.baseURL(baseUrl)
			.responseInfoExtractor((response: Response) => {
				List[String](response.getStatusCode.toString())
			})
		val scenario = getTokenThenInsertGraphItemsScenario(itemCount,
			loop, loopIsDuration)

		setUp(scenario.users(users).ramp(rampUp).delay(delay)
			.protocolConfig(httpConf))
	}

	insertGraphItemsWithConcurrencyLevels(Predef.configs())

}
