var target = UIATarget.localTarget();
UIALogger.logStart("Test Demo 1");
target.delay(2);
function _BaseView
{
	// Target
	this.Target = target;
	this.App = target.frontMostApp();
	
	// Common actions
	this.Wait = Wait;
	this.TypeText = TypeText;
	this.Flick = Flick;
	this.FlickLeft = FlickLeft;
	this.FlickRight = FlickRight;
	this.SwipeLeft = FlickLeft;
	this.SwipeRight = FlickRight;

	// Common action definition
	function Wait(sec)
	{
		if (sec === undefined)
			Target.delay(1);
		else
			Target.delay(sec);
	}
	
	function TypeText(var textfield, String text)
	{
		textfield.tap();
		Wait(0.5);
		App.keyboard().typeString(text);
	}
	
	function Flick(xf, yf, xt, yt) 
	{
		Wait();
	    Target.dragFromToForDuration({x:xf, y:yf}, {x:xt, y:yt}, 0.6);
		Wait();
	}

	function FlickLeft()
	{
		Wait();
		if (deviceType == "IPOD")
		{
	    	Target.dragFromToForDuration({x:240, y:416}, {x:16, y:402}, 0.6);
    	} 
    	else if (deviceType == "IPAD") 
    	{
        	Target.dragFromToForDuration({x:440, y:416}, {x:16, y:402}, 0.6);
    	}
		Wait();
	}
	
	function FlickRight() 
	{
		Wait();
		if (deviceType == "IPOD") 
		{
        	Target.dragFromToForDuration({x:16, y:402}, {x:240, y:416}, 0.6);
    	} 
    	else if (deviceType == "IPAD") 
    	{
        	// IPAD
        	Target.dragFromToForDuration({x:16, y:402}, {x:440, y:416}, 0.6);
    	}
		Wait();
	}
}










SignUp.prototype = new _BaseView();
SignUp.prototype.constructor = SignUp;
SignUp.prototype.baseClass = _BaseView.prototype.constructor;

function SignUp(var target)
{
	// Target
	this.Target = target;
	this.App = target.frontMostApp();

	// Fields
	this.MainView = App.mainWindow();
	this.Email = MainView.textFields()["email"];
	
	this.ErrNull = "Email must not be empty";
	this.ErrInvalid = "The email is invalid";
	
	// Methods
	this.FillEmailAndSubmit = FillEmailAndSubmit;
	this.GetErrorMessage = GetErrorMessage;
	
	// Method definition
	function FillEmailAndSubmit(String email)
	{
		Wait();
		TypeText(Email, email + "\n");
		alert("fill");
	}
	
	function GetErrorMessage()
	{
		Wait(0.2);
		return App.staticTexts()[0];
	}
}







Agreement.prototype = new _BaseView();
Agreement.prototype.constructor = Agreement;
Agreement.prototype.baseClass = _BaseView.prototype.constructor;

function Agreement(var target)
{
UIALogger.logDebug("agreement");
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






#UIALogger.logStart("Test Demo");
#var agreement = new Agreement(target);
#var signup = agreement.TabAgree();
#signup.FillEmailAndSubmit("");
#var error = signup.GetErrorMessage();

#if(error == signup.ErrNull)
#	UIALogger.logPass("Passed");
#else
	UIALogger.logFail("Failed");