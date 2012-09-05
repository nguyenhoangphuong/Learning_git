/*
This file handler all the alert which relate to the tests.

When an action cause an alert to popup, the alert title and message
will be store in 3 global vars named:
	+ alertObj
	+ alertTitle
	+ alertMsg

These 2 vars should set to null or empty after using for safe.

You can also make choice dynamically when handler alert by bypassing
the setting value for the var alertChoice, and this should do before
trigger an alert.
--- For example:
	alertChoice = "Add";
	buttonThatTriggerAlert.tap();
	wait(2);
*/


// ======================== global vars for alert =============================
function Alert()
{
	// Private fields
	this.alertObj = null;
	this.alertTitle = null;
	this.alertMsg = null;
	this.alertChoice = null;
	
	// All the alert title name
	this.NoInternetAlert = "No connection";
	
	// Methods
	this.resetAlert = resetAlert;
	
	// Method definitions
	function resetAlert()
	{
		alertObj = null;
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
	var message = _alert.staticTexts()[0];
   
	// log the alert
	UIALogger.logMessage("Alert " + name + " encountered");
	
	// check for test-related alert
	
	//	----- no internet connection alert
	if(name == alert.NoInternetAlert)
	{
		// track the alert
		alert.alertObj = _alert;
		alert.alertTitle = name;
		alert.alertMsg = message;
		
		// choose base on the param
		if(alert.alertChoice != null)
			_alert.buttons[alert.alertChoice].tap();
		else
			_alert.defaultButton().tap();
			
		// reset the alertChoice and acknowledge the alert
		alert.alertChoice = null;
		return true;
	}
	
	// for other alert, choose by default
	app.alert().defaultButton().tap();
	return false;
}