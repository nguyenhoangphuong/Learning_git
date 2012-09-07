#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
GoalStart function:
- isVisible()	:	check if the current view is GoalStart
- canPause()	:	check if the progress is running
- canResume()	:	check if the progress is pausing
- canDone()		:	check if the progress is finished can be done
- pause()		:	pause the progress
- resume()		:	resume the progress
- finish()		:	finish the progress
- done()		:	done after view the stat results

*not implemented yet
- runFor(duration, mile)	:	keep running for "mile" in "duration" seconds
- getCurrentInfo()			:	get the current {percent, distance, duration}
- getResults()				:	get the results {percent, distance, duration, speed}
*/

function GoalStart()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window;
	
	var finishBtn = mainView.buttons()["Finish"];
	var toggleBtn = mainView.buttons()[1];
	var doneBtn = mainView.buttons()["Done"];
	
	// Methods
	this.isVisible = isVisible;
	
	this.canPause = canPause;
	this.canResume = canResume;
	this.canDone = canDone;
	
	this.finish = finish;
	this.pause = pause;
	this.resume = resume;
	this.done = done;
	
	this.runFor = runFor;
	this.getCurrentInfo = getCurrentInfo;
	this.getResults = getResults;
	
	// Methods definition
	function isVisible()
	{
		return finishBtn.isValid() && finishBtn.isVisible();
	}
	
	
	function canPause()
	{
		return toggleBtn.name() == "Pause";
	}
	
	function canResume()
	{
		return toggleBtn.name() == "Resume";
	}
	
	function canDone()
	{
		return doneBtn.isValid() && doneBtn.isVisible();
	}
	
	function finish()
	{
		wait();
		finishBtn.tap();
	}
	
	function pause()
	{
		if(canPause())
		{
			wait();
			toggleBtn.tap();
		}
	}
	
	function resume()
	{
		if(canResume())
		{
			wait();
			toggleBtn.tap();
		}
	}
	
	function done()
	{
		if(canDone())
		{
			wait();
			doneBtn.tap();
		}
	}
	
	function runFor(duration, mile)
	{
		// this is not implemented yet due to data and waiting time problem
	}
	
	function getCurrentInfo()
	{
		var info = {};
		info.percent = mainView.staticTexts()[2].name();
		info.duration = mainView.staticTexts()[4].name();
		info.distance = mainView.staticTexts()[6].name();
		
		log("info.percent: " + info.percent);
		log("info.duration: " + info.duration);
		log("info.distance: " + info.distance);
		
		return info
	}
	
	function getResults()
	{
		var info = {};
		info.percent = mainView.staticTexts()[2].name();
		info.duration = mainView.staticTexts()[3].name();
		info.distance = mainView.staticTexts()[5].name();
		info.speed = mainView.staticTexts()[7].name();
		
		log("info.percent: " + info.percent);
		log("info.duration: " + info.duration);
		log("info.distance: " + info.distance);
		log("info.speed: " + info.speed);
		
		return info;
	}
}