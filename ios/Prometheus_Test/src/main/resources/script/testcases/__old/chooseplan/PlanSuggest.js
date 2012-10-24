#import "../../view/_Navigator.js"


function goToChoosePlan()
{
	nav.toPlanChooser(null, null, null, "Running");
	wait(1);
}

function choosePlan(option)
{
	var pc = new PlanChooser();
	var info = pc.getPlanAmounts();
	var weekgoal;
	
	if (option == 1)
	{
		pc.selectEasy();
		weekgoal = info.easy;
	}
	if (option == 2)
	{
		pc.selectNormal();
		weekgoal = info.normal;
	}
	if (option == 3)
	{
		pc.selectActive();
		weekgoal = info.active;
	}
	if (option == 4)
	{
		pc.selectOther();
		
		pc.back();
		assertTrue(pc.isVisible(), "Back button in edit mode work OK");

		pc.selectOther();			
		pc.setValue(15);
		
		assertEqual(pc.getPickerValue(), 15, "Picker in edit mode work OK");
		
		pc.done();
		
		weekgoal = 15;
	}
	
	assertTrue(pc.isLocationConfirmShown(), "Location Permission alert is shown");

	verifyPlan(weekgoal);
}

function verifyPlan(WeekGoal)
{
	var gp = new GoalProgress();
	tips.closeTips(1);
	gp.scrollToTodaysGoal();
	tips.closeTips(1);
	gp.scrollToPlanner();
	wait(1);

	var goalplan = new GoalPlan();
	var weekinfogoal = goalplan.getTotalPlanAmount();
	weekinfogoal = Math.round(weekinfogoal);

	assertEqual(weekinfogoal, WeekGoal, "WeekGoal is consistent with PlanChooser");
}
