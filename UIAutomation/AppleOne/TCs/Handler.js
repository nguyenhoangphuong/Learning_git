#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"


var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("Log In", function(target, app) {
	 UIALogger.logMessage("step 1");
	 app.mainWindow().activityIndicators()["LOGIN"].waitForInvalid();
	 target.delay(1);
	
	 //app.mainWindow().buttons()["Login b1"].tap();
	 
	 target.frontMostApp().mainWindow().buttons()[2].tap();
	 //target.frontMostApp().mainWindow().buttons()[6].tap();
	 
	 
	 target.frontMostApp().mainWindow().textFields()[0].tap();
	 //var textUsername = target.frontMostApp().mainWindow().textFields()[0].getValue(); 
	  //UIALogger.logMessage(textUsername);
	 
	 //target.frontMostApp().mainWindow().textFields()[0].buttons()["Clear text"].tap();
	 target.frontMostApp().keyboard().typeString("1");
	 
	 // expect failure - User Does Not Enter Username or Password
	 //app.mainWindow().scrollViews()[0].secureTextFields()["Password"].setValue("notcorrect");
	 target.frontMostApp().mainWindow().secureTextFields()[0].tap();
	 //target.frontMostApp().mainWindow().secureTextFields()[0].buttons()["Clear text"].tap();
	 target.frontMostApp().mainWindow().secureTextFields()[0].setValue("qwe1");
	 target.frontMostApp().keyboard().typeString("\n");

	 
	 UIATarget.onAlert = function onAlert(alert) {
		target.delay(2);
		UIALogger.logPass("got alert");
	 	return false;
	 
	 }
	 target.frontMostApp().mainWindow().buttons()["Intro b1"].tap();

});
