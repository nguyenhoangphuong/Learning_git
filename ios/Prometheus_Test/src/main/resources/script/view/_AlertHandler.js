#import "_Tips.js"

/*
This file handler all the alert which relate to the tests.

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
	this.alertTitle = null;
	this.alertMsg = null;
	this.alertChoice = null;
	
	// All the alert title name
	// ----- Home view
	this.Error = "Error";
	this.Warning = "Warning";
	
	this.InvalidPasswordMsg = "Sorry, the password should be at least 6 characters long, with at least 1 digit and 1 letter";
	this.InvalidEmailMsg = "Sorry, this email is invalid";
	this.ExistedUserMsg = "Sorry, someone else has used this before";
	this.WrongLoginMsg = "Incorrect email or password";
	
	this.LocationConfirm = "\“Shine\” Would Like to Use Your Current Location";
	this.NoEmailAccount = "Warning";
	
	this.Congratulation = "Congratulations!";
	this.TodayGoalFinishMsg = "You hit today’s goal!";
	this.WeekGoalFinishMsg = "You hit your weekly goal!"; 
	
	this.ResetConfirm = "Are you sure?";
	this.TooHard = "That might be tough";
	
	this.DeletePlanConfirm = "Are you sure?"
	
	// Methods
	this.reset = reset;
	this.isCustomAlertShown = isCustomAlertShown;
	this.getCustomAlertInfo = getCustomAlertInfo;
	this.confirmCustomAlert = confirmCustomAlert;
	
	// Method definitions
	function reset()
	{
		this.alertTitle = null;
		this.alertMsg = null;
	}
	
	function isCustomAlertShown()
	{
		win = app.mainWindow();
		ele = win.staticTexts()[0];
	
		if(ele.isValid())
		{
			title = ele.name();
			msg = win.staticTexts()[1].name();
			
			if(	(title	==	alert.ResetConfirm)	||
				(title	==	alert.TooHard)		||
				(title	==	alert.Congratulation && msg == alert.TodayGoalFinishMsg) || 
				(title	==	alert.Congratulation && msg == alert.WeekGoalFinishMsg) )
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
			info.title = win.staticTexts()[0].name();
			info.message = win.staticTexts()[1].name();
			
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
   
	// log the alert
	log("Alert [" + name + "] encountered");
	
	// check for test-related alert
	if( (name == alert.Error && message == alert.InvalidPasswordMsg)||
		(name == alert.Error && message == alert.InvalidEmailMsg)	||
		(name == alert.Error && message == alert.ExistedUserMsg)	||
		(name == alert.Error && message == alert.WrongLoginMsg)		||
		(name == alert.Warning && message == alert.DeletePlanConfirm) ||
		(name == alert.LocationConfirm)								||
		(name == alert.NoEmailAccount))
	{
		// log
		log("Interesting [" + name + "] encountered!");
		log("Message [" + message + "]");
		
		// track the alert
		alert.alertTitle = name;
		alert.alertMsg = message;
		
		// choose base on the param
		if(alert.alertChoice != null)
			_alert.buttons()[alert.alertChoice].tap();
		else
			_alert.defaultButton().tap();
			
		// reset the alertChoice and acknowledge the alert
		alert.alertChoice = null;
		
		return true;
	}
	
	// for other alert, choose by default
	_alert.defaultButton().tap();
	return false;
}
