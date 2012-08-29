#import "../core/testcaseBase.js"

function Agreement()
{
	// Private fields
	var mainView = app.mainWindow();
	var agreeButton = mainView.buttons()["agree"];
	var disagreeButton = mainView.buttons()["disagree"];
	
	// Methods
	this.tapAgree = tapAgree;
	this.tapDisagree = tapDisagree;
	
	// Methods definition
	function tapAgree()
	{
		wait();
		agreeButton.tap();
	}
	
	function tapDisagree()
	{
		wait();
		disagreeButton.tap();
	}
}