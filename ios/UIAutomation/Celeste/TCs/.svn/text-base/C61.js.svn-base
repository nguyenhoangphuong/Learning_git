#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Agenda Detail C61", function(target, app) {
     
     target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda"].tap();	 
     target.frontMostApp().navigationBar().segmentedControls()[0].buttons()[0].tap();
     target.flickFromTo({x:30, y:100}, {x:300, y:100});
     target.delay(0.5);
     target.flickFromTo({x:30, y:100}, {x:300, y:100});
     target.delay(0.5);
     target.flickFromTo({x:30, y:100}, {x:300, y:100});
     target.delay(0.5);	
     target.flickFromTo({x:30, y:100}, {x:300, y:100});
     target.delay(0.5);
     if (target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells()[0].buttons()["Agenda list STAR OFF"].value() == 1)
     {
     target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells()[0].buttons()["Agenda list STAR OFF"].tap();	 
     }
     var nameCell = target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells()[0].name();
     
     target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells()[0].tap();
     target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda list STAR OFF"].tap();
     target.frontMostApp().navigationBar().leftButton().tap();
     target.frontMostApp().navigationBar().segmentedControls()[0].buttons()[1].tap();
     
     var cells = target.frontMostApp().mainWindow().tableViews()[0].cells();
     var dem = 0;
     for (i=0; i < cells.length ; i++)
     {
     if (cells[i].name() == nameCell)
     dem++;
     }
	 if (dem == 1)
     UIALogger.logPass(dem.toString());
	 else
     UIALogger.logFail(dem.toString());	
	 
	 target.frontMostApp().navigationBar().leftButton().tap();
	 
	 
     });