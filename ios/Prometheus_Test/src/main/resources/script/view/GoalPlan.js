#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
GoalPlan functions:
=========================================================================================
- isVisible()			:	check if the current view is GoalPlan
- isTipsVisible()		:	check if tips on this view is shown up
=========================================================================================
- edit()				:	tap the Edit button
- reset()				:	tap the Auto Suggest button
- save()				:	tap the Done button (while in edit mode)
- cancel()				:	tap the Cancel button (while in edit mode)
=========================================================================================
- scrollToTodaysGoal()	:	back to TodayGoal view
- scrollToHistory()		:	scroll to History view
=========================================================================================
- getTotalDays()		:	get the number of days (or records) in this goal
- getPassedDays()		:	get the number of days (or records) which can't be planned any more
- getTotalPlanAmount()	:	get the number of miles that is planned (before and after)
- getRemainPlanAmount()	:	get the number of miles that is planned for the following days
- getDoneAmount()			:	get the number of miles that you run
=========================================================================================
- getUnit()				:	get the unit display in this view ("miles" / "reps"...)
- getWeekInfo()			:	get info of a goal {duration, goal}
	+ duration		: 	"Sep 14 - Sep 20"
	+ goal			: 	7 (float)
- getDayInfoByIndex(i)	:	get info of a day by index {text, date, temperature, run, total}
	+ date			: 	"Sat Sep 15th"
	+ run			: 	0.74 (float)
	+ total			:	1.10 (float)		
- getDayInfoByName(n)	:	get info of a day by its name {text, date, temperature, run, total}
- getTodayInfo()		:	get info of today {text, date, temperature, run, total}
=========================================================================================
- planDayByIndex(i, miles, confirm)		:	set the goal of day i to miles (and dismiss the alert)
	+ planDayByIndex(i, miles)			- 	just drag the slider, don't touch the alert
	+ planDayByIndex(i, miles, false) 	- 	drag the slider and choose NO when the alert shown up
	+ planDayByIndex(i, miles, true) 	- 	drag the slider and choose YES when the alert shown up
	
- planDayByName(n, miles, confirm)		:	similar to planDayByIndex but use NAME instead
	+ planDayByName("Sat Sep 15th", 0, 4.3)
=========================================================================================
- isInEditMode()						:	check if the current view is edit mode
- isHardAlertShown()					:	check if the Too Hard alert is CURRENTLY showing up
- confirmHardAlert(confirm)				:	close the alert by pressing specify button
	+ confirm == true || not defined	-	YES button
	+ confirm == false					-	NO button
=========================================================================================

MAY BE CHANGES:
- Yes/No index of TooHard alert
*/

maxMPD = 2.95;
function GoalPlan()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0];
	var recordsView = mainView.scrollViews()[1];

	var cancelBtn = mainView.buttons()[5];
	var autoBtn = mainView.buttons()[6];
	var doneBtn = mainView.buttons()[7];
	var editBtn = mainView.buttons()[8];
	
	var weekDays = mainView.staticTexts()[11];
	var weekGoal = mainView.staticTexts()[10];

	
	// Methods
	this.isVisible = isVisible;
	this.isTipsVisible = isTipsVisible;
	this.autoSetMPD = autoSetMPD;
	
	this.edit = edit;
	this.reset = reset;
	this.save = save;
	this.cancel = cancel;
	
	this.scrollToTodaysGoal = scrollToTodaysGoal;
	this.scrollToHistory = scrollToHistory;
	
	this.getTotalDays = getTotalDays;
	this.getPassedDays = getPassedDays;
	this.getTotalPlanAmount = getTotalPlanAmount;
	this.getDoneAmount = getDoneAmount;
	this.getTotalPlanAmount = getTotalPlanAmount;
	this.getRemainPlanAmount = getRemainPlanAmount;
	
	this.getUnit = getUnit;
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
		exist = (page == "page 3 of 3" && recordsView.isValid() && recordsView.isVisible()) ||
				tips.isTipsDisplay("GoalPlan", mainView);
		
		log("GoalPlan visible: " + exist);
		return exist;
	}
	
	function isTipsVisible()
	{
		display = tips.isTipsDisplay("GoalPlan");
		log("Tips on GoalPlan is shown: " + display);
		
		return display;
	}
	
	function autoSetMPD()
	{
		// drag to maximum
		editBtn.tap();
		wait(0.5);
		
		n = recordsView.sliders().length;
		recordsView.sliders()[n - 1].dragToValue(1);
		
		// close the alert if there is one
		if(staticTextExist(alert.TooHard))
			window.buttons()[1].tap();
		
		// get the value
		value = recordsView.staticTexts()[(n - 1) * 2 + 1].name();
		
		// cancel change and set maxMPD;
		cancelBtn.tap();
		maxMPD = parseFloat(value);
		
		log("The <maxMPD> is automatic set to: " + maxMPD);
	}
	
	
	function edit()
	{
		wait(0.5);
		editBtn.tap();
		log("Tap [Edit]");
	}
	
	function reset()
	{
		wait(0.5);
		autoBtn.tap();
		log("Tap [Auto Suggest]");
	}
	
	function save()
	{
		wait(0.5);
		doneBtn.tap();
		log("Tap [Save]");
	}
	
	function cancel()
	{
		wait(0.5);
		cancelBtn.tap();
		log("Tap [Done]");
	}
	
	
	function scrollToTodaysGoal()
	{
		wait(0.5);
		mainView.scrollLeft();
		log("Scroll to TodayGoal");
	}
	
	function scrollToHistory()
	{
		wait(0.5);
		app.dragInsideWithOptions({startOffset:{x:0.5, y:0.2}, endOffset:{x:0.5, y:0.5}, duration:0.3});
		log("Scroll to History");
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
	
	function getTotalPlanAmount()
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
		
		return result;
	}
	
	function getDoneAmount()
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
	
	function getRemainPlanAmount()
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
	
	
	function getUnit()
	{
		text = weekGoal.name();
		unit = text.substring(text.indexOf(" ") + 1);
		
		log("Unit used: " + unit);
		return unit;
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
		if(confirm == true) confirm = 1;	// Yes
		else confirm = 0;					// No
		
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
				if(confirm == true) confirm = 1;
				else confirm = 0;	
		
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
				confirm = 1;
			if(confirm == true)
				confirm = 1;
			else
				confirm = 0;
				
			alert.confirmCustomAlert(confirm);
		}
	}
	
	
	// Helpers
	function getDayInfo(index)
	{
		text = recordsView.staticTexts()[index * 2 + 0].name();
		value = recordsView.staticTexts()[index * 2 + 1].name();
		var info = {};
		//info.text = text;
		if(text.indexOf("/") >= 0)
		{
			info.date = text.substring(0, text.indexOf(" /"));
			//info.temperature = text.substring(text.indexOf("/ ") + 2);
		}
		else
		{
			info.date = text;
			//info.temperature = "";
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
		
		log("info.date: " + info.date);
		log("info.run: " + info.run);
		log("info.total: " + info.total);
		
		return info;
	}
}
