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

*not implemented
- getWeekInfo()				:	get total (in miles) and week percent
	return object ex: {total: 18, percent: 25}
- getTodayInfo()			:	get total (in miles) and current day percent
	return object ex: {total: 18, percent: 25}
- getWeatherInfo()			:	get the location, temperature and image file name
	return object ex: {location: Ho Chi Minh city, temperature: 81 oF, image: sunny}
*/

// To improve: check isWeekGoalVisible and isTodayGoalVisible

function GoalProgress()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0];
	
	var weekGoal = mainView.staticTexts()["week goal"];
	var weekProgress = mainView.staticTexts()["week progress"];
	var todayGoal = mainView.staticTexts()["today goal"];
	var todayProgress = mainView.staticTexts()["today percent"];
	
	var temperature = mainView.staticTexts()["temperature"];
	var location = mainView.staticTexts()["location"];
	var quote = mainView.staticTexts()["quote"];
	
	var start = mainView.buttons()["start"];
	
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
	
	this.getQuote = getQuote;
	
	// Methods definition
	function isWeekGoalVisible()
	{
		page = window.pageIndicators()[0].value();
		return page == "page 2 of 3" && weekGoal.isValid() && weekGoal.isVisible();
	}

	function isTodayGoalVisible()
	{
		page = window.pageIndicators()[0].value();
		return page == "page 2 of 3" && todayGoal.isValid() && todayGoal.isVisible();
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
		start.tap();
	}
	
	
	function getWeekInfo()
	{
		// currently can't implemented due to accessibility problem
	}
	
	function getTodayInfo()
	{
		// currently can't implemented due to accessibility problem
	}
	
	function getWeatherInfo()
	{
		// currently can't implemented due to accessibility problem
	}
	
	function getQuote()
	{
		// currently can't implemented due to accessibility problem
	}
}