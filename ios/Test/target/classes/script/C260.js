#import "/usr/bin/tuneup_js/tuneup.js"
#import "common.js"
#import "config.js"

function C260() {
	target.delay(20);
	
	isGiftBox() ? UIALogger.logPass("Gift box is displayed") : UIALogger.logFail("Gift box is not displayed");
	
	//target.deactivateAppForDuration(10);
	target.lockForDuration(2);
	target.delay(5);
	
	isGiftBox() ? UIALogger.logPass("Gift box is displayed") : UIALogger.logFail("Gift box is not displayed");
}

test("C260", function(target, app) {
	 C260();
});