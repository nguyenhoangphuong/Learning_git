#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"

// ===================== Navigate =======================================
function initStartView()
{
	// to history
	nav.toHistory(null, null, null, "Running", 10);
}

function goFromStartToHistory()
{
	// go to history
	tg = new GoalProgress();
	tg.scrollToPlanner();
	
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

function addRecordByGPS()
{
	tg = new GoalProgress();
	
}

function addRecordByManual()
{
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

// ==================== Verify ==========================================
function verifyNoHistory()
{
	h = new History();
	
	assertTrue(h.isNoHistory(), "No History");
}

function verifyDateConsitent(e)
{
	// e = ["Oct 09th", "Oct 10th", "Oct 11th", ...]
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