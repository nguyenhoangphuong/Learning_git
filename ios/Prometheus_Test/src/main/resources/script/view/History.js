#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
*/

function History()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window.ScrollViews()[0];
	
	// Methods
	this.edit = edit;
	this.reset = reset;
	this.save = save;
	
	this.canAdjust = canAdjust;
	this.adjustRecord = adjustRecord;
	
	this.isEasyAlertPopup = isEasyAlertPopup;
	this.isHardAlertPopup = isHardAlertPopup;
	
	this.scrollToAbout = scrollToAbout;
	
	// Methods definition
	function scrollToAbout()
	{
		mainView.scrollRight();
	}
	
}