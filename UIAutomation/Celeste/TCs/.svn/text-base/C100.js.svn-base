//#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"
#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C100", function(target, app) {

    target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.85, y:0.75}, endOffset:{x:0.00, y:0.73}});
     target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.85, y:0.75}, endOffset:{x:0.00, y:0.73}});
     
    target.delay(1);
     
    target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Feedback SEND"].tap();
         
    var isAlert =  0;
     
    UIATarget.onAlert = function onAlert(alert) {
        
        target.delay(2);
     
        UIALogger.logDebug("got alert");
        
        isAlert =  target.frontMostApp().alert().staticTexts()["Invalid email address!"].isValid(); 
     
        target.frontMostApp().alert().defaultButton().tap();
    
        // test if your script should handle the alert, and if so, return true
        // otherwise, return false to use the default handler
        return true;
    }
    
    target.delay(2);
     
    isAlert?UIALogger.logPass("C100 pass"):UIALogger.logFail("C100 fail");
     
    
    target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.00, y:0.73}, endOffset:{x:0.85, y:0.75}});
    target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.00, y:0.73}, endOffset:{x:0.85, y:0.75}});
     
     
 });