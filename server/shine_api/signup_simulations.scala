package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._

import Shine.Predef._
import Shine.SignupScenarios._

/** This class contains simulations of signing up
*/
class SignupSimulations extends Simulation {

	/** Signup concurrently with durations
	  * @param users number of users
	  * @param duration duration for each user
	  * @param rampUp ramp up period
	  */
	def signupConcurrentlyWithDuration(users: Int = 0,
		duration: Int = 0,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		rampUp: Int = 10) {
		signupConcurrently(users, duration, true, rampUp, baseUrl)
	}

	/** Signup concurrently with looping times
	  * @param users number of users
	  * @param loopingTimes looping times of each user
	  * @param rampUp ramp up period
	  */
	def signupConcurrentlyWithLoopTimes(users: Int = 0,
		loopingTimes: Int = 0,
		baseUrl: String = Predef.apiUrl("baseUrl"),
		rampUp: Int = 10) {
		signupConcurrently(users, loopingTimes, false, rampUp, baseUrl)
	}

	/** Signup concurrently
	  * @param users number of users
	  * @param loop looping duration/times (depends on loopIsDuration)
	  * @param loopIsDuration decides if loop is duration or repeating times
	  * @param rampUp ramp up period
	  */
	def signupConcurrently(users: Int = 0,
		loop: Int = 0,
		loopIsDuration: Boolean = true,
		rampUp: Int = 10,
		baseUrl: String = Predef.apiUrl("baseUrl")) {
		val httpConf = httpConfig.baseURL(baseUrl)
			.responseInfoExtractor((response: Response) => {
				List[String](response.getStatusCode.toString())
			})
		val scenario = signupMultipleAccountsScenario(loop, loopIsDuration)

		setUp(scenario.users(users).ramp(rampUp).protocolConfig(httpConf))
	}

 	var users: Int = 2
	var loop: Int = 5
	var baseUrl: String = Predef.apiUrl("largeUrl")
	var rampUp: Int = 1

	signupConcurrentlyWithDuration(users, loop, baseUrl, rampUp)
	// signupConcurrentlyWithLoopTimes(users, loop, baseUrl, rampUp)

}
