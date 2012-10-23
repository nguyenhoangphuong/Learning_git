#import "../core/testcaseBase.js"
#import "_AlertHandler.js"
#import "_NavigationBar.js"

/*
List of function:
================================================================================
- assignControls()
- isVisible()
================================================================================
- setName(planName)							: set name of the plan
- back										: press back button
- save										: press save button
- pickActivity(activityName)				: pick an activity
- removeActivity(index)							: remove an activity
- getNumberOfActivities						: check how many activities are currently in the plan
- setActivityGoal(index, amount)			: set the goal for a certain activity
		+ index: index of activity in the plan
		+ amount: how much work out
================================================================================
*/

function PlanBuilder()
{
	// Private fields
	var window;
	var mainView;
	var backBtn;
	var saveBtn;
	
	// Initalize controls
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.setName = setName;
	this.back = back;
	this.save = save;
	this.pickActivity = pickActivity;
	this.removeActivity = removeActivity;
	this.getNumberOfActivities = getNumberOfActivities;
	this.setActivityGoal = setActivityGoal;
	
	// Methods definition
	function assignControls() {
		window = app.mainWindow();
		backBtn = app.navigationBar().leftButton();
		saveBtn = app.navigationBar().rightButton();
	}
	
	function isVisible() {
		assignControls();
		visible = app.navigationBar().name == "Plan builder";
		
		log("PlanBuilder is visible: " + visible);	
		return visible;
	}
	
	function setName(planName) {
		assignControls();
		var text = window.textFields()[0];
		text.setValue(planName);
		app.keyboard().typeString("\n");
		
	}
	
	function back() {
		backBtn.tap();
	}
	
	function save() {
		saveBtn.save();
	}
	
	/**
	 * Drag an activity to create a plan.
	 * @param activityName for instance: leglift, running etc.
	 *   
	 * @returns
	 */
	function pickActivity(activityName) {
		log("Picking activity: " + activityName);
		var button = window.scrollViews()[0].buttons()["icon " + activityName];
		button.scrollToVisible();
		wait();
		var rect = button.rect();
        UIATarget.localTarget().dragFromToForDuration({x:rect.origin.x, y:rect.origin.y}, {x:160, y:380}, 2);
	}
	
	function pickActivityAtIndex(index) {
		log("Picking activity at index: " + index);
		var button = window.scrollViews()[0].buttons()[index];
		dragActivity(button);
	}
	
	function dragActivity(button) {
		button.scrollToVisible();
		wait();
		var rect = button.rect();
        UIATarget.localTarget().dragFromToForDuration({x:rect.origin.x, y:rect.origin.y}, {x:160, y:380}, 1);
	}
	
	function removeActivity(index) {
		log("removing");
		
		if (index >= getNumberOfActivities()) {
			return;
		}
		
		var button = window.buttons()[index];
		wait();
		var rect = button.rect();
		log("x= " + rect.origin.x + " : y= " + rect.origin.y);
        UIATarget.localTarget().dragFromToForDuration({x:rect.origin.x, y:rect.origin.y}, {x:10, y:10}, 1);
	}	
	
	function getNumberOfActivities() {
		return window.buttons().length;
	}
	
	function setActivityGoal(activityIndex, amount) {
		if (activityIndex >= getNumberOfActivities()) {
			return;
		}
		
		var text = window.textFields()[activityIndex + 1]; // the first text field is for plan name
		text.setValue(amount);
		app.keyboard().typeString("\n");
	}
	
}

var planBuilder = new PlanBuilder();
