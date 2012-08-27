#import "_BaseView.js"

Agreement.prototype = new _BaseView();
Agreement.prototype.constructor = Agreement;
Agreement.prototype.baseClass = _BaseView.prototype.constructor;

function Agreement(var target)
{
	// Target
	this.Target = target;
	this.App = target.frontMostApp();
	
	// Fields
	this.MainView = App.mainWindow();
	this.AgreeButton = MainView.buttons()["agree"];
	this.DisagreeButton = MainView.buttons()["disagree"];
	
	// Methods
	this.TabAgree = TabAgree;
	this.TabDisagree = TabDisagree;
	
	// Methods definition
	function TabAgree()
	{
		Wait();
		AgreeButton.tab();
		return new SignUp(target);
	}
	
	function TabDisagree()
	{
		Wait();
		AgreeButton.tab();
	}
}