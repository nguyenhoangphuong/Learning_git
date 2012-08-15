#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"
//#import "/Users/tungnguyen/Dropbox/MISFIT-QA/SVN/MQ/trunk/src/UIAutomation/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

function assertMenuScreen() {
	assertEquals("Maps", app.mainWindow().scrollViews()[0].buttons()["Maps"].name());
	assertEquals("Agenda", app.mainWindow().scrollViews()[0].buttons()["Agenda"].name());
	assertEquals("Speakers", app.mainWindow().scrollViews()[0].buttons()["Speakers"].name());
	assertEquals("Sponsors", app.mainWindow().scrollViews()[0].buttons()["Sponsors"].name()); 
}

test("Menu C20", function(target, app) {
	 // map
	 app.mainWindow().scrollViews()[0].buttons()["Maps"].tap();
	 assertEquals("Venue Map", app.navigationBar().name());
	 app.navigationBar().leftButton().tap();
	 assertMenuScreen();
	 
	 // agenda
	 app.mainWindow().scrollViews()[0].buttons()["Agenda"].tap();
	 assertEquals("Agenda", app.navigationBar().name());
	 app.navigationBar().leftButton().tap();
	 assertMenuScreen();
	 
	 // speakers
	 app.mainWindow().scrollViews()[0].buttons()["Speakers"].tap();
	 assertEquals("Speakers", app.navigationBar().name());
	 app.navigationBar().leftButton().tap();
	 assertMenuScreen();
	 
	 // sponsors
	 app.mainWindow().scrollViews()[0].buttons()["Sponsors"].tap();
	 assertEquals("Sponsors", app.navigationBar().name());
	 app.navigationBar().leftButton().tap();
	 assertMenuScreen();
	 
	 // swipe finger to the right
	 app.mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.09, y:0.75}, endOffset:{x:0.88, y:0.73}});
	 assertMenuScreen();
	 
	 // swipe finger to the left
	 app.mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.89, y:0.75}, endOffset:{x:0.28, y:0.79}});
	 try { // assert News screen
	 	app.mainWindow().pageIndicators()[0].selectPage(1);
	 	UIALogger.logPass();
	 }
	 catch (error) {
	 	UIALogger.logFail("Menu screen does not link to News screen");
	 }
});