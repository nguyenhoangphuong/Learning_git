#import "../../view/About.js"
#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"
#import "../../view/_Prometheus.js"

function GoToSettingScreen()
{
	UIALogger.logPass("GoToSettingScreen");
	nav.toAbout();
}

function VerifyRateButton()
{
	setting = new About();
	// tap on rate button
	setting.rateApp();
	// verify it will open app in app store
	// wait();
	if(!prometheus.isActive())
		UIALogger.logPass("Verify Rate button: Pass");
	else
		UIALogger.logFail("Verify Rate button: Fail");
}

function VerifyEmailSupportButton()
{
	//UIALogger.logPass("VerifyEmailSupportButton");
	setting = new About();
	// tap on email button
	setting.getSupport();
	// verify it will open email client
	if(!prometheus.isActive())
		UIALogger.logPass("Verify Email Support button: Pass");
	else
		UIALogger.logFail("Verify Email Support button: Fail");
}

function VerifyEmailWithoutAccount()
{
	//UIALogger.logPass("VerifyEmailWithoutAccount");
	setting = new About();
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
	setting = new About();
	// tap on like button
	setting.likeApp();
	// verify it will open misfit's fanpage
	if(prometheus.isActive())
		UIALogger.logPass("Verify Like button: Pass");
	else
		UIALogger.logFail("Verify Like button: Fail");
}

function VerifyResetButton()
{
	//UIALogger.logPass("VerifyResetButton");
	setting = new About();
	// tap on Reset button
	setting.resetPlan("yes");
	// verify it will go to plan chooser screen
	if(staticTextExist("Please set your plan"))
		UIALogger.logPass("Verify Reset plan: Pass");
	else
		UIALogger.logFail("Verify Reset plan: Fail");
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
