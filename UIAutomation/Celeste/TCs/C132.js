//#import "/Users/trongvu/open-source/tuneup_js/tuneup.js"
#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"
var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C132: Old state of Agenda Comments screen", function(target, app) {
     //target.lockForDuration(1);

     // go to agenda screen
     app.mainWindow().scrollViews()[0].buttons()["Agenda"].tap();
         
     // Find the session have not been raised yet
     var index = findFirstUnraisedSession(target, app);
	 
	 
	 target.delay(2);
     
     // raised 2 stars
     target.frontMostApp().mainWindow().scrollViews()[0].images()["Agenda detail_Rating.png"].buttons()[2].tap(); 
     
     target.delay(1);
     
     // deavice the app as press Home button
     target.deactivateAppForDuration(2);

     var timeout = 20;
     
     // set time out to verify agenda screen come back
     target.pushTimeout(timeout);
     
     var item = app.navigationBar().staticTexts()["Agenda"];
     item.isValid() ? UIALogger.logPass("Agenda") : UIALogger.logFail("Agenda");
     
     item = app.mainWindow().scrollViews()[0].staticTexts()["Rate this session"];
     item.isValid() ? UIALogger.logPass("Rate this session") : UIALogger.logFail("Rate this session");
     
     item = app.mainWindow().staticTexts()["Do you have anything else to share?"];
     item.isValid() ? UIALogger.logPass("Do you have anything else to share?") : UIALogger.logFail("Do you have anything else to share?");
     
     target.popTimeout();
     
     target.frontMostApp().navigationBar().leftButton().tap();
     target.frontMostApp().navigationBar().leftButton().tap();
     
});

function findFirstUnraisedSession(target, app) {

    var cells = target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells();
    for (i = 0 ; i < cells.length ; i++)
    {
        cells[i].tap();
		
		target.delay(2);
        var tag = target.frontMostApp().mainWindow().scrollViews()[0].images()["Agenda detail_Rating_new.png"].buttons()[0].name();
        if (tag == "Agenda list STAR ON")
        {
            target.frontMostApp().navigationBar().leftButton().tap();
        }
        else
        {
            return i;
            target.frontMostApp().navigationBar().leftButton().tap();
            target.frontMostApp().navigationBar().leftButton().tap();        
        }
     
    }
}