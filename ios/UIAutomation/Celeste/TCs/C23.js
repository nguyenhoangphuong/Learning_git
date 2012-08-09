//#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"
#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C23: Old state of Menu screen", function(target, app) {
     //target.lockForDuration(1);
    
	 target.delay(2);
     target.deactivateAppForDuration(5);

     var timeout = 20;
     var isMenuScreen = false;
     
     target.pushTimeout(timeout);
     
     var item = app.mainWindow().scrollViews()[0].buttons()["Sponsored by Misfit"];
     item.isValid() ? UIALogger.logPass("Sponsored by Misfit") : UIALogger.logFail("Sponsored by Misfit");
     
     item = app.mainWindow().scrollViews()[0].staticTexts()["When:"];
     item.isValid() ? UIALogger.logPass("When:") : UIALogger.logFail("When:");
     
     item = app.mainWindow().scrollViews()[0].staticTexts()["Where:"];
     item.isValid() ? UIALogger.logPass("Where:") : UIALogger.logFail("Where:");
     
     item = app.mainWindow().scrollViews()[0].staticTexts()["Accommodation:"];
     item.isValid() ? UIALogger.logPass("Accommodation:") : UIALogger.logFail("Accommodation:");
     item = app.mainWindow().scrollViews()[0].buttons()["Home Register"];
     item.isValid() ? UIALogger.logPass("Home Register") : UIALogger.logFail("Home Register");
     
     item = app.mainWindow().scrollViews()[0].buttons()["Maps"];
     item.isValid() ? UIALogger.logPass("Maps") : UIALogger.logFail("Maps");
     
     item = app.mainWindow().scrollViews()[0].buttons()["Agenda"];
     item.isValid() ? UIALogger.logPass("Agenda") : UIALogger.logFail("Agenda");
     
     item = app.mainWindow().scrollViews()[0].buttons()["Speakers"];
     item.isValid() ? UIALogger.logPass("Speakers") : UIALogger.logFail("Speakers");
     
     item = app.mainWindow().scrollViews()[0].buttons()["Sponsors"];
     item.isValid() ? UIALogger.logPass("Sponsors") : UIALogger.logFail("Sponsors");
     
     target.popTimeout();
});