#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
About functions:
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
	this.rateApp = rateApp;
	this.getSupport = getSupport;
	this.likeApp = likeApp;
	this.resetPlan = resetPlan;
	
	this.isNoEmailAlertShown = isNoEmailAlertShown;
	this.isResetCofirmAlertShown = isResetConfirmAlertShown;
	
	// Methods definition
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
	
	function resetPlan(confirm)
	{
		if(typeof confirm != "undefined")
			alertChoice = "No";
		else if(confirm == "yes")
			alertChoice = "Yes";
		else
			alertChoice = "No";
		
		reset.tap();
		
		// wait for alert to shown up
		wait(3);
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