package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import bootstrap._
import com.excilys.ebi.gatling.core.structure._
import com.excilys.ebi.gatling.http.request.builder._

import Shine.Helper._
import Shine.Predef._
import Shine.SignupScenarios._

/** This object contains scenarios of logging in
  */
object LoginScenarios {

	/** Login multiple times
	  * @param loop looping duration/times (depends on loopIsDuration)
	  * @param loopIsDuration decides loop is duration or number of repeating
	  * @return com.excilys.ebi.gatling.core.structure.ScenarioBuilder
	  */
	def loginMultipleTimesScenario(loop: Int = 0,
		loopIsDuration: Boolean = true): ScenarioBuilder = {
		var scenarioName: String = "Login multiple times"

		scenarioName += " - Run ID: " + Helper.randomNumericString(1)
		scenario(scenarioName)
		.feed(Predef.newUser)
		.exec(
			SignupScenarios.signupRequest("${email}", "${password}", "${udid}")
		)
		.doIfOrElse(loopIsDuration == true) {
			during(loop) {
				login("${email}", "${password}", "${udid}")
			}
		} { // else: loop is looping times
			repeat(loop) {
				login("${email}", "${password}", "${udid}")
			}
		}
	}

	/** Login with an account
	  * @param email
	  * @param password
	  * @param udid
	  * @return com.excilys.ebi.gatling.core.structure.ChainBuilder
	  */
	def login(email: String,
		password: String,
		udid: String): ChainBuilder = {
		exec(
			loginRequest(email, password, udid)
		)
	}

	/** Login request
	  * @param email
	  * @param password
	  * @param udid
	  * @return com.excilys.ebi.gatling.http.request.builder.PostHttpRequestBuilder
	  */
	def loginRequest(email: String,
		password: String,
		udid: String): PostHttpRequestBuilder = {
		http("Login request")
			.post(Predef.apiUrl("login"))
			.headers(Predef.apiKey)
			.param("email", email)
			.param("password", password)
			.param("udid", udid)
			.check(bodyString)
	}

}
