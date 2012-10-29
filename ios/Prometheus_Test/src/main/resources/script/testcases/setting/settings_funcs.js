#import "../../view/MVPLibs.js"

function GoToSettingScreen()
{
	nav.toSettings(null, null, null, null);
}

function VerifyEmailSupportWithoutAccount()
{
	settings = new Settings();

	settings.tapSupport();
	assertTrue(settings.isNoMailAccountsAlertShown(),
			"No Mail Accounts alert is shown");
	alert.reset();
}

// ===============================================================
// THESE FUNCTIONS DO NOT RESTORE OLD STATE FOR CONTINUING TESTING
// ===============================================================

function VerifyLikeButton()
{
	settings = new Settings();
	settings.tapLike();

	// check if app is inactive
	assertFalse(prometheus.isActive(), "App is not active after tap Like our page");
}

function VerifyWebsiteButton()
{
	settings = new Settings();
	settings.tapWebsite();
	
	// check if app is inactive
	assertFalse(prometheus.isActive(), "App is not active after tap Website");
}

function VerifyRateButton()
{
	settings = new Settings();
	settings.tapRate();
	
	// check if app is inactive
	assertFalse(prometheus.isActive(), "App is not active after tap Rate");
}

function VerifyResetButton()
{
	// tap Reset button and choose NO
	settings = new Settings();
	settings.resetPlan("no");
	assertTrue(settings.isVisible(), "Still in Settings view");
	wait(2);
	
	// tap Reset button and choose YES
	settings.resetPlan("yes");
	wait(2);
	
	var a = new MultiGoalChooser();
	
	assertTrue(a.isVisible(), "Current view is Activity");
}