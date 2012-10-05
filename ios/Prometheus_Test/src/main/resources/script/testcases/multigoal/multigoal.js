#import "../../view/Home.js"
#import "../../view/UserInfo.js"
#import "../../view/PlanChooser.js"
#import "../../view/_Navigator.js"
#import "../../view/MultiGoalChooser.js"
#import "../../view/GoalProgress.js"
#import "../../core/testcaseBase.js"


/**
This test verifies that user can:
- Choose different activities and goals 
- Units of activities are different
- User can reset the activity to choose another one
*/

start("Start a test");

var nav = new Navigator();
//var info = {100, 0.1, 1, 0.75, 20, "male", "us"};
nav.toMultiGoalChooser(generateSignupAccount(), "123456", null);
wait();


var activity = new MultiGoalChooser();

assertTrue(activity.isVisible(), "Multi goal view is not visible");

//check the back button, make sure we get back to user info
activity.back();
wait();

var info = new UserInfo();
assertTrue(info.isVisible());
info.submit();
wait();

assertTrue(activity.isVisible(), "Multi goal view is not visible again");


activity.chooseActivityWithIndex(0);
var plan = new PlanChooser();
assertTrue(plan.isVisible(), "Multi goal view is not visible again");
//TODO get different unit
var unit = plan.getUnit();
log("Unit= " + unit)
assertTrue(plan.getUnit() == "reps", "Unit is wrong: expected reps, got " + plan.getUnit());

plan.selectEasy();
wait(3);
var goal = new GoalProgress();
goal.scrollToSettings();

var settings = new Settings();
settings.resetPlan("yes");

pass("Test done");




