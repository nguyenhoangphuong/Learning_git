#import "../core/testcaseBase.js"


var target = UIATarget.localTarget();
function MultiGoalChooser()
{
	// define name
	var nameTableView = "Empty list";
	var nameBackButton = "Done";
	var titleStr = "Please pick an activity";
	//define name of activity
	var activities = new Array();
	
	// Private fields
	var mainWindow = app.mainWindow();
	
	this.isVisible = isVisible;
	this.ChooseActivityWithIndex = ChooseActivityWithIndex;
	this.back = back;

	
	function isVisible()
	{
		wait(0.5);
		return staticTextExist(titleStr);
	}
	
	function ChooseActivityWithIndex(index)
	{		
		mainWindow.tableViews()[nameTableView].cells()[index].tap();
	}

	function back()
	{		
		target.frontMostApp().mainWindow().buttons()[nameBackButton].tap();	
	}
	
	function get
	
}


MultiGoalChooser();