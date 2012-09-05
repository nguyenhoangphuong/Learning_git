#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
*/

function About()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0];
	
	var rate = mainView.buttons()["rate"];
	var email = mainView.buttons()["email"];
	var like = mainView.buttons()["like"];
	var reset = mainView.buttons()["reset"];
	
	var confirmFlag = "no";
	
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