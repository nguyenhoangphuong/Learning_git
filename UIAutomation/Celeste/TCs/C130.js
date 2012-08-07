//#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"
#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"
var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C130: Old state of Agenda screen", function(target, app) {
     //target.lockForDuration(1);

     // go to agenda screen
     app.mainWindow().scrollViews()[0].buttons()["Agenda"].tap();
     
     target.delay(1);
     
     // deavice the app as press Home button
     target.deactivateAppForDuration(2);

     var timeout = 20;
     
     // set time out to verify agenda screen come back
     target.pushTimeout(timeout);
     
     var item = app.navigationBar().segmentedControls()[0].buttons()["Agenda list StarButton"];
     item.isValid() ? UIALogger.logPass("Favorite button") : UIALogger.logFail("Favorite buttont");
     
     item = app.navigationBar().segmentedControls()[0].buttons()["Agenda list ListButton"];
     item.isValid() ? UIALogger.logPass("Default button") : UIALogger.logFail("Default button");
     
     item = app.navigationBar().staticTexts()["Agenda"];
     item.isValid() ? UIALogger.logPass("Agenda") : UIALogger.logFail("Agenda");
     
     target.popTimeout();
     
     target.frontMostApp().navigationBar().leftButton().tap();
     
});