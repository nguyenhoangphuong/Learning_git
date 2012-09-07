/*
PlanChooser functions:
- selectEasy()		:	tap easy button
- selectNormal()	:	tap normal button
- selectActive()	:	tap active button
- selectOther(mile, confirm)	:	tap other and pick "mile" value and if
									click back or done base on confirm
	+ selectOther(18) for select 18 then process
	+ selectOther(18, "no") for select 18 then back
- isLocationConfirmShown()		:	check if the location require alert is shown
*/

#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

function PlanChooser()
{
	// Private fields
	var mainWindow = app.mainWindow();
	var pickWindow = app.windows()[1];
	
	var easyBtn = mainWindow.buttons()[0];	
	var normalBtn = mainWindow.buttons()[1];
	var activeBtn = mainWindow.buttons()[2];
	var otherBtn = mainWindow.buttons()[3];
	
	// Methods
	this.selectEasy = selectEasy;
	this.selectNormal = selectNormal;
	this.selectActive = selectActive;
	this.selectOther = selectOther;
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
	
	function selectOther(value, confirm) 
	{
		if(typeof confirm == "undefined")
			confirm = "yes";
			
		otherBtn.tap();
		wait();
		backBtn = pickWindow.buttons()["Back"];
		doneBtn = pickWindow.buttons()["Done"];
		
		picker = pickWindow.pickers()[0];
		wheelPick(picker, 0, value.toString());
		if(confirm == "yes")
		{
			doneBtn.tap();
			wait(2);
		}
		else
			backBtn.tap();
		
		wait(0.2);
	}
	
	function isLocationConfirmShown()
	{
		return alert.alertTitle != null && alert.alertTitle == alert.LocationConfirm;
	}
}