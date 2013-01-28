#import "../core/testcaseBase.js"

var target = UIATarget.localTarget();
var timeout = 30;
var oldTimeout = target.timeout();
var loop = 10;
var startTime, endTime;
var signedIn = false;
var time = "";

function signIn(username) {
	target.frontMostApp().mainWindow().buttons()["signin tab"].tap();
	target.frontMostApp().mainWindow().textFields()["email"].setValue(username);
	target.frontMostApp().mainWindow().secureTextFields()["password"].setValue("qwerty1");
	target.frontMostApp().mainWindow().buttons()["finish"].tap();
}

function signOut() {
	target.frontMostApp().tabBar().buttons()["Settings"].tap();
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Sign out"].buttons()["Sign out"].tap();
}

function tabBarIsAvailable() {
	var tabBar = target.frontMostApp().tabBar();
	
	return (tabBar != null
			&& tabBar.isVisible()
			&& tabBar.checkIsValid());
}

function createPlan() {
	target.frontMostApp().mainWindow().buttons()["next"].tap();
	target.frontMostApp().mainWindow().scrollViews()[0].tableViews()[0].cells()["Normal"].tap();
	target.frontMostApp().mainWindow().scrollViews()[0].tableViews()[0].cells()["The Mover's Plan"].tap();
	target.frontMostApp().mainWindow().buttons()["Go for it"].tap();
}

target.pushTimeout(timeout);

function signInStressTest() {
	for (var i = 1; i <= loop; i++) {
		UIALogger.logMessage("Run: " + i);
		//var username = "tung_" + i + "@misfitwearables.com";
		var username = "qa-fullplan@a.a";
		signIn(username);
		startTime = new Date().getTime();
		
		if (tabBarIsAvailable()) {
			endTime = new Date().getTime();
			time += (endTime - startTime) + ",";
			signedIn = true;
			UIALogger.logMessage("Run: " + i + " time: " + time);
		}

		if (signedIn) {
			target.delay(5);
			signOut();
		}
	}
}


function signUpStressTest() {
	for (var i = 1; i <= loop; i++) {
		UIALogger.logMessage("Run: " + i);
		var username = "qa-fullplan@a.a";
		signIn(username);
		startTime = new Date().getTime();

		createPlan();
		
		if (tabBarIsAvailable()) {
			endTime = new Date().getTime();
			time += (endTime - startTime) + ",";
			signedIn = true;
			UIALogger.logMessage("Run: " + i + " time: " + time);
		}

		if (signedIn) {
			target.delay(5);
			signOut();
		}
	}
}

start("Start the stress test");

signInStressTest();

log(time.toString());
pass(" ==== TEST END ====");

