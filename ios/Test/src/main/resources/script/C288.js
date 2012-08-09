#import "/usr/bin/tuneup_js/tuneup.js"
#import "common.js"
#import "config.js"

function C288() {
	app.mainWindow().staticTexts()["MISFIT OF THE DAY"].tap();
	app.mainWindow().buttons()[2].tap();
	
	if (isAboutUsScreen()) {
		UIALogger.logPass("About Us screen is shown");
	}
	else {
		UIALogger.logFail("About Us screen is not shown");
	}
	
	target.lockForDuration(5);
	
	if (isAboutUsScreen()) {
		UIALogger.logPass("About Us screen is shown");
	}
	else {
		UIALogger.logFail("About Us screen is not shown");
	}
}

test("C288", function(target, app) {
	 C288();
});