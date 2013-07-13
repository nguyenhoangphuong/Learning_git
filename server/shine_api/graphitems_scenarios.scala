package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import bootstrap._
import com.excilys.ebi.gatling.core.structure._
import com.excilys.ebi.gatling.http.request.builder._

import Shine.Helper._
import Shine.Predef._
import Shine.SignupScenarios._

object GraphItemsScenarios {
	def getTokenThenInsertGraphItemsScenario(itemCount: Int = 0,
		loop: Int = 0,
		loopIsDuration: Boolean = true): ScenarioBuilder = {
		var scenarioName: String = "Get token and then insert graph items"

		scenarioName += " - Run ID: " + Helper.randomNumericString(1)
		scenario(scenarioName)
		.exec(
			feed(Predef.newUser)
			.exec( // sign up to get token
				http("Signup request")
				.post(Predef.apiUrl("signup"))
				.headers(Predef.apiKey)
				.param("email", "${email}")
				.param("password", "${password}")
				.param("udid", "${udid}")
				.check(jsonPath("auth_token").find.saveAs("auth_token"))
			) // loop insert graph items
			.doIfOrElse(loopIsDuration == true) {
				during(loop) {
					insertGraphItems("${auth_token}", itemCount)
				}
			} {
				repeat(loop) {
					insertGraphItems("${auth_token}", itemCount)
				}
			}
		)
	}


	def insertGraphItems(auth_token: String, itemCount: Int = 0):
		ChainBuilder = {
		exec(
			insertGraphItemsRequest(auth_token,
				Predef.newGraphItems(itemCount))
		)
	}

	/** Insert graph items request
	  * @param auth_token
	  * @param graph
	  * @return com.excilys.ebi.gatling.http.request.builder.PostHttpRequestBuilder
	  */
	def insertGraphItemsRequest(auth_token: String,
		graph_items: String): PostHttpRequestBuilder = {
		http("Insert graph items")
			.post(Predef.apiUrl("insertGraphItems"))
			.headers(Predef.apiKey)
			.header("auth_token", auth_token)
			.param("graph_items", graph_items)
			.check(bodyString)
	}
}