#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
Settings functions:
=========================================================================================
- isVisible()		: check if current view is Settings
=========================================================================================
- emailSupport()	: tap "Email support" button
- likePage()		: tap "Like our page" button
- goToWebsite()		: tap Website button
- backToSettings()	: tap Back button in Support screen
=========================================================================================
- isNoMailAccountAlertShown()
=========================================================================================
*/

function Support()
{
	// Private fields
	var window = app.mainWindow();
	var supportView = window.scrollViews()[0];
	
	var btnEmail = supportView.buttons()["Email Support"];
	var btnLike = supportView.buttons()["Like our Page"];
	var btnWebsite = supportView.buttons()["Website"];
	var btnBack = supportView.buttons()["Back"];
	
	// Methods
	this.isVisible = isVisible;

	this.emailSupport = emailSupport;
	this.likePage = likePage;
	this.goToWebsite = goToWebsite;
	this.backToSettings = backToSettings;
	
	this.isNoMailAccountAlertShown = isNoMailAccountAlertShown;
		
	// Method definitions
	function isVisible()
	{
		return btnEmail.isValid() && btnEmail.isVisible() &&
			btnLike.isValid() && btnLike.isVisible();
	}
	
	function emailSupport()
	{
		wait(0.5);
		btnEmail.tap();
		log("Tap [Email Support]");
		wait();
	}
	
	function likePage()
	{
		wait(0.5);
		btnLike.tap();
		log("Tap [Like our Page]");
	}
	
	function goToWebsite()
	{
		wait(0.5);
		btnWebsite.tap();
	}
	
	function backToSettings()
	{
		wait(0.5);
		btnBack.tap();
		log("Tap [Back]");
		wait(0.5);
	}
	
	function isNoMailAccountAlertShown()
	{
		return alert.alertTitle != null && alert.alertTitle == alert.NoEmailAccount;
	}
}
