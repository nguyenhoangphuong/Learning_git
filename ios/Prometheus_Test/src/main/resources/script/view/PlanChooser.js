/*
PlanChooser functions:
- isVisible()		: 	check if current view is PlanChooser
- isManualVisible()	:	check if current view is PlanChooser after tap Other button

- selectEasy()		:	tap easy button
- selectNormal()	:	tap normal button
- selectActive()	:	tap active button
- selectOther()		:	tap other button

- setValue(mile)	:	select picker value base on "mile"
- getPickerValue()	:	get the current value of picker (float)
- back()			:	tap the back button
- done()			:	tap the done button

- isLocationConfirmShown()		:	check if the location require alert is shown
- getPlanAmounts	:	get the static texts and value of the text of actual plan, return
						an object consist of:
	+ easytext		:
	+ normaltext	:	"5 miles / 7 days"
	+ activetext	:
	+ easy			:
	+ normal		:	10 (float)
	+ active		:
*/

#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

function PlanChooser()
{
	// Private fields
	var mainWindow = app.mainWindow();
	var pickWindow = app.windows()[1];
	
	this.easy=5; 
	this.normal=10;
	this.active=21;
	
	var titleStr = "Please set your WEEKLY plan";

	var easyBtn = mainWindow.buttons()[0];	
	var normalBtn = mainWindow.buttons()[1];
	var activeBtn = mainWindow.buttons()[2];
	var otherBtn = mainWindow.buttons()[3];
	
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
	
	this.setValue = setValue;
	this.getPickerValue = getPickerValue;
	this.back = back;
	this.done = done;
	
	this.getPlanAmounts = getPlanAmounts;
	this.isLocationConfirmShown = isLocationConfirmShown;
	
	// Method definition
	function isVisible()
	{
		wait(0.5);
		return staticTextExist(titleStr);
	}
	
	function isManualVisible()
	{
		wait(0.5);
		picker = pickWindow.pickers()[0];
		return picker.isValid();
	}
	
	
	function selectEasy() 
	{
		wait(0.5);
		easyBtn.tap();
		wait(2);
	}
	
	function selectNormal() 
	{
		wait(0.5);
		normalBtn.tap();
		wait(2);
	}
	
	function selectActive() 
	{
		wait(0.5);
		activeBtn.tap();
		wait(2);
	}
	
	function selectOther()
	{
		wait(0.5);
		otherBtn.tap();
		wait();
	}
	
	
	function setValue(value)
	{
		wait(0.5);
		picker = pickWindow.pickers()[0];
		if(picker.isValid())
			wheelPick(picker, 0, value.toString());
	}
	
	function getPickerValue()
	{
		wait(0.5);
		picker = pickWindow.pickers()[0];
		if(picker.isValid())
		{
			wheel = picker.wheels()[0];
			items = wheel.values();

			// get current value
			wait(0.5);
			current = wheel.value();
			current = current.substring(0, current.lastIndexOf("."));
			current = parseFloat(current);
			log("current picker: " + current);
			
			return current;
		}
		
		return null;
	}
	
	function back()
	{
		wait(0.5);
		backBtn = pickWindow.buttons()[0];
		if(backBtn.isValid())
		{
			backBtn.tap();
			wait(0.2);
		}
	}
	
	function done()
	{
		wait(0.5);
		doneBtn = pickWindow.buttons()[1];
		if(doneBtn.isValid())
		{
			doneBtn.tap();
			wait(2);
		}
	}
	
		
	function getPlanAmounts()
	{
		var info = {};
		info.easytext = easyTxt.name();
		info.normaltext = normalTxt.name();
		info.activetext = activeTxt.name();
		info.easy = parseFloat(easyTxt.name().substring(0, easyTxt.name().indexOf(" miles")));
		info.normal = parseFloat(normalTxt.name().substring(0, normalTxt.name().indexOf(" miles")));
		info.active = parseFloat(activeTxt.name().substring(0, activeTxt.name().indexOf(" miles")));
		
		log("plan.easy: " + info.easytext + " - " + info.easy);
		log("plan.normal: " + info.normaltext + " - " + info.normal);
		log("plan.active: " + info.activetext + " - " + info.active);
		
		return info;
	}
	
	function isLocationConfirmShown()
	{
		return alert.alertTitle != null && alert.alertTitle == alert.LocationConfirm;
	}
}