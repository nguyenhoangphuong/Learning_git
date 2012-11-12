#import "../MVPLibs.js"

/*
List of function:
================================================================================
- assignControls()
- isVisible()
================================================================================
- tapBack()
- tapDelete(confirm)			: tap [Recycle] and confirm the alert
	+ true/undefined			: tap [Yes] when alert is shown
	+ false						: tap [No] when alert is shown
================================================================================
- getPlanInfo()					: return [{name: Running, value:4.00 miles}, 
										  {name: Treadmill, value: 7.00 miles}]
- tapGo()						: tap [Go for it]
- tapCustom()					: tap [Custimize]
================================================================================
*/

function PlanInfo()
{
	// Private fields
	var window;
	var mainView;
	var cells;

	var backBtn;
	var deleteBtn;
	var goBtn;
	var customBtn;
	
	// Initalize controls
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.tapBack = tapBack;
	this.tapDelete = tapDelete;
	
	this.getPlanInfo = getPlanInfo;
	this.tapGo = tapGo;
	this.tapCustom = tapCustom;
	
	// Methods definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window.tableViews()[0];
		
		cells = mainView.cells(); 
		
		backBtn = app.navigationBar().leftButton();
		deleteBtn = app.navigationBar().rightButton();
		goBtn = window.buttons()[0];
		customBtn = window.buttons()[1];
	}
	
	function isVisible(name)
	{
		// check visible implicit
		if(typeof name == "undefined")
		{
			visible = goBtn.isVisible() && goBtn.name() == "Go for it";
			
			log("PlanInfo is visible: " + visible);	
			return visible;
		}
		
		// check visible explicit
		visible = app.navigationBar().name() == name;
		
		log("PlanInfo <" + name + "> is visible: " + visible);	
		return visible;
	}
	
	function tapBack()
	{
		backBtn.tap();
		log("Tap [Back]");
	}
	
	function tapDelete(confirm)
	{
		// if not visible
		if(!isDeletePlanBtnVisible())
		{
			log("No [Delete Plan] button");
			return;
		}
			
		// set up alert choice
		if(typeof confirm == "undefined")
			confirm = true;
		alert.alertChoice = confirm ? "YES" : "NO";
		
		// trigger
		deleteBtn.tap();
		log("Tap [Delete]");
		
		// wait for and handle alert
		wait();
	}
	
	function getPlanInfo()
	{	
		// get info
		var info = [];
		n = cells.length;
		
		for(i = 0; i < n; i++)
		{
			text = cells[i].name();
			
			/*
			name = text.substring(0, text.indexOf(","));
			value = text.substring(text.indexOf(", ") + 2);
			*/
			
			name = text;
			value = "n/a";
			
			info[i] = {name: name, value: value};
		}
		
		log("Plan info: " + JSON.stringify(info));
		return info;
	}
	
	function tapGo() {
		assignControls();
		goBtn.tap();
		log("Tap [GoForIt]");
		
		wait();
	}
	
	function tapCustom()
	{
		customBtn.tap();
		log("Tap [Customize]");
	}
}
