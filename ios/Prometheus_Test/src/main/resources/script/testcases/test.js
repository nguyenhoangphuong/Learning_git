#import "../view/SignUp.js"
#import "../view/UserInfo.js"
#import "../view/PlanChooser.js"
#import "../view/WeeklyGoal.js"
#import "../core/testcaseBase.js"


var target = UIATarget.localTarget()

UIALogger.logStart("TESTTTTT");
//throw("An exception");
var app = target.frontMostApp();
var window = app.mainWindow();
logTree();
//var text= window.staticTexts()[0];
//log("Value= " + text.value);
//text.logElement();

pass("==== END ====");

