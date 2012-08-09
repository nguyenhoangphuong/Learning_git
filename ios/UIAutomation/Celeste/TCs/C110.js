//#import "/Users/misfit/Desktop/tuneup_js/tuneup.js"

#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C110", function(target, app) {

target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.98, y:0.40}, endOffset:{x:0.21, y:0.42}});
target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.98, y:0.40}, endOffset:{x:0.11, y:0.43}});
	 
	 
	 
	 
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].textFields()[0].setValue("tata@gmail.com");
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].textViews()[0].tapWithOptions({tapOffset:{x:0.20, y:1.76}});
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].textViews()[0].setValue("tata");
	 
	 target.frontMostApp().windows()[1].toolbar().buttons()["Cancel"].tap();
	 
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.07, y:0.52}, endOffset:{x:0.84, y:0.45}});
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda"].scrollToVisible();
	 target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.04, y:0.25}, endOffset:{x:0.59, y:0.26}, duration:2.4});
	 target.frontMostApp().mainWindow().scrollViews()[0].textViews()[0].scrollToVisible();
	 
	 
	 if(target.frontMostApp().mainWindow().scrollViews()[0].textFields()[0].value()=="tata@gmail.com" && target.frontMostApp().mainWindow().scrollViews()[0].textViews()[0].value()=="tata")
	 		UIALogger.logPass("C110 pass");
	 else
	 		UIALogger.logFail("C110 fail");
	 
 });
	 
