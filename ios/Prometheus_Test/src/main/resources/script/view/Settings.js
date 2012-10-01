#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
Settings functions:
- isVisible()		: check if current view is Settings
- isSupportView()	: check if current view is Support
- isTroublemaker()	: check if current user is a troublemaker
- hasSignedIn()		: check if current user has signed in
- goToProfile		: tap "User Profile" button
- rateApp()			: tap Rate button
- tapSupport()		: tap Support button
	- emailSupport(): tap "Email support" button
	- likePage()	: tap "Like our page" button
	- goToWebsite()	: tap Website button
	- backToSettings()	: tap Back button in Support screen
- tapFeedback()		: tap "JIRA Feedback" button (w/ troublemaker)
- resetPlan(confirm)	: tap Reset button and choose Yes/No when alert shown up
	+ resetPlan("yes")
	+ resetPlan("no")
- signOut()			: tap "Sign out" button (w/ user having logged in)
- signUp()			: tap "Sign up" button (w/ user having not logged in)
- isNoEmailAlertShown()			: check if the NoEmail alert is shown up
- isResetConfirmAlertShown()	: check if the ResetConfirm alert is shown up
*/

function Settings()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0];
	var supportView = window.scrollViews()[0]; // todo: change this
	
	// todo: check indexes
	var profile = mainView.buttons()[0];
	var rate = mainView.buttons()[1];
	var support = mainView.buttons()[2];
	var email = supportView.buttons()[0];
	var like = supportView.buttons()[1];
	var website = supportView.buttons()[2];
	var back = supportView.buttons()[3];
	var feedback = mainView.buttons()[3];
	var reset = mainView.buttons()[4];
	var signOut = mainView.buttons()[5];
	var signUp = mainView.buttons()[6];
	
	// Methods
	this.isVisible = isVisible;
	this.isSupportView = isSupportView;
	this.isTroublemaker = isTroublemaker;
	this.hasSignedIn = hasSignedIn;
	
	this.goToProfile = goToProfile; 
	this.rateApp = rateApp;
	this.tapSupport = tapSupport;
	this.emailSupport = emailSupport;
	this.likePage = likePage;
	this.goToWebsite = goToWebsite;
	this.backToSettings = backToSettings;
	this.tapFeedback = tapFeedback;
	this.resetPlan = resetPlan;
	this.signOut = signOut;
	this.signUp = signUp;
	
	this.isSignOutBtnExist = isSignOutBtnExist;
	
	this.isNoEmailAlertShown = isNoEmailAlertShown;
	this.isResetConfirmAlertShown = isResetConfirmAlertShown;
	this.confirmResetAlert = confirmResetAlert;
	
	// Method definitions
	function isVisible()
	{
		// todo: refine
		page = window.pageIndicators()[0].value();
		return page == "page 1 of 3" && resetBtn.isValid() && resetBtn.isVisible();
	}
	
	function isSupportView()
	{
		// todo: refine
		return email.isValid() && email.isVisible() &&
			like.isValid() && like.isVisible();
	}
	
	function isTroublemaker()
	{
		// todo: refine
		return feedback.isValid() && feedback.isVisible();
	}
	
	function hasSignedIn()
	{
		// todo: refine
		return signOut.isValid() && signOut.isVisible();
	}
	
	function goToProfile()
	{
		// todo: refine
		profile.tap();
	}
	
	function rateApp()
	{
		// todo: refine
		rate.tap();
	}
	
	function tapSupport()
	{
		// todo: refine
		support.tap();
	}
	
	function emailSupport()
	{
		// todo: change this function, use supportView
		email.tap();
		wait(3); // what is this for?
	}
	
	function likePage()
	{
		// todo: change this function, use supportView
		like.tap();
	}
	
	function goToWebsite()
	{
		// todo: change this function, use supportView
		website.tap();
	}
	
	function backToSettings()
	{
		// todo: change this function, use supportView
		back.tap();
	}
	
	function tapFeedback()
	{
		// todo: refine, maybe add methods to feedback
		if (isTroublemaker())
			feedback.tap();
	}
	
	function resetPlan(yes)
	{
		// todo: check
		reset.tap();
		
		// wait for popup
		wait(1);
		
		if (yes == true)
		{
			alert.alertTitle = alert.ResetConfirm;
			window.buttons()[0].tap();
		}
		else 
		{
			alert.alertTitle = alert.ResetConfirm;
			window.buttons()[1].tap();
		}
	}
	
	function signOut()
	{
		// todo: refine
		if (hasSignedIn())
			signOut.tap();
	}
	
	function signUp()
	{
		// todo: refine, maybe add more steps, just maybe
		if (!hasSignedIn())
			signUp.tap();
	}
	
	function isNoEmailAlertShown()
	{
		// todo: check
		log("checking: " + alert.NoEmail);
		return alert.alertTitle != null && alert.alertTitle == alert.NoEmail;
	}
	
	function isResetConfirmAlertShown()
	{
		// todo: check
		log("checking: " + alert.ResetConfirm);
		return alert.alertTitle != null &&
			alert.alertTitle == alert.ResetConfirm;
	}
}
