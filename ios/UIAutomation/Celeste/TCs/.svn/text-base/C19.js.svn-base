#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"
//#import "/Users/tungnguyen/Dropbox/MISFIT-QA/SVN/MQ/trunk/src/UIAutomation/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = target.frontMostApp();

test("Menu C19", function(target, app) {
	 var loop = 20;
	 var timeout = 3;
	 var count = 0;
	 var minCount = 4;
	 var maxCount = 8;
	 
	 for (var i = 0; i < loop; i++) {
	 	target.pushTimeout(0);
	 	count += app.mainWindow().buttons()["MISFIT"].checkIsValid() ? 1 : 0;
	 	target.popTimeout();
	 
	 	target.delay(timeout);	
	 }
	 
	 var message = "Misfit logo has appeared for "
	 	+ count
	 	+ " time(s) within "
	 	+ (loop * timeout)
	 	+ " second(s). The rate is 3/10 so the number should be from "
	 	+ minCount
	 	+ " to "
	 	+ maxCount;
	 
	 (minCount <= count) && (count <= maxCount) ? UIALogger.logPass(message) : UIALogger.logFail(message);
});