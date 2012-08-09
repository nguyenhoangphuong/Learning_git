#import "/usr/bin/tuneup_js/tuneup.js"
#import "common.js"
#import "config.js"

function C289() {
	// pull menu down
	app.mainWindow().staticTexts()["MISFIT OF THE DAY"].tap();
	
	// go to About Us
	app.mainWindow().buttons()[2].tap();
	
	// go to Announcement
	app.mainWindow().scrollViews()[0].buttons()["btn aboutus anounce"].tap();
	
	if (isAnnouncementScreen()) {
		UIALogger.logPass("Announcement screen is shown");
	}
	else {
		UIALogger.logFail("Announcement screen is not shown");
	}
	
	// lock device
	target.lockForDuration(5);
	
	if (isAnnouncementScreen()) {
		UIALogger.logPass("Announcement screen is shown");
	}
	else {
		UIALogger.logFail("Announcement screen is not shown");
	}
}

test("C289", function(target, app) {
	 C289();
});