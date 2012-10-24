#import "../MVPLibs.js"

/*
List of function:
================================================================================
- assignControls()
- isVisible()
================================================================================
- getDateRange()		: return ["Oct 14", "Oct 15", "Oct 16"...]
- getPassedDate()		: same as above but only passed days
- getWeatherInfo()		: return {city: "HCM city", temperature: "90 F"}
- getActivities()		: return ["Swimming", "Running"...]
================================================================================
- tapActivity(id)		: tap the activity with name/index = id
- isTodaysRecordsShown(): check if current day progress history is shown
- getTodaysRecords()	: return object consist of
================================================================================
*/

function Progress()
{
	// Private fields
	var window;
	var mainView;
	var dateRange;
	
	var cityTxt;
	var temperatureTxt;
	
	// Initalize controls
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.getDateRange = getDateRange;
	this.getPassedDay = getPassedDay;
	this.getWeatherInfo = getWeatherInfo;
	this.getActivities = getActivities;
	
	this.tapActivity = tapActivity;
	this.isTodaysRecordsShown = isTodaysRecordsShown;
	this.getTodaysRecords = getTodaysRecords;
	
	// Methods definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window.scrollViews()[0];
		dateRange = mainView.scrollViews()[0].buttons();
		
		cityTxt = mainView.staticTexts()[0];
		temperatureTxt = mainView.staticTexts()[1];
	}
	
	function isVisible()
	{
		visible = navigationBar.progressIsVisible();
		
		log("Progress is visible: " + visible);	
		return visible;
	}
	
	function getDateRange()
	{
		n = dateRange.length;
		var info = [];
		
		for(i = 0; i < n; i++)
			info[i] = dateRange[i].name();
		
		log("Date range: " + JSON.stringify(info));
		return info;
	}
	
	function getPassedDay()
	{
		// not implemented
	}
	
	function getWeatherInfo()
	{
		var info = {};
		info.city = cityTxt.name();
		info.temperature = temperatureTxt.name();
		
		log("Weather: " + JSON.stringify(info));
	}
	
	function getActivities()
	{
		// not implemented 
	}
	
	function tapActivity(id)
	{
		// not implemented		
	}
	
	function isTodaysRecordsShown()
	{
		// not implemented
	}
	
	function getTodaysRecords()
	{
		// not implemented
	}
}
