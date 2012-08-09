//#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"
#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"
var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C134: Old state of Speakers screen", function(target, app) {
     //target.lockForDuration(1);

     // go to agenda screen
     app.mainWindow().scrollViews()[0].buttons()["Speakers"].tap();
     
     target.delay(1);
     
     // deavice the app as press Home button
     target.deactivateAppForDuration(2);

     var timeout = 20;
     
     // set time out to verify agenda screen come back
     target.pushTimeout(timeout);
     
     item = app.navigationBar().staticTexts()["Speakers"];
     item.isValid() ? UIALogger.logPass("Speakers") : UIALogger.logFail("Speakers");
     
     target.popTimeout();
     
     target.frontMostApp().navigationBar().leftButton().tap();
     
});