/*
PlanChooser functions:
- selectEasy()		:	tap easy button
- selectNormal()	:	tap normal button
- selectActive()	:	tap active button
- selectOther()		:	tap other button
- setValue(mile)	:	select picker value base on "mile"
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
	
	var easyBtn = mainWindow.buttons()[0];	
	var normalBtn = mainWindow.buttons()[1];
	var activeBtn = mainWindow.buttons()[2];
	var otherBtn = mainWindow.buttons()[3];
	
	var easyTxt = mainWindow.staticTexts()[1];
	var normalTxt = mainWindow.staticTexts()[2];
	var activeTxt = mainWindow.staticTexts()[3];
	
	// Methods
	this.selectEasy = selectEasy;
	this.selectNormal = selectNormal;
	this.selectActive = selectActive;
	this.selectOther = selectOther;
	this.setValue = setValue;
	this.back = back;
	this.done = done;
	this.getPlanAmounts = getPlanAmounts;
	this.isLocationConfirmShown = isLocationConfirmShown;
	
	// Method definition	
	function selectEasy() 
	{
		easyBtn.tap();
		wait(2);
	}
	
	function selectNormal() 
	{
		normalBtn.tap();
		wait(2);
	}
	
	function selectActive() 
	{
		activeBtn.tap();
		wait(2);
	}
	
	function selectOther()
	{
		otherBtn.tap();
		wait();
	}
	
	function setValue(value)
	{
		picker = pickWindow.pickers()[0];
		if(picker.isValid())
			wheelPick(picker, 0, value.toString());
	}
	
	function back()
	{
		backBtn = pickWindow.buttons()["Back"];
		if(backBtn.isValid())
		{
			backBtn.tap();
			wait(0.2);
		}
	}
	
	function done()
	{
		doneBtn = pickWindow.buttons()["Done"];
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