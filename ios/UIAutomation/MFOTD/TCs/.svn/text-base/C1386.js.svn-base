#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = target.frontMostApp();

test("C1386", function(target, app) {
	 
	 
	 target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.34, y:0.24}});
	 
	 target.delay(2);
	 
	 target.tap({x:91.50, y:391.50});
	 
	 
	 if(target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid())
	 
	 		UIALogger.logFail("C1386 Fail1");
	 
	 else
	 		UIALogger.logPass("C1386 Pass1");
	 
	 
	 target.delay(2);
	 
	 target.tap({x:91.50, y:391.50});
	 
	 
	 

	 target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.44, y:0.46}});
	 
	 
	 
	
	 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
	 
	
	 
	  if(target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid())
	 
	 		UIALogger.logPass("C1386 Pass2");
	 
	 else
	 		UIALogger.logFail("C1386 Fail1");
	 
	 
	 
	 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
	 
	 
	 });