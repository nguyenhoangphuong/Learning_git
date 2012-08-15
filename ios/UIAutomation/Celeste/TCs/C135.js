//#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"
#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"
var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C135: Old state of Speaker Details screen", function(target, app) {
     //target.lockForDuration(1);

     // go to agenda screen
     app.mainWindow().scrollViews()[0].buttons()["Speakers"].tap();
     
     target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[0].tap();
     
     target.delay(1);
     
     // deavice the app as press Home button
     target.deactivateAppForDuration(2);

     var timeout = 20;
     
     // set time out to verify agenda screen come back
     target.pushTimeout(timeout);
     
     item = app.navigationBar().staticTexts()["Speakers"];
     item.isValid() ? UIALogger.logPass("Speakers") : UIALogger.logFail("Speakers");
     
     target.popTimeout();
     
     target.frontMostApp().navigationBar().buttons()["Back"].tap();
     target.frontMostApp().navigationBar().leftButton().tap();
     
});