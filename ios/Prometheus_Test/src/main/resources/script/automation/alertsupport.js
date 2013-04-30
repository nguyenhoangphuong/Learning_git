var target = UIATarget.localTarget();

// ======================== handler for Prometheus app ========================
UIATarget.onAlert = PrometheusAlertHandler;



// ======================== the alert hander ==================================
UIALogger.logDebug("Alert Support loaded");

function PrometheusAlertHandler(_alert)
{
	// get alert title and message
	var name = _alert.name();
	var message = _alert.staticTexts()[1].name();
	
   if (name.indexOf("Current Location") != -1 || message.indexOf("Current Location" != -1)) {
	   UIALogger.logDebug("Tap OK");
	   _alert.buttons()["OK"].tap();
	   return true;
   }
   	UIALogger.logDebug("Do not do anything");
	return true;
	
}

target.delay(1000);

UIALogger.logDebug("If you see this log, maybe something went wrong");