package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._

import Shine.Predef._
import Shine.LoginScenarios._

/** This class contains simulations of logging in
*/
class LoginSimulations extends Simulation {

	// /** Signup new user list
	//   * @param count number of users
	//   * @return user list (List[Map[String, String]])
	//   */
	// def signupNewUserList(count: Int = 10): List[Map[String, String]] = {
	// 	var userList = Predef.newUserList(count)
	// 	var i: Int = 0
	// 	val httpConf = httpConfig.baseURL(baseUrl)
	// 		.responseInfoExtractor((response: Response) => {
	// 			List[String](response.getStatusCode.toString())
	// 		})
	// 	val scn = scenario("Signup new user list" +
	// 		" - Run ID: " + Helper.randomNumericString(1))
	// 	// .repeat(count) {
	// 	// 	feed(userList)
	// 	// 	.exec(
	// 	// 		signupRequest("${email}", "${password}", "${udid}")
	// 	// 	)
	// 	// }
	// 	.exec(
	// 		for (i <- 0 until count) {
	// 			signupRequest(userList(i)("email"), userList(i)("password"), userList(i)("udid"))
	// 		}
	// 	)

	// 	setUp(scn.users(1).ramp(1).protocolConfig(httpConf))

	// 	return userList
	// }

	/** Login concurrently with different concurrency levels.
	  * Concurrency levels are stored in a list
	  * @param configs list of scenarios' configurations. Each list member is a
	  * 	Map[Sring, String] which stores:
	  * 		"users": number of users (concurrency level)
	  * 		"duration": duration for that concurrency level
	  * 		"baseUrl"
	  * 		"rampUp": ramp up period for this concurrency level
	  */
	def loginWithConcurrencyLevels(
		configs: List[Map[String, String]]) {
		var i: Int = 0
		var size: Int = configs.size
		var x: Map[String, String] = Map("" -> "")
		var delay: Int = 0

		for (i <- 0 until size) {
			x = configs(i)
			loginConcurrentlyWithDuration(x("users").toInt,
				x("duration").toInt,
				x("baseUrl"),
				x("rampUp").toInt,
				delay)
			delay += x("duration").toInt
		}
	}

	/** Login concurrently with durations
	  * @param users number of users
	  * @param duration duration for each user
	  * @param baseUrl
	  * @param rampUp ramp up period
	  * @param delay delay before starting scenario
	  */
	def loginConcurrentlyWithDuration(users: Int = 0,
		duration: Int = 0,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		rampUp: Int = 10,
		delay: Int = 0) {
		loginConcurrently(users, duration, true, rampUp, baseUrl, delay)
	}

	/** Login concurrently with looping times
	  * @param users number of users
	  * @param loopingTimes looping times of each user
  	  * @param baseUrl
	  * @param rampUp ramp up period
	  * @param delay delay before starting scenario
	  */
	def loginConcurrentlyWithLoopTimes(users: Int = 0,
		loopingTimes: Int = 0,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		rampUp: Int = 10,
		delay: Int = 0) {
		loginConcurrently(users, loopingTimes, false, rampUp, baseUrl, delay)
	}

	/** Login concurrently
	  * @param users number of users
	  * @param loop looping duration/times (depends on loopIsDuration)
	  * @param loopIsDuration decides if loop is duration or repeating times
	  * @param rampUp ramp up period
	  * @param baseUrl
	  * @param delay delay before starting scenario
	  */
	def loginConcurrently(users: Int = 0,
		loop: Int = 0,
		loopIsDuration: Boolean = true,
		rampUp: Int = 10,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		delay: Int = 0) {
		val httpConf = httpConfig.baseURL(baseUrl)
			.responseInfoExtractor((response: Response) => {
				List[String](response.getStatusCode.toString())
			})
		val scenario = loginMultipleTimesScenario(loop, loopIsDuration)

		setUp(scenario.users(users).ramp(rampUp).delay(delay)
			.protocolConfig(httpConf))
	}

	var duration: Int = 180
	var baseUrl: String = Predef.apiUrl("largeUrl")
	var rampUp: Int = 10
	var configs: List[Map[String, String]] = List(
		Map("users" -> "10",
			"duration" -> duration.toString(),
			"baseUrl" -> baseUrl,
			"rampUp" -> rampUp.toString()),
		Map("users" -> "50",
			"duration" -> duration.toString(),
			"baseUrl" -> baseUrl,
			"rampUp" -> rampUp.toString()),
		Map("users" -> "100",
			"duration" -> duration.toString(),
			"baseUrl" -> baseUrl,
			"rampUp" -> rampUp.toString()),
		Map("users" -> "200",
			"duration" -> duration.toString(),
			"baseUrl" -> baseUrl,
			"rampUp" -> rampUp.toString()),
		Map("users" -> "500",
			"duration" -> duration.toString(),
			"baseUrl" -> baseUrl,
			"rampUp" -> rampUp.toString()),
		Map("users" -> "1000",
			"duration" -> duration.toString(),
			"baseUrl" -> baseUrl,
			"rampUp" -> rampUp.toString())
		)

	loginWithConcurrencyLevels(configs)

}
