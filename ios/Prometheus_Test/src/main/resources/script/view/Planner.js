#import "../core/testcaseBase.js"
#import "_AlertHandler.js"
#import "_NavigationBar.js"

/*
List of functions:
=========================================================================================
- isVisible()
- assignControls()
=========================================================================================

=========================================================================================
*/

maxMPD = 2.95;
function GoalPlan()
{
	// Private fields
	var window;
	var mainView;
	
	var dateTab;
	var activityTab;

	// Initalize
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	this.isInEditMode = isInEditMode;
	
	this.groupByDate = groupByDate;
	this.groupByActivity = groupByActivity;
	
	this.getPlanInfoByDate = getPlanInfoByDate;
	this.getPlanInfoByActivity = getPlanInfoByActivity;
	this.getDateRange = getDateRange;
	
	this.getDetailPlanOfDate = getDetailPlanOfDate;
	this.getDetailPlanOfActivity = getDetailPlanOfActivity;
	
	this.tapEdit = tapEdit;
	this.tapX = tapX;
	this.tapV = tapV;
	this.tapCancel = tapCancel;
	this.tapDone = tapDone;
	this.tapSuggest = tapSuggest;
	this.tapUndo = tapUndo;
	
	this.setPlanAmount = setPlanAmount;

	// Methods definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window.tableViews()[0];
		
		dateTab = app.navigationBar().segmentedControls()[0].buttons()[0];
		activityTab = app.navigationBar().segmentedControls()[0].buttons()[1];
	}
	
	function isVisible()
	{
		visible = dateTab.isVisible() && dateTab.name() == "Date" &&
				  activityTab.isVisible() && activityTab.name() == "Activity";
		
		log("Planner is visible: " + visible);
		return visible;
	}
	
	function isInEditMode()
	{
		// to be update
	}
	
	function groupByDate()
	{
		dateTab.tap();
		log("Tap [Date]");
	}
	
	function groupByActivity()
	{
		activityTab.tap();
		log("Tap [Activity]");
	}
	
	function getPlanInfoByDate()
	{
		// group by date first
		groupByDate();
		
		// get info
		var info = [];
		cells = recordList.cells();
		groups = mainView.groups();
		n = groups.length;
		step = cells.length / n;
		
		for(i = 0; i < n; i++)
		{
			date = groups[i].name();
			activities = [];
			
			for(j = 0; j < step; j++)
			{
				text = cells[i * step + j].name();
				name = text.substring(0, text.indexOf(","));
				goal = text.substring(text.indexOf(", ") + 1);
				
				activities[j] = {name: name, goal: goal};
			}
			
			info[i] = {date: date, activities: activities};
		}

		log("Plan info by date: " + JSON.stringify(info));
		return info;
	}
	
	function getPlanInfoByActivity()
	{
		// group by activity first
		groupByActivity();
		
		// get info
		var info = [];
		cells = mainView.cells();
		n = cells.length;
		
		for(i = 0; i < n; i++)
		{
			text = cells[i].name();
			name = text.substring(0, text.indexOf(","));
			goal = text.substring(text.indexOf(", ") + 1);
			
			info[i] = {name: name, goal: goal};
		}
		
		log("Plan info by activity: " + JSON.stringify(info));
		return info;
	}
	
	function getDateRange()
	{
		// group by date first
		groupByDate();
		
		// get range
		var info = [];
		groups = mainView.groups();
		n = groups.length;
		
		for(i = 0; i < n; i++)
			info[i] = groups[i].name();
		
		log("Date range: " + JSON.stringify(info));
		return info;
	}
	
	function getDetailPlanOfDate(date, useIndex)
	{
		// group by date first
		groupByDate();
		cells = mainView.cells();
		groups = mainView.groups();
		n = groups.length;
		step = cells.length / n;
		
		// find date index
		if(typeof useIndex == "undefined")
			useIndex = false;
		
		if(!useIndex)
		{
			for(i = 0; i < n; i++)
				if(groups[i].name() == date)
				{
					date = i;
					break;
				}
		}
		
		// get detail plan of that date
		var info = {};
		info.date = groups[date].name();
		info.activities = [];
		
		for(j = 0; j < step; j++)
		{
			text = cells[date * step + j].name();
			name = text.substring(0, text.indexOf(","));
			goal = text.substring(text.indexOf(", ") + 1);
			
			activities[j] = {name: name, goal: goal};
		}
		
		log("Detail plan of date: " + JSON.stringify(info));
		return info;
	}
	
	function getDetailPlanOfActivity(activity, useIndex)
	{
		// group by activity first
		groupByActivity();
		cells = mainView.cells();
		n = cells.length;
		
		// find activity index
		if(typeof useIndex == "undefined")
			useIndex = false;
		
		if(!useIndex)
		{
			for(i = 0; i < n; i++)
				if(cells[i].name().indexOf(activity) >= 0)
				{
					activity = i;
					break;
				}
		}
		
		// find start and end index
		cells[activity].tap();
		cells = mainView.cells();
		m = cells.length;
		var start = activity;
		var end = (m - n) + start;
		
		// get information
		str = cells[start].name();
		
		var info = {};
		info.activity = str.substring(0, str.indexOf(","));
		info.total = str.substring(str.indexOf(", ") + 1);
		info.dates = [];
		
		for(i = start + 1; i <= end; i++)
		{
			text = cells[i].name();
			date = text.substring(0, text.indexOf(","));
			goal = text.substring(text.indexOf(", ") + 1);
			
			info.dates[i - start - 1] = {date: date, goal: goal};
		}
		
		cells[start].tap();
		
		// return
		log("Detail plan of activity: " + JSON.stringify(info));
		return info;
	}
	
	function tapEdit()
	{
		
	}
	
	function tapX()
	{
		
	}
	
	function tapV()
	{
		
	}
	
	function tapCancel()
	{
		
	}
	
	function tapDone()
	{
		
	}
	
	function tapSuggest()
	{
		
	}
	
	function tapUndo()
	{
		
	}
	
	function setPlanAmount()
	{
		
	}
}
