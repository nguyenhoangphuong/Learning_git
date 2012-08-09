//#import "/Users/misfit/Desktop/tuneup_js/tuneup.js"

#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C93", function(target, app) {

target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.98, y:0.40}, endOffset:{x:0.21, y:0.42}});
target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.98, y:0.40}, endOffset:{x:0.11, y:0.43}});
	 
	 
target.frontMostApp().mainWindow().scrollViews()[0].staticTexts()["Please take some time giving your feedback"].tapWithOptions({tapOffset:{x:0.07, y:0.05}});
	 
	 assertEquals("Feedback SEND",target.frontMostApp().mainWindow().scrollViews()[0].buttons()[21].name());
	 
	 
	 
 });
	 
