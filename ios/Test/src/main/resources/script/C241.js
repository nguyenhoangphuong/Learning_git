#import "/usr/bin/tuneup_js/tuneup.js"
#import "common.js"
#import "config.js"


var target = UIATarget.localTarget();
var app = target.frontMostApp();

test("C241", function(target, app) {
	 
	
	 target.frontMostApp().mainWindow().staticTexts()["MISFIT OF THE DAY"].tapWithOptions({tapOffset:{x:0.48, y:0.37}});
	 target.frontMostApp().mainWindow().buttons()[2].tap();
	 
	 
	 var isPass = target.frontMostApp().mainWindow().scrollViews()[0].staticTexts()["OUR COMPANY"].isValid()
	 && target.frontMostApp().mainWindow().scrollViews()[0].staticTexts()[1].isValid()
	 &&  target.frontMostApp().mainWindow().scrollViews()[0].staticTexts()["LOCATION"].isValid()
	 && target.frontMostApp().mainWindow().scrollViews()[0].buttons()["btn aboutus anounce"].isValid()
	 &&  target.frontMostApp().mainWindow().scrollViews()[0].buttons()["btn aboutus emailforsupport"].isValid() 
	 && target.frontMostApp().mainWindow().scrollViews()[0].buttons()["btn aboutus website"].isValid();
	 
	 
	 if(isPass)
	 	UIALogger.logPass("C241 pass");
	 
	 else
	 	UIALogger.logFail("C241 fail");
	 
	 
	 
	 
	 
	 target.frontMostApp().mainWindow().staticTexts()["ABOUT US"].tapWithOptions({tapOffset:{x:0.49, y:0.40}});
	 target.frontMostApp().mainWindow().buttons()[0].tap();
	 

	 
 });