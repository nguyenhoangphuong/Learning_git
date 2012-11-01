#import "../MVPLibs.js"

/*
List of functions:
=========================================================================================
- assignControls()
- isVisible()
- isInInputMode()
=========================================================================================
- getFieldsInfo()				: return all the name and value of avaiable fields 
	ex: 
	{
		names: ["Start time", "Duration", "Distance (miles)"],
		values: ["09:53:AM", "01:00:00", "82"]
	}
- tapField(fieldname)			: tap the field with name == fieldname
- setField(params)				: set the value for current selected field
	ex: setField([1, 59, "AM"])
		setField([82])
- tapBack()						: tap [Back] in input mode
- tapDone()						: tap [Cancel] in input mode
=========================================================================================
- cancel()						: cancel input
- save()						: confirm input
=========================================================================================
*/

function ManualTracking()
{
	// Private fields
	var window;
	var mainView;

	var cancelBtn;
	var saveBtn;
	
	// Initalize
	assignControls();
	
	// Public methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	this.isInInputMode = isInInputMode;
	
	this.getFieldsInfo = getFieldsInfo;
	this.tapField = tapField;
	this.setField = setField;
	this.tapBack = tapBack;
	this.tapDone = tapDone;
	
	this.cancel = cancel;
	this.save = save;
	
	//MVP3: done()
	function done()
	{
		wait(0.5);
		doneBtn.tap();
		
		log("Tap [Save activity]");	
		
		// wait for alert
		wait();
		if(isWeekGoalFinishedAlertShown() || isTodayGoalFinishedAlertShown())
			alert.confirmCustomAlert();
	}
	
	// Method definitions
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window;
		
		cancelBtn = app.navigationBar().leftButton();
		saveBtn = mainView.buttons()["Save"];
	}
	
	function isVisible()
	{
		visible = staticTextExist("Start time", mainView) && 
			      buttonExist("Save", mainView);
		
		log("ManualTracking visible: " + visible);
		return visible;
	}
	
	function isInInputMode()
	{
		picker = app.windows()[1].pickers()[0];
		exist = picker.isValid() && picker.isVisible();
		
		log("ManualTracking is in input mode: " + exist);
		return exist;
	}
	
	
	function getFieldsInfo()
	{
		var texts = mainView.staticTexts();
		var btns = mainView.buttons();
		
		var info = {};
		info.names = [];
		info.values = [];
		
		for(i = 0; i < texts.length; i++)
		{
			info.names[i] = texts[i].name();
			info.values[i] = btns[i].name();
		}
		
		log("Field information: " + JSON.stringify(info));
		return info;
	}
	
	function tapField(id, useIndex)
	{
		field = mainView.staticTexts()[id];
		if(field.isValid())
		{
			// use index
			if(useIndex)
			{
				fieldBtn = mainView.buttons()[id];
				
				wait(0.5);
				fieldBtn.tap();
				log("Tap field [" + id + "]");
				
				return;
			}
			
			// use field name
			for(i = 0; ; i++)
			{
				if(mainView.staticTexts()[i].name().indexOf(id) >= 0)
				{
					fieldBtn = mainView.buttons()[i];
					
					wait(0.5);
					fieldBtn.tap();
					log("Tap field [" + id + "]");
					
					return;
				}
			}
		}
		
		log("No field with ID = " + id);
	}
	
	function setField(params)
	{
		if(isInInputMode())
		{
			// input using keyboard
			if(isKeyboardVisible())
			{
				app.keyboard().typeString(params[0].toString());
				return;
			}
			// input using picker
			else
			{
				// wait for render
				wait(0.5);
				picker = app.windows()[1].pickers()[0];
				
				// get length
				n = picker.wheels().length;
				
				for(i = 0; i < n; i++)
					wheelPick(picker, i, params[i].toString());
	
				return;
			}
		}
		
		log("No selected field");
	}
	
	
	function tapBack()
	{
		wait(0.5);
		var iback = app.windows()[1].toolbar().buttons()[0];
		iback.tap();
		log("Tap [Back] in input mode");
	}
	
	function tapDone()
	{
		wait(0.5);
		var idone = app.windows()[1].toolbar().buttons()[1];
		idone.tap();
		log("Tap [Done] in input mode");
	}
	
	function cancel()
	{
		wait(0.5);
		cancelBtn.tap();
		
		log("Tap [Cancel]");
	}
	
	function save()
	{
		wait(0.5);
		saveBtn.tap();
		
		log("Tap [Save]");
	}
}