#import "../MVPLibs.js"

/*
List of function:
================================================================================
- assignControls()
- isVisible()
================================================================================
- tapBack()
- tapDelete(confirm)			: tap [Recycle] and confirm the alert
	+ true/undefined			: tap [Yes] when alert is shown
	+ false						: tap [No] when alert is shown
================================================================================
- groupByActivity()				: tap the Activity tab
- groupByDate()					: tap the Date tab
- getPlanInfoByActivity()		: return plan information group by activity
	[
		{name: "Running", goal: "27 miles"}, 
		{name: "Running", goal: "27 miles"}, 
		{name: "Running", goal: "27 miles"}
		
		note: return obj is an array, each element is an object that contain
		information of an activity: name and goal
	]
- getPlanInfoByDate()			: return plan information group by date
	[
		{
			date: "Oct 23", 
			activities: 
			[
				{name: "Running", goal: "27 miles"}, 
				{name: "Running", goal: "27 miles"}, 
				{name: "Running", goal: "27 miles"}
			]
		},
		...
		
		note: return obj is an array, each element is an object that contain
		information of a date: date name and date's activities
	]
================================================================================
- tapGo()						: tap [Go for it]
- tapCustom()					: tap [Custimize]
================================================================================
- isDeletePlanBtnVisible()		: check if the delete plan button is visible
- isDeletePlanAlertShown()		: check if the delete cofirmation is shown
================================================================================
*/

function PlanInfo()
{
	// Private fields
	var window;
	var mainView;

	var backBtn;
	var deleteBtn;
	
	var activityTab;
	var dateTab;
	var recordList;
		
	var goBtn;
	var customBtn;
	
	// Initalize controls
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.tapBack = tapBack;
	this.tapDelete = tapDelete;
	
	this.groupByActivity = groupByActivity;
	this.groupByDate = groupByDate;
	this.getPlanInfoByActivity = getPlanInfoByActivity;
	this.getPlanInfoByDate = getPlanInfoByDate;
	
	this.tapGo = tapGo;
	this.tapCustom = tapCustom;
	
	this.isDeletePlanBtnVisible = isDeletePlanBtnVisible;
	this.isDeletePlanAlertShown = isDeletePlanAlertShown;
	
	// Methods definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window;
		
		backBtn = app.navigationBar().leftButton();
		deleteBtn = app.navigationBar().rightButton();
		
		activityTab = mainView.segmentedControls()[0].buttons()[0];
		dateTab = mainView.segmentedControls()[0].buttons()[1];
		recordList = mainView.tableViews()[0];
		
		goBtn = mainView.buttons()[0];
		customBtn = mainView.buttons()[1];
	}
	
	function isVisible(name)
	{
		// check visible implicit
		if(typeof name == "undefined")
		{
			visible = goBtn.isVisible() && goBtn.name() == "Go for it";
			
			log("PlanInfo is visible: " + visible);	
			return visible;
		}
		
		// check visible explicit
		visible = app.navigationBar().name() == name;
		
		log("PlanInfo <" + name + "> is visible: " + visible);	
		return visible;
	}
	
	function tapBack()
	{
		backBtn.tap();
		log("Tap [Back]");
	}
	
	function tapDelete(confirm)
	{
		// if not visible
		if(!isDeletePlanBtnVisible())
		{
			log("No [Delete Plan] button");
			return;
		}
			
		// set up alert choice
		if(typeof confirm == "undefined")
			confirm = true;
		alert.alertChoice = confirm ? "YES" : "NO";
		
		// trigger
		deleteBtn.tap();
		log("Tap [Delete]");
		
		// wait for and handle alert
		wait();
	}
	
	function groupByActivity()
	{
		activityTab.tap();
		log("Tap [Activity]");
	}
	
	function groupByDate()
	{
		dateTab.tap();
		log("Tap [Date]");
	}
	
	function getPlanInfoByActivity()
	{
		// group by activity first
		groupByActivity();
		
		// get info
		var info = [];
		cells = recordList.cells();
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
	
	function getPlanInfoByDate()
	{
		// group by date first
		groupByDate();
		
		// get info
		var info = [];
		cells = recordList.cells();
		step = cells.length / 7;
		
		for(i = 0; i < 7; i++)
		{
			date = cells[i * step + 0].name();
			activities = [];
			
			for(j = 1; j < step; j++)
			{
				text = cells[i * step + j].name();
				name = text.substring(0, text.indexOf(","));
				goal = text.substring(text.indexOf(", ") + 1);
				
				activities[j - 1] = {name: name, goal: goal};
			}
			
			info[i] = {date: date, activities: activities};
		}
		
		log("Plan info by date: " + JSON.stringify(info));
		return info;
	}
	
	function tapGo()
	{
		goBtn.tap();
		log("Tap [GoForIt]");
	}
	
	function tapCustom()
	{
		customBtn.tap();
		log("Tap [Customize]");
	}
	
	function isDeletePlanBtnVisible()
	{
		visible = deleteBtn.isValid() && deleteBtn.isVisible();
		
		log("[Delete Plan] is visible: " + visible);
		return visible;
	}
	
	function isDeletePlanAlertShown()
	{
		shown = alert.alertTitle != null && alert.alertTitle == alert.Warning && alert.alertMsg == alert.DeletePlanConfirm;
		alert.reset();
		
		log("Alert [delete plan confirm] is shown: " + shown);
		return shown;
	}
}
