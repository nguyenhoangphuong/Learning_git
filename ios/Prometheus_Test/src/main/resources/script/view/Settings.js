#import "../core/testcaseBase.js"
#import "_AlertHandler.js"
#import "_NavigationBar.js"

/*
Settings functions:
=========================================================================================
- getResetButton()	: get Reset button since there are 2 cases
=========================================================================================
- isVisible()		: check if current view is Settings
=========================================================================================
- isTroublemaker()	: check if current user is a troublemaker
- hasSignedIn()		: check if current user has signed in
=========================================================================================
- goToProfile		: tap "User Profile" button
- rateApp()			: tap Rate button
- tapSupport()		: tap Support button
- tapFeedback()		: tap "JIRA Feedback" button (w/ troublemaker)
=========================================================================================
- resetPlan(confirm)	: tap Reset button and choose Yes/No when alert shown up
	+ resetPlan("yes")
	+ resetPlan("no")
- signOut(yes = true)	: tap "Sign out" button (w/ user having logged in)
	+ signOut() or signOut(true)	: tap YES when being asked
	+ signOut(false)				: tap NO when being asked
- signUp()				: tap "Sign up" button (w/ user having not logged in)
=========================================================================================
- isSignOutBtnVisible()
- isSignUpBtnVisible()
=========================================================================================
*/

function Settings()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0].scrollViews()[0];
	
	var btnProfile = mainView.buttons()["User Profile"];
	var btnRate = mainView.buttons()["Rate our App"];
	var btnSupport = mainView.buttons()["Support"];

	var btnFeedback = mainView.buttons()["Behind the scenes"];
	var btnReset = getResetButton();
	var btnSignOut = mainView.buttons()["Sign out"];
	var btnSignUp = mainView.buttons()["Sign up"];
	
	// Methods
	this.isVisible = isVisible;
	
	this.isTroublemaker = isTroublemaker;
	this.hasSignedIn = hasSignedIn;
	this.getResetButton = getResetButton;
	
	this.goToProfile = goToProfile; 
	this.rateApp = rateApp;
	this.tapSupport = tapSupport;
	this.tapFeedback = tapFeedback;
	
	this.resetPlan = resetPlan;
	this.signOut = signOut;
	this.signUp = signUp;
	
	this.isSignOutBtnVisible = isSignOutBtnVisible;
	this.isSignUpBtnVisible = isSignUpBtnVisible;
		
	// Method definitions
	
	function isVisible()
	{
		return navigationBar.settingsIsVisible();
	}
	
	function isTroublemaker()
	{
		return btnFeedback.isValid() && btnFeedback.isVisible();
	}
	
	function hasSignedIn()
	{
		return btnSignOut.isValid() && btnSignOut.isVisible();
	}
	
	function getResetButton()
	{
		if (isTroublemaker())
			return mainView.buttons()[5];
		else
			return mainView.buttons()[3];
	}
	
	function goToProfile()
	{
		btnProfile.tap();
		wait();
	}
	
	function rateApp()
	{
		btnRate.tap();
		wait(0.5);
	}
	
	function tapSupport()
	{
		btnSupport.tap();
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
			window.buttons()[1].tap();
		}
		else 
		{
			alert.alertTitle = alert.ResetConfirm;
			window.buttons()[0].tap();
		}
	}
	
	function signOut(yes)
	{
		if (!hasSignedIn())
		{
			log("User has not signed in. No signing out at this time :P");
			
			return;
		}
		
		log("Tap Sign out");
		btnSignOut.tap();
		
		if (typeof yes == "undefined")
			yes = true;
		
		if (yes)
		{
			log("Tap YES to sign out");
			app.mainWindow().buttons()[1].tap();
		}
		else
		{
			log("Tap NO to back to Settings");
			app.mainWindow().buttons()[0].tap();
		}
	}
	
	function signUp()
	{
		if (!hasSignedIn())
		{
			log("Tap Sign up");
			btnSignUp.tap();
		}
	}
	
	function isSignUpBtnVisible()
	{
		btn = mainView.buttons()["Sign up"];
		return btn.isValid() && btn.isVisible();
	}
	
	function isSignOutBtnVisible()
	{
		btn = mainView.buttons()["Sign out"];
		return btn.isValid() && btn.isVisible();
	}
}
