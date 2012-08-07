//#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"
#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C131: Old state of Agenda Details screen", function(target, app) {
     //target.lockForDuration(1);

     // go to agenda screen
     app.mainWindow().scrollViews()[0].buttons()["Agenda"].tap();
     
     target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells()[0].tap();
     
     target.delay(1);
     
     // deavice the app as press Home button
     target.deactivateAppForDuration(2);

     var timeout = 20;
     
     // set time out to verify agenda screen come back
     target.pushTimeout(timeout);
     
     var item = app.navigationBar().staticTexts()["Agenda"];
     item.isValid() ? UIALogger.logPass("Agenda") : UIALogger.logFail("Agenda");
     
     item = app.mainWindow().scrollViews()[0].staticTexts()["Rate this session"];
     item.isValid() ? UIALogger.logPass("Rate this session") : UIALogger.logFail("Rate this session");
     
     target.popTimeout();
     
     target.frontMostApp().navigationBar().leftButton().tap();
     target.frontMostApp().navigationBar().leftButton().tap();
     
});