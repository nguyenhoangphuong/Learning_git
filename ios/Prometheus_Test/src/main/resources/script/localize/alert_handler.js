var alert = {};

// helpers
UIATarget.onAlert = function(_alert) {
	
	// capture
	var today = new Date();
	var ms = today.getTime();
	var name = _alert.name;
	if(name === undefined || name === null || name === "")
		name = "alert_" + ms.toString();
	capture(name);
	
	
	// handle
	if(alert.choice !== undefined && alert.choice !== null) {
		
		if(alert.choice < 0)
			_alert.cancelButton().tap();
		else
			_alert.buttons()[alert.choice].tap();
	}
	else
		_alert.defaultButton().tap();
	
	// reset
	alert.choice = null;
	alert.name = null;
	
	return true;
}