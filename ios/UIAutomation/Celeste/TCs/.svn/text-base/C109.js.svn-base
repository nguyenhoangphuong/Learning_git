//#import "/Users/misfit/Desktop/tuneup_js/tuneup.js"

#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C109", function(target, app) {

target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.98, y:0.40}, endOffset:{x:0.21, y:0.42}});
target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.98, y:0.40}, endOffset:{x:0.11, y:0.43}});
	 
	 
	 
	 
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].textFields()[0].setValue("tata@gmail.com");
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].textViews()[0].tapWithOptions({tapOffset:{x:0.20, y:1.76}});
	 
	 
	 var text = "";
	 
	 for (var i = 0;i<2500;i++)
	 {
	 	text = text+"e";
	 }
	 
	  target.frontMostApp().mainWindow().scrollViews()[0].textViews()[0].setValue(text);
	 
	 
	  target.frontMostApp().keyboard().typeString("tata");
	 
	 target.delay(1);
	 var curText =  target.frontMostApp().mainWindow().scrollViews()[0].textViews()[0].value();
	 
	 
	 
	 target.frontMostApp().windows()[1].toolbar().buttons()["Cancel"].tap();
	 
	 
	 UIALogger.logPass(text);
	 UIALogger.logPass(curText);
	 
	 
	 if(curText==text)
	 	UIALogger.logPass("C109 pass");
	 else
	 	UIALogger.logFail("C109 fail")
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].textViews()[0].setValue("");
	 
	  target.frontMostApp().windows()[1].toolbar().buttons()["Cancel"].tap();
	 
		 target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.26, y:0.39}, endOffset:{x:0.91, y:0.55}});	 
	 
	 	 target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.26, y:0.39}, endOffset:{x:0.91, y:0.55}});
	 
	 
	 
 });
	 
