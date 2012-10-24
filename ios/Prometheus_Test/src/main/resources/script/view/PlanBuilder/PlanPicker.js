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
================================================================================
*/

function PlanPicker()
{
	// Private fields
	
	var window;
	var mainView;
	
	// Initalize controls
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.pickPlan = pickPlan;
	this.getCustomPlans = getCustomPlans;
	this.tapCustomPlan = tapCustomPlan;
	
	// Methods definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window.tableViews()[0];
	}
	
	function isVisible()
	{
		visible = app.navigationBar().name == "Plan picker";
		
		log("Progress is visible: " + visible);	
		return visible;
	}
	
	function pickPlan(type, name)
	{
		// for custom plan, or visible pre-plan
		if(type == 0 || type == "Custom" || 
		  (mainView.cells()[name].isValid() && mainView.cells()[name].isVisible()))
		{
			mainView.cells()[name].tap();
			log("Tap [" + name + "]");
			
			return;
		}
		
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
		// find start index of personal plans
		start = 3;
		
		// get all personal plan's names
		var cells = mainView.cells();
		var info = [];
		for(i = start; i < cells.length - 1; i++)
			info[i - start] = cells[i].name();
		
		log("Personal plans: " + JSON.stringify(info));
		return info;
	}
	
	function tapCustomPlan()
	{
		var cells = mainView.cells();
		
		n = cells.length;
		cells[n - 1].tap();
		log("Tap [CustomPlan]");
	}
	
	
}

var planPicker = new PlanPicker();
