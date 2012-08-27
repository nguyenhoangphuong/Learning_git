

var target = UIATarget.localTarget()

UIALogger.logStart("RUNNING A TEST TEST CASE11");
//throw("An exception");
UIALogger.logStart("--------------0");
var app = target.frontMostApp();
var window = app.mainWindow();
target.logElementTree();
UIALogger.logStart("--------------1");

//throw("An exception");
// UIALogger.logPass("-------------- PASS");
UIALogger.logFail("-------------- FAIL");

