#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = target.frontMostApp();

test("C242", function(target, app) {
	 
	
	 target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.48, y:0.37}});
	 
	 target.frontMostApp().mainWindow().buttons()[2].tap();
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["btn aboutus anounce"].tap();
	 
	 if(target.frontMostApp().mainWindow().staticTexts()["ANNOUNCEMENT"].isValid())
	 	
	 	UIALogger.logPass("Announcement screen is shown");
	 
	 else
	 	
	 	UIALogger.logFail("Announcement screen is not shown");
	 
	 
	 target.frontMostApp().mainWindow().buttons()["btn web back"].tap();
	 
	 //verify about us is shown
	 
	 if(target.frontMostApp().mainWindow().scrollViews()[0].staticTexts()["OUR COMPANY"].isValid())
	 	
	 	
	 	UIALogger.logPass("About us screen is shown");
	 
	 else
	 	
	 	UIALogger.logFail("About us screen is not shown");
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["btn aboutus website"].tap();
	 
	 if(target.frontMostApp().mainWindow().staticTexts()["WEBSITE"].isValid())
	  	
	 	UIALogger.logPass("Website screen is shown");
	 
	 else
	 	
	 	UIALogger.logFail("Website screen is not shown");
	 
	 
	 
	 target.frontMostApp().mainWindow().buttons()["btn web back"].tap();
	 
	 //verify about us is shown
	 
	  if(target.frontMostApp().mainWindow().scrollViews()[0].staticTexts()["OUR COMPANY"].isValid())
	 	
	 	UIALogger.logPass("About us screen is shown");
	 
	 else
	 	
	 	UIALogger.logFail("About us screen is not shown");
	 
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["btn aboutus emailforsupport"].tap();
	 
	 //Verify app close
	 
	 if(target.frontMostApp().mainWindow().scrollViews()[0].staticTexts()["OUR COMPANY"].isValid())
	 	
	 	UIALogger.logFail("App is not closed");
	 
	 else
	 	
	 	UIALogger.logPass("App is closed");
	 
	 
	 
/*	  target.frontMostApp().mainWindow().buttons()["btn web back"].tap();
	 
	 target.frontMostApp().mainWindow().staticTexts()["ABOUT US"].tapWithOptions({tapOffset:{x:0.56, y:0.44}});
	 target.frontMostApp().mainWindow().buttons()[0].tap();*/
	 
	 
	 
 });