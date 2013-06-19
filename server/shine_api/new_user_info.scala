package Shine

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._
import scala.util.Random
import scala.compat.Platform

object NewUserInfo {
	val newUserInfo = new Feeder[String] {
		override def hasNext = true

		override def next: Map[String, String] = {
			var newEmail = "test@email.number"
			
			newEmail += (new Random).nextInt(100000).toString
			newEmail += (new Random).nextInt(100000).toString
			newEmail += (new Random).nextInt(100000).toString
			//newEmail += Platform.currentTime.toString

			Map("email" -> newEmail,
			"password" -> "qwerty1",
			"udid" -> "2d37b0812fb99cb76648cc99fc427eb7fc1613ad")
		}
	}
}
