var target = UIATarget.localTarget();
var AllowLocationMsg = "\"Shine\" Would Like to Use Your Current Location";

// ======================== handler for Prometheus app ========================
UIATarget.onAlert = PrometheusAlertHandler;

// ======================== the alert hander ==================================
UIALogger.logDebug("Alert Support loaded");

function PrometheusAlertHandler(_alert) {
	// get alert title and message
	var name = _alert.name();
	var message = _alert.staticTexts()[1].name();

	if (name === null)
		name = "";

	if (message === null)
		message = "";

	if (name == AllowLocationMsg || message == AllowLocationMsg
			|| name.indexOf("Current Location") >= 0
			|| message.indexOf("Current Location") >= 0
			|| name.indexOf("Facebook") >=0 || message.indexOf("Facebook") >= 0) {
		UIALogger.logDebug("Alert title: " + name);
		UIALogger.logDebug("Alert message: " + message);
		UIALogger.logDebug("Tap OK");
		_alert.buttons()["OK"].tap();
		return true;
	}

	UIALogger.logDebug("Do not do anything");
	target.delay(5);
	return true;

}

target.delay(1000);

UIALogger.logDebug("If you see this log, maybe something went wrong");