#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Agenda C33", function(target, app) {
	
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda"].tap();
	 
	 
	 var numOfviews = 1000;
	 
	 for (k = 0 ; k < numOfviews ; k++)
	 {
     
	 	
     	var views = target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[k+1];
	 
     	if (!views.isValid())
     	{
     		numOfviews = k-1;
     
     	}
     	if (k!= 0)
     	{
     		target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[k].scrollRight();
     		target.delay(1);
     	}
     	else
     	{
     		target.flickFromTo({x:30, y:100}, {x:300, y:100});
     		target.delay(0.5);
     		target.flickFromTo({x:30, y:100}, {x:300, y:100});
     		target.delay(0.5);
     		target.flickFromTo({x:30, y:100}, {x:300, y:100});
     		target.delay(0.5);	
     		target.flickFromTo({x:30, y:100}, {x:300, y:100});
     		target.delay(0.5);
     	}
 
	 	 
	 
	 var DayInfo = target.frontMostApp().mainWindow().staticTexts()[1].name();

	 target.delay(3);
	 
	 var tableCells = target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[k].cells();
	 
	 for (i = 0 ; i < tableCells.length ; i++)
	 {
	 	tableCells[i].tap();
	 	 
	 	
	 	var dayExpect = target.frontMostApp().mainWindow().scrollViews()[0].staticTexts()[0].name();
	 	if (dayExpect.indexOf(DayInfo) != -1)
	 		UIALogger.logPass("Pass name element :" + i.toString() + " at Day " + (k+1).toString());
	 	else
	 		UIALogger.logFail("name Fail at element :" + i.toString() + " at Day " + (k+1).toString());
	  
	 	
	 	 	
	 	target.frontMostApp().navigationBar().leftButton().tap();
	 	
	 	var tableCellsExpect = target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[k].cells();
	 	for (j = 0 ; j < tableCellsExpect.length ; j++)
	 	{
	 		if (tableCells[j].name() == tableCellsExpect[j].name())
	 			UIALogger.logPass("");
	 		else
	 			UIALogger.logFail(" Agenda screen is loaded back different with the old state ");
	 	}
	 	
	 	
	 }
	 }
	 

	 target.frontMostApp().navigationBar().leftButton().tap();
	 
	 
	 
	 
	 
});

