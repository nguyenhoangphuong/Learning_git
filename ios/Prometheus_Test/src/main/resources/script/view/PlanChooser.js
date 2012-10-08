	/*
PlanChooser functions:
=========================================================================================
- isVisible()		: 	check if current view is PlanChooser
- isManualVisible()	:	check if current view is PlanChooser after tap Other button
=========================================================================================
- selectEasy()		:	tap easy button
- selectNormal()	:	tap normal button
- selectActive()	:	tap active button
- selectOther()		:	tap other button
- backToActivity()	:	back to Activity view
=========================================================================================
- setValue(val)		:	select picker value base on <val>
- getPickerValue()	:	get the current value of picker (float)
- getPickerRange()	:	get the {min, max} of picker
- back()			:	tap the back button
- done()			:	tap the done button
=========================================================================================
- isLocationConfirmShown()		:	check if the location require alert is shown
- getPlanAmounts	:	get the static texts and value of the text of actual plan, return
						an object consist of:
	+ easytext		:
	+ normaltext	:	"5 miles / 7 days"
	+ activetext	:
	+ easy			:
	+ normal		:	10 (float)
	+ active		:
- getUnit()			: get the unit of this current activity
=========================================================================================
*/

#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

function PlanChooser()
{
	// Private fields
	var mainWindow = app.mainWindow();
	var pickWindow = app.windows()[1];
	
	this.easy = 5; 
	this.normal = 10;
	this.active = 21;
	
	var titleStr = "Set your 7-day fitness plan";
	var titleManualStr = "Please set your custom plan";
	
	var easyBtn = mainWindow.buttons()[0];	
	var normalBtn = mainWindow.buttons()[1];
	var activeBtn = mainWindow.buttons()[2];
	var otherBtn = mainWindow.buttons()[3];
	var backBtn = mainWindow.buttons()[4];
	
	var easyTxt = mainWindow.staticTexts()[1];
	var normalTxt = mainWindow.staticTexts()[2];
	var activeTxt = mainWindow.staticTexts()[3];
	
	// Methods
	this.isVisible = isVisible;
	this.isManualVisible = isManualVisible;
	
	this.selectEasy = selectEasy;
	this.selectNormal = selectNormal;
	this.selectActive = selectActive;
	this.selectOther = selectOther;
	this.backToActivity = backToActivity;
	
	this.setValue = setValue;
	this.getPickerValue = getPickerValue;
	this.getPickerRange = getPickerRange;
	this.back = back;
	this.done = done;
	
	this.getPlanAmounts = getPlanAmounts;
	this.isLocationConfirmShown = isLocationConfirmShown;
	this.getUnit = getUnit;
	
	// Method definition
	function isVisible()
	{
		wait(0.5);
		exist = staticTextExist(titleStr);
		
		log("PlanChooser visible: " + exist);
		return exist;
	}
	
	function isManualVisible()
	{
		wait(0.5);
		picker = pickWindow.pickers()[0];
		exist = picker.isValid();
		
		log("PlanChooser manual visible: " + exist);
		return exist;
	}
	
	
	function selectEasy() 
	{
		wait(0.5);
		easyBtn.tap();
		log("Tap [Easy]");
		wait(3);
	}
	
	function selectNormal() 
	{
		wait(0.5);
		normalBtn.tap();
		log("Tap [Normal]");
		wait(3);
	}
	
	function selectActive() 
	{
		wait(0.5);
		activeBtn.tap();
		log("Tap [Active]");
		wait(3);
	}
	
	function selectOther()
	{
		wait(0.5);
		otherBtn.tap();
		log("Tap [Other]");
		wait();
	}
	
	function backToActivity()
	{
		wait(0.5);
		backBtn.tap();
		log("Tap [Back]");
		wait();
	}
	
	
	function setValue(value)
	{
		picker = app.windows()[1].pickers()[0];
		
		if(picker.isValid())
		{
			wheelPick(picker, 0, value.toString());
			log("Pick custom value: " + value);
			return;
		}
		else
			log("No picker visible");
	}
	
	function getPickerValue()
	{
		wait(0.5);
		picker = app.windows()[1].pickers()[0];
		if(picker.isValid())
		{
			wheel = picker.wheels()[0];
			items = wheel.values();

			// get current value
			wait(0.5);
			current = wheel.value();
			current = current.substring(0, current.lastIndexOf("."));
			current = parseFloat(current);
			log("Current picker value: " + current);
			
			return current;
		}
		
		log("No picker visible");
		return null;
	}
	
	function getPickerRange()
	{
		wait(0.5);
		picker = app.windows()[1].pickers()[0];
		range = getWheelRange(picker, 0);
		
		if(range != null)
		{
			info = {};
			info.min = parseFloat(range.min);
			info.max = parseFloat(range.max);
			
			log("PickerRange.min: " + info.min);
			log("PickerRange.max: " + info.max);
			
			return info;
		}
		
		log("No picker visible");
		return null;
	}
	
	
	function back()
	{
		wait(0.5);
		backBtn = pickWindow.buttons()[0];
		if(backBtn.isValid())
		{
			backBtn.tap();
			log("Tap [Back]");
			wait(0.5);
			return;
		}
		
		log("No [Back] button");
	}
	
	function done()
	{
		wait(1);
		doneBtn = app.windows()[1].buttons()["Done"];
		
		if (doneBtn.isValid())
		{
			doneBtn.tap();
			log("Tap [Done]");
			wait(3);
			return;
		}
		
		log("No [Done] button");
	}
	
		
	function getPlanAmounts()
	{
		var info = {};
		info.easytext = easyTxt.name();
		info.normaltext = normalTxt.name();
		info.activetext = activeTxt.name();
		info.easy = parseFloat(easyTxt.name());
		info.normal = parseFloat(normalTxt.name());
		info.active = parseFloat(activeTxt.name());
		
		log("plan.easy: " + info.easytext + " - " + info.easy);
		log("plan.normal: " + info.normaltext + " - " + info.normal);
		log("plan.active: " + info.activetext + " - " + info.active);
		
		return info;
	}
	
	function isLocationConfirmShown()
	{
		shown = alert.alertTitle != null && alert.alertTitle == alert.LocationConfirm;
		log("Checking alert: [" + alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);
		
		return shown;
	}
	
	/**
	 * Gets the unit of the current activity
	 * @returns unit in string
	 */
	function getUnit() 
	{
		var text = easyTxt.name();
		var unit = text.substring(text.indexOf(" ") + 1);
		return unit;
	}
}