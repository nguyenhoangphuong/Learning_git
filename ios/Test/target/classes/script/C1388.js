#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = target.frontMostApp();

test("C1388", function(target, app) {
	 
	 //Verify touch on it
	 
	 target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.57, y:0.76}});
	 
	 
	 
	 
	 target.delay(2);
	 
	 target.tap({x:59.00, y:41.50});
	 
	 target.delay(2);

	 target.tap({x:160.00, y:242.00});
	 
	 target.delay(2);
	 
	 if(target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid())
	 	
	 	UIALogger.logPass("Menu can be closed by touching on it");
	 
	 else
	 
	 	UIALogger.logFail("Menu can not be closed by touching on it");
	 
	 	
	 
	 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
 	 
	 
	 
	 //Verify touch outside
	 
	 target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.55, y:0.56}});
	 
	 target.delay(2);
	 
	 target.tap({x:237.50, y:397.00});
	 
	 target.delay(2);

	 target.tap({x:160.00, y:242.00});
	 
	 target.delay(2);
	 
	 if(target.frontMostApp().mainWindow().buttons()["btn getthiscard"].isValid())
	 	
	 	UIALogger.logPass("Menu can be closed by touching outside of it");
	 
	 else
	 
	 	UIALogger.logFail("Menu can not be closed by touching outside of it");
	 
	 	
	 
	 target.frontMostApp().mainWindow().buttons()["btn getthiscard"].tap();
 	 
	 
	 
	 
	 
	 
});