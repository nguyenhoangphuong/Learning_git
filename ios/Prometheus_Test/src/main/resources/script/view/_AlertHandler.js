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
	this.NoInternet = "No connection";
	this.BMINotRealistic = "Error";
	this.NoEmail = "Warning";
	this.ResetConfirm = "Confirm";
	this.LocationConfirm = "\“Prometheus\” Would Like to Use Your Current Location";
	this.TooEasy = "Easy goal";
	this.TooHard = "Too hard";
	
	// Methods
	this.reset = reset;
	
	// Method definitions
	function reset()
	{
		alertTitle = null;
		alertMsg = null;
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
	if(	name == alert.NoInternet		||
		name == alert.BMINotRealistic	||
		name == alert.NoEmail			||
		name == alert.ResetConfirm		||
		name == alert.LocationConfirm	||
		name == alert.TooEasy			||
		name == alert.TooHard	)
	{
		// log
		log("Interesting [" + name + "] encountered!");
		log("Message [" + message + "].");
		
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
		
		if (name == alert.LocationConfirm) {
			log("Wait a bit to close tips in weekgoal");
			wait(4);
			tips.closeTips(1);
			wait();
		
		} else {
			log("Not interesting");
		}
		return true;
	}
	
	// for other alert, choose by default
	app.alert().defaultButton().tap();
	return false;
}