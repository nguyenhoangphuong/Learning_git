#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = target.frontMostApp();

test("C243", function(target, app) {
	 
	
	 target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.48, y:0.37}});
	 
	 target.frontMostApp().mainWindow().buttons()[2].tap();
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["btn aboutus anounce"].tap();
	 
	 if(target.frontMostApp().mainWindow().staticTexts()["ANNOUNCEMENT"].isValid())
	 	
	 	UIALogger.logPass("Announcement screen is shown");
	 
	 else
	 	
	 	UIALogger.logFail("Announcement screen is not shown");
	 
	 
	 target.frontMostApp().mainWindow().buttons()["btn web back"].tap();
	 
 });