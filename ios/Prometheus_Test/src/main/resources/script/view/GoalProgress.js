#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
GoalProgress function:
- isWeekViewValid()		:	check if current view is WeekProgress view
- isTodayViewValid()	:	check if current view is TodayProgress view
- scrollToWeekProgress():	scroll up from today to week view
- scrollToDayProgress()	:	scroll down from week to day view
- scrollToHistory()		:	scroll right to history view
- start()				:	press start button

*not implemented
- isWeekPlanValid(total, percent)	:	check if the current week plan is valid
- isTodayPlanValid(total, percent)	:	check if the current today plan is valid
- isWeatherValid(location, temp)	:	check if the location and temperature is valid
*/

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
	this.isWeekViewValid = isWeekViewValid;
	this.isTodayViewValid = isTodayViewValid;
	this.scrollToWeekProgress = scrollToWeekProgress;
	this.scrollToDayProgress = scrollToDayProgress;
	this.scrollToHistory = scrollToHistory;
	this.start = start;
		
	this.isWeekPlanValid = isWeekPlanValid;
	this.isTodayPlanValid = isTodayPlanValid;
	this.isWeatherValid = isWeatherValid;
	
	this.getQuote = getQuote;
	
	// Methods definition
	function isWeekViewValid()
	{
		return weekGoal.isValid() && weekGoal.isVisible();
	}

	function isTodayViewValid()
	{
		return todayGoal.isValid() && todayGoal.isVisible();
	}
	
	function scrollToWeekProgress()
	{
		if(isWeekViewValid())
			return;
		
		wait();
		app.dragInsideWithOptions({startOffset:{x:0.5, y:0.2}, endOffset:{x:0.5, y:0.5}, duration:0.3});
	}
	
	function scrollToDayProgress()
	{
		if(isTodayViewValid())
			return;
		
		wait();
		app.dragInsideWithOptions({startOffset:{x:0.5, y:0.8}, endOffset:{x:0.5, y:0.5}, duration:0.3});
	}
	
	function scrollToHistory()
	{
		wait();
		mainView.scrollRight();
	}
	
	function start()
	{
		start.tap();
	}
	
	function isWeekPlanValid(total, percent)
	{
		// currently can't implemented due to accessibility problem
	}
	
	function isTodayPlanValid(total, percent)
	{
		// currently can't implemented due to accessibility problem
	}
	
	function isWeatherValid(location, temperature)
	{
		// currently can't implemented due to accessibility problem
	}
	
	function getQuote()
	{
		// currently can't implemented due to accessibility problem
	}
}