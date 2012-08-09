#import "/usr/bin/tuneup_js/tuneup.js"
#import "common.js"
#import "config.js"

function hasErrorMessage() {
	return target.frontMostApp().mainWindow().staticTexts()["Sorry, there was an error with                                              internet connection. Can you                                              help checking your internet                                              connection?"].isValid();
}

function C261() {
	target.delay(20);
	
	target.frontMostApp().mainWindow().buttons()["Open"].tap();
	
	hasErrorMessage() ? UIALogger.logPass("Error message is displayed") : UIALogger.logFail("Error message is not displayed");
	
	target.lockForDuration(2);
	target.delay(2);
	
	hasErrorMessage() ? UIALogger.logPass("Error message is displayed") : UIALogger.logFail("Error message is not displayed");	
}

test("C261", function(target, app) {
	 C261();
});