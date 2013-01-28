#import "../MVPLibs.js"

/*
List of functions:
=========================================================================================
- assignControls()
- isVisible()
=========================================================================================
- groupByDate()						: tap Date tab
- groupByActivity()					: tap Activity tab
=========================================================================================
- getDateRange()					: get all dates [Oct 25, Oct 26, ...]
- getActivities()					: get all activities [Running, Swimming, Push-up]
- getAllRecordsOfDate(date)			: get all records of date 
									  [{name: Running, value: 0.47 miles}, {}, ...]
- getAllRecordsOfActivity(name)		: get all records of activity
									  {name: Running, total: 4.00 miles,
									   dates: [{date: Oct 25, value: 0.47 miles}, {}, ...]}
=========================================================================================
- tapEdit()							: tap Edit button
- tapEditRecord(date, activity)		: tap a record's edit button
									  ex: tapEditRecord("Oct 25", "Running");
- tapX()							: tap X to cancel edit a record
- tapV()							: tap V to confirm a edit
- tapCancel()						: tap Cancel
- tapDone()							: tap Done
- tapUndo()							: tap Undo
- tapSuggest()						: tap Suggest
=========================================================================================
- isInEditMode()					: check if Planner is in edit mode
- setPlanAmount(amount, epsilon)	: set amount for the current selected record
	+ amount: the amount to set
	+ epsilon: error range (0 is default) (because some activities like swimming have 
		large scale, therefor a small drag will change value significantly)
=========================================================================================
*/

function Planner()
{
	// Private fields
	var window;
	var mainView;
	
	var dateTab;
	var dateTable;
	var activityTab;
	var activityTable;

	var undoBtn;
	var autoBtn;
	
	// Initalize
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.groupByDate = groupByDate;
	this.groupByActivity = groupByActivity;
	
	this.getDateRange = getDateRange;
	this.getActivities = getActivities;
	this.getAllRecordsOfDate = getAllRecordsOfDate;
	this.getAllRecordsOfActivity = getAllRecordsOfActivity;
	
	this.tapEdit = tapEdit;
	this.tapEditRecord = tapEditRecord;
	this.tapX = tapX;
	this.tapV = tapV;
	this.tapCancel = tapCancel;
	this.tapDone = tapDone;
	this.tapSuggest = tapSuggest;
	this.tapUndo = tapUndo;
	
	this.isInEditMode = isInEditMode;
	this.setPlanAmount = setPlanAmount;

	// Methods definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window;
		
		dateTab = app.navigationBar().segmentedControls()[0].buttons()[0];
		dateTable = window.tableViews()[1];
		activityTab = app.navigationBar().segmentedControls()[0].buttons()[1];
		activityTable = window.tableViews()[0];
		
		undoBtn = window.buttons()[0];
		autoBtn = window.buttons()[1];
	}
	
	function isVisible()
	{
		visible = navigationBar.historyIsVisible();
		
		log("Planner is visible: " + visible);
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
	
	
	function getDateRange()
	{
		// group by date first
		groupByDate();
		
		// get range;
		groups = dateTable.groups();
		var info = [];
		
		for(i = 0; i < groups.length; i++)
			info[i] = groups[i].name();
		
		log("Date range: " + JSON.stringify(info));
		return info;
	}
	
	function getActivities()
	{
		// group by date first
		groupByActivity();
		
		// get range;
		var texts = mainView.scrollViews()[0].staticTexts();
		var total = texts.length / 3;
		
		var info = [];
		for(var i = 0; i < total; i++)
		{
			text = texts[i * 3 + 2].name();
			info[i] = text;
		}
		
		log("Activities: " + JSON.stringify(info));
		return info;
	}
	
	function getAllRecordsOfDate(date)
	{
		// group by date first
		groupByDate();
		
		// var
		i = 0;
		cells = dateTable.cells();
		groups = dateTable.groups();
		n = groups.length;
		step = cells.length / n;
		
		// find date index
		for(i = 0; i < n; i++)
			if(groups[i].name().indexOf(date) >= 0)
			{
				date = i;
				break;
			}
		
		// find start and end index
		start = date * step;
		end = start + step;
		
		// get info
		var info = [];
		for(i = start; i < end; i++)
		{
			text = cells[i].name();
			name = text.substring(0, text.indexOf(","));
			value = text.substring(text.indexOf(", ") + 2);
				
			info[i - start] = {name: name, value: value};
		}

		log("Plan info by date: " + JSON.stringify(info));
		return info;
	}
	
	function getAllRecordsOfActivity(activity)
	{
		// group by activity first
		groupByActivity();
		
		// var
		cells = activityTable.cells();
		n = cells.length;
		
		// find activity index
		for(i = 0; i < n; i++)
			if(cells[i].name().indexOf(activity) >= 0)
			{
				activity = i;
				break;
			}
		
		// find start and end index
		n = expandActivity(activity);
		cells = activityTable.cells();
		m = cells.length;
		
		var start = activity;
		var end = (m - n) + start;
		
		log(m + "-" + n);
		
		// get information
		str = cells[start].name();
		
		var info = {};
		info.name = str.substring(0, str.indexOf(","));
		info.total = str.substring(str.indexOf(", ") + 2);
		info.dates = [];
		
		for(i = start + 1; i <= end; i++)
		{
			text = cells[i].name();
			date = text.substring(0, text.indexOf(","));
			value = text.substring(text.indexOf(", ") + 2);
			
			info.dates[i - start - 1] = {date: date, value: value};
		}
		
		// return
		log("Detail plan of activity: " + JSON.stringify(info));
		return info;
	}
	
	
	function tapEdit()
	{
		if(!isInEditMode())
			app.navigationBar().rightButton().tap();
		
		log("Tap [Edit]");
		
		// close tips
		if(tips.isTipsDisplay("Planner"))
			tips.closeTips();
	}
	
	function tapEditRecord(date, activity)
	{
		index = -1;
		
		if(app.navigationBar().name() == "Date")
		{
			// var
			cells = dateTable.cells();
			groups = dateTable.groups();
			n = groups.length;
			
			// find date index
			for(i = 0; i < n; i++)
				if(groups[i].name().indexOf(date) >= 0)
				{
					date = i;
					break;
				}
			
			// find start and end index
			step = cells.length / n;
			start = date * step;
			end = start + step;
			
			// get info
			for(i = start; i < end; i++)
				if(cells[i].name().indexOf(activity) >= 0)
				{
					index = i;
					break;
				}
		}
		else
		{		
			// var
			cells = activityTable.cells();
			n = cells.length;
			
			// find activity index
			for(i = 0; i < n; i++)
				if(cells[i].name().indexOf(activity) >= 0)
				{
					activity = i;
					break;
				}
			
			// find start and end index
			n = expandActivity(activity);
			cells = activityTable.cells();
			m = cells.length;
			
			var start = activity;
			var end = (m - n) + start;
			
			// get information			
			for(i = start + 1; i <= end; i++)
				if(cells[i].name().indexOf(date) >= 0)
				{
					index = i;
					break;
				}
		}
		
		cells[index].scrollToVisible();
		cells[index].buttons()[1].tap();
	}
	
	function tapX()
	{
		if(!isInEditMode())
			return;
		
		cells = (app.navigationBar().name() == "Date" ? dateTable.cells() : activityTable.cells());
		for(i = 0; i < cells.length; i++)
			if(cells[i].buttons().length > 1)
			{
				cells[i].scrollToVisible();
				cells[i].buttons()[1].tap();
				log("Tap [X]");
				return;
			}
		
		log("Error: TapX");
	}
	
	function tapV()
	{
		if(!isInEditMode())
			return;
		
		cells = (app.navigationBar().name() == "Date" ? dateTable.cells() : activityTable.cells());
		for(i = 0; i < cells.length; i++)
			if(cells[i].buttons().length > 1)
			{
				cells[i].scrollToVisible();
				cells[i].buttons()[2].tap();
				log("Tap [V]");
				return;
			}
		
		log("Error: TapV");
	}
	
	function tapCancel()
	{
		// ui missing
	}
	
	function tapDone()
	{
		if(!isInEditMode())
			return;
		
		done = app.navigationBar().rightButton();
		done.tap();
		log("Tap [Done]");
	}
	
	function tapSuggest()
	{
		if(!isInEditMode())
			return;
		
		autoBtn.tap();
		log("Tap [Auto-suggest]");
	}
	
	function tapUndo()
	{
		if(!isInEditMode())
			return;
		
		undoBtn.tap();
		log("Tap [Undo]");
	}
	
	
	function isInEditMode()
	{
		editmode = !dateTab.isVisible();
		
		log("In edit mode: " + editmode);
		return editmode;
	}
	
	function setPlanAmount(amount, epsilon)
	{		
		if(!isInEditMode())
			return;
		
		if(typeof epsilon == "undefined")
			epsilon = 0;
		
		index = -1;
		cells = (app.navigationBar().name() == "Date" ? dateTable.cells() : activityTable.cells());
		for(i = 0; i < cells.length; i++)
			if(cells[i].buttons().length > 1)
			{
				index = i;
				break;
			}
		
		// drag until
		cells[index].scrollToVisible();
		current = parseFloat(cells[i].name());
		if(current > amount)
			end = 0.57;
		else
			end = 0.43;
		
		while(true)
		{
			current = parseFloat(cells[i].name());
			if(Math.abs(current - amount) <= epsilon)
				break;
			
			cells[index + 1].dragInsideWithOptions({startOffset:{x:0.5, y:0.5}, endOffset:{x:end, y:0.5}, duration:0.1});
		}
	}
	
	function expandActivity(start)
	{
		// original
		cells = activityTable.cells();
		n = cells.length;
		
		// after tap
		cells[start].scrollToVisible();
		cells[start].tap();
		cells = activityTable.cells();
		m = cells.length;
	
		if(m < n)
		{
			cells[start].scrollToVisible();
			cells[start].tap();
		}
		
		return m > n ? n : m;
	}
}
