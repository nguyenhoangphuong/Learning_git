#import "../../view/MVPLibs.js"


// ==============  navigate  ==================== 
function GotoPlanPicker()
{
	nav.toPlanPicker(null, null, null);
}

function GoToPlanBuilder()
{
	GotoPlanPicker();
	var planpicker = new PlanPicker();
	planpicker.tapCustomPlan();
}

function GotoCustomizePlanInfo()
{
	nav.toPlanInfo(null, null, null, pinfodefault, null)
	var planinfo = new PlanInfo();
	planinfo.tapCustom();
}


// ==============================================

// ===================== Test function =========================


function VerifyNameNotNull()
{
	print("<Verify Name not null>");
	var planbuilder = new PlanBuilder();
	planbuilder.setName(""); 	// set name is ""
	planbuilder.start();		// tap start
	wait(1);
	assertTrue(planbuilder.isVisible(),"Name of plan null ?") 
}

function VerifyAddActivity()
{
	print("<Add activity tab>");
	var planbuilder = new PlanBuilder();
	var numofactivities;
	
	//add 1 activity
	planbuilder.pickActivity(0,true);	
	numofactivities = planbuilder.getNumberOfActivities();
	assertEqual(numofactivities,1,"only one activity");
	
	//add one more activity
	planbuilder.pickActivity(1,true);	
	numofactivities = planbuilder.getNumberOfActivities();
	assertEqual(numofactivities,2,"2 activity");
	
	//add one more activity
	planbuilder.pickActivity(2,true);	
	numofactivities = planbuilder.getNumberOfActivities();
	assertEqual(numofactivities,3,"3 activity");
	
	//add one more activity
	planbuilder.pickActivity(3,true);	
	numofactivities = planbuilder.getNumberOfActivities();
	assertEqual(numofactivities,3,"just 3 activity");
	
}

function VerifyRemoveActivity()
{
	print("<Remove activities>");
	var planbuilder = new PlanBuilder();
	var numofactivities;
	
	//add 1 activity
	planbuilder.removeActivity(0,true);	
	numofactivities = planbuilder.getNumberOfActivities();
	assertEqual(numofactivities,2,"two activity");
	
	//add one more activity
	planbuilder.removeActivity(0,true);	
	numofactivities = planbuilder.getNumberOfActivities();
	assertEqual(numofactivities,1,"only one activity");
	
	//add one more activity
	planbuilder.removeActivity(0,true);	
	numofactivities = planbuilder.getNumberOfActivities();
	assertEqual(numofactivities,0,"no activity");
	
}

