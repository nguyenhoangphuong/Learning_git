#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Speakers C26", function(target, app) {
	target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Speakers"].tap();
	 var tableCells = target.frontMostApp().mainWindow().tableViews()[0].cells();	
	
	 for (i = 0 ; i < tableCells.length ; i++)
	 {
	 	UIALogger.logDebug(i.toString());
	  
	 	var nameOfCell =  tableCells[i].name();
	 
	 //check 3 field not null
	 	var nameSplits = nameOfCell.split(",");
	 	if (nameSplits.length >= 3)
	 		UIALogger.logPass("have >3 fields");
	 	else 
	 		UIALogger.logFail("Fail in element" + i.toString());
	 
	 	for (j=0; j < nameSplits.length ; j++)
	 	{
	 		var nameTrimSplit = nameSplits[j].replace(/^\s*/,"");
	 		if (nameTrimSplit == "")
	 			UIALogger.logFail("Fail in element" + i.toString());
	 		else
	 		{
	 			if (j==0)
	 				UIALogger.logPass("Not null in name");
	 			if (j==1)
	 				UIALogger.logPass("Not null in position");
	 			if (j==2)
	 				UIALogger.logPass("Not null in organization");
	 		}
	 	}
	
	 
	 
	 
	 
	 	var checkCells = tableCells.withName(nameOfCell);
	 	if ( checkCells.length > 1 )
	 		UIALogger.logFail(nameOfCell + 'is duplicated' );
	 	else
	 		UIALogger.logPass("No duplicated");
	 }
	 
	 target.frontMostApp().navigationBar().leftButton().tap();
		 

		 
		 
	 });


