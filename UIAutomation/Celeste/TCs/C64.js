#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("AgendaDetail C64", function(target, app) {
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda"].tap();
	 target.frontMostApp().navigationBar().segmentedControls()[0].buttons()[0].tap();
	 
	 var cells = target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells();
     for (i = 0 ; i < cells.length ; i++)
	 {
	 
     	cells[i].tap();
	 
	 	target.delay(3);
	 
	 
     	var tag =  target.frontMostApp().mainWindow().scrollViews()[0].images()["Agenda detail_Rating_new.png"].buttons()[0].name();
	 
	 	target.delay(3);
	 
	 
     	if (tag == "Agenda list STAR ON")
     	{
	 		UIALogger.logPass(tag);
     		target.frontMostApp().navigationBar().leftButton().tap();
     	}
     	else
     	{
	 		UIALogger.logPass(tag);
	 
	 		target.delay(2);		
	 
     		target.frontMostApp().mainWindow().scrollViews()[0].images()["Agenda detail_Rating_new.png"].buttons()[2].tap();;
     		i = 2000;
     	if (target.frontMostApp().mainWindow().buttons()[0].checkIsValid())
     	{
     		UIALogger.logPass();
     		target.frontMostApp().mainWindow().buttons()[0].tap();
     	}
     	else
     		UIALogger.logFail("Not found button No Thanks"); 
	 
	 
	 		//target.frontMostApp().navigationBar().leftButton().tap(); 
	 		target.frontMostApp().navigationBar().leftButton().tap();
	 
	 
     
	 
	 
     //assertNotNull(target.frontMostApp().mainWindow().buttons()[0]);
     
     	}
	 
	 }
	 
	 
	 target.frontMostApp().navigationBar().leftButton().tap();
	 
	 
	 
	 
	 });