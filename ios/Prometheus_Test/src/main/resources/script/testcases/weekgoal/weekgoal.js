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
	nav.toTodayGoal(null, null, null, "Running", 20);

	// run by gps
	progress = new GoalProgress();

	var miles = 1;
	progress.simulateARunDontStop(miles);
	
	// check view visible
	var run = new RunView();
	if (!run.isVisible() || !run.canPause()) 
	{
		fail("Run view is not visible");
	}
	
	// check duration is changed
	var currentInfo1 = run.getCurrentInfo();
	wait(2);
	var currentInfo2 = run.getCurrentInfo();
	if (currentInfo2.duration == currentInfo1.duration) 
	{
		fail("Duration doesnt change : 1: " + currentInfo1.duration + " 2: " + currentInfo2.duration);
	}
	
	// check pause
	run.pause();
	currentInfo1 = run.getCurrentInfo();
	wait(2);
	currentInfo2 = run.getCurrentInfo();
	if (currentInfo2.duration != currentInfo1.duration) 
	{
		fail("Duration is still changing when paused: 1: " + currentInfo1.duration + " 2: " + currentInfo2.duration);
	}
	
	// check resume
	run.resume();
	currentInfo1 = run.getCurrentInfo();
	wait(2);
	currentInfo2 = run.getCurrentInfo();
	if (currentInfo2.duration == currentInfo1.duration) 
	{
		fail("Duration doesnt change after resume : 1: " + currentInfo1.duration + " 2: " + currentInfo2.duration);
	}
	
	// check finish
	run.finish();
	if (!run.canDone()) 
	{
		fail("Finished run but cant be done");
	}
	
	// check result consistent with run amount
	var result = run.getResults();
	if ( miles < parseFloat(result.distance)) 
	{
		fail("Miles = " + miles + " - while distance = " + result.distance);
	}
	
	// done and run again
	run.done();
	miles = 25;
	progress.simulateARunDontStop(miles);
	
	// make sure there is an alert
	if (!run.isCongratulationAlertVisible()) 
	{
		fail("Congratulation alert should have been shown");
	}
	
	// close alert and finish run
	log("Finish the run"); //wait(10);
	run.finish();
	log("finish ok");
	
	run.done();
	log("done ok");

	// verify set a new goal button is available
	wait();
	progress.scrollToWeekGoal();

	if (!progress.isWeekGoalVisible() && !progress.isSetANewGoalBtnVisible())
	{
		fail("Weekgoal or the reset button is not visible");
	}
	
	progress.setANewGoal("active");

	// -------------------------------------------------------------------
	pass("WeekGoal testcases");
}
