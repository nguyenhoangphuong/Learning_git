/*
MultiGoalChooser functions:
=========================================================================================
- isVisible()		: 	check if current view is MultiGoalChooser (title verify)
- isAllActivitiesVisible():	check all activities is shown in cell 
(if you want to change activities, please set it on the array activities[] (line 23)
=========================================================================================
- chooseActivityWithIndex(index)		:	tap button with its index
- chooseActivityWithName(name)	:	tap button with name
- back()			:	tap the back button
=========================================================================================
*/
#import "../core/testcaseBase.js"

function MultiGoalChooser()
{
	// define name
	var titleStr = "Please pick an activity";
	var nameTableView = "Empty list";
	var nameBackButton = "Done";
	
	//define name of activity
	var activities = ["Cycling","Running","Treadmill", "Elliptical", 
			"Indoor Cycling","Swimming","Push-up","Sit-up",
			"Leg-lift","Pull-up","Plank"
			];
	
	// Private fields
	var mainWindow = app.mainWindow();	
	this.isVisible = isVisible;
	this.isAllActivitiesVisible = isAllActivitiesVisible;
	
	this.chooseActivityWithIndex = chooseActivityWithIndex;
	this.back = back;
	this.chooseActivityWithName = chooseActivityWithName;
	
	function isVisible()
	{
		wait(0.5);
		return staticTextExist(titleStr);
	}
	
	function isAllActivitiesVisible()
    {  	
		wait(0.5);
    		for (i=0;i<activities.length;i++)
    			if (!mainWindow.tableViews()[nameTableView].cells()[activities[i]].isValid())
    			{
    				log("Not found :" + activities[i]);
    				return false;
    			}
    		return true;  				
    }
	function chooseActivityWithIndex(index)
	{		
		mainWindow.tableViews()[nameTableView].cells()[index].tap();
	}

	function back()
	{		
		target.frontMostApp().mainWindow().buttons()[nameBackButton].tap();	
	}
	
    function chooseActivityWithName(name)
	{		
		mainWindow.tableViews()[nameTableView].cells()[name].tap();
	}
    
    	
	
}
