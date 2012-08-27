#import "../testcaseBase.js"

function Agreement(target)
{
	// Target
	this.Target = target;
	this.App = target.frontMostApp();
	
	// Fields
	this.MainView = this.App.mainWindow();
	this.AgreeButton = this.MainView.buttons()["agree"];
	this.DisagreeButton = this.MainView.buttons()["disagree"];
	
	// Methods
	this.tapAgree = tapAgree;
	this.TabDisagree = TabDisagree;
	
	// Methods definition
	function tapAgree()
	{
		wait();
		this.AgreeButton.tap();
//		return new SignUp(target;
	}
	
	function TabDisagree()
	{
		wait();
		this.AgreeButton.tap();
	}
}