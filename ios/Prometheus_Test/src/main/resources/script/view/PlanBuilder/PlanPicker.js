#import "../MVPLibs.js"

/*
List of function:
================================================================================
- assignControls()
- isVisible()
================================================================================
- pickPlan(type, name)		: pick a specified plan
	+ type: [Custom/Easy/Normal/Active] or [0/1/2/3]
	+ name: "Normal #1" or "Personal #1"
- getCustomPlans()			: return a list of all personal plan's names
- tapCustomPlan()			: tap the custom plan button
- getPrepackagePlan()		: get a prepackage plan as described in PlanBuilderData
================================================================================
*/

function PlanPicker()
{
	// Private fields
	
	var window;
	var mainView;
	var customTable;
	
	// Initalize controls
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.pickPlan = pickPlan;
	this.getCustomPlans = getCustomPlans;
	this.tapCustomPlan = tapCustomPlan;
	this.getPrepackagePlan = getPrepackagePlan;
	
	this.easy = "Easy";
	this.normal = "Normal";
	this.active = "Active";
	
	this.easyStarter = "The Starter’s Plan";
	this.normalMover= "The Mover’s Plan";
	this.activeShaker="The Shaker’s Plan";
	
	// Methods definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window.scrollViews()[0].tableViews()[0];
		customTable = window.scrollViews()[0].tableViews()[1];
	}
	
	function isVisible()
	{
		visible = app.navigationBar().name() == "Activity Plans";
		
		log("PlanPicker is visible: " + visible);	
		return visible;
	}
	
	function pickPlan(type, name)
	{
		// for custom plan
		if(type == 0 || type == "Custom")
		{
			customTable.cells()[name].tap();
			log("Tap [" + name + "]");
			
			return;
		}
		
		// for prepackage plan which is visible now
		if(mainView.cells()[name].isValid() && mainView.cells()[name].isVisible())
		{
			mainView.cells()[name].tap();
			log("Tap [" + name + "]");
			
			return;
		}
		
		// if not visible, expand the group
		if(type == 1 || type == "Easy")
			mainView.cells()["Easy"].tap();
		
		if(type == 2 || type == "Normal")
			mainView.cells()["Normal"].tap();
		
		if(type == 3 || type == "Active")
			mainView.cells()["Active"].tap();
		
		wait(0.5);
		
		mainView.cells()[name].tap();
		log("Tap [" + name + "]");
	}
	
	function getCustomPlans()
	{	
		// get all personal plan's names
		var cells = customTable.cells();
		var info = [];
		for(i = 0; i < cells.length - 1; i++)
			info[i] = cells[i].name();
		
		log("Personal plans: " + JSON.stringify(info));
		return info;
	}
	
	function tapCustomPlan()
	{
		var cells = customTable.cells();
		
		n = cells.length;
		cells[n - 1].tap();
		log("Tap [CustomPlan]");
	}
	
	var planBuilderData = 
	{
		prepackage:
		{
			theStartersPlan: [{name: "Treadmill", value: "3.00 miles"}, {name: "Plank", value: "120 secs"}, {name: "Sit-up", value: "35 reps"}],
			theMoversPlan: [{name: "Running", value: "5.00 miles"}, {name: "Push-up", value: "90 reps"}, {name: "Swimming", value: "1500.00 ft"}],
			theShakersPlan: [{name: "Swimming", value: "1800.00 miles"}, {name: "Push-up", value: "245 reps"}, {name: "Treadmill", value: "8.00 miles"}],
		}
	}
	
	function getPrepackagePlan(name) {
		log("Get the plan name: " + name);
		if (name == this.easyStarter) {
			return planBuilderData.prepackage.theStartersPlan;
		} else if (name == this.normalMover) {
			return planBuilderData.prepackage.theMoversPlan;
		} else if (name == this.activeShaker) {
			return planBuilderData.prepackage.theShakersPlan;
		}
		return null;
	}
}
