#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Speakers C28", function(target, app) {
	 
	target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Speakers"].tap();
	 var tableCells = target.frontMostApp().mainWindow().tableViews()[0].cells();	
	
	 for (i = 0 ; i < tableCells.length ; i++)
	 {
	 	var nameOfCell =  tableCells[i].name();
	 	
	 	tableCells[i].tap();
	 	UIALogger.logDebug("Speaker Details screen is the same as respective speaker in the previous screen");
	 	 	
	 
	 	var infoArray = target.frontMostApp().mainWindow().scrollViews()[0].staticTexts();	 	
	 
	 	var expectName = infoArray[1].name() + infoArray[0].name()+ infoArray[2].name();
	 
	 	for ( k = 0; k < 300 ; k++)
	 	{
	 		expectName = expectName.replace(" ","");
	 		expectName = expectName.replace(",","");
	 		nameOfCell = nameOfCell.replace(" ","");
	 		nameOfCell = expectName.replace(",","");
	 	}
		
	 	UIALogger.logDebug(expectName);
	 	UIALogger.logDebug(nameOfCell);
	 
	 	if (expectName == nameOfCell)

	 	{
	 		UIALogger.logPass("Pass element :" + i.toString());
	 		
	 	}
	 	else
	 	{
	 		UIALogger.logFail("Fail at element :" + i.toString());	 		
	 	}
	 
	 	
	 
	 
	 	//Verify();
	 	target.frontMostApp().navigationBar().leftButton().tap();
	 	UIALogger.logDebug("Speakers screen is shown with the same state ");
	 	var  tableCellsActual = target.frontMostApp().mainWindow().tableViews()[0].cells();	
		for (j = 0 ; j < tableCellsActual.length ; j++)
		 {
	 		if(tableCells[j].name() == tableCellsActual[j].name())
	 			UIALogger.logPass("Pass element :" + j.toString());
			else
	 		
	 			UIALogger.logFail("Fail at element :" + j.toString());
		 }
	 
	 	
	 }
	 
	 target.frontMostApp().navigationBar().leftButton().tap();
	 
	 	 
	 }	 
	 
	 
	 
	
	 
	 
	 
	 
);

function Verify(tableNames)
{
		UIALogger.logDebug("a");
}

