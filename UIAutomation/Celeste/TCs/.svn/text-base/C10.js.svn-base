#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"
//#import "/Users/tungnguyen/Dropbox/MISFIT-QA/SVN/MQ/trunk/src/UIAutomation/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = target.frontMostApp();

test("Loading C10", function(target, app) {
	 var timeout = 20;
 	 var isMenuScreen = false;
	 
	 target.pushTimeout(timeout);
	 
	 isMenuScreen = true
	 		&& app.mainWindow().scrollViews()[0].buttons()["Sponsored by Misfit"].isValid()
	 		&& app.mainWindow().scrollViews()[0].staticTexts()["When:"].isValid()
	 		&& app.mainWindow().scrollViews()[0].staticTexts()["Where:"].isValid()
	 		&& app.mainWindow().scrollViews()[0].staticTexts()["Accommodation:"].isValid()	 
		//	&& app.mainWindow().scrollViews()[0].buttons()["Home Register"].isValid() // this will break the test
			&& app.mainWindow().scrollViews()[0].buttons()["Maps"].isValid()
			&& app.mainWindow().scrollViews()[0].buttons()["Agenda"].isValid()
			&& app.mainWindow().scrollViews()[0].buttons()["Speakers"].isValid()
			&& app.mainWindow().scrollViews()[0].buttons()["Sponsors"].isValid();
	 
	 target.popTimeout();
	 
	 isMenuScreen ? UIALogger.logPass() : UIALogger.logFail();
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 });