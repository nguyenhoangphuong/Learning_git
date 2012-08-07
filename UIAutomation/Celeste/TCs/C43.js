#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Agenda C43", function(target, app) {
     
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda"].tap();
	 target.frontMostApp().navigationBar().segmentedControls()[0].buttons()["Agenda list StarButton"].tap();
	 
	 while ( target.frontMostApp().mainWindow().tableViews()[0].cells()[0].isValid())
	 {
     target.frontMostApp().mainWindow().tableViews()[0].cells()[0].buttons().firstWithPredicate("value == 1").tap();
     
	 }
     target.frontMostApp().navigationBar().segmentedControls()[0].buttons()["Agenda list ListButton"].tap();
	 
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
     target.frontMostApp().mainWindow().scrollViews()[1].scrollRight();
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
	 
	 if (cells.length >= 1)
	 {
     cells[0].scrollToVisible();
     target.delay(1);
     cells[0].buttons()["Agenda list STAR OFF"].tap();
     nCount++;
	 }
	 if (cells.length >= 2)
	 {
     cells[1].scrollToVisible();
     target.delay(1);
     cells[1].buttons()["Agenda list STAR OFF"].tap();
     nCount++;
	 }
	 if (cells.length >= 3)
	 {
     var num = cells.length;
     cells[num-1].scrollToVisible();
     cells[num-1].buttons()["Agenda list STAR OFF"].tap();
     nCount++;
	 }
	 
	 
     }
	 
	 numOfviews = 1000;
     
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
	 
	 if (cells.length >= 1)
	 {
     cells[0].scrollToVisible();
     target.delay(1);
     cells[0].buttons()["Agenda list STAR OFF"].tap();
     nCount--;
	 }
	 if (cells.length >= 2)
	 {
     cells[1].scrollToVisible();
     target.delay(1);
     cells[1].buttons()["Agenda list STAR OFF"].tap();
     nCount--;
	 }
	 if (cells.length >= 3)
	 {
     var num = cells.length;
     cells[num-1].scrollToVisible();
     cells[num-1].buttons()["Agenda list STAR OFF"].tap();
     nCount--;
	 }
	 
	 
     }
	 
	 
	 target.frontMostApp().navigationBar().segmentedControls()[0].buttons()["Agenda list StarButton"].tap();
	 
	 
	 var favoucells = target.frontMostApp().mainWindow().tableViews()[0].cells();
	 if (favoucells.length == nCount)
     UIALogger.logPass("Pass. Have" + nCount.toString() + " favourite");
	 else
     UIALogger.logFail("Fail. Not have "+ nCount.toString() + " favourite");
	 
	 if (target.frontMostApp().mainWindow().staticTexts()["You have no favorite session yet!"].isValid())
     UIALogger.logPass("Pass.You have no favorite session yet!");
	 else
     UIALogger.logFail("Fail.Not found You have no favorite session yet!");
	 
  	 
     target.frontMostApp().navigationBar().leftButton().tap();
	 
	 
	 
	 
	 });