//#import "/Users/tungnguyen/Documents/tuneup_js/tuneup.js"

#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C111", function(target, app) {

target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.98, y:0.40}, endOffset:{x:0.21, y:0.42}});
target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.98, y:0.40}, endOffset:{x:0.11, y:0.43}});
	 
	 		var isAlert = 0;
	 
	 
	 		for(var i = 0;i<3;i++)
	 		{
	 
	 			target.frontMostApp().mainWindow().scrollViews()[0].textFields()[0].setValue("tata@gmail.com");
	 
	 			target.frontMostApp().mainWindow().scrollViews()[0].textViews()[0].tapWithOptions({tapOffset:{x:0.20, y:1.76}});
	 
	 
	 			target.frontMostApp().mainWindow().scrollViews()[0].textViews()[0].setValue("tata");
	 
	 			target.frontMostApp().windows()[1].toolbar().buttons()["Cancel"].tap();
	 
	 
	 
	 			// Alert detected. Expressions for handling alerts should be moved into the UIATarget.onAlert function definition.
	 
	 			target.frontMostApp().mainWindow().scrollViews()[0].buttons()[21].tap();
	 			//target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Feedback SEND"].tap();
	 			// Alert detected. Expressions for handling alerts should be moved into the UIATarget.onAlert function definition.
	 
     
    			UIATarget.onAlert = function onAlert(alert) {
        
        		target.delay(2);
     
        		UIALogger.logDebug("got alert");
        
        		isAlert =  target.frontMostApp().alert().staticTexts()["Thank you!"].isValid(); 
     
       			target.delay(1);
	 
	 			target.frontMostApp().alert().defaultButton().tap();
	 
    
        		// test if your script should handle the alert, and if so, return true
        		// otherwise, return false to use the default handler
        		return true;
    		}
    
    			target.delay(2);
	 
 
	 
			}
	 
	  isAlert?UIALogger.logPass("C104 pass"):UIALogger.logFail("C104 fail");
	 
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.17, y:0.17}, endOffset:{x:0.81, y:0.17}});
	 target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.17, y:0.08}, endOffset:{x:0.93, y:0.01}});
	 
	 
	 
 });
	 
