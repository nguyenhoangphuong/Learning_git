#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Agenda C37", function(target, app) {
     
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda"].tap();
	 target.frontMostApp().navigationBar().segmentedControls()[0].buttons()["Agenda list StarButton"].tap();
	 
	 while ( target.frontMostApp().mainWindow().tableViews()[0].cells()[0].isValid())
	 {
     target.frontMostApp().mainWindow().tableViews()[0].cells()[0].buttons().firstWithPredicate("value == 1").tap();
     
	 }
     target.frontMostApp().navigationBar().segmentedControls()[0].buttons()["Agenda list ListButton"].tap();
	 
	 var nCount = 0;
	 for(t=1 ; t < 6; t++)
	 {
     target.flickFromTo({x:30, y:100}, {x:300, y:100});
     target.delay(0.5);	 		
	 }
     
     
	 var cellsDay1 = target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells();
	 
	 if (cellsDay1.length >= 1)
	 {
     cellsDay1[0].scrollToVisible();
     target.delay(1);
     cellsDay1[0].buttons()["Agenda list STAR OFF"].tap();
     nCount++;
	 }
	 
	 target.frontMostApp().mainWindow().scrollViews()[1].scrollRight();
	 target.delay(1);
	 
	 var cellsDay2 = target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[1].cells();
	 
	 if (cellsDay2.length >= 1)
	 {
     cellsDay2[cellsDay2.length - 1].scrollToVisible();
     target.delay(1);
     cellsDay2[cellsDay2.length - 1].tap();
     target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda list STAR OFF"].tap();
     nCount++;
	 
	 }
	 
	 target.frontMostApp().navigationBar().leftButton().tap(); 
	 target.frontMostApp().navigationBar().segmentedControls()[0].buttons()["Agenda list StarButton"].tap();
	 
	 
	 target.frontMostApp().mainWindow().tableViews()[0].cells()[0].buttons().firstWithPredicate("value == 1").tap();
	 nCount--;
	 
	 target.frontMostApp().mainWindow().tableViews()[0].cells()[0].buttons().firstWithPredicate("value == 1").tap();
	 nCount--;
	 
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