#import "../../view/MVPLibs.js"

// ============================= Navigate
function GoToPlanPicker()
{
	// try out with default user info
	nav.toPlanPicker(null, null, null);
}

// ============================= Verify
function VerifyPlanInfo(type, name, expect)
{
	hr();
	
	// pick plan
	pp = new PlanPicker();
	pp.pickPlan(type, name);
	
	// get plan info
	pi = new PlanInfo();
	assertTrue(pi.isVisible(), "PlanInfo is visible");
	actual = pi.getPlanInfo();
	
	// check default value plan info
	for(i = 0; i < actual.length; i++) 
	{
		assertEqual(actual[i].name, expect[i].name, "Activity name");
		assertEqual(actual[i].value, expect[i].value, "Activity goal");
	}
	
	// restore state
	pi.tapBack();
}

function VerifyCustomPlanList(expect)
{
	hr();
	
	// get all plans
	pp = new PlanPicker();
	actual = pp.getCustomPlans();
	
	// compare
	for(i = 0; i < actual; i++)
		assertEqual(actual[i], expect[i], "Custom plan name");
}

function VerifyCreatePlan(pinfo)
{
	hr();
	
	// tap create
	pp = new PlanPicker();
	pp.tapCustomPlan();
	
	pb = new PlanBuilder();
	assertTrue(pb.isVisible(), "PlanBuilder is visible");
	
	// set name, activities, value and submit
	pb.setName(pinfo.name);
	
	var n = pinfo.activities.length;
	for(act = 0; act < n; act++)
	{
		name = pinfo.activities[act];
		goal = pinfo.values[act];
		
		pb.pickActivity(name);
		pb.setActivityGoal(name, goal);
	}
	
	pb.start();
	wait(2);
	
	// check plan is saved correctly
	p = new Progress();
	assertTrue(p.isVisible(), "Current view is Progress");
	tabBar.tapPlanner();
	
	p = new Planner();
	acts = p.getActivities();
	assertEqual(acts.length, pinfo.activities.length, "Total plan is the same");
	
	for(i = 0; i < n; i++)
	{
		expectName = pinfo.activities[i];
		assertTrue(acts.indexOf(expectName) >= 0, "Activity name is correct");
		
		expectGoal = pinfo.goals[i];
		actualGoal = p.getAllRecordsOfActivity(expectName).total;
		assertEqual(actualGoal, expectGoal, "Total goal is correct");
	}
	
	// reset plan
	tabBar.tapSettings();
	s = new Settings();
	s.resetPlan();
	wait(2);
	
	// check custom list is updated
	pp = new PlanPicker();
	assertTrue(pp.isVisible(), "At PlanPicker after reset");
	customs = pp.getCustomPlans();
	assertTrue(customs.indexOf(pinfo.name) >= 0, "Custom list is updated");
	
	// tap on that plan
	pp.pickPlan("Custom", pinfo.name);
	
	// check value is saved correctly
	pi = new PlanInfo();
	actual = pi.getPlanInfo();
	
	// check default value plan info
	for(i = 0; i < actual.length; i++) 
	{
		expectName = pinfo.activities[i];
		expectGoal = pinfo.goals[i];
		
		assertEqual(actual[i].name, expectName, "Activity name");
		assertEqual(actual[i].value, expectGoal, "Activity goal");
	}
		
	// restore state
	pi.tapBack();
}
