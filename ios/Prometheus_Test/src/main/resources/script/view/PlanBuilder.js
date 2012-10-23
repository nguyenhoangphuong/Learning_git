#import "../core/testcaseBase.js"
#import "_AlertHandler.js"
#import "_NavigationBar.js"

/*
List of function:
================================================================================
- assignControls()
- isVisible()
================================================================================
- pickPlan(type, name)		: pick a specified plan
	+ type: [Custom/Easy/Normal/Active] or [0/1/2/3]
	+ name: "Normal #1" or "Personal #1"
- getPersonalPlans()		: return a list of all personal plan's names
- tapCustomPlan()			: tap the custom plan button
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
	
	// Methods definition
	function assignControls() {
		window = app.mainWindow();
		backBtn = app.navigationBar().leftButton();
		saveBtn = app.navigationBar().rightButton();


	}
	
	function isVisible()
	{
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
//		log("x= " + rect.origin.x + " : y= " + rect.origin.y);
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
        UIATarget.localTarget().dragFromToForDuration({x:rect.origin.x, y:rect.origin.y}, {x:160, y:380}, 2);
	}
	
	function removeActivity() {
		log("removing");
		
		var button = window.buttons()[0];
		wait();
		var rect = button.rect();
		log("x= " + rect.origin.x + " : y= " + rect.origin.y);
        UIATarget.localTarget().dragFromToForDuration({x:rect.origin.x, y:rect.origin.y}, {x:10, y:10}, 6);
	}	
}

var planBuilder = new PlanBuilder();
