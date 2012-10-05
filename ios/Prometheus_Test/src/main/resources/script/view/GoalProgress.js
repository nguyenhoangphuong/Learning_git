#import "../core/testcaseBase.js"
#import "_AlertHandler.js"
#import "_Tips.js"
#import "RunView.js"

/*
GoalProgress function:
================================================================================
- isWeekGoalVisible()	: check if current view is WeekProgress view
- isTodaysGoalVisible()	: check if current view is TodayProgress view
================================================================================
- scrollToWeekGoal()	: scroll up from today to week view
- scrollToTodaysGoal()	: scroll down from week to day view
- scrollToPlanner()		: scroll right to goal planning view
- scrollToSettings()	: scroll left to settings view
================================================================================
- start()				: press start button
- simulateARun()		: set location, press start and simulate a run
- simulateARunDontStop():
================================================================================
- getWeekInfo()			: get week information, return an object contains:
	+ text		: "7 days goal: 5 miles" (string)
	+ days		: 7 (int)
	+ goal		: 5 (float)
	+ percent	: 25 (float)
	+ distance	: 1.2 (float)
- getTodayInfo()		: get today information, return an object contains:
	+ text		: "Today's goal: 0.74 miles"
	+ goal		: 0.74 (float)
	+ percent	: 7 (float)
- getWeatherInfo()		: get the location, temperature and image name
	+ location		: "Ho Chi Minh city"
	+ temperature	: "30 ºC"
	+ image			: "sunny.png"
- getGPSSignal()		: get the gps signal strength
	+ return	: "Fair"
- getQuote()			: get the random quote
================================================================================
- setNewGoal()				: set a new goal after finish the week goal
- isSetNewGoalBtnVisible()	: check if the goal has been achieved
- isMusicBtnVisible()		: check if the music button is on screen
- isStartBtnVisible()		: check if the start button is on screen
- isInputTypeAlertShown()	: check if the input type dialog is on screen
- confirmInputAlert(opt)	: choose option in the input type dialog
	+ opt = 1: GPS
	+ opt = 2: Manual
	+ otherwise or undefined: Cancel
================================================================================
*/

function GoalProgress()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0];
	
	var weekGoal = mainView.staticTexts()[0];
	var weekProgress = mainView.staticTexts()[1];
	var todayGoal = mainView.staticTexts()[5];
	var todayProgress = mainView.staticTexts()[6];

	var location = mainView.staticTexts()[2];
	var temperature = mainView.staticTexts()[3];
	var weatherimg = mainView.images()[1];

	var gps = mainView.staticTexts()[7];
	var quote = mainView.staticTexts()[8];
		
	var startBtn = mainView.buttons()[3];
	var musicBtn = mainView.buttons()[2];
	var newGoalBtn = mainView.buttons()[0];
	
	// Methods
	this.isWeekGoalVisible = isWeekGoalVisible;
	this.isTodaysGoalVisible = isTodaysGoalVisible;
	
	this.scrollToWeekGoal = scrollToWeekGoal;
	this.scrollToTodaysGoal = scrollToTodaysGoal;
	this.scrollToPlanner = scrollToPlanner;
	this.scrollToSettings = scrollToSettings;
	this.tapMusic = tapMusic;
	
	this.start = start;
	this.simulateARun=simulateARun;
	this.simulateARunDontStop= simulateARunDontStop;
		
	this.getWeekInfo = getWeekInfo;
	this.getTodayInfo = getTodayInfo;
	this.getWeatherInfo = getWeatherInfo;
	this.getGPSSignal = getGPSSignal;
	this.getQuote = getQuote;
	
	this.setNewGoal = setNewGoal;
	this.isSetNewGoalBtnVisible = isSetNewGoalBtnVisible;
	this.isMusicBtnVisible = isMusicBtnVisible;
	this.isStartBtnVisible = isStartBtnVisible;
	this.isInputTypeAlertShown = isInputTypeAlertShown;
	this.confirmInputAlert = confirmInputAlert;
	
	// Methods definition
	function isWeekGoalVisible()
	{
		page = window.pageIndicators()[0].value();
		exist = (page == "page 2 of 3"  && startBtn.name() == "Start" && !startBtn.isVisible());
		
		log("WeekGoal visible: " + exist);
		return exist;
	}

	function isTodaysGoalVisible()
	{
		page = window.pageIndicators()[0].value();
		exist = (page == "page 2 of 3" && startBtn.name() == "Start" && startBtn.isVisible());
		
		log("TodayGoal visible: " + exist);
		return exist;
	}
	
	function scrollToWeekGoal()
	{
		wait(0.5);
		app.dragInsideWithOptions({startOffset:{x:0.5, y:0.2}, endOffset:{x:0.5, y:0.5}, duration:0.3});
		log("Scroll to WeekGoal");
	}
	
	function scrollToTodaysGoal()
	{
		wait(0.5);
		app.dragInsideWithOptions({startOffset:{x:0.5, y:0.8}, endOffset:{x:0.5, y:0.2}, duration:0.3});
		log("Scroll to TodaysGoal");
	}
	
	function scrollToPlanner()
	{
		wait(0.5);
		mainView.scrollRight();
		log("Scroll to Planner");
	}

	function scrollToSettings()
	{
		wait(0.5);
		mainView.scrollLeft();
		log("Scroll to Settings");
	}
	
	function tapMusic()
	{
		if(musicBtn.isValid() && musicBtn.isVisible())
		{
			wait(0.5);
			musicBtn.tap();
			log("Tap [Music]");
		}
		else
			log("No [Music] on this view");
	}
	
	
	function start()
	{
		wait(0.5);
		startBtn.tap();
		log("Tap [Start]");
	}
	
	
	function getWeekInfo()
	{
		scrollToWeekGoal();
		wait();
		
		text = weekGoal.name();
		value = weekProgress.name();
		
		var info = {};
		info.text = text;
		info.days = parseInt(text.substring(0, text.indexOf(" ")));
		info.goal = parseFloat(text.substring(text.indexOf(":") + 1));

		if(value.indexOf("%") >= 0)
		{
			info.percent = parseFloat(value);
			weekProgress.tap();
			wait();
			info.distance = parseFloat(weekProgress.name());
		}
		else
		{
			info.distance = parseFloat(value);
			weekProgress.tap();
			wait();
			info.percent = parseFloat(weekProgress.name());
		}
		
		// logging
		log("weekinfo.text: " + info.text);
		log("weekinfo.days: " + info.days);
		log("weekinfo.goal: " + info.goal);
		log("weekinfo.percent: " + info.percent);
		log("weekinfo.distance: " + info.distance);
		
		return info;
	}
	
	function getTodayInfo()
	{
		text = todayGoal.name();
		value = todayProgress.name();
		
		var info = {};
		info.text = text;
		info.goal = parseFloat(text.substring(text.indexOf(":") + 1));
		info.percent = value;//parseFloat(value);

		// logging
		log("dayinfo.text: " + info.text);
		log("dayinfo.goal: " + info.goal);
		log("dayinfo.percent: " + info.percent);

		return info;
	}
	
	function getWeatherInfo()
	{
		var info = {};
		info.location = location.name();
		info.temperature = temperature.name();
		info.image = weatherimg.name();

		// logging
		log("weather.location: " + info.location);
		log("weather.temperature: " + info.temperature);
		log("weather.image: " + info.image);
		
		return info;
	}

	function getGPSSignal()
	{
		text = gps.name();
		gps = text.substring(text.lastIndexOf(" ") + 1);
		log("gps: " + gps);
		return gps;
	}
		
	function getQuote()
	{
		log("quote: " + quote.name());
		return quote.name();
	}
	
	
	function simulateARun(miles, error) 
	{
		if(typeof error == "undefined")
		{
			error = {};
			error.AH = 10;
			error.AV = 15;
			error.BH = 10;
			error.BV = 15;
		}
		
		simulateRunAndStop(miles, true, error);
	}
	
	function simulateRunAndStop(miles, stop, error) 
	{
		log("Run " + miles);
		setN(error);
		start();
		wait();
		
		for (i=1; i<= miles; i++) 
		{
			if (i%2 == 1) 
			{
				setB(error);
			} else 
			{
				setA(error);
			}
			wait();
		}
		
		var run = new RunView();
		//if (!run.canPause()) 
		//{
		//	fail("The run has not started");
		//} 
		wait(2);
		
		if (stop) 
		{
			log("Finish run");
		 	//check the crash
			run.pause();
			wait(2);
			run.finish();
		}
	}
	
	function simulateARunDontStop(miles, error) 
	{
		if(typeof error == "undefined")
		{
			error = {};
			error.AH = 10;
			error.AV = 15;
			error.BH = 10;
			error.BV = 15;
		}
					
		simulateRunAndStop(miles, false, error);
	}
	
	function setA(error) 
	{
		var pointA = {location:{latitude:10.775154,longitude:106.679465}, options:{speed:8, altitude:200, horizontalAccuracy: error.AH, verticalAccuracy: error.AV}};
		target.setLocationWithOptions(pointA.location,pointA.options);  
	}
	
	function setB(error) 
	{
		var pointB = {location:{latitude:10.768071,longitude:106.667256}, options:{speed:11, altitude:200, horizontalAccuracy: error.BH, verticalAccuracy: error.BV}};
		target.setLocationWithOptions(pointB.location,pointB.options);
	}
	
	function isSetNewGoalBtnVisible() 
	{
		return newGoalBtn.isVisible();
	}
	
	function setNewGoal(planType) 
	{
		newGoalBtn.tap();
		var planChooser = new PlanChooser();
		if (planType == "easy") {
			planChooser.selectEasy();
		} else if (planType == "normal") {
			planChooser.selectNormal();
		} else {
			planChooser.selectActive();
		}
	}
	
	function isMusicBtnVisible()
	{
		exist = musicBtn.isValid() && musicBtn.isVisible();
		log("[Music] visible: " + exist);
		return exist;
	}
	
	function isStartBtnVisible()
	{
		exist = startBtn.isValid() && startBtn.isVisible() && startBtn.name() == "Start";
		log("[Start] visible: " + exist);
		return exist;
	}
	
	function isInputTypeAlertShown()
	{
		
	}
	
	function confirmInputAlert(opt)
	{
		
	}
}
