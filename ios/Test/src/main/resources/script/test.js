#import "tuneup_js/tuneup.js"
#import "common.js"
#import "config.js"

var target = UIATarget.localTarget()

UIALogger.logStart("RUNNING A TEST TEST CASE");
var app = target.frontMostApp();
var window = app.mainWindow();
target.logElementTree();

