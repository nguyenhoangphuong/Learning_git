//#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"
#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C139: Old state of Feedbacks screen", function(target, app) {
     //target.lockForDuration(1);

     target.delay(1);
     
     // go to News screen
     target.dragFromToForDuration({x:240.00, y:416.00}, {x:16.00, y:402.00}, 0.6);
     
     
     target.delay(1);
     
     // go to Feedback screen
     target.dragFromToForDuration({x:240.00, y:416.00}, {x:16.00, y:402.00}, 0.6);
     
     
     target.delay(1);
     
     // deavice the app as press Home button
     target.deactivateAppForDuration(2);

     var timeout = 20;
     
     // set time out to verify agenda screen come back
     target.pushTimeout(timeout);
     
     item = app.mainWindow().pageIndicators()[0];
     item.value() == "page 3 of 3" ? UIALogger.logPass("FEEDBACK view selected") : UIALogger.logFail("FEEDBACK view selected");
     
     target.popTimeout();
     
     target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.18, y:0.08}, endOffset:{x:0.95, y:0.09}});
     
     
     target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.18, y:0.08}, endOffset:{x:0.95, y:0.09}});
     
     
});