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
- getActivities()	:	get all Activities.
*/
#import "../core/testcaseBase.js"

function MultiGoalChooser()
{
	// define name
	var titleStr = "Please pick an activity";
	var nameTableView = "Empty list";
	var indexOfButtonBack = 0;
	
	// define button
	var backBtn = target.frontMostApp().mainWindow().buttons()[0];
		
	//define name of activity
	var activities = [	
						{
							"Name" : "Cycling",  
							"Unit" : "Distance",
							"RangeMin" : 10,
							"RangeMax" : 300,								
							"EasyPlan" : 70,
							"MediumPlan" : 140,
							"HardPlan" : 35*7
						},
						{  
							"Name" : "Running",  
							"Unit" : "Distance",
							"RangeMin" : 3,
							"RangeMax" : 31,								
							"EasyPlan" : 5,
							"MediumPlan" : 10,
							"HardPlan" : 21
						},
						
						{  
							"Name" : "Treadmill",  
							"Unit" : "Distance",
							"RangeMin" : 3,
							"RangeMax" : 50,								
							"EasyPlan" : 6,
							"MediumPlan" : 12,
							"HardPlan" : 25
						},
							
						{  
							"Name" : "Elliptical",  
							"Unit" : "Distance",
							"RangeMin" : 4,
							"RangeMax" : 60,								
							"EasyPlan" : 5,
							"MediumPlan" : 12,
							"HardPlan" : 25
						},
						 
						{ 
						 	"Name" : "Indoor Cycling",  
							"Unit" : "Distance",
							"RangeMin" : 10,
							"RangeMax" : 300,								
							"EasyPlan" : 70,
							"MediumPlan" : 140,
							"HardPlan" : 35*7
						},
						
						{  
							"Name" : "Swimming", 
							"Unit" : "LapsLength",
							"RangeMin" : 50*1*7,
							"RangeMax" : 50*20*7,								
							"EasyPlan" : 50*2*7,
							"MediumPlan" : 50*5*7,
							"HardPlan" : 50*10*7
						},
						
						{  
							"Name" : "Push-up", 
							"Unit" : "Count",
							"RangeMin" : 5*7,
							"RangeMax" : 60*7,								
							"EasyPlan" : 10*7,
							"MediumPlan" : 25*7,
							"HardPlan" : 60*7
						},
						
						{  
							"Name" : "Sit-up", 
							"Unit" : "Count",
							"RangeMin" : 5*7,
							"RangeMax" : 60*7,								
							"EasyPlan" : 10*7,
							"MediumPlan" : 25*7,
							"HardPlan" : 60*7
						},
						
						{  
							"Name" : "Leg-lift", 
							"Unit" : "Count",
							"RangeMin" : 10*7,
							"RangeMax" : 100*7,								
							"EasyPlan" : 15*7,
							"MediumPlan" : 25*7,
							"HardPlan" : 40*7
						},
						
						{  
							"Name" : "Pull-up", 
							"Unit" : "Count",
							"RangeMin" : 3*7,
							"RangeMax" : 30*7,								
							"EasyPlan" : 7*7,
							"MediumPlan" : 12*7,
							"HardPlan" : 20*7
						},
						
						{  
							"Name" : "Plank", 
							"Unit" : "Duration",
							"RangeMin" : 70,
							"RangeMax" : 840,								
							"EasyPlan" : 210,
							"MediumPlan" : 420,
							"HardPlan" : 840
						}
						
 ];
	
	// private fields
	var mainWindow = app.mainWindow();	
	
	// methods
	this.isVisible = isVisible;
	this.isAllActivitiesVisible = isAllActivitiesVisible;
	
	this.chooseActivityWithIndex = chooseActivityWithIndex;
	this.chooseActivityWithName = chooseActivityWithName;
	this.back = back;
	
	function isVisible()
	{
		wait(0.5);
		exist = staticTextExist(titleStr);
		
		log("MultiGoal visible: " + exist);
		return exist;
	}
	
	function isAllActivitiesVisible()
    {  	
		wait(0.5);
    	for (i=0; i<activities.length; i++)
    		if (!mainWindow.tableViews()[nameTableView].cells()[activities[i]].isValid())
    		{
    			log("Not found :" + activities[i]);
    			return false;
    		}
    	
    	log("All activities are valid");
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
	function getActivities()
	{
		return activities;
	}
    
	function back()
	{		
		wait(0.5);
		target.frontMostApp().mainWindow().buttons()[nameBackButton].tap();
		log("Tap [Back]");
	}
	
}
