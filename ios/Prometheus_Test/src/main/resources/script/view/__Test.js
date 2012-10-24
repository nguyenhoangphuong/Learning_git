#import "MVPLibs.js"

function testPlanPicker()
{
	pp = new PlanPicker();
	
	pp.isVisible();
	pp.getCustomPlans();
	pp.pickPlan("Normal", "Weight Loss");
	
	target.frontMostApp().navigationBar().leftButton().tap();
	
	pp.tapCustomPlan();
}

function testPlanInfo()
{
	pi = new PlanInfo();
	
	pi.isVisible();
	pi.isVisible("Personal #1");
	pi.isVisible("Personal #2");
	pi.groupByDate();
	//pi.groupByActivity();
	pi.getPlanInfoByActivity();
	pi.getPlanInfoByDate();
	
	pi.isDeletePlanBtnVisible();
	pi.isDeletePlanAlertShown();
}

function testTracking()
{
	t = new Tracking();
	t.tapGPS();
	t.getActivitiesInfo();
	t.tapManual();
	t.getActivitiesInfo();
	t.tapCancel();
	t.tapActivity("Running");
}

start("Demo");
listControls(app.mainWindow().scrollViews()[0]);
listControls(app.mainWindow());
logTree();
pass("Demo pass");
