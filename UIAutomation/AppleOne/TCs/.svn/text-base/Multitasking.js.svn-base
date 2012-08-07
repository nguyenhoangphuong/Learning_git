#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Log In", function(target, app) {
	UIALogger.logMessage("step 1");
	 
	 
	app.mainWindow().activityIndicators()["LOGIN"].waitForInvalid();
	target.delay(1);
	 
	//app.mainWindow().buttons()["Login b1"].tap();
	 
    var loginButton = app.mainWindow().buttons()["Login b1"];
	 
	 if (loginButton.isValid()) {
	 	loginButton.tap();
	 } else {
	 	UIALogger.logError("Could not find 'login' button!");
	 	
	 	// Sign out to re-login
	 	SignOut(target, app);
	 	target.delay(1);
	 	loginButton = app.mainWindow().buttons()["Login b1"];
	 	loginButton.tap();
	 }
	 
	 
	target.frontMostApp().mainWindow().textFields()[0].tap();
	 
	target.frontMostApp().keyboard().typeString("1");
	 

	target.frontMostApp().mainWindow().secureTextFields()[0].tap();


	target.frontMostApp().mainWindow().secureTextFields()[0].setValue("qwe");
	target.frontMostApp().keyboard().typeString("\n");
	target.delay(1) ;
	 
	 
//set orientation to landscape left
	target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_LANDSCAPELEFT);
	UIALogger.logMessage("Current orientation now " + app.interfaceOrientation());

	target.delay(2);
	 
//reset orientation to portrait
	target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_PORTRAIT);
	UIALogger.logMessage("Current orientation now " + app.interfaceOrientation());
	 
	 
	target.delay(2);
	 
	 
//reset orientation to portrait
	target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_FACEDOWN);
	UIALogger.logMessage("Current orientation now " + app.interfaceOrientation());
	 
	 
	target.delay(2);
	 

	 //reset orientation to portrait
	target.setDeviceOrientation(UIA_DEVICE_ORIENTATION_FACEUP);
	UIALogger.logMessage("Current orientation now " + app.interfaceOrientation());
	 
	 
	target.delay(2);
	 
	 
	UIATarget.localTarget().deactivateAppForDuration(10);

	target.delay(3);

});

function SignOut (target, app) {
	target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.23, y:0.71}, endOffset:{x:0.99, y:0.71}});
	 
	 app.mainWindow().activityIndicators()["SIGN OUT"].waitForInvalid();
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["SIGN OUT"].tap();
	 target.delay(2);
}