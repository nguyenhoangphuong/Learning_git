#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Stress C151", function(target, app) {
	target.frontMostApp().mainWindow().scrollViews()[0].scrollRight();
	 target.delay(1); 	
	 target.frontMostApp().mainWindow().scrollViews()[0].scrollRight();
	 target.delay(1);
	 
	 //target.logElementTree();
	for(i = 0 ; i < 100 ; i++)
	 {
	 
	target.frontMostApp().mainWindow().scrollViews()[0].textViews()[0].setValue("lalala " + i.toString());
	 
	 
	 target.frontMostApp().mainWindow().scrollViews()[0].textFields()[0].setValue("khoa@yahoo.com");
	
	 target.frontMostApp().mainWindow().scrollViews()[0].textFields()[0].tap();
	 target.frontMostApp().windows()[1].toolbar().buttons()["Send"].tap();	  
	 
	 target.delay(1);
	 UIATarget.onAlert = function onAlert(alert) {		
	 alert.buttons()["OK"].tap();
	 return true;			
	}
	 
	 }
	 
	 
	
	target.frontMostApp().mainWindow().scrollViews()[0].scrollLeft();
	target.delay(1);
	target.frontMostApp().mainWindow().scrollViews()[0].scrollLeft();
	 
});
