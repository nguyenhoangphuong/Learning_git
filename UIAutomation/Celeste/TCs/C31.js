#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Comment C31", function(target, app) {
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda"].tap();
	 target.frontMostApp().navigationBar().segmentedControls()[0].buttons()[0].tap();
	
	 var numOfviews = 1000;
	 var nCount = 0;
	 
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
	 
	 
	 var cells = target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[k].cells();
	for (i = 0 ; i < cells.length ; i++)
	{
	 	target.delay(3);
	 	cells[i].tap();
	 	target.delay(3);
	 
	 	var tag = target.frontMostApp().mainWindow().scrollViews()[0].images()["Agenda detail_Rating.png"].buttons()[0].name();
	 	if (tag == "Agenda list STAR ON")
	 	{
	 		target.frontMostApp().navigationBar().leftButton().tap();
	 	}
	 	else
	 	{
			target.frontMostApp().mainWindow().scrollViews()[0].images()["Agenda detail_Rating.png"].buttons()[2].tap(); 
			i = 2000;
	 		if (target.frontMostApp().mainWindow().buttons()[0].checkIsValid())
	 		{
	 			
	 			var comm = "";
	 			for (i = 0 ; i < 2499 ; i++)
	 			{ 	
	 				comm = comm + "a"; 
	 			}
	
	 			comm = comm + "b";
	 			target.frontMostApp().mainWindow().textViews()[0].setValue(comm);
	 			target.frontMostApp().keyboard().typeString("k");
	 			target.frontMostApp().keyboard().typeString("h");
	 			target.frontMostApp().keyboard().typeString("o");
	 			target.frontMostApp().keyboard().typeString("a");
	 			
	 			var actual = target.frontMostApp().mainWindow().textViews()[0].value();
	 			
	 			if (comm == actual)
	 				UIALogger.logPass("Reach");
	 			else
	 				UIALogger.logfail("Not Reach");
	 					
	 		}
	 i=3000;
	 k=3000;
	 }
	 
	 
	 	}
	 
	}
	 target.frontMostApp().navigationBar().leftButton().tap();
	 		target.frontMostApp().navigationBar().leftButton().tap();
	 
	 
	 });