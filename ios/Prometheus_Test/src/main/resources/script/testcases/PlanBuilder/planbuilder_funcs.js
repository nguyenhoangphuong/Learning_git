#import "../../MVPLibs.js"

// ------------ Navigate
function GoToPlanPicker()
{
	// try out with default user info
	nav.toPlanPicker(null, null, null);
}

//------------ Verify
function VerifyPrepackagePlanInfo(type, name, expect) {
	// pick plan
	pp = new PlanPicker();
	pp.pickPlan(type, name);
	
	// get plan info
	pi = new PlanInfo();
	actual = pi.getPlanInfo();
	
	// check default value plan info
	for(i = 0; i < actual.length; i++) {
		assertEqual(actual[i].name, expect[i].name, "Activity name");
		assertEqual(actual[i].value, expect[i].value, "Activity goal");
	}
	
	// tap custom plan
	pi.tapCustom();
	
	// check default value for plan builder
	// .. maybe imposible
	
	// restore state
	pi.tapBack();
}

function VerifyCustomPlanList(expect)
{
	// get all plans
	pp = new PlanPicker();
	actual = pp.getCustomPlans();
	
	// compare
	for(i = 0; i < actual; i++)
		assertEqual(actual[i], expect[i], "Custom plan name");
}

function VerifyCreatePlan()
{
	// tap create
	
	// check drag drop default value
	
	// check null name
	
	// set value and submit
	
	// check plan is saved correctly
	
	// reset plan
	
	// check custom list is updated
	
	// tap on that plan
	
	// check value is saved correctly
	
	// restore state
}

function VerifyDeletePlan()
{
	// tap existed custom plan
	
	// check delete button exist
	
	// press delete and tap no
	
	// check alert
	
	// press delete and tap yes
	
	// check custom list is updated
}