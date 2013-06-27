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

	/** Login concurrently with durations
	  * @param users number of users
	  * @param duration duration for each user
	  * @param rampUp ramp up period
	  */
	def loginConcurrentlyWithDuration(users: Int = 0,
		duration: Int = 0,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		rampUp: Int = 10) {
		loginConcurrently(users, duration, true, rampUp, baseUrl)
	}

	/** Login concurrently with looping times
	  * @param users number of users
	  * @param loopingTimes looping times of each user
	  * @param rampUp ramp up period
	  */
	def loginConcurrentlyWithLoopTimes(users: Int = 0,
		loopingTimes: Int = 0,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		rampUp: Int = 10) {
		loginConcurrently(users, loopingTimes, false, rampUp, baseUrl)
	}

	/** Login concurrently
	  * @param users number of users
	  * @param loop looping duration/times (depends on loopIsDuration)
	  * @param loopIsDuration decides if loop is duration or repeating times
	  * @param rampUp ramp up period
	  */
	def loginConcurrently(users: Int = 0,
		loop: Int = 0,
		loopIsDuration: Boolean = true,
		rampUp: Int = 10,
		baseUrl: String = Predef.apiUrl("baseUrl")) {
		val httpConf = httpConfig.baseURL(baseUrl)
			.responseInfoExtractor((response: Response) => {
				List[String](response.getStatusCode.toString())
			})
		val scenario = loginMultipleTimesScenario(loop, loopIsDuration)

		setUp(scenario.users(users).ramp(rampUp).protocolConfig(httpConf))
	}

 	var users: Int = 100
	var loop: Int = 300
	var baseUrl: String = Predef.apiUrl("largeUrl")
	var rampUp: Int = 10

	loginConcurrentlyWithDuration(users, loop, baseUrl, rampUp)
	// loginConcurrentlyWithLoopTimes(users, loop, baseUrl, rampUp)

}
