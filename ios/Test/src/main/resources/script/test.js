
var target = UIATarget.localTarget()

UIALogger.logStart("RUNNING A TEST TEST CASE11");
//throw("An exception");
var app = target.frontMostApp();
var window = app.mainWindow();
target.logElementTree();

UIALogger.logPass("-------------- PASS");
// UIALogger.logFail("-------------- FAIL");

