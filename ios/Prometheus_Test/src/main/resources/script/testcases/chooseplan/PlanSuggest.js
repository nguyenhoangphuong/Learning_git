#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"
#import "../../view/PlanChooser.js"
#import "../../core/common.js"




function goToChoosePlan()
{
	var nav = new Navigator();
	nav.toPlanChooser();
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
		pc.selectOther(15,"yes");
		weekgoal = 15;
	}
	if(pc.isLocationConfirmShown())
		pass("location shown");
	verifyPlan(weekgoal);
}

function verifyPlan(WeekGoal)
{
	var gp = new GoalProgress();
	var weekinfo = gp.getWeekInfo();
	gp.scrollToGoalPlan();	
	wait(1);

	var goalplan = new GoalPlan();
	var weekinfogoal = goalplan.getTotalPlanMiles();

	if (weekinfogoal == WeekGoal)
		pass("pass " + WeekGoal.toString());
	else
		fail("fail " + WeekGoal.toString());
}

function exit()
{
	var gp = new GoalProgress();
	gp.scrollToDayGoal();
	gp.scrollToAbout();
	var ab = new About();
	ab.resetPlan(true);
}
