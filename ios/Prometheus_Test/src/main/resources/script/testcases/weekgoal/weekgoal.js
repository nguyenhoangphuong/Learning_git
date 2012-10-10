#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"

/**
 * This test cover: - Weekgoal
 */

runTest();


function runTest() 
{
	start("Starting the weekgoal test");
	// -------------------------------------------------------------------
	
	// to today view
	//nav.toTodaysGoal(null, null, null, "Running", 20);

	// run by gps
	progress = new GoalProgress();

	var miles = 1;
	progress.simulateARunDontStop(miles);
	
	// check view visible
	var run = new RunView();
	assertTrue(run.isVisible() && run.canPause(), "Run view is visible and can pause");
	
	// check duration is changed
	var currentInfo1 = run.getCurrentInfo();
	wait(2);
	var currentInfo2 = run.getCurrentInfo();
	assertFalse(currentInfo2.duration == currentInfo1.duration, "Duration is changed when running");
	
	// check pause
	run.pause();
	currentInfo1 = run.getCurrentInfo();
	wait(2);
	currentInfo2 = run.getCurrentInfo();
	assertFalse(currentInfo2.duration != currentInfo1.duration, "Duration is not changed when paused");
	
	// check resume
	run.resume();
	currentInfo1 = run.getCurrentInfo();
	wait(2);
	currentInfo2 = run.getCurrentInfo();
	assertFalse(currentInfo2.duration == currentInfo1.duration, "Duration is changed after resuming");
	
	// check finish
	run.finish();
	assertTrue(run.canDone(), "Done button is visible after finish the run");
	
	// check result consistent with run amount
	var result = run.getResults();
	assertTrue(Math.abs(parseFloat(result.distance) - miles) <= 0.05, "Result is the same with run amount");
	
	// done and run again
	run.done();
	miles = 25;
	progress.simulateARunDontStop(miles);
	
	// make sure there is an alert
	assertTrue(run.isCongratulationAlertVisible(), "Congratulations alert is shown");
	
	// close alert and finish run
	log("Finish the run"); //wait(10);
	run.finish();
	log("finish ok");
	
	run.done();
	log("done ok");

	// verify set a new goal button is available
	wait();
	progress.scrollToWeekGoal();

	assertTrue(progress.isWeekGoalVisible() && progress.isSetNewGoalBtnVisible(), "Set new goal button is visible on 7DaysGoal view");
	
	// set new goal and verify current view is today goal and new goal is set
	progress.setNewGoal("Running", 20);
	wait();
	
	progress = new GoalProgress();
	assertTrue(progress.isTodaysGoalVisible(), "TodayGoal is visible after set new goal");

	// -------------------------------------------------------------------
	pass("WeekGoal testcases");
}
