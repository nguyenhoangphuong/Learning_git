//#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"
#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C47", function(target, app) {
    target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Maps"].tap();
    	
    target.frontMostApp().navigationBar().leftButton().tap();
     
     
});


