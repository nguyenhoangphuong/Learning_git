#import "../MVPLibs.js"

/*
List of functions:
=========================================================================================
- assignControls()
- isVisible()
=========================================================================================
- tapCancel()		: tap cancel button
- tapGPS()			: tap GPS tab
- tapManual()		: tap Manual tab
- tapActivity(name)	: tap activity icon (ex: "Running")

- getActivitiesInfo()	: return [{name: Running, percent: 0%}, ...]
- hasGPSActivity()	: TODO: check if this plan has at least 1 GPS activity or not
					GPS button is tapped after this function
=========================================================================================
*/

function Tracking()
{
	// Private fields
	var window;
	var mainView;
	
	var cancelBtn;
	var gpsBtn;
	var manualBtn;
	
	// Initalize
	assignControls();
	
	// Public methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.tapCancel = tapCancel;
	this.tapGPS = tapGPS;
	this.tapManual = tapManual;
	this.tapActivity = tapActivity;
	
	this.getActivitiesInfo = getActivitiesInfo;
	this.hasGPSActivity = hasGPSActivity;
	
	// Method definitions
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window;
		
		cancelBtn = app.navigationBar().leftButton();
		gpsBtn = mainView.segmentedControls()[0].buttons()[0];
		manualBtn = mainView.segmentedControls()[0].buttons()[1];
	}
	
	function isVisible()
	{
		visible = app.navigationBar().name() == "New Session";
		
		log("Tracking is visible: " + visible);
		return visible;
	}
	
	function tapCancel()
	{
		cancelBtn.tap();
		wait(0.5);
		
		log("Tap [Cancel]");
	}
	
	function tapGPS()
	{
		gpsBtn.tap();
		wait(0.5);
		
		log("Tap [GPS]");
	}
	
	function tapManual()
	{
		manualBtn.tap();
		wait(0.5);
		
		log("Tap [Manual]");
	}
	
	function tapActivity(name)
	{
		// look for the text and tap on the image with 100 offset
		log("Tap on activity: " + name);
		text = mainView.staticTexts()[name];
		var rect = text.rect();
		x = rect.origin.x + rect.size.width / 2;
		y = rect.origin.y + rect.size.height / 2;
		target.tap({x:x, y:y - 100});
	}
	
	function getActivitiesInfo()
	{
		var info = [];
		count = 0;
		texts = mainView.staticTexts();
		
		for(i = 2; i < texts.length; i = i + 2)
		{
			name = texts[i + 1].name();
			percent = texts[i].name();
			
			info[count] = {name: name, percent: percent};
			count++;
		}
		
		log("Activities info: " + JSON.stringify(info));
		
		return info;
	}
	
	function hasGPSActivity()
	{
//		gpsBtn.tap();
//		assignControls();
//		
//		return gpsBtn.value() == 1;
	}
}