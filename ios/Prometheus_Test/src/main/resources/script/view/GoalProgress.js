#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
GoalProgress function:
- isWeekGoalVisible()		:	check if current view is WeekProgress view
- isTodayGoalVisible()		:	check if current view is TodayProgress view
- scrollToWeekGoal()		:	scroll up from today to week view
- scrollToDayGoal()			:	scroll down from week to day view
- scrollToGoalPlan()		:	scroll right to goal planning view
- scrollToAbout()			:	scrol left to about view
- start()					:	press start button
- getWeekInfo()				:	get week information, return an object contains:
	+ text		:	"7 days goal: 5 miles" (string)
	+ days		:	7 (int)
	+ goal		:	5 (float)
	+ percent	:	25 (float)
	+ distance	:	1.2 (float)
- getTodayInfo()			:	get today information, return an object contains:
	+ text		:	"Today's goal: 0.74 miles"
	+ goal		:	0.74 (float)
	+ percent	:	7 (float)
- getWeatherInfo()			:	get the location, temperature and image name
	+ location	:	"Ho Chi Minh city"
	+ temperature:	"30 ºC"
	+ image		:	"sunny.png"
- getGPSSignal()			:	get the gps signal strength
	+ return: "Fair"
- getQuote()				:	get the random quote

- setStartPoint()			:	set the start point for the next run
*/


function GoalProgress()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0];
	
	var weekGoal = mainView.staticTexts()[2];
	var weekProgress = mainView.staticTexts()[3];
	var todayGoal = mainView.staticTexts()[7];
	var todayProgress = mainView.staticTexts()[8];

	var temperature = mainView.staticTexts()[5];
	var location = mainView.staticTexts()[4];
	var weatherimg = mainView.images()[1];

	var gps = mainView.staticTexts()[9];
	var quote = mainView.staticTexts()[10];
		
	var start = mainView.buttons()[6];
	
	// Methods
	this.isWeekGoalVisible = isWeekGoalVisible;
	this.isTodayGoalVisible = isTodayGoalVisible;
	this.scrollToWeekGoal = scrollToWeekGoal;
	this.scrollToDayGoal = scrollToDayGoal;
	this.scrollToGoalPlan = scrollToGoalPlan;
	this.scrollToAbout = scrollToAbout;
	this.start = start;
		
	this.getWeekInfo = getWeekInfo;
	this.getTodayInfo = getTodayInfo;
	this.getWeatherInfo = getWeatherInfo;
	
	this.getGPSSignal = getGPSSignal;
	this.getQuote = getQuote;
	
	this.setStartPoint = setStartPoint;
	
	// Methods definition
	function isWeekGoalVisible()
	{
		page = window.pageIndicators()[0].value();
		log("isWeekGoalVisible: " + (page == "page 2 of 3" && weekProgress.isValid() && weekProgress.isVisible()).toString());

		return page == "page 2 of 3" && weekProgress.isValid() && weekProgress.isVisible();
	}

	function isTodayGoalVisible()
	{
		return !isWeekGoalVisible();
	}
	
	function scrollToWeekGoal()
	{
		/*
		if(isWeekGoalVisible())
		{
			log("Note: current view is week goal");
			return;
		}
		*/
		
		wait();
		app.dragInsideWithOptions({startOffset:{x:0.5, y:0.2}, endOffset:{x:0.5, y:0.5}, duration:0.3});
	}
	
	function scrollToDayGoal()
	{
		/*
		if(isTodayGoalVisible())
		{
			log("Note: current view is today goal");
			return;
		}
		*/
		
		wait();
		app.dragInsideWithOptions({startOffset:{x:0.5, y:0.8}, endOffset:{x:0.5, y:0.5}, duration:0.3});
	}
	
	function scrollToGoalPlan()
	{
		wait();
		mainView.scrollRight();
	}

	function scrollToAbout()
	{
		wait();
		mainView.scrollLeft();
	}
	
	function start()
	{
		wait();
		start.tap();
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
		info.goal = parseFloat(text.substring(text.indexOf(":") + 1, text.indexOf("miles")));

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
		info.goal = parseFloat(text.substring(text.indexOf(":") + 1, text.indexOf("miles")));
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
	
	function setStartPoint()
	{
	}
}