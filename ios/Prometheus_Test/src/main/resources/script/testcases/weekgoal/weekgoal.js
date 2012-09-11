#import "../../view/UserInfo.js"
#import "../../view/_Tips.js"
#import "../../view/SignUp.js"
#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"

/**
 * This test cover: - Weekgoal
 */

var target = UIATarget.localTarget();

start("Starting the weekgoal test");

pass("This too shall pass");

log("Go to plan chooser");
goToPlanChooser();
log("Select active");
var planChooser = new PlanChooser();
planChooser.selectActive();
wait(3);
var progress = new GoalProgress();
if (progress.isWeekGoalVisible) {
	fail("Week goal is not visible");
}
wait(2);
tips.closeTips(1);
wait();
var weekInfo = progress.getWeekInfo();
log("Week goal= " + weekInfo.text);
if (weekInfo.goal != planChooser.active) {
	fail("Chosen: " + planChooser.active + " - got: " + weekInfo.goal);
}


var goal = new GoalProgress();
goal.scrollToDayGoal();
wait();

goal.simulateARun(3);
var run = new RunView();
var info = run.getResults();
log("results:  " + info);


function testRunning() {
	var goal = new GoalProgress();
	goal.simulateARun(3);
}


function goToPlanChooser() {
	nav.toPlanChooser();
}

