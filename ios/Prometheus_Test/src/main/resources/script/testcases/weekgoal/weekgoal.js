#import "../../view/UserInfo.js"
#import "../../view/_Tips.js"
#import "../../view/SignUp.js"
#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"

/**
 * This test cover: - Weekgoal
 */
var target = UIATarget.localTarget();

gpsRunLittle();


function gpsRunLittle() {
	start("Starting the weekgoal test");
	log("Go to plan chooser");
	goToPlanChooser();
	log("Select active");
	var planChooser = new PlanChooser();
	planChooser.selectActive();
//	wait(10);
	var progress = new GoalProgress();

	wait(2);

	if (!progress.isWeekGoalVisible()) {
		fail("Week goal is not visible");
	}
	
	wait(2);

	progress.scrollToDayGoal();
	wait();
	var miles = 1;
	progress.simulateARunDontStop(miles);
	
	var run = new RunView();
	if (!run.isVisible() || !run.canPause()) {
		fail("Run view is not visible");
	}
	
	var currentInfo1 = run.getCurrentInfo();
	wait(2);
	var currentInfo2 = run.getCurrentInfo();
	if (currentInfo2.duration == currentInfo1.duration) {
		fail("Duration doesnt change : 1: " + currentInfo1.duration + " 2: " + currentInfo2.duration);
	}
	// check pause
	run.pause();
	currentInfo1 = run.getCurrentInfo();
	wait(2);
	currentInfo2 = run.getCurrentInfo();
	if (currentInfo2.duration != currentInfo1.duration) {
		fail("Duration is still changing when paused: 1: " + currentInfo1.duration + " 2: " + currentInfo2.duration);
	}
	
	// check resume
	run.resume();
	currentInfo1 = run.getCurrentInfo();
	wait(2);
	currentInfo2 = run.getCurrentInfo();
	if (currentInfo2.duration == currentInfo1.duration) {
		fail("Duration doesnt change after resume : 1: " + currentInfo1.duration + " 2: " + currentInfo2.duration);
	}
	run.finish();
	if (!run.canDone()) {
		fail("Finished run but cant be done");
	}
	var result = run.getResults();
	
	if ( miles < parseFloat(result.distance)) {
		fail("Miles= " + miles + " - while distance= " + result.distance);
	}
	
	// done and run again
	run.done();
	miles = 25;
	progress.simulateARunDontStop(miles);
	
	
	// make sure there is an alert
	
	if (!run.isCongratulationAlertVisible()) {
		fail("Congratulation alert should have been shown");
	}
	log("Finish the run");
	wait(10);
	run.finish();
	log("finish ok");
	run.done();
	log("done ok");

	// verify set a new goal button is available
	
	progress.scrollToWeekGoal();
	log("ok good");
	wait(2);
	if (!progress.isWeekGoalVisible() && !progress.isSetANewGoalBtnVisible()) {
		fail("Weekgoal or the reset button is not visible");
	}
	progress.setANewGoal("active");
	log("ok good");	
	log("finish");
	
}

function goToPlanChooser() {
	nav.toPlanChooser();
}

