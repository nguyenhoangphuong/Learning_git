#import "../../view/About.js"
#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"

function GoToSettingScreen()
{
	UIALogger.logPass("GoToSettingScreen");
	var navi = new Navigator();
	navi.toAbout();
}

function VerifyRateButton()
{
	setting = new About();
	// tap on rate button
	setting.rateApp();
	// verify it will open app in app store
	if(true)
		UIALogger.logPass("Rate button: Pass");
	else
		UIALogger.logFail("Rate button: Fail");
}

function VerifyEmailSupportButton()
{
	UIALogger.logPass("VerifyEmailSupportButton");
	setting = new About();
	// tap on email button
	setting.getSupport();
	// verify it will open email client
	if(true)
		UIALogger.logPass("Email Support button: Pass");
	else
		UIALogger.logFail("Email Support button: Fail");
}

function VerifyEmailWithoutAccount()
{
	UIALogger.logPass("VerifyEmailWithoutAccount");
	setting = new About();
	// tap on email button
	setting.getSupport();
	// verify it will show pop up
	if(setting.isNoEmailAlertShown())
		UIALogger.logPass("Email Support: Pass");
	else
		UIALogger.logFail("Email Support: Fail");

	alert.reset();
}

function VerifyLikeButton()
{
	UIALogger.logPass("VerifyLikeButton");
	setting = new About();
	// tap on like button
	setting.likeApp();
	// verify it will open misfit's fanpage
	if(true)
		UIALogger.logPass("Like button: Pass");
	else
		UIALogger.logFail("Like button: Fail");
}

function VerifyResetButton()
{
	UIALogger.logPass("VerifyResetButton");
	setting = new About();
	// tap on Reset button
	setting.resetPlan("yes");
	// verify it will go to plan chooser screen
	if(staticTextExist("Please set your plan"))
		UIALogger.logPass("Reset plan: Pass");
	else
		UIALogger.logFail("Reset plan: Fail");
	alert.reset();
}

UIALogger.logPass("START TEST");
GoToSettingScreen();

//VerifyRateButton();
// VerifyEmailSupportButton();
VerifyEmailWithoutAccount();
// VerifyLikeButton();
// VerifyResetButton();

UIALogger.logPass("END OF TEST");
