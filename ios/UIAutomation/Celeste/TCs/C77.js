#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("C77", function(target, app) {
	target.frontMostApp().mainWindow().scrollViews()[0].scrollRight();
	target.delay(1); 

	 for (i = 0 ; i < 12 ; i++)
	 {
	 	UIALogger.logDebug("------------------------------");
	 	var tableCells = target.frontMostApp().mainWindow().scrollViews()[0].tableViews()[0].cells();
	 
	 	var num = tableCells.length;
	 	UIALogger.logDebug(num.toString());
	 	target.frontMostApp().mainWindow().scrollViews()[0].tableViews()["Empty list"].cells()[num - 3].scrollToVisible();
	 	
	 	target.flickFromTo({x:160, y:400}, {x:160, y:100});
	 	target.delay(8);
  
  	 }
 
	 var verifyTableCells = target.frontMostApp().mainWindow().scrollViews()[0].tableViews()[0].cells();
	
						
	 if (verifyTableCells.length <= 500)
	 	UIALogger.logPass("Pass . num of cells :" + verifyTableCells.length.toString());
	 else
	 	UIALogger.logFail("Fail . num of cells :" + verifyTableCells.length.toString());
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].scrollLeft();
	 target.delay(1);
	 
	 
	 
	 });