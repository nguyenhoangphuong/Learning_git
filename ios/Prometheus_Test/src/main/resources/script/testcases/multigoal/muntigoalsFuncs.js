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
	nav.toMultiGoalChooser(generateSignupAccount(), "a123456", null);
	wait();
}

function checkBackButtonInTheFirstTime()
{
	var activity = new MultiGoalChooser();
	//check the back button, make sure we get back to user info
	activity.back();
	wait();

	var info = new UserInfo();
	assertTrue(info.isVisible(),"work of back button");
	info.submit();
	wait();

	assertTrue(activity.isVisible(), "Multi goal view is visible again");
	
}

function checkCorrectData()
{
	// get activities
	var multigoalchooser = new MultiGoalChooser();
	var activities = multigoalchooser.getActivities();
	
	// check all activities
	for (i=0 ; i < activities.length ; i++)
	{
		//check swimming
		if (activities[i].Name != "Swimming")
		{
			multigoalchooser.chooseActivityWithName(activities[i].Name);
			// check unit
			wait();
			var isunitcorrect = checkCorrectUnit(activities[i].Unit);
			assertTrue(isunitcorrect, activities[i].Name + " :check unit");
			
			//check range
			var israngecorrect = checkCorrectRange(activities[i]);
			assertTrue(israngecorrect, activities[i].Name + " :check range");
			backToMultiGoalChooserScreen();
			wait();
		}
		
	}
}

function checkCorrectUnit(unitexpected)
{
	var planchooser = new PlanChooser();
	var unitactual = planchooser.getUnit();
/*
	log(">>>>>Unit>>>>");
	log(unitexpected);
	log(unitactual);
	log(">>>>>>>>>");
*/
	
	// validate
	if (unitexpected == "Distance" && (unitactual == "km" || unitactual == "miles" ))
		return true;
	if (unitexpected == "LapsLength" && (unitactual == "ft"))
		return true;
	if (unitexpected == "Count" && (unitactual == "reps"))
		return true;
	if (unitexpected == "Duration" && (unitactual == "secs"))
		return true;
	return false;			
}

function checkCorrectRange(activityexpected)
{
	// get range actual
	var planchooser = new PlanChooser();
	planchooser.selectOther();
	wait();
	var range = planchooser.getPickerRange();
	var rangemin = range.min;
	var rangemax = range.max;
	
	//get plan amount actual
	planchooser.back();
	wait();
	var planamount = planchooser.getPlanAmounts();
	var easyplan = planamount.easy;
	var mediumplan = planamount.normal;
	var hardplan = planamount.active;
/*
	log(">>>>>Range>>>>");
	log(rangemin);
	log(rangemax);
	log(easyplan);
	log(mediumplan);
	log(hardplan);
	log(activityexpected.RangeMin);
	log(activityexpected.RangeMax);
	log(activityexpected.EasyPlan);
	log(activityexpected.MediumPlan);
	log(activityexpected.HardPlan);	
	log(">>>>>>>>>");
*/
	// validate
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




















