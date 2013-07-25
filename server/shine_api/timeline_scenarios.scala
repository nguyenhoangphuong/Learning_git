package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import bootstrap._
import com.excilys.ebi.gatling.core.structure._
import com.excilys.ebi.gatling.http.request.builder._

import Shine.Helper._
import Shine.Predef._
import Shine.SignupScenarios._

/** This object contains scenarios of timeline
  */
object TimelineScenarios {

	def getTokenThenInsertTimelineItemsScenario(itemCount: Int = 0,
		loop: Int = 0,
		loopIsDuration: Boolean = true): ScenarioBuilder = {
		var scenarioName: String = "Get token and then insert timeline items"

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
			) // loop insert timeline items
			.doIfOrElse(loopIsDuration == true) {
				during(loop) {
					insertTimelineItems("${auth_token}", itemCount)
				}
			} {
				repeat(loop) {
					insertTimelineItems("${auth_token}", itemCount)
				}
			}
		)
	}

	def insertTimelineItems(auth_token: String, itemCount: Int = 0):
		ChainBuilder = {
		exec(
			insertTimelineItemsRequest(auth_token,
				Predef.newTimelineItems(itemCount))
		)
	}

	/** Insert timeline items request
	  * @param auth_token
	  * @param timeline_items
	  * @return com.excilys.ebi.gatling.http.request.builder.PostHttpRequestBuilder
	  */
	def insertTimelineItemsRequest(auth_token: String,
		timeline_items: String): PostHttpRequestBuilder = {
		http("Insert timeline items request")
			.post(Predef.apiUrl("insertTimelineItems"))
			.headers(Predef.apiKey)
			.header("auth_token", auth_token)
			.param("timeline_items", timeline_items)
			.check(bodyString)
	}

}
