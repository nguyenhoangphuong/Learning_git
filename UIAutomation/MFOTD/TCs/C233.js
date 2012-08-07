#import "/usr/bin/tuneup_js/tuneup.js"
#import "common.js"
#import "config.js"

var target = UIATarget.localTarget();
var app = target.frontMostApp();

test("C233", function(target, app) {
	 
	 target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.48, y:0.37}});
	 target.frontMostApp().mainWindow().buttons()[1].tap();
	 
	 
	 target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.54, y:0.27}});
	 target.frontMostApp().mainWindow().buttons()[0].tap();
	 
	 
	 
	 if(target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].isValid())
	 		UIALogger.logPass("Misfit Collection is shown");
	 
	 else
	 		UIALogger.logFail("Misfit Collection is not shown");
	 
	 
	 
	  target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.48, y:0.37}});
	 
	 target.frontMostApp().mainWindow().buttons()[1].tap();
	 
	 
	 if( target.frontMostApp().mainWindow().staticTexts()["DECK"].isValid())
	 	
	 	UIALogger.logPass("Deck Collection is shown");
	 
	 else
	 	
	 	UIALogger.logFail("Deck Collection is not shown");
	 
	 
	 
	 
	 	 //Need to replace "MISFIT OF THE DAY" by "DECK" when app finish
	  target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.47, y:0.58}});
	 
	 
	 target.frontMostApp().mainWindow().buttons()[2].tap();
	 
	 
	 
	 if( target.frontMostApp().mainWindow().staticTexts()["ABOUT US"].isValid())
	 
	 	
	 	UIALogger.logPass("About us is shown");
	 
	 else
	 	
	 	UIALogger.logFail("About us is not shown");
	 
	 
	 
	 
	 target.frontMostApp().mainWindow().staticTexts()["ABOUT US"].tapWithOptions({tapOffset:{x:0.41, y:0.51}});
	 
	 
	 target.frontMostApp().mainWindow().buttons()[1].tap();
	 
	 
	 if( target.frontMostApp().mainWindow().staticTexts()["DECK"].isValid())
	 
	 	
	 	UIALogger.logPass("Deck Collection is shown");
	 
	 else
	 	
	 	UIALogger.logFail("Deck Collection is not shown");
	 
	 
	 
	 //Need to replace "MISFIT OF THE DAY" by "DECK" when app finish
	 
	  target.frontMostApp().mainWindow().staticTexts()["ABOUT US"].tapWithOptions({tapOffset:{x:0.54, y:0.27}});
	 
	 target.frontMostApp().mainWindow().buttons()[1].tap();
	 
	 if( target.frontMostApp().mainWindow().staticTexts()["DECK"].isValid())
	 
	 	
	 	UIALogger.logPass("Deck Collection is shown");
	 
	 else
	 	
	 	UIALogger.logFail("Deck Collection is not shown");
	 
	 
	 
	 
 });