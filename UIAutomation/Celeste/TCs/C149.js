#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("Stress C149", function(target, app) {
	for (i = 0 ; i < 50 ; i++)
	{
		target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Maps"].tap();
	 	target.frontMostApp().navigationBar().leftButton().tap();
	 	target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda"].tap();
	 	target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells()[0].tap();
		target.frontMostApp().navigationBar().leftButton().tap();
		target.frontMostApp().navigationBar().leftButton().tap();
		target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Speakers"].tap();
	 	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[1].tap();
	 	target.frontMostApp().navigationBar().leftButton().tap();
	 	target.frontMostApp().navigationBar().leftButton().tap();
	 	target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Sponsors"].tap();
	 	target.frontMostApp().navigationBar().leftButton().tap();	 
	 
	 	target.frontMostApp().mainWindow().scrollViews()[0].scrollRight(); 
	 	target.delay(0.5);
	 	target.frontMostApp().mainWindow().scrollViews()[0].scrollRight();
	 	target.delay(0.5);
	 	target.frontMostApp().mainWindow().scrollViews()[0].scrollLeft();
	 	target.delay(0.5);
	 	target.frontMostApp().mainWindow().scrollViews()[0].scrollLeft();
	 	target.delay(0.5);
	}
	 
	 
	 
	 
});

