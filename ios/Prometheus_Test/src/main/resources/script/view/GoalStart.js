#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
GoalStart function:
- canPause()	:	check if the progress is running
- canResume()	:	check if the progress is pausing
- canDone()		:	check if the progress is finished can be done
- pause()		:	pause the progress
- resume()		:	resume the progress
- finish()		:	finish the progress
- done()		:	done after view the stat results

*not implemented yet
- runFor(duration, mile)	:	keep running for "mile" in "duration" seconds
- isProgressValid(percent)	:	check if current progress is valid
- isResultValid(duration, mile, speed)	:	check if the result is valid
*/

function GoalStart()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = app.scrollViews()[0];
	
	var finishBtn = mainView.buttons()["Finish"];
	var pauseBtn = mainView.buttons()["Pause"];
	var resumeBtn = mainView.buttons()["Resume"];
	var doneBtn = mainView.buttons()["Done"];
	
	// Methods
	this.canPause = canPause;
	this.canResume = canResume;
	this.canDone = canDone;
	
	this.finish = finish;
	this.pause = pause;
	this.resume = resume;
	this.done = done;
	
	this.runFor = runFor;
	this.isProgressValid = isProgressValid;
	
	// Methods definition
	function canPause()
	{
		return pauseBtn.isValid() && pauseBtn.isVisible();
	}
	
	function canResume()
	{
		return resumeBtn.isValid() && resumeBtn.isVisible();
	}
	
	function canDone()
	{
		return doneBtn.isValid() && doneBtn.isVisible();
	}
	
	function finish()
	{
		finishBtn.tap();
	}
	
	function pause()
	{
		if(canPause())
			pauseBtn.tap();
	}
	
	function resume()
	{
		if(canResume())
			resumeBtn.tap();
	}
	
	function done()
	{
		if(canDone())
			doneBtn.tap();
	}
	
	function runFor(duration, mile)
	{
		// this is not implemented yet due to accessibility problem
	}
	
	function isProgressValid(percent)
	{
		// this is not implemented yet due to accessibility problem
	}
	
	function isResultValid(duration, mile, speed)
	{
		// this is not implemented yet due to accessibility problem
	}
}