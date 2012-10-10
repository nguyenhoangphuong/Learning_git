#import "../../view/Settings.js"
#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"
#import "../../view/_Prometheus.js"

function GoToSettingScreen()
{
	nav.toSettings(null, null, null, "Running", 10);
}

function VerifyUserProfileButton()
{
	// tap profile
	s = new Settings();
	s.goToProfile();
	
	// check current view is userinfo not setting
	ui = new UserInfo(app.mainWindow().scrollViews()[0]);
	assertTrue(ui.isVisible(), "Current view is UserInfo");
	assertFalse(s.isVisible(), "Current view is not Settings");
	
	// back to setting
	ui.submit();
	
	// check current view is setting not userinfo
	s = new Settings();
	assertFalse(ui.isVisible(), "Current view is not UserInfo");
	assertTrue(s.isVisible(), "Current view is Settings");
}

function VerifySupportButton()
{
	// tap support
	s = new Settings();
	s.tapSupport();
	sp = new Support();
	
	// check view is in support sub view
	assertTrue(sp.isVisible(), "Settings is in support view");
	assertFalse(s.isVisible(), "Settings is not in default view");
	
	// back to settings
	sp.backToSettings();
	s = new Settings();
	
	// check view is in its default view
	assertFalse(sp.isVisible(), "Setting is not in support view");
	assertTrue(s.isVisible(), "Settings is in default view");
}

function VerifyEmailSupportWithoutAccount()
{
	// tap support and then tap email in sub view
	setting = new Settings();
	setting.tapSupport();
	
	sp = new Support();
	sp.emailSupport();
	
	// check no email account alert is shown
	assertTrue(sp.isNoMailAccountAlertShown(), "No mail account alert is shown");
	alert.reset();
	
	// restore starting state
	sp.backToSettings();
}

// =======================================================================
// THESE FUNCTIONS DO NOT RESTORE OLD STATE FOR CONTINUE TEST
// =======================================================================
function VerifyResetButton()
{
	// tap reset button and choose no
	setting = new Settings();
	setting.resetPlan(false);
	assertTrue(setting.isVisible(), "Still in Setting view");
	
	// tap reset button and choose yes
	setting.resetPlan(true);
	wait(2);
	a = new MultiGoalChooser();
	assertTrue(a.isVisible(), "Current view is Activity");
}

function VerifyRateButton()
{
	// tap rate
	setting = new Settings();
	setting.rateApp();
	
	// check app is inactive
	assertFalse(prometheus.isActive(), "App is not active after tap Rate");
}

function VerifyLikeButton()
{
	// tap support and then tap like page in sub view
	setting = new Settings();
	setting.tapSupport();
	
	sp = new Support();
	sp.likePage();
	
	// check app is inactive
	assertFalse(prometheus.isActive(), "App is not active after tap Like our page");
}

function VerifyWebsiteButton()
{
	// tap support and then tap like page in sub view
	setting = new Settings();
	setting.tapSupport();
	
	sp = new Support();
	sp.goToWebsite();
	
	// check app is inactive
	assertFalse(prometheus.isActive(), "App is not active after tap Website");
}
