package Shine

import scala.util.Random

/** This object contains general methods to help out
  */
object Helper {

	/** Generate random numeric string
	  * @param randomLevel random level (0 - 10)
	  * @return random numeric string
	  */
	def randomNumericString(randomLevel: Int = 3): String = {
		var niceRandomLevel: Int = randomLevel
		var i: Int = 0
		var n: String = ""

		if (niceRandomLevel < 0) niceRandomLevel = 0
		if (niceRandomLevel > 10) niceRandomLevel = 10

		for (i <- 1 to niceRandomLevel) {
			n += (new Random).nextInt(100000).toString
		}

		return n
	}
	
}
