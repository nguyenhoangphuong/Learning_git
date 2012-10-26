#import "MVPLibs.js"

start("Demo");

testProgress()

pass("Demo pass");


function testProgress()
{
	p = new Progress();
	
	/*
	p.isVisible();
	p.getActivities();
	
	p.tapDate(0);
	p.tapDate(1);
	p.tapDate(2);
	p.tapDate(3);
	p.tapDate(4);
	p.tapDate(5);
	p.tapDate(6);
	p.tapToday();
	*/
	
	p.tapActivity(0);
	p.isHistoryRecordsShown();
	p.isNoActivity();
	p.getCurrentHistory();
	p.tapOutside();
	
	p.tapActivity(1);
	p.isHistoryRecordsShown();
	p.getCurrentHistory();
	p.isNoActivity();
	p.tapOutside();
	
	p.tapActivity(2);
	p.isHistoryRecordsShown();
	p.getCurrentHistory();
	p.isNoActivity();
	p.tapOutside();
}

function testPlanner()
{
	p = new Planner();
	p.getActivities();
	/*
	p.isVisible();
	p.groupByActivity();
	p.groupByDate();
	p.getDateRange();
	p.getAllRecordsOfDate("Today");
	p.getAllRecordsOfActivity("Running");
	p.getAllRecordsOfDate("Oct 29");
	p.getAllRecordsOfActivity("Treadmill");
	p.getAllRecordsOfActivity("Running");
	p.getAllRecordsOfActivity("Treadmill");
	 
	p.tapEdit();
	p.tapEditRecord("Oct 29", "Push-up");
	p.tapX();
	p.tapEditRecord("Oct 30", "Running");
	p.setPlanAmount(2.2);
	p.tapV();
	wait();
	p.tapEditRecord("Oct 31", "Swimming");
	p.setPlanAmount(150);
	p.tapX();
	
	p.tapUndo();
	p.tapSuggest();
	p.tapDone();
	 */
}

function debug()
{
	hr();
	listControls(app.mainWindow().scrollViews()[0].scrollViews()[0]);
	hr();
	listControls(app.mainWindow().scrollViews()[0]);
	hr();
	listControls(app.mainWindow());
	logTree();
}

function testTracking()
{
	t = new Tracking();
	t.tapGPS();
	t.getActivitiesInfo();
	t.tapManual();
	t.getActivitiesInfo();
	
	t.tapGPS();
	t.tapActivity("Running");
	log(app.navigationBar().name());
	app.navigationBar().leftButton().tap();
	
	t.tapManual();
	t.tapActivity("Treadmill");
	log(app.navigationBar().name());
	app.navigationBar().leftButton().tap();
	
	t.tapManual();
	t.tapActivity("Elliptical");
	log(app.navigationBar().name());
	app.navigationBar().leftButton().tap();
	
	t.tapManual();
	t.tapActivity("Running");
	log(app.navigationBar().name());
	app.navigationBar().leftButton().tap();
}


