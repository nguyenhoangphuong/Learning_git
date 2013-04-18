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

	// All the alert's titles
	this.Error = "Error";
	this.Warning = "Warning";
	this.IncorrectEmail = "Incorrect email";
	this.EmailSent = "Email Sent";
	
	// All alert's messages
	this.InvalidPasswordMsg = "Sorry, the password should have at least 6 characters, at least 1 digit and 1 letter";
	this.InvalidEmailMsg = "Invalid Email";
	this.DuplicatedEmailMsg = "Sorry, someone else has used this before";
	this.WrongAccountMsg = "Sorry, your email or password is not correct";
	this.IncorrectEmailMsg = "The email you entered is not associated with a Shine account";
	this.EmailSentMsg = "Check your email for the password reset link";
	this.AllowLocationMsg = "\"Prometheus\" Would Like to Use Your Current Location";

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
	if( message == alert.InvalidEmailMsg ||
		message == alert.InvalidPasswordMsg ||
		message == alert.DuplicatedEmailMsg ||
		message == alert.AllowLocationMsg ||
		message == alert.WrongAccountMsg ||
		message == alert.IncorrectEmailMsg ||
		message == alert.EmailSentMsg)
	{
		// log
		log("Expected Alert [" + name + "] [" + message + "] encountered!");
		target.delay(1);
		return false;
	}
	// location alert
	else if(name == alert.AllowLocationMsg)
	{
		// log the alert
		log("Expected Alert [" + name + "] [" + message + "] encountered!");
		_alert.defaultButton().tap();
		target.delay(1);
		return true;
	}
	
	// for other alert, choose by default
	_alert.defaultButton().tap();
	target.delay(1);
	return true;
}