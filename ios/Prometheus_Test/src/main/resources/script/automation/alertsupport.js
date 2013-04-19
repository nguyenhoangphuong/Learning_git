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
	this.AllowBluetoothMsg = "Turn On Bluetooth to Allow \"Prometheus\" to Connect to Accessories";
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
	if( name == alert.AllowLocationMsg ||
		name == alert.AllowLocationMsg || 
		message == alert.AllowBluetoothMsg ||
		message == alert.AllowBluetoothMsg)
	{
		// log the alert
		UIALogger.logDebug("Expected Alert [" + name + "] [" + message + "] encountered!");
		_alert.defaultButton().tap();
		target.delay(1);
		return true;
	}
	
	return false;
}

target.delay(1000);