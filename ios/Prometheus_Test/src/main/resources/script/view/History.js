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

#import "../core/testcaseBase.js"

function History()
{
	// Private fields
	var mainWindow = app.mainWindow();
	var mainView = mainWindow.scrollViews()[0];
	var table = mainView.tableViews()[0];
	
	// Public methods
	this.isVisible = isVisible;
	this.isNoHistory = isNoHistory;
	
	this.getAllDates = getAllDates;
	this.getAllRecordsOfDate = getAllRecordsOfDate;
	
	this.scrollToPlanner = scrollToPlanner;
	
	// Method definitions
	function isVisible()
	{
		exist = staticTextExist("No history") || (table.isVisible() && table.groups().length > 0);
		log("History visible: " + exist);
		return exist;
	}
	
	function isNoHistory()
	{
		exist = staticTextExist("No history");
		log("History is currently have no record: " + exist);
		return exist;
	}
	
	
	function getAllDates()
	{
		groups = table.groups();
		n = groups.length;
		
		var info = {};
		info.total = n;
		info.dates = [];
		
		for(i = 0; i < n; i++)
			info.dates[i] = groups[i].name();
		
		log("HistoryDays.total: " + info.total);
		log("HistoryDays.days: " + info.dates);
		
		return info;
	}
	
	function getAllRecordsOfDate(date)
	{
		// get day list
		groups = table.groups();
		n = groups.length;
		days = [];
		
		for(i = 0; i < n; i++)
			days[i] = groups[i].name();
		
		// get index of date
		index = days.indexOf(date);
		if(index < 0)
		{
			log("No date fix");
			return null;
		}
		
		// find start record and end record 
		cells = table.cells();
		n = cells.length;
		var begins = [];
		count = 0;
		
		for(i = 0; i < n; i++)
		{
			row = cells[i].name().split(", ");

			if(row[0] == "1")
			{
				begins[count] = i;
				count++;
			}
		}
		
		start = begins[index];
		if(index == begins.length - 1)
			end = n;
		else
			end = begins[index + 1];
		
		// return
		var info = {};
		info.total = end - start;
		info.records = []
		
		log("Total records: " + info.total);
		for(i = start; i < end; i++)
		{
			text = cells[i].name();
			texts = text.split(", ");
			
			var r = {};
			r.text = text;
			r.no = parseInt(texts[0]);
			r.activity = texts[1];
			r.startTime = texts[2];
			r.goal = texts.slice(3);
			
			info.records[i] = r;
			/*
			log("Record: [" 
					+ info.records[i].text + "], " 
					+ info.records[i].no + ", " 
					+ info.records[i].activity + ", "
					+ info.records[i].startTime + ", ["
					+ info.records[i].goal + "]");
			*/
		}
		
		return info;
	}
	
	
	function scrollToPlanner()
	{
		wait(0.5);
		table.dragInsideWithOptions({startOffset:{x:0.5, y:0.95}, endOffset:{x:0.5, y:0.5}, duration:0.6});
		log("Scroll to Planner");
	}
}