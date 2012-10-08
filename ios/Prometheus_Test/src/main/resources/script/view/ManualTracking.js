/*
List of functions:
=========================================================================================
- isVisible()					: check if current view is ManualTracking
- isInInputMode()				: check if this view is in input mode
=========================================================================================
- getFieldsInfo()				: return all the name and value of avaiable fields 
	ex: 
	{
		total: 3,
		names: ["Start time", "Duration", "Distance"],
		values: ["09:53:AM", "01:00:00", "82"]
	}
- tapField(fieldname)			: tap the field with name = fieldname
- getFieldRanges()				: return all the column range in current selected field
								  (min and max range)
	ex:	
	{
		mins: [1, 82]
		maxs: [100, 328]
	}
- setField(params)				: set the value for current selected field
	ex: setField([1, 59, "AM"])
		setField([82])
=========================================================================================
- tapBack()						: tap [Back] in input mode
- tapDone()						: tap [Cancel] in input mode
- cancel()						: cancel input
- done()						: confirm input
================================================================================
- isWeekGoalFinishedAlertShown()	: check if the week goal finish alert is shown
- isTodayGoalFinishedAlertShown()	: check if the todays goal finish alert is shown
=========================================================================================
*/

#import "../core/testcaseBase.js"

function ManualTracking()
{
	// Private fields
	var mainWindow = app.mainWindow();
	var mainView = mainWindow;
	var cancelBtn = mainView.buttons()["Cancel"];
	var doneBtn = mainView.buttons()["Done"];
	
	// Public methods
	this.isVisible = isVisible;
	this.isInInputMode = isInInputMode;
	
	this.getFieldsInfo = getFieldsInfo;
	this.tapField = tapField;
	this.getFieldRanges = getFieldRanges;
	this.setField = setField
	
	this.tapBack = tapBack;
	this.tapDone = tapDone;
	this.cancel = cancel;
	this.done = done;
	
	this.isWeekGoalFinishedAlertShown = isWeekGoalFinishedAlertShown;
	this.isTodayGoalFinishedAlertShown = isTodayGoalFinishedAlertShown;
	
	// Method definitions
	function isVisible()
	{
		exist = staticTextExist("Start time", mainView) && buttonExist("Done", mainView) && buttonExist("Cancel", mainView);
		
		log("ManualTracking visible: " + exist);
		return exist;
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
		texts = mainView.staticTexts();
		btns = mainView.buttons();
		
		var info = {};
		info.total = texts.length;
		info.names = [];
		info.values = [];
		
		for(i = 0; i < info.total; i++)
		{
			info.names[i] = texts[i].name();
			info.values[i] = btns[i].name();
		}
		
		log("Fields.total: " + info.total);
		log("Field.names: " + info.names);
		log("Field.values: " + info.values);
		
		return info;
	}
	
	function tapField(id)
	{
		field = mainView.staticTexts()[id];
		if(field.isValid())
		{
			for(i = 0; ; i++)
			{
				if(mainView.staticTexts()[i].name() == id)
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
	
	function getFieldRanges()
	{
		if(isInInputMode())
		{		
			// wait for render
			picker = app.windows()[1].pickers()[0];
			
			// get range of each column
			var info = {};
			info.mins = [];
			info.maxs = [];
			n = picker.wheels().length;
			
			for(i = 0; i < n; i++)
			{
				range = getWheelRange(picker, i);
				if(range != null)
				{
					info.mins[i] = range.min;
					info.maxs[i] = range.max;
				}
			}
			
			// log and back
			log("FieldRange.mins: " + info.mins);
			log("FieldRange.maxs: " + info.maxs);
			
			return info;
		}
		
		log("No selected field");
		return null;
	}
	
	function setField(params)
	{
		if(isInInputMode())
		{		
			// wait for render
			picker = app.windows()[1].pickers()[0];
			
			// get length
			n = picker.wheels().length;
			
			for(i = 0; i < n; i++)
				wheelPick(picker, i, params[i].toString());

			return;
		}
		
		log("No selected field");
	}
	
	
	function tapBack()
	{
		wait(0.5);
		iback = app.windows()[1].buttons()[0];
		iback.tap();
		log("Tap [Back] in input mode");
	}
	
	function tapDone()
	{
		wait(0.5);
		idone = app.windows()[1].buttons()[1];
		idone.tap();
		log("Tap [Done] in input mode");
	}
	
	function cancel()
	{
		wait(0.5);
		cancelBtn.tap();
		
		log("Tap [Cancel]");
	}
	
	function done()
	{
		wait(0.5);
		doneBtn.tap();
		
		log("Tap [Done]");		
	}
	
	
	function isWeekGoalFinishedAlertShown()
	{
		wait();
		info = alert.getCustomAlertInfo();
		
		if(info == null)
			return false;
			
		shown = info.title == alert.Congratulation && info.message == alert.WeekGoalFinishMsg;
		
		log("WeekGoalFinish alert shown: " + shown);
		return shown;
	}
	
	function isTodayGoalFinishedAlertShown()
	{
		wait();
		info = alert.getCustomAlertInfo();
		
		if(info == null)
			return false;
			
		shown = info.title == alert.Congratulation && info.message == alert.TodayGoalFinishMsg;	
		
		log("TodayGoalFinish alert shown: " + shown);
		return shown;
	}
	
}