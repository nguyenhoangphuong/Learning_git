package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import bootstrap._
import com.excilys.ebi.gatling.core.structure._
import com.excilys.ebi.gatling.http.request.builder._

import Shine.Helper._
import Shine.Predef._

/** This object contains scenarios of hello world
  */
object HelloScenarios {

	/** Hello multiple times
	  * @param loop looping duration/times (depends on loopIsDuration)
	  * @param loopIsDuration decides loop is duration or number of repeating
	  * @return com.excilys.ebi.gatling.core.structure.ScenarioBuilder
	  */
	def helloMultipleTimesScenario(loop: Int = 0,
		loopIsDuration: Boolean = true): ScenarioBuilder = {
		var scenarioName: String = "Hello multiple times"

		scenarioName += " - Run ID: " + Helper.randomNumericString(1)
		scenario(scenarioName)
		.doIfOrElse(loopIsDuration == true) {
			during(loop) {
				hello()
			}
		} { // else: loop is looping times
			repeat(loop) {
				hello()
			}
		}
	}

	/** Hello
	  * @return com.excilys.ebi.gatling.core.structure.ChainBuilder
	  */
	def hello(): ChainBuilder = {
		exec(
			http("Hello request")
			.post(Predef.apiUrl("hello"))
			.headers(Predef.apiKey)
			.check(bodyString)
		)
	}

}
