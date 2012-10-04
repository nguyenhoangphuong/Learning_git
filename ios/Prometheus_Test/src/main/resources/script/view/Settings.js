#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
Settings functions:
- assignControls()	: assign controls for both Settings and Support views
- isVisible()		: check if current view is Settings
- isSupportView()	: check if current view is Support
- isTroublemaker()	: check if current user is a troublemaker
- hasSignedIn()		: check if current user has signed in
- getResetButton()	: get Reset button since there are 2 cases
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
//- isNoEmailAlertShown()			: check if the NoEmail alert is shown up
//- isResetConfirmAlertShown()	: check if the ResetConfirm alert is shown up
*/

function Settings()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0];
	var supportView = mainView;
	
	var btnProfile = mainView.scrollViews()[0].buttons()["User Profile"];
	var btnRate = mainView.scrollViews()[0].buttons()["Rate our App"];
	var btnSupport = mainView.scrollViews()[0].buttons()["Support"];
	var btnEmail = supportView.buttons()["Email Support"];
	var btnLike = supportView.buttons()["Like our Page"];
	var btnWebsite = supportView.buttons()["Website"];
	var btnBack = supportView.buttons()["Back"];
	var btnFeedback = mainView.scrollViews()[0].buttons()["Behind the scenes"];
	var btnReset = getResetButton();
	var btnSignOut = mainView.scrollViews()[0].buttons()["Sign out"]; // CHECK
	var btnSignUp = mainView.scrollViews()[0].buttons()["Sign up"];
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	this.isSupportView = isSupportView;
	this.isTroublemaker = isTroublemaker;
	this.hasSignedIn = hasSignedIn;
	this.getResetButton = getResetButton;
	
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
		
//	this.isNoEmailAlertShown = isNoEmailAlertShown;
//	this.isResetConfirmAlertShown = isResetConfirmAlertShown;
	
	// Method definitions
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window.scrollViews()[0];
		supportView = mainView;
		
		btnProfile = mainView.scrollViews()[0].buttons()["User Profile"];
		btnRate = mainView.scrollViews()[0].buttons()["Rate our App"];
		btnSupport = mainView.scrollViews()[0].buttons()["Support"];
		btnEmail = supportView.buttons()["Email Support"];
		btnLike = supportView.buttons()["Like our Page"];
		btnWebsite = supportView.buttons()["Website"];
		btnBack = supportView.buttons()["Back"];
		btnFeedback = mainView.scrollViews()[0].buttons()["Behind the scenes"];
		btnReset = getResetButton();
		btnSignOut = mainView.scrollViews()[0].buttons()["Sign out"]; // CHECK
		btnSignUp = mainView.scrollViews()[0].buttons()["Sign up"];	
	}
	
	function isVisible()
	{
		return btnProfile.isValid() && btnProfile.isVisible();
	}
	
	function isSupportView()
	{
		return btnEmail.isValid() && btnEmail.isVisible() &&
			btnLike.isValid() && btnLike.isVisible();
	}
	
	function isTroublemaker()
	{
		return btnFeedback.isValid() && btnFeedback.isVisible();
	}
	
	function hasSignedIn()
	{
		// check this function when bug (there is no "Sign out" button) is fixed
		return btnSignOut.isValid() && btnSignOut.isVisible();
	}
	
	function getResetButton()
	{
		if (isTroublemaker())
			return mainView.scrollViews()[0].buttons()[5];
		else
			return mainView.scrollViews()[0].buttons()[3];
	}
	
	function goToProfile()
	{
		btnProfile.tap();
	}
	
	function rateApp()
	{
		btnRate.tap();
	}
	
	function tapSupport()
	{
		btnSupport.tap();
	}
	
	function emailSupport()
	{
		if (isVisible())
		{
			tapSupport();
			wait(2);
		}
		
		assignControls();
		btnEmail.tap();
	}
	
	function likePage()
	{
		if (isVisible())
		{
			tapSupport();
			wait(2);
		}
		
		assignControls();
		btnLike.tap();
	}
	
	function goToWebsite()
	{
		if (isVisible())
		{
			tapSupport();
			wait(2);
		}
		
		assignControls();
		btnWebsite.tap();
	}
	
	function backToSettings()
	{
		btnBack.tap();
	}
	
	function tapFeedback()
	{
		if (isTroublemaker())
			btnFeedback.tap();
	}
	
	function resetPlan(yes)
	{
		btnReset.tap();
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
		// TODO: refine
		if (hasSignedIn())
			btSignOut.tap();
	}
	
	function signUp()
	{
		// TODO: refine, maybe add more steps, just maybe
		if (!hasSignedIn())
			btnSignUp.tap();
	}
	
//	function isNoEmailAlertShown()
//	{
//		// todo: check
//		log("checking: " + alert.NoEmail);
//		return alert.alertTitle != null && alert.alertTitle == alert.NoEmail;
//	}
//	
//	function isResetConfirmAlertShown()
//	{
//		// todo: check
//		log("checking: " + alert.ResetConfirm);
//		return alert.alertTitle != null &&
//			alert.alertTitle == alert.ResetConfirm;
//	}
}
