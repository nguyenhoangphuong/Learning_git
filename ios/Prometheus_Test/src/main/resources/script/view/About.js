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
	
	// Methods definition
	function rateApp()
	{
		rate.tap();
	}
	
	function getSupport()
	{
		email.tap();
	}
	
	function likeApp()
	{
		like.tap();
	}
	
	function resetPlan(confirm)
	{
		if(typeof confirm != "undefined")
			alertChoice = "no";
		else if(confirm == "yes")
			alertChoice = "yes";
		else
			alertChoice = "no";
			
		reset.tap();
	}
}