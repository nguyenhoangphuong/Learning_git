package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import bootstrap._
import com.excilys.ebi.gatling.core.structure._
import com.excilys.ebi.gatling.http.request.builder._

import Shine.Helper._
import Shine.Predef._

/** This object contains scenarios of signing up
*/
object SignupScenarios {

	/** Signup multiple accounts
	  * @param loop looping duration/times (depends on loopIsDuration)
	  * @param loopIsDuration decides loop is duration or number of repeating
	  * @param feeder user info data source
	  * @return com.excilys.ebi.gatling.core.structure.ScenarioBuilder
	  */
	def signupMultipleAccountsScenario(loop: Int = 0,
		loopIsDuration: Boolean = true,
		feeder: Feeder[String] = Predef.newUser): ScenarioBuilder = {
		var scenarioName: String = "Signup multiple accounts"

		scenarioName += " - Run ID: " + Helper.randomNumericString(1)
		scenario(scenarioName)
		.doIfOrElse(loopIsDuration == true) {
			during(loop) {
				feedNewUserThenSignup(feeder)
			}
		} { // else: loop is looping times
			repeat(loop) {
				feedNewUserThenSignup(feeder)
			}
		}
	}

	/** Feed new user info and then signup new account w/ this user
	  * @param feeder user info data source
	  * @return com.excilys.ebi.gatling.core.structure.ChainBuilder
	  */
	def feedNewUserThenSignup(feeder: Feeder[String] = Predef.newUser)
		: ChainBuilder = {
		feed(feeder)
		.exec(
			signupRequest("${email}", "${password}", "${udid}")
		)
	}

	/** Signup an account
	  * @param email
	  * @param password
	  * @param udid
	  * @return com.excilys.ebi.gatling.core.structure.ChainBuilder
	  */
	def signup(email: String,
		password: String,
		udid: String): ChainBuilder = {
		exec(
			signupRequest(email, password, udid)
		)
	}

	/** Signup request
	  * @param email
	  * @param password
	  * @param udid
	  * @return com.excilys.ebi.gatling.http.request.builder.PostHttpRequestBuilder
	  */
	def signupRequest(email: String,
		password: String,
		udid: String): PostHttpRequestBuilder = {
		http("Signup request")
			.post(Predef.apiUrl("signup"))
			.headers(Predef.apiKey)
			.param("email", email)
			.param("password", password)
			.param("udid", udid)
			.check(bodyString)
	}

}
