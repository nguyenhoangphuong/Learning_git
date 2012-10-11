#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"

// ===================== Navigate =======================================
function initStartView()
{
	// to history
	nav.toHistory(null, null, null, "Running", 10);
}

function goFromStartToPlanner()
{
	// go to planner
	tg = new GoalProgress();
	tg.scrollToPlanner();
}

function goFromPlannerToHistory()
{
	// go to history
	p = new GoalPlan();
	p.scrollToHistory();
}

function goFromHistoryToStart()
{
	// go to start
	h = new History();
	h.scrollToPlanner();
	
	p = new GoalPlan();
	p.scrollToTodaysGoal();
}

function resetPlan(activity, amount)
{
	// go to settings and press reset plan
	h = new History();
	h.scrollToPlanner();
	
	p = new GoalPlan();
	p.scrollToTodaysGoal();
	
	tg = new GoalProgress();
	tg.scrollToSettings();
	
	s = new Settings();
	s.resetPlan(true);
	wait()
	
	// choose an activity and its amount
	a = new MultiGoalChooser();
	a.chooseActivityWithName(activity);
	
	pc = new PlanChooser();
	pc.selectOther();
	pc.setValue(amount);
	pc.done();
	wait();
}

// ===================== Add records ====================================
function addRecordByManual(e)
{
	/*
	e.names = ["Start time", "Duration", "Distance (miles)"];
	e.ranges =
	{
		mins: null, 20, 10
		maxs: null, 70, 50
	}
	e.record = 
	[
		[10, 15, "AM"],
		[0, 30, 00],
		[1]
	]
	e.text =
	[
		"10:15 AM",
		"00:30:00",
		"1"
	]
	*/
	
	// to manual tracking view
	tg = new GoalProgress();
	tg.start(2);
	
	// do some verify
	verifyManualFieldsName(e.names);
	
	// add record's fields and done
	mt = new ManualTracking();
	for(i = 0; i < e.record.length; i++)
	{
		// tap field
		mt.tapField(e.names[i]);
		wait(0.5);
		
		// check range
		verifyManualFieldsRange(e.ranges[i]);
		
		// input values
		if(e.record[i] != null)
		{
			mt.setField(e.record[i]);
			mt.tapDone();
			wait(0.5);
		}
	}
	
	// verify input work
	verifyManualInputWork(e.text);
	
	// done
	mt.done();
	wait(0.5);
}

// ==================== Verify manual input =============================
function verifyManualFieldsName(names)
{
	mt = new ManualTracking();
	fields = mt.getFieldsInfo();
	
	assertEqual(fields.total, names.length, "Total field is correct");
	for(i = 0; i < fields.total; i++)
		assertEqual(fields.names[i], names[i], "Field " + names[i] + " is correct");
}

function verifyManualFieldsRange(range)
{
	mt = new ManualTracking();
	fr = mt.getFieldRanges();
	
	assertEqual(fr.mins.length, range.length, "Total column is same for this field");
	for(i = 0; i < range.total; i++)
	{
		if(range.mins[i] != null)
			assertEqual(fr.mins[i], range.mins[i], "Min value is correct");
		if(range.maxs[i] != null)
			assertEqual(fr.maxs[i], range.maxs[i], "Max value is correct");
	}
}

function verifyManualInputWork(text)
{
	mt = new ManualTracking();
	info = mt.getFieldsInfo();
	
	for(i = 0; i < info.total; i++)
		if(text[i] != null)
			assertEqual(info.values[i], text[i], "Record is display correctly")
}

// ==================== Verify goal plan history ========================
function verifyNoHistory()
{
	h = new History();
	
	assertTrue(h.isNoHistory(), "No History");
}

function verifyDateConsitent(e)
{
	// e = ["Oct 09", "Oct 10", "Oct 11", ...]
	h = new History();
	
	dates = h.getAllDates();
	n = dates.length;
	
	for(i = 0; i < n; i++)
	{
		date = dates[i].substring(date[i].indexOf(" ") + 1);
		assertTrue(e.indexOf(date) >= 0, "Date " + dates[i] + " is in date list");
	}
}

function verifyNoRecordInFutureDay(e)
{
	// e = {today: "Wed Oct 10th", tomorrow: "Thu Oct 11th"} 
	h = new History();
	
	todayrecord = h.getAllRecordsOfDate(e.today);
	assertTrue(todayrecord.total >= 0, "Today's total record is >= 0");
	
	tomorrowRecord = h.getAllRecordsOfDate(e.tomorrow);
	assertTrue(tomorrowRecord == null, "No history for tomorrow");
}

function verifyTotalNumberOfRecords(e)
{
	// e = {date: "Wed Oct 09th", expectNumber: 3}
	h = new History();
	
	rec = h.getAllRecordsOfDate(e.date);
	assertEqual(rec.total, e, "Total record is correct");
}

function verifyRecordsOfDate(e)
{
	// e = {date: "Wed Oct 09th", expectRecords: [{no: 1, activity: "Running", startTime: "12:23 PM", goal: ["00:32:30", "1.00 miles"]}, ... ]}
	h = new History();
	
	rec = h.getAllRecordsOfDate(e.date);
	actual = rec.records;
	expect = e.expectRecords;
	
	for(i = 0; i < rec.total; i++)
	{
		assertEqual(actual[i].no, expect[i].no, "Record number is correct");
		assertEqual(actual[i].activity, expect[i].activity, "Record activity is correct");
		assertEqual(actual[i].startTime, expect[i].startTime, "Record start time is correct");
		assertEqual(actual[i].goal, expect[i].goal, "Record goal is correct");
	}
}