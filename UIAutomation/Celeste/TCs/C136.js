//#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"
#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C136: Old state of Venue Map screen", function(target, app) {
     //target.lockForDuration(1);

     // go to agenda screen
     app.mainWindow().scrollViews()[0].buttons()["Maps"].tap();
     
     
     target.delay(1);
     
     // deavice the app as press Home button
     target.deactivateAppForDuration(2);

     var timeout = 20;
     
     // set time out to verify agenda screen come back
     target.pushTimeout(timeout);
     
     item = app.navigationBar().staticTexts()["Venue Map"];
     item.isValid() ? UIALogger.logPass("Venue Map") : UIALogger.logFail("Venue Map");
     
     target.popTimeout();
     
     target.frontMostApp().navigationBar().leftButton().tap();
     
});