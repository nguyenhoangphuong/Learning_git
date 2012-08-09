#import "/usr/bin/tuneup_js/tuneup.js"
#import "common.js"
#import "config.js"

function C291() {
	// pull menu down
	app.mainWindow().staticTexts()["MISFIT OF THE DAY"].tap();
	
	// go to About Us
	app.mainWindow().buttons()[2].tap();
	
	// go to website (using in-app browser)
	app.mainWindow().scrollViews()[0].buttons()["btn aboutus website"].tap();
	
	if (isWebsiteScreen()) {
		UIALogger.logPass("Website screen is shown");
	}
	else {
		UIALogger.logFail("Website screen is not shown");
	}
	
	// lock device
	target.lockForDuration(5);
	
	if (isWebsiteScreen()) {
		UIALogger.logPass("Website screen is shown");
	}
	else {
		UIALogger.logFail("Website screen is not shown");
	}
}

test("C291", function(target, app) {
	 C291();
});