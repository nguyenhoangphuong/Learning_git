#import "planPicker_funcs.js"

// ========================== Test Data ==============================
custom1 = 
{
	name : "Custom 1",
	activities : ["Swimming"],
	values : ["1250"],
	goals : ["1250.00 ft"]
}

custom2 = 
{
	name : "Custom 2",
	activities : ["Running", "Cycling"],
	values : ["20", "80"],
	goals : ["20.00 miles", "80.00 miles"]
}

custom3 = 
{
	name : "Custom 3",
	activities : ["Running", "Swimming", "Treadmill"],
	values : ["20", "1250", "5"],
	goals : ["20.00 miles", "1250.00 ft", "5.00 miles"]
}

// ========================== Test Logic =============================
start("PlanPicker, PlanInfo testcases")

log("1. Go to plan picker");
GoToPlanPicker();

log("2. Verify prepackage plans");
VerifyPlanInfo("Easy", "The Starter’s Plan", planBuilderData.prepackage.theStartersPlan);
VerifyPlanInfo("Normal", "The Mover’s Plan", planBuilderData.prepackage.theMoversPlan);
VerifyPlanInfo("Active", "The Shaker’s Plan", planBuilderData.prepackage.theShakersPlan);

log("3. Verify custom plan")
list = [];

list.push(custom1.name);
VerifyCreatePlan(custom1);
VerifyCustomPlanList(list);

list.push(custom2.name);
VerifyCreatePlan(custom2);
VerifyCustomPlanList(list);

list.push(custom3.name);
VerifyCreatePlan(custom3);
VerifyCustomPlanList(list);

pass();