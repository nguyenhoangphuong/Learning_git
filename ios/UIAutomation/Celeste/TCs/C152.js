#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Stress C152", function(target, app) {
	target.frontMostApp().mainWindow().scrollViews()[0].scrollRight();
	 target.delay(5); 
	 
	 for (i = 1 ; i < 100 ; i++)
	 {
	 	target.flickFromTo({x:160, y:100}, {x:160, y:400});
	 
	 }						 
	 target.frontMostApp().mainWindow().scrollViews()[0].scrollLeft();
 	 
	 
	 });

