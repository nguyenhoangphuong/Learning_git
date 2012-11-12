#import "../../view/MVPLibs.js"

function GoToSettingScreen(email, password, uinfo, login)
{
	nav.toSettings(email, password, uinfo, null, login);
}

function FromSettingToSignIn()
{
	s = new Settings();
	
	if(s.isSignOutBtnVisible())
		s.signOut();
	else
		s.signUp();
}

function SetUserInfo(uinfo)
{
	s = new Settings();
	s.setName(uinfo.name);
	s.setGender(uinfo.gender);
	s.setUnit(uinfo.unit);
	s.setWeight(uinfo.w1, uinfo.w2);
	s.setHeight(uinfo.h1, uinfo.h2);
}

//===============================================================

function VerifyEmailSupportWithoutAccount()
{
	settings = new Settings();

	settings.tapSupport();
	assertTrue(settings.isNoMailAccountsAlertShown(),
			"No Mail Accounts alert is shown");
	alert.reset();
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
	
	var p = new PlanPicker();
	
	assertTrue(p.isVisible(), "Current view is PlanPicker");
	
	// reset start point for continue testing
	GoToSettingScreen();
}

function VerifyUserInfoSaved(e)
{
	s = new Settings();
	var i = s.getUserInfo();
	
	if(e.name === "")
		assertTrue(i.name == null, "Name saved");
	else
		assertEqual(i.name, e.name, "Name saved");
	assertEqual(i.unit, e.unit.toLowerCase(), "Unit saved");
	assertEqual(i.gender, e.gender.toLowerCase(), "Gender saved");
	assertEqual(i.weight, e.weight.toLowerCase(), "Weight saved");
	assertEqual(i.height, e.height.toLowerCase(), "Height saved");
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