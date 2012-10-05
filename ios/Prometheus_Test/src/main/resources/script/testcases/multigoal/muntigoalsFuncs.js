#import "../../view/Home.js"
#import "../../view/UserInfo.js"
#import "../../view/PlanChooser.js"
#import "../../view/_Navigator.js"
#import "../../view/MultiGoalChooser.js"
#import "../../view/GoalProgress.js"
#import "../../core/testcaseBase.js"
#import "../../core/common.js"



function goToMultiGoalChooser()
{
	nav.toMultiGoalChooser(generateSignupAccount(), "123456", null);
	wait();
}

function checkBackButtonInTheFirstTime()
{
	var activity = new MultiGoalChooser();
	//check the back button, make sure we get back to user info
	activity.back();
	wait();

	var info = new UserInfo();
	assertTrue(info.isVisible());
	info.submit();
	wait();

	assertTrue(activity.isVisible(), "Multi goal view is not visible again");
	
}

function checkCorrectData()
{
	var multigoalchooser = new MultiGoalChooser();
	var activities = multigoalchooser.getActivities();
	
	// check all activities
	for (i=0 ; i < activities.lenght ; i++)
	{
		multigoalchooser.chooseActivityWithIndex(i);
		checkCorrectUnit(activities[i].Unit);
		checkCorrectRange(activities[i]);
		backToMultiGoalChooserScreen();
	}
}

function checkCorrectUnit(unitexpected)
{
	var planchooser = new PlanChooser();
	var unitactual = planchooser.getUnit();
	
	if (unitexpected == "Distance" && (unitactual == "km" || unitactual == "miles" )
		return true;
	if (unitexpected == "LapsLength" && (unitactual == "ft")
		return true;
	if (unitexpected == "Count" && (unitactual == "reps")
		return true;
	if (unitexpected == "Duration" && (unitactual == "min")
		return true;
	return false;			
}

function checkCorrectRange(activityexpected)
{
	var planchooser = new PlanChooser();
	planchooser.selectOther();
	var range = planchooser.getPickerRange();
	var rangemin = range.min;
	var rangemax = range.max;
	planchooser.back();
	var planamount = planchooser.getPlanAmounts();
	var easyplan = planamount.easy;
	var mediumplan = planamount.medium;
	var hardplan = planamount.active;
	
	if (activityexpected.RangeMin == rangemin &&
		activityexpected.RangeMax == rangemax &&
		activityexpected.EasyPlan == easyplan &&
		activityexpected.MediumPlan == mediumplan &&
		activityexpected.HardPlan == hardplan)
		return true;
	else
		return false;
		
}

function backToMultiGoalChooserScreen()
{
	var planchooser = new PlanChooser();
	planchooser.backToActivity();
}






















