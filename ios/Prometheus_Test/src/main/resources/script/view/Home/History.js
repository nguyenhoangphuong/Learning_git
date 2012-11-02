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
- getNumberOfEntries()
=========================================================================================
*/

function History()
{
	// Private fields
	var window;
	var mainView;
	
	var dateTab;
	var activityTab;
	
	// Initalize
	assignControls();
	
	// Public methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.groupByDate = groupByDate;
	this.groupByActivity = groupByActivity;
	
	this.isNoHistory = isNoHistory;
	this.getNumberOfEntries = getNumberOfEntries;
	
	// Method definitions
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window.tableViews()[0];
		
		dateTab = app.navigationBar().segmentedControls()[0].buttons()[0];
		activityTab = app.navigationBar().segmentedControls()[0].buttons()[1];
	}
	
	function isVisible()
	{
		visible = navigationBar.historyIsVisible();
		log("History is visible: " + visible);
		return visible;
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
	
	
	function isNoHistory()
	{
		exist = staticTextExist("No History") || staticTextExist("No history");
		log("History is currently have no record: " + exist);
		return exist;
	}
	
	function getNumberOfEntries() 
	{
		entries = mainView.cells();
		
		log("Number of entries: " + entries.length);
		return entries.length;
	}
}
