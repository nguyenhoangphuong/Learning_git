#import "../MVPLibs.js"
#import "PlanBuilderData.js"
/*
List of function:
================================================================================
- assignControls()
- isVisible()
================================================================================
- setName(planName)							: set name of the plan
- back()									: press back button
- start()									: press start button

- pickActivity(id, useIndex)				: pick an activity with name or index
- removeActivity(id, useIndex)				: remove an activity with name or index
- setActivityGoal(id, amount, useIndex)		: set the goal for a certain activity
		+ id: name or index of activity in the plan
		+ amount: how much work out
		
- getNumberOfActivities()					: check how many activities are currently in the plan
================================================================================
*/

function PlanBuilder()
{
	// Private fields
	var window;
	var mainView;
	var iconsList;
	
	var nameTxt;
	var backBtn;
	var startBtn;
	
	// Initalize controls
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.setName = setName;
	this.back = back;
	this.start = start;
	
	this.pickActivity = pickActivity;
	this.removeActivity = removeActivity;
	this.getNumberOfActivities = getNumberOfActivities;
	this.setActivityGoal = setActivityGoal;
	
	// Methods definition
	function assignControls() 
	{
		window = app.mainWindow();
		mainView = window.scrollViews()[0];
		iconsList = mainView.scrollViews()[0].buttons();
		
		nameTxt = mainView.textFields()[0];
		backBtn = app.navigationBar().leftButton();
		startBtn = app.navigationBar().rightButton();
	}
	
	function isVisible() 
	{
		assignControls();
		visible = app.navigationBar().name() == "Your plan";
		
		log("PlanBuilder is visible: " + visible + ", [" + app.navigationBar().name() + "]");	
		return visible;
	}
	
	function setName(planName) 
	{
		wait();
		assignControls();
		nameTxt.setValue(planName);
		app.keyboard().typeString("\n");
		
	}
	
	function back() 
	{
		backBtn.tap();
	}
	
	function start() 
	{
		startBtn.tap();
	}
	
	/**
	 * Drag an activity to create a plan.
	 * @param activityName for instance: leglift, running etc.
	 *   
	 * @returns
	 */
	function pickActivity(id, useIndex)
	{
		log("Picking activity: " + id);
		
		if(typeof useIndex == "undefined") {
			useIndex = false;
		}
		var	button = useIndex? iconsList[id] : iconsList["icon " + id.toLowerCase().replace("-", "")];

		// dragging
		button.scrollToVisible();
		wait();
		var rect = button.rect();
        UIATarget.localTarget().dragFromToForDuration({x:rect.origin.x + rect.size.width/2, y:rect.origin.y + rect.size.height/2}, {x:160, y:380}, 2);
        wait();
	}
	
	function removeActivity(id, useIndex) 
	{
		log("Removing activity: " + useIndex);
		
		if(typeof useIndex == "undefined")
			useIndex = false;
		
		if (useIndex && id >= getNumberOfActivities()) 
			return;
		
		// find index
		if(!useIndex)
		{
			texts = mainView.staticTexts();
			for(i = 0; i < texts.length; i++)
				if(texts[i].name() == id)
					id = i;
		}
		
		// double id because there are (i) buttons
		var button = mainView.buttons()[id * 2];
		
		// dropping
		wait();
		var rect = button.rect();
		log("x= " + rect.origin.x + " : y= " + rect.origin.y);
        UIATarget.localTarget().dragFromToForDuration({x:rect.origin.x + rect.size.width/2, y:rect.origin.y + rect.size.height/2}, {x:10, y:10}, 2);
        wait();
	}	
	
	function getNumberOfActivities() 
	{
		return mainView.staticTexts().length;
	}
	
	function setActivityGoal(id, amount, useIndex) 
	{
		if(typeof useIndex == "undefined")
			useIndex = false;
		
		if (useIndex && id >= getNumberOfActivities())
			return;
		
		// find index
		var i;
		if(!useIndex)
		{
			texts = mainView.staticTexts();
			for(i = 0; i < texts.length; i++)
				if(texts[i].name() == id)
					id = i;
		}
		
		var btn = mainView.buttons()[id * 2 + 1];
		btn.tap()
		app.keyboard().typeString(amount);
		app.toolbar().buttons()["Done"].tap();
	}
	
}

var planBuilder = new PlanBuilder();
