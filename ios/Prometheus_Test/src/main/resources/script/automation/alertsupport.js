var target = UIATarget.localTarget();

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
	this.AgreeNDA = "Do you agree to the NDA?"

}

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
		UIALogger.logDebug("Expected Alert [" + name + "] [" + message + "] encountered!");
		target.delay(1);
		return false;
	}
	// location alert
	else if(name == alert.AllowLocationMsg || message == alert.AllowLocationMsg)
	{
		// log the alert
		UIALogger.logDebug("Expected Alert [" + name + "] [" + message + "] encountered!");
		_alert.defaultButton().tap();
		target.delay(1);
		return true;
	}
	
	// for other alert, choose by default
	UIALogger.logDebug("Unexpected Alert [" + name + "] [" + message + "] encountered!");
	_alert.defaultButton().tap();
	target.delay(1);
	return true;
}

target.delay(1000);