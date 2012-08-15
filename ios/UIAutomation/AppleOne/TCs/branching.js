#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"


var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("Log In sucess", function(target, app) {
	 
	 // Define data
	 var usrname = [];
	 usrname[0] = "January";
	 usrname[1] = "February";
	 //usrname[2] = "March";
	 var password = [];
	 password[0] = "qwe";
	 password[1] = "qwe";
	 //password[2] = "qwe";
	
	 // test each data
	 for (i=0; i<password.length; i++) {
	 //UIALogger. logMessage("data: " + i);
	 var strUser = usrname[i];
	 var strPass = password[i];
	 
	 UIALogger.logMessage("Data: " + i + " - user:" + strUser + " - pass:" + strPass);
	 
	 app.mainWindow().activityIndicators()["LOGIN"].waitForInvalid();
	 target.delay(1);
	 
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
	 
	 //var textUsername = target.frontMostApp().mainWindow().textFields()[0].getValue(); 
	  //UIALogger.logMessage(textUsername);
	 
	 //target.frontMostApp().mainWindow().textFields()[0].buttons()["Clear text"].tap();

	 target.frontMostApp().mainWindow().textFields()[0].setValue(strUser);
	 //target.frontMostApp().keyboard().typeString("1");

	 target.delay(1);

	 target.frontMostApp().mainWindow().secureTextFields()[0].tap();
	 //target.frontMostApp().mainWindow().secureTextFields()[0].buttons()["Clear text"].tap();
	 target.frontMostApp().mainWindow().secureTextFields()[0].setValue(strPass);
	 target.frontMostApp().keyboard().typeString("\n");
	 
	 target.delay(1) ;
	
	 
     target.frontMostApp().mainWindow().scrollViews()[0].tableViews()[1].cells()["Misfit"].buttons()["ADD"].tap();
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Go back"].tap();
	 
	 /*
	 target.frontMostApp().mainWindow().scrollViews()[0].tableViews()[1].cells()["Withings"].buttons()["ADD"].tap();
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Go back"].tap();
	 target.frontMostApp().mainWindow().scrollViews()[0].tableViews()[1].cells()["Fitbit"].buttons()["ADD"].tap();
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Go back"].tap();
	 target.frontMostApp().mainWindow().scrollViews()[0].tableViews()[1].cells()["Zeo"].buttons()["ADD"].tap();
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Go back"].tap();
	 target.frontMostApp().mainWindow().scrollViews()[0].tableViews()[1].cells()["Bodymedia"].tap();
	 target.frontMostApp().mainWindow().scrollViews()[0].tableViews()[1].cells()["Bodymedia"].buttons()["ADD"].tap();
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Go back"].tap();
	 */
	 
	 target.delay(1);
	 
	 // create the branching issue
	 if (i != 0) {
	 	SignOut(target, app);
	 }
	 
	}

});

function SignOut (target, app) {
	target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.23, y:0.71}, endOffset:{x:0.99, y:0.71}});
	 
	 app.mainWindow().activityIndicators()["SIGN OUT"].waitForInvalid();
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["SIGN OUT"].tap();
	 target.delay(2);
}
