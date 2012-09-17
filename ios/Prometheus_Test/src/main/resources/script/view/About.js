#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
About functions:
- isVisible()	:	check if the current view is About
- rateApp()		:	tap Rate button
- getSupport()	:	tap Email button and handler message box
- likeApp()		:	tap Like button
- resetPlan(confirm)	:	tap Reset button and choose Yes/No when alert shown up
	+ resetPlan("yes")
	+ resetPlan("no")
- isNoEmailAlertShown()			:	check if the NoEmail alert is shown up
- isResetConfirmAlertShown()	:	check if the ResetConfirm alert is shown up
*/

function About()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0];
	
	var rate = mainView.buttons()[0];
	var email = mainView.buttons()[1];
	var like = mainView.buttons()[2];
	var reset = mainView.buttons()[3];
	
	// Methods
	this.isVisible = isVisible;
	
	this.rateApp = rateApp;
	this.getSupport = getSupport;
	this.likeApp = likeApp;
	this.resetPlan = resetPlan;
	
	this.isNoEmailAlertShown = isNoEmailAlertShown;
	this.isResetCofirmAlertShown = isResetConfirmAlertShown;
	
	// Methods definition
	function isVisible()
	{
		page = window.pageIndicators()[0].value();
		return page == "page 1 of 3" && rate.isValid() && rate.isVisible();
	}

	
	function rateApp()
	{
		rate.tap();
	}
	
	function getSupport()
	{
		email.tap();
		wait(3);
	}
	
	function likeApp()
	{
		like.tap();
	}
	
	function resetPlan(yes)
	{
		reset.tap();
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
	
	
	function isNoEmailAlertShown()
	{
		log("checking: " + alert.NoEmail);
		return alert.alertTitle != null && alert.alertTitle == alert.NoEmail;
	}
	
	function isResetConfirmAlertShown()
	{
		log("checking: " + alert.ResetConfirm);
		return alert.alertTitle != null && alert.alertTitle == alert.ResetConfirm;
	}
}
