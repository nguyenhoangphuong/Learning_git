/*#import "_Navigator.js"
#import "_AlertHandler.js"
#import "_Prometheus.js"
#import "../core/testcaseBase.js"
#import "_TabBar.js"
#import "PlanPicker.js"
#import "PlanBuilder.js"

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

start("Demo");
//listControls(app.mainWindow().scrollViews()[0]);
//logTree();


planBuilder.assignControls();
//planBuilder.setName("Some name");
//log("pick activities");


//planBuilder.pickActivity("running");
//log("picked running");
//planBuilder.pickActivity("leglift");

log("wait a bit and remove");
//wait(4);
planBuilder.removeActivity();



//planPicker.tapCustomPlan();
pass("Demo pass");


