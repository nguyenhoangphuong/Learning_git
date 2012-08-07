#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"


var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Stress C150", function(target, app) {
	 
	 
	 for (i=0; i< 50 ; i++)
	 {
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
	 		target.frontMostApp().mainWindow().scrollViews()[1].scrollRight();
	 		target.delay(0.5);
	 	}
	 
	 	
	 	var cells = target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[k].cells();
	 	for (j = 0 ; j < cells.length ; j++)
	 	{
	 		cells[j].scrollToVisible();
	 		cells[j].buttons()["Agenda list STAR OFF"].tap();
	 
		} 
	 
	 }
	 target.frontMostApp().navigationBar().segmentedControls()[0].buttons()["Agenda list StarButton"].tap();
	 
	 while ( target.frontMostApp().mainWindow().tableViews()[0].cells()[0].isValid())
	 {
	 	target.frontMostApp().mainWindow().tableViews()[0].cells()[0].buttons().firstWithPredicate("value == 1").tap();
	 	
	 }
	 
	 target.frontMostApp().navigationBar().segmentedControls()[0].buttons()["Agenda list ListButton"].tap();
 	 
	 target.frontMostApp().navigationBar().leftButton().tap();
	 }
	 
	// target.frontMostApp().navigationBar().segmentedControls()[0].buttons()["Agenda list StarButton"].tap();
	 
	 //target.frontMostApp().navigationBar().segmentedControls()[0].buttons()["Agenda list ListButton"].tap();
	 
	 
	 
});