#import "../MVPLibs.js"

/*
List of functions:
=========================================================================================
- isVisible()				: check if current view is History
- isNoHistory()				: check if the history is currently empty
=========================================================================================
- getAllDates()				: get the total days and date list, return:
	{
		total: 2,
		dates: ["Wed Oct 02nd", "Wed Oct 03rd"]
	}
- getAllRecordsOfDate(date)	: get all the records of the specified date, return:
	{
		total: 3,
		records:
		[
			{no: 1, activity: "Running", startTime: "12:23 PM", goal: ["00:32:30", 	"1.00 miles"]},
			{no: 1, activity: "Running", startTime: "12:23 PM", goal: ["00:32:30", 	"1.00 miles"]},
			{no: 1, activity: "Running", startTime: "12:23 PM", goal: ["00:32:30", 	"1.00 miles"]},
		]
	}
	note: records[i].goal is an array of multiple values based on each activity type
=========================================================================================
- scrollToPlanner()			: scroll back to Planner view
=========================================================================================
*/

function History()
{
	// Private fields
	var mainWindow = app.mainWindow();
	var table = mainWindow.tableViews()[0];
	
	// Public methods
	this.isVisible = isVisible;
	this.isNoHistory = isNoHistory;
	this.getNumberOfEntries = getNumberOfEntries;
	this.assignControls = assignControls;
	
	function assignControls() {
		table = mainWindow.tableViews()[0];
	}
	// Method definitions
	function isVisible()
	{
		return navigationBar.historyIsVisible();
	}
	
	function isNoHistory()
	{
		exist = staticTextExist("No history");
		log("History is currently have no record: " + exist);
		return exist;
	}
	
	
	function getNumberOfEntries() {
		assignControls();
		log("some messages");
		entries = table.cells();
		
		log("Number of entries= " + entries.length);
		
		
		return entries.length;
	}
	
	
	
}