#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
GoalPlan functions:
- isVisible()			:	check if the current view is GoalPlan
- edit()				:	tap the Edit button
- reset()				:	tap the Auto Suggest button
- save()				:	tap the Done button (while in edit mode)
- cancel()				:	tap the Cancel button (while in edit mode)

- getTotalDays()		:	get the number of days (or records) in this goal
- getPassedDays()		:	get the number of days (or records) which can't be planned any more
- getTotalPlanMiles()	:	get the number of miles that is planned (before and after)
- getRemainPlanMiles()	:	get the number of miles that is planned for the following days
- getRunMiles()			:	get the number of miles that you run

- getWeekInfo()			:	get info of a goal {duration, goal}
	+ duration		: 	"Sep 14 - Sep 20"
	+ goal			: 	5 (float)
- getDayInfoByIndex(i)	:	get info of a day by index {text, date, temperature, run, total}
	+ text			:	"Sat Sep 15th / 76 - 81ºF"
	+ date			: 	"Sat Sep 15th"
	+ temperature	: 	"76 - 81ºF"
	+ run			: 	0.74 (float)
	+ total			:	1.10 (float)		
- getDayInfoByName(n)	:	get info of a day by its name {text, date, temperature, run, total}
- getTodayInfo()		:	get info of today {text, date, temperature, run, total}

- planDayByIndex(i, miles, confirm)		:	set the goal of day i to miles (and dismiss the alert)
	+ planDayByIndex(i, miles)			- 	just drag the slider, don't touch the alert
	+ planDayByIndex(i, miles, false) 	- 	drag the slider and choose NO when the alert shown up
	+ planDayByIndex(i, miles, true) 	- 	drag the slider and choose YES when the alert shown up
	
- planDayByName(n, miles, confirm)		:	similar to planDayByIndex but use NAME instead
	+ planDayByName("Sat Sep 15th", 0, 4.3)

- isInEditMode()						:	check if the current view is edit mode
- isHardAlertShown()					:	check if the Too Hard alert is CURRENTLY showing up
- confirmHardAlert(confirm)				:	close the alert by pressing specify button
	+ confirm == true || not defined	-	YES button
	+ confirm == false					-	NO button
*/

maxMPD = 2.95;
function GoalPlan()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0];
	var recordsView = mainView.scrollViews()[0];
	
	var autoBtn = mainView.buttons()[8];
	var editBtn = mainView.buttons()[10];
	var cancelBtn = mainView.buttons()[7];
	var doneBtn = mainView.buttons()[9];

	var weekDays = mainView.staticTexts()[12];
	var weekGoal = mainView.staticTexts()[11];

	
	// Methods
	this.isVisible = isVisible;
	this.edit = edit;
	this.reset = reset;
	this.save = save;
	this.cancel = cancel;
	
	this.getTotalDays = getTotalDays;
	this.getPassedDays = getPassedDays;
	this.getTotalPlanMiles = getTotalPlanMiles;
	this.getRunMiles = getRunMiles;
	this.getTotalPlanMiles = getTotalPlanMiles;
	this.getRemainPlanMiles = getRemainPlanMiles;
	
	this.getWeekInfo = getWeekInfo;
	this.getDayInfoByIndex = getDayInfoByIndex;
	this.getDayInfoByName = getDayInfoByName;	
	this.getTodayInfo = getTodayInfo;
	
	this.planDayByIndex = planDayByIndex;
	this.planDayByName = planDayByName;
	
	this.isInEditMode = isInEditMode;
	this.isHardAlertShown = isHardAlertShown;
	this.confirmHardAlert = confirmHardAlert;
	

	// Methods definition
	function isVisible()
	{
		page = window.pageIndicators()[0].value();
		return page == "page 3 of 3" && recordsView.isValid() && recordsView.isVisible();
	}
	
	function edit()
	{
		wait();
		editBtn.tap();
	}
	
	function reset()
	{
		wait();
		autoBtn.tap();
	}
	
	function save()
	{
		wait();
		doneBtn.tap();
	}
	
	function cancel()
	{
		wait();
		cancelBtn.tap();
	}
	
	
	function getTotalDays()
	{
		count = 0;
		element = recordsView.staticTexts()[count];
		
		while(element.isValid())
		{
			count++;
			element = recordsView.staticTexts()[count * 2];
		}
		
		log("Total days: " + count.toString());
		return count;
	}
	
	function getPassedDays()
	{
		count = 0;
		element = recordsView.staticTexts()[count * 2 + 1];
		
		while(element.isValid() && element.name().indexOf("/") >= 0)
		{
			count++;
			element = recordsView.staticTexts()[count * 2 + 1];
		}
		
		log("Passed days: " + count.toString());
		return count;
	}
	
	function getTotalPlanMiles()
	{
		count = 0;
		miles = 0;
		element = recordsView.staticTexts()[count];
		
		while(element.isValid())
		{
			// parse the string to get the miles value
			value = recordsView.staticTexts()[count * 2 + 1].name();
			if(value.indexOf("/") >= 0) 
				value = value.substring(value.indexOf("/") + 1);
			else 
				value = value.substring(0);
			
			// parse the value
			miles += parseFloat(value);
			
			count++;
			element = recordsView.staticTexts()[count * 2];
		}

		var result = miles.toFixed(2);
		log("Total plan: " + result.toString());
		return Math.round(result);
	}
	
	function getRunMiles()
	{
		count = 0;
		miles = 0;
		element = recordsView.staticTexts()[count];
		
		while(element.isValid())
		{
			// parse the string to get the miles value
			// only take the string which have "/" character
			value = recordsView.staticTexts()[count * 2 + 1].name();
			if(value.indexOf("/") >= 0)
			{
				value = value.substring(0, value.indexOf("/"));
				miles += parseFloat(value);
			}
			
			count++;
			element = recordsView.staticTexts()[count * 2];
		}
		
		miles = miles.toFixed(2);
		log("Distance run: " + miles.toString());
		return miles;
	}
	
	function getRemainPlanMiles()
	{
		count = 0;
		miles = 0;
		element = recordsView.staticTexts()[count];
		
		while(element.isValid())
		{
			// parse the string to get the miles value
			// only take the string that DON'T have the "/" char
			value = recordsView.staticTexts()[count * 2 + 1].name();
			if(value.indexOf("/") < 0)
			{
				value = value.substring(0);
		
				// parse the value
				miles += parseFloat(value);
			}
			
			count++;
			element = recordsView.staticTexts()[count * 2];
		}
		
		miles = miles.toFixed(2);
		log("Plan remain: " + miles.toString());
		return miles;
	}
	
	
	function getWeekInfo()
	{
		var info = {};
		info.duration = weekDays.name();
		info.goal = parseFloat(weekGoal.name());
		
		log("Weekinfo.duration: " + info.duration);
		log("Weekinfo.goal: " + info.goal);
		
		return info;
	}
	
	function getDayInfoByIndex(index)
	{
		total = getTotalDays();
		if(index < 0 || index >= total)
		{
			log("[Input invalid] - Total days is [" + total.toString() + "], but index is [" + index.toString() + "]");
			return null;
		}

		var info = getDayInfo(index);
		return info;
	}
	
	function getDayInfoByName(name)
	{
		index = 0;
		element = recordsView.staticTexts()[index];
		
		while(element.isValid())
		{
			// fount a record which match name
			if(element.name().indexOf(name) >= 0)
			{
				var info = getDayInfo(index);
				return info;
			}
			
			// jump to next record
			index++;
			element = recordsView.staticTexts()[index * 2];
		}
		
		// can't find any matched record
		log("[Input invalid] - Can't find any record whose name is [" + name.toString() + "]");
		return null;
	}
	
	function getTodayInfo()
	{
		index = 0;
		element = recordsView.staticTexts()[index];
		
		while(element.isValid())
		{
			// fount a record which match name
			if(element.name().indexOf("Today") >= 0)
			{			
				var info = getDayInfo(index);
				return info;
			}
			
			// jump to next record
			index++;
			element = recordsView.staticTexts()[index * 2];
		}
		
		// can't find any matched record
		log("[Input invalid] - Can't find any record whose name is [" + name.toString() + "]");
		return null;
	}


	function planDayByIndex(index, miles, confirm)
	{	
		total = getTotalDays();
		if(index < 0 || index >= total)
		{
			log("[Input invalid] - Total days is [" + total.toString() + "], but index is [" + index.toString() + "]");
			return;
		}
		
		wait();
		ratio = miles / maxMPD;
		ratio = Math.min(ratio, 1);
		recordsView.sliders()[index].dragToValue(ratio);
		
		// if confirm is not set, don't auto click the alert
		if(typeof confirm == "undefined") return;

		// check if the alert is shown and click the desired button
		if(confirm == true) confirm = 0;	// Yes
		else confirm = 1;					// No
		
		wait();
		if(staticTextExist(alert.TooHard))
		{
			alert.alertTitle == alert.TooHard;
			window.buttons()[confirm].tap();
		}
	}
	
	function planDayByName(name, miles, confirm)
	{	
		index = 0;
		element = recordsView.staticTexts()[index];
		
		while(element.isValid())
		{
			// fount a record which match name
			if(element.name().indexOf(name) >= 0)
			{
				wait();
				ratio = miles / maxMPD;
				ratio = Math.min(ratio, 1);
				recordsView.sliders()[index].dragToValue(ratio);
				
				// if confirm is not set, don't auto click the alert
				if(typeof confirm == "undefined") return;
			
				// check if the alert is shown and click the desired button
				if(confirm == true) confirm = 0;
				else confirm = 1;	
		
				wait();
				if(staticTextExist(alert.TooHard))
				{
					alert.alertTitle == alert.TooHard;
					window.buttons()[confirm].tap();
				}
				
				return;
			}
			
			// jump to next record
			index++;
			element = recordsView.staticTexts()[index * 3];
		}
		
		// can't find any matched record
		log("[Input invalid] - Can't find any record whose name is [" + name.toString() + "]");
	}
	

	function isInEditMode()
	{
		return cancelBtn.isValid() && cancelBtn.isVisible();
	}
	
	function isHardAlertShown()
	{
		wait();
		info = alert.getCustomAlertInfo();
		
		if(info == null)
			return false;
			
		return info.title == alert.TooHard;	
	}
	
	function confirmHardAlert(confirm)
	{
		if(isHardAlertShown())
		{
			if(typeof confirm == "undefined")
				confirm = 0;
			if(confirm == true)
				confirm = 0;
			else
				confirm = 1;
				
			alert.confirmCustomAlert(confirm);
		}
	}
	
	
	// Helpers
	function getDayInfo(index)
	{
		text = recordsView.staticTexts()[index * 2 + 0].name();
		value = recordsView.staticTexts()[index * 2 + 1].name();
		var info = {};
		info.text = text;
		if(text.indexOf("/") >= 0)
		{
			info.date = text.substring(0, text.indexOf(" /"));
			info.temperature = text.substring(text.indexOf("/ ") + 2);
		}
		else
		{
			info.date = text;
			info.temperature = "";
		}
		
		if(value.indexOf("/") >= 0)
		{		
			info.run = parseFloat(value.substring(0, value.indexOf("/")));
			info.total = parseFloat(value.substring(value.indexOf("/") + 1));
		}
		else
		{
			info.run = 0;
			info.total = parseFloat(value);
		}
		
		info.run = parseFloat(info.run.toFixed(2));
		info.total = parseFloat(info.total.toFixed(2));
		
		log("info.text: " + info.text);
		log("info.date: " + info.date);
		log("info.temperature: " + info.temperature);
		log("info.run: " + info.run);
		log("info.total: " + info.total);
		
		return info;
	}
}