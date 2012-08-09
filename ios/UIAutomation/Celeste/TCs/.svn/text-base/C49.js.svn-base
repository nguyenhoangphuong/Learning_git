#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("c49", function(target, app) {
     
     target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Maps"].tap();
	 
	 UIATarget.localTarget().pinchCloseFromToForDuration({x:100, y:100}, {x:200, y:200}, 2); 
	 UIATarget.localTarget().pinchCloseFromToForDuration({x:100, y:100}, {x:200, y:200}, 2);
	 target.delay(1);
	 UIATarget.localTarget().captureScreenWithName("MapTiNy"); 
	 target.delay(1);
	 UIATarget.localTarget().pinchOpenFromToForDuration({x:200, y:200}, {x:100, y:100}, 2);
	 UIATarget.localTarget().pinchOpenFromToForDuration({x:200, y:200}, {x:100, y:100}, 2);
	 target.delay(1);
	 UIATarget.localTarget().captureScreenWithName("MapLarge");
	 target.delay(1);
	 
	 
	 
     target.frontMostApp().navigationBar().leftButton().tap();
	 
	 
	 
	 
     });