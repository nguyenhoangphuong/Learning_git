#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
GoalPlan functions:
- isVisible()			:	check if the current view is GoalPlan
- edit()				:	tap the Edit button
- reset()				:	tap the Reset button (while in edit mode)
- save()				:	tap the Done button (while in edit mode)

- getTotalDays()		:	get the number of days (or records) in this goal
- getPassedDays()		:	get the number of days (or records) which can't be planned any more
- getTotalPlanMiles()	:	get the number of miles that is planned (before and after)
- getRemainPlanMiles()	:	get the number of miles that is planned for the following days
- getRunMiles()			:	get the number of miles that you run

- getWeekInfo()			:	get info of a goal {duration, goal}
- getDayInfoByIndex(i)	:	get info of a day by index {date, temperature, miles}
- getDayInfoByName(n)	:	get info of a day by its name {date, temperature, miles}
- getTodayInfo()		:	get info of today {date, temperature, run, remain}

- planDayByIndex(i, miles, confirm)		:	set the goal of day i to miles
	+ planDayByIndex(i, miles)	- choose YES when alert is shown up
	+ planDayByIndex(i, miles, "No") - choose NO when alert is shown up
- planDayByName(n, miles, confirm)		:	set the goal of day have name n to miles
	+ planDayByName(i, miles)	- choose YES when alert is shown up
	+ planDayByName(i, miles, "No") - choose NO when alert is shown up
	
- isEasyAlertShown()	:	check if the Easy Goal alert is shown up
- isHardAlertShown()	:	check if the Too Hard alert is shown up
*/

function GoalPlan()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0];
	var recordsView = mainView.scrollViews()[0];
	
	var editBtn = mainView.buttons()["Edit"];
	var resetBtn = mainView.buttons()["Reset"];
	var saveBtn = mainView.buttons()["Done"];

	var weekDays = mainView.staticTexts()[12];
	var weekGoal = mainView.staticTexts()[11];
	
	var maxMPD = 5.31;
	
	// Methods
	this.isVisible = isVisible;
	this.edit = edit;
	this.reset = reset;
	this.save = save;
	
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
	
	this.isEasyAlertShown = isEasyAlertShown;
	this.isHardAlertShown = isHardAlertShown;

	
	// Methods definition
	function isVisible()
	{
		page = window.pageIndicators()[0].value();
		return page == "page 3 of 3" && recordsView.isValid() && recordsView.isVisible();
	}
	
	function edit()
	{
		wait(0.5);
		editBtn.tap();
	}
	
	function reset()
	{
		wait(0.5);
		resetBtn.tap();
	}
	
	function save()
	{
		wait(0.5);
		saveBtn.tap();
	}
	
	
	function getTotalDays()
	{
		count = 0;
		element = recordsView.staticTexts()[count];
		
		while(element.isValid())
		{
			count++;
			element = recordsView.staticTexts()[count * 3];
		}
		
		log("Total days: " + count.toString());
		return count;
	}
	
	function getPassedDays()
	{
		count = 0;
		element = recordsView.staticTexts()[count * 3 + 2];
		
		while(element.isValid() && element.name().indexOf("/") >= 0)
		{
			count++;
			element = recordsView.staticTexts()[count * 3 + 2];
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
			value = recordsView.staticTexts()[count * 3 + 2].name();
			if(value.indexOf("/") >= 0) value = value.substring(value.indexOf("/") + 2, value.indexOf(" miles"));
			else value = value.substring(0, value.indexOf(" miles"));
			
			// parse the value
			miles += parseFloat(value);
			
			count++;
			element = recordsView.staticTexts()[count * 3];
		}
		
		log("Total plan: " + miles.toString());
		return miles;
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
			value = recordsView.staticTexts()[count * 3 + 2].name();
			if(value.indexOf("/") >= 0)
			{
				value = value.substring(0, value.indexOf(" / "));
				miles += parseFloat(value);
			}
			
			count++;
			element = recordsView.staticTexts()[count * 3];
		}
		
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
			value = recordsView.staticTexts()[count * 3 + 2].name();
			if(value.indexOf("/") < 0)
			{
				value = value.substring(0, value.indexOf(" miles"));
		
				// parse the value
				miles += parseFloat(value);
			}
			
			count++;
			element = recordsView.staticTexts()[count * 3];
		}
		
		log("Plan remain: " + miles.toString());
		return miles;
	}
	
	
	function getWeekInfo()
	{
		var info = {};
		info.duration = weekDays.name();
		info.goal = weekGoal.name();
		
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
		
		var info = {};
		info.date = recordsView.staticTexts()[index * 3 + 0].name();
		info.temperature = recordsView.staticTexts()[index * 3 + 1].name();
		info.miles = recordsView.staticTexts()[index * 3 + 2].name();
		
		log("info.date: " + info.date);
		log("info.temperature: " + info.temperature);
		log("info.miles: " + info.miles);
		
		return info;
	}
	
	function getDayInfoByName(name)
	{
		index = 0;
		element = recordsView.staticTexts()[index];
		
		while(element.isValid())
		{
			// fount a record which match name
			if(element.name() == name)
			{
				var info = {};
				info.date = recordsView.staticTexts()[index * 3 + 0].name();
				info.temperature = recordsView.staticTexts()[index * 3 + 1].name();
				info.miles = recordsView.staticTexts()[index * 3 + 2].name();
		
				log("info.date: " + info.date);
				log("info.temperature: " + info.temperature);
				log("info.miles: " + info.miles);

				return info;
			}
			
			// jump to next record
			index++;
			element = recordsView.staticTexts()[index * 3];
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
				value = recordsView.staticTexts()[index * 3 + 2].name();
				var info = {};
				info.date = recordsView.staticTexts()[index * 3 + 0].name();
				info.temperature = recordsView.staticTexts()[index * 3 + 1].name();
				info.run = parseFloat(value.substring(0, value.indexOf(" / ")));
				info.remain = parseFloat(value.substring(value.indexOf("/") + 2, value.indexOf(" miles")));
		
				log("info.date: " + info.date);
				log("info.temperature: " + info.temperature);
				log("info.run: " + info.run);
				log("info.remain: " + info.remain);

				return info;
			}
			
			// jump to next record
			index++;
			element = recordsView.staticTexts()[index * 3];
		}
		
		// can't find any matched record
		log("[Input invalid] - Can't find any record whose name is [" + name.toString() + "]");
		return null;
	}


	function planDayByIndex(index, miles, confirm)
	{
		if(typeof confirm == "undefined") confirm = "YES";
		else confirm = "NO";
		
		total = getTotalDays();
		if(index < 0 || index >= total)
		{
			log("[Input invalid] - Total days is [" + total.toString() + "], but index is [" + index.toString() + "]");
			return;
		}
		
		wait();
		alert.alertChoice = confirm;
		recordsView.sliders()[index].dragToValue(miles / maxMPD);
		wait(2);
	}
	
	function planDayByName(name, miles, confirm)
	{
		if(typeof confirm == "undefined") confirm = "YES";
		else confirm = "NO";
		
		index = 0;
		element = recordsView.staticTexts()[index];
		
		while(element.isValid())
		{
			// fount a record which match name
			if(element.name() == name)
			{
				wait();
				alert.alertChoice = confirm;
				recordsView.sliders()[index].dragToValue(miles / maxMPD);
				wait(2);
				
				return;
			}
			
			// jump to next record
			index++;
			element = recordsView.staticTexts()[index * 3];
		}
		
		// can't find any matched record
		log("[Input invalid] - Can't find any record whose name is [" + name.toString() + "]");
	}
	
	
	function isEasyAlertShown()
	{
		wait();
		return alert.alertTitle != null && alert.alertTitle == alert.TooEasy;
	}
	
	function isHardAlertShown()
	{
		wait();
		return alert.alertTitle != null && alert.alertTitle == alert.TooHard;	
	}
}