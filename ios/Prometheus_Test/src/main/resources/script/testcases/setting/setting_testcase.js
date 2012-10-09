#import "../../view/Settings.js"
#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"
#import "../../view/_Prometheus.js"

function GoToSettingScreen(email, password)
{
	nav.toSettings(email, password, null, "Running", 10, true);
}

function VerifyRateButton()
{
	setting = new Settings();
	// tap on rate button
	setting.rateApp();
	// verify it will open app in app store
	// wait();
	if(!prometheus.isActive())
		UIALogger.logPass("Verify Rate button: Pass");
	else
		UIALogger.logFail("Verify Rate button: Fail");
}

function VerifyEmailWithoutAccount()
{
	//UIALogger.logPass("VerifyEmailWithoutAccount");
	setting = new Settings();
	// tap on email button
	setting.getSupport();
	// verify it will show pop up
	if(setting.isNoEmailAlertShown())
		UIALogger.logPass("Verify Email Support: Pass");
	else
		UIALogger.logFail("Verify Email Support: Fail");

	alert.reset();
}

function VerifyLikeButton()
{
	//UIALogger.logPass("VerifyLikeButton");
	setting = new Settings();
	// tap on like button
	setting.likeApp();
	// verify it will open misfit's fanpage
	if(!prometheus.isActive())
		UIALogger.logPass("Verify Like button: Pass");
	else
		UIALogger.logFail("Verify Like button: Fail");
}

function VerifyResetButton()
{
	//UIALogger.logPass("VerifyResetButton");
	setting = new Settings();
	// tap on Reset button
	setting.resetPlan(false);
	assertTrue(setting.isResetConfirmAlertShown(), "Reset confirm alert is shown");
	assertTrue(setting.isVisible(), "Still in Setting view");
	
	setting.resetPlan(true);
	wait(2);
	pc = new PlanChooser();
	assertTrue(pc.isVisible(), "Current view is PlanChooser");
	
	alert.reset();
}

//UIALogger.logPass("START TEST");
//GoToSettingScreen();

//VerifyRateButton();
// VerifyEmailSupportButton();
// VerifyEmailWithoutAccount();
// VerifyLikeButton();
// VerifyResetButton();

//UIALogger.logPass("END OF TEST");
