#import "../MVPLibs.js"

/*
List of function:
================================================================================
- assignControls()
- isVisible()
================================================================================
- tapToday()					: tap Today button
- tapDate(index)				: tap on a date (can only use with index)
- tapActivity(index)			: tap on an activity
- tapOutside()					: tap outside the activity buttons
================================================================================
- getActivities()				: return {activities: [Running, Swimming, ...], 
								  		  percent: [0%, 100%, ...]}
- getCurrentHistory()			: return {name: Treadmill, value: 0.00 miles, 
										  percent: n/a}
- isHistoryRecordsShown()		: check if current hisotry is shown
- isNoActivity()				: check if there are no records in an activity
================================================================================

to be update:
	+ getDateRange
	+ getHistoryInfo
*/

function Progress()
{
	// Private fields
	var window;
	var mainView;
	var dateRange;
	
	var todayBtn;
	var activityNo;
	
	// Initalize controls
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.tapToday = tapToday;
	this.tapDate = tapDate;
	this.tapActivity = tapActivity;
	this.tapOutside = tapOutside;
	
	this.getActivities = getActivities;
	this.getCurrentHistory = getCurrentHistory;
	this.isHistoryRecordsShown = isHistoryRecordsShown;
	this.isNoActivity = isNoActivity;
	
	// Methods definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window.scrollViews()[0];
		dateRange = mainView.scrollViews()[0].buttons();
		
		todayBtn = app.navigationBar().rightButton();
		activityNo = getNumberOfActivity();
	}
	
	function isVisible()
	{
		visible = navigationBar.progressIsVisible();
		
		log("Progress is visible: " + visible);	
		return visible;
	}
	
	function tapToday()
	{
		todayBtn.tap();
		log("Tap [Today]");
	}
	
	function tapDate(date)
	{
		dateRange[date].tap();
		log("Tap [" + date + "]");
	}
	
	function tapActivity(index)
	{
		images = mainView.images();
		count = -1;
		
		for(i = 0; i < images.length; i++)
		{
			size = images[i].rect().size;
			if(size.width == 83 && size.height == 83)
				count++;
			
			if(count == index)
			{
				index = i;
				break;
			}
		}
		
		log("Tap activity with index: " + count);
		rect = images[index].rect();
		
		target.tapWithOptions({x: rect.origin.x + 40, y: rect.origin.y + 40});
		wait();
	}
	
	function tapOutside()
	{
		target.tapWithOptions({x: 240, y: 120});
		log("Tap outside");
		wait();
	}
	
	function getActivities()
	{
		// start, end index
		start = 3;
		
		// get list
		var info = {};
		info.activities = [];
		info.percent = [];
		
		texts = mainView.staticTexts();
		for(i = 0; i < activityNo; i++)
		{
			percent = texts[start].name();
			activity = texts[start + 1].name();
			start = start + 2;
			
			info.activities[i] = activity;
			info.percent[i] = percent;
		}
		
		log("Activities: " + JSON.stringify(info));
		return info;
	}
	
	function getCurrentHistory()
	{
		if(!isHistoryRecordsShown())
			return null;
		
		start = 3 + activityNo * 2;
		texts = mainView.staticTexts();
		
		var info = {};
		info.name = texts[start + 0].name();
		info.value = texts[start + 1].name();
		info.percent = texts[start + 2].name();
		
		log("Current history: " + JSON.stringify(info));
		return info;
	}
	
	function isHistoryRecordsShown()
	{
		start = 3 + activityNo * 2;
		shown = mainView.staticTexts()[start].isVisible();
		
		log("History records shown: " + shown);
		return shown;
	}
	
	function isNoActivity()
	{	
		no = staticTextExist("No activity", mainView);
		
		log("No activity: " + no);
		return no;
	}
	
	function getNumberOfActivity()
	{
		images = mainView.images();
		count = 0;
		
		for(i = 0; i < images.length; i++)
		{
			size = images[i].rect().size;
			if(size.width == 83 && size.height == 83)
				count++;
		}
		
		return count;
	}
}
