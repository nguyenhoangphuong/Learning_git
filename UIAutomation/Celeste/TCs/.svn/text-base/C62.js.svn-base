#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Agenda Detail C62", function(target, app) {
     
     target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda"].tap();	 
     target.frontMostApp().navigationBar().segmentedControls()[0].buttons()[0].tap();
	 
     
     if (target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells()[0].buttons()["Agenda list STAR OFF"].value() == 0)
     {
     target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells()[0].buttons()["Agenda list STAR OFF"].tap();	 
     }
     var nameCell = target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells()[0].name();
     
     target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells()[0].tap();
     target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda list STAR OFF"].tap();
     target.frontMostApp().navigationBar().leftButton().tap();
     target.frontMostApp().navigationBar().segmentedControls()[0].buttons()[1].tap();
     
     var cells = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells();
     var dem = 0;
     for (i=0 ; i < cells.length ; i++)
     {
     if (cells[i].name() == nameCell)
     dem++;
     }
	 
	 if (dem == 0)
     UIALogger.logPass(dem.toString());
	 else
     UIALogger.logFail(dem.toString());		 
     
     target.frontMostApp().navigationBar().leftButton().tap();
	 
	 
     });