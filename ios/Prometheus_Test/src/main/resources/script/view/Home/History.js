#import "../MVPLibs.js"

/*
List of functions:
=========================================================================================
- assignControls()
- isVisible()
=========================================================================================
- groupByDate()				: tap Date tab
- groupByActivity()			: tap Activity tab
=========================================================================================
- isNoHistory()				: check if the history is currently empty
- getDateRange()			: get all dates [Oct 24, Oct 25, ...]
- getAllRecordsOfDate()		: get all records of a specified date
- getAllRecordsOfActivity()	: get all records of a specified activity
=========================================================================================
*/

function History()
{
	// Private fields
	var mainWindow;
	var mainView;
	
	var dateTab;
	var activityTab;
	var recordList;
	
	// Initalize
	//assignControls();
	
	// Public methods
	//this.assignControls = assignControls;
	this.isVisible = isVisible;
	this.isNoHistory = isNoHistory;
	this.getDateRange = getDateRange;
	this.getAllRecordsOfDate = getAllRecordsOfDate;
	this.getAllRecordsOfActivity = getAllRecordsOfActivity;
	
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
