/*
This file handler all the alert which relate to the tests

When an action cause an alert to popup, the alert title and message
will be store in 2 global vars named:
	+ alert.alertTitle
	+ alert.alertMsg

You can also make choice dynamically when handler alert by bypassing
the setting value for the var alertChoice, and this should do before
trigger an alert.

--- For example:
	alert.alertChoice = "Add";
	buttonThatTriggerAlert.tap();
	wait(2);
	
When working with Alert, please make sure to reset alert's properties
after use by calling
	alert.reset();
*/


// ======================== global vars for alert =============================
function Alert()
{
	// Private fields
	this.title = null;
	this.message = null;
	this.choice = null;
	
	// All the alert title name
	this.Error = "Error";
	this.Warning = "Warning";
	this.IncorrectEmail = "Incorrect email";
	this.EmailSent = "Email Sent";
	
	this.InvalidPasswordMsg = "Sorry, the password should have at least 6 characters, at least 1 digit and 1 letter";
	this.InvalidEmailMsg = "Invalid Email";
	this.DuplicatedEmailMsg = "Sorry, someone else has used this before";
	this.WrongAccountMsg = "Sorry, your email or password is not correct";
	this.IncorrectEmailMsg = "The email you entered is not associated with a Shine account";
	this.EmailSentMsg = "Check your email for the password reset link";
	
	// Methods
	this.reset = reset;
	this.isCustomAlertShown = isCustomAlertShown;
	this.getCustomAlertInfo = getCustomAlertInfo;
	this.confirmCustomAlert = confirmCustomAlert;
	
	// Method definitions
	function reset()
	{
		this.title = null;
		this.message = null;
	}
	
	function isCustomAlertShown()
	{
		win = app.mainWindow();
		ele = win.staticTexts()[1];
	
		if(ele.isValid())
		{
			title = ele.name();
			msg = win.staticTexts()[2].name();
			
			if(	title	==	alert.Error )
			{
				log("Message is on screen: [" + title + "]" + " - [" + msg + "]");
				return true;
			}
		}
		
		return false;
	}
	
	function getCustomAlertInfo()
	{
		if(isCustomAlertShown())
		{
			win = app.mainWindow();
			
			var info = {};
			info.title = win.staticTexts()[1].name();
			info.message = win.staticTexts()[2].name();
			
			return info;
		}
		
		return null;
	}
	
	function confirmCustomAlert(confirm)
	{
		if(isCustomAlertShown())
		{
			if(typeof confirm == "undefined")
				confirm = 0;
			
			win = app.mainWindow();
			btn = win.buttons()[confirm];
			
			if(!btn.isValid())
				btn = win.buttons()[0];
				
			btn.tap();
			
			reset();
		}
	}
}

// global var
alert = new Alert();


// ======================== handler for Prometheus app ========================
UIATarget.onAlert = PrometheusAlertHandler;



// ======================== the alert hander ==================================
function PrometheusAlertHandler(_alert)
{
	// get alert title and message
	var name = _alert.name();
	var message = _alert.staticTexts()[1].name();
   
	// check for test-related alert
	if(false)
	{
		// log
		log("Expected Alert [" + name + "] [" + message + "] encountered!");
		
		// track the alert
		alert.title = name;
		alert.message = message;
		
		// choose base on the param
		if(alert.alertChoice != null)
			_alert.buttons()[alert.alertChoice].tap();
		else
			_alert.defaultButton().tap();
			
		// reset the alertChoice and acknowledge the alert
		alert.alertChoice = null;
		
		return true;
	}
	else
	{
		// log the alert
		log("Unexpected Alert [" + name + "] [" + message + "] encountered!");
	}
	
	// for other alert, choose by default
	_alert.defaultButton().tap();
	return true;
}
