#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();


test("AgendaDetail C66", function(target, app) {
	 target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda"].tap();
	 target.frontMostApp().navigationBar().segmentedControls()[0].buttons()[0].tap();	 
	 target.frontMostApp().mainWindow().scrollViews()[1].tableViews()[0].cells()[0].tap();
	 
	 var buttons = target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons();
     
     var name0 = buttons[0].name();
     var name1 = buttons[1].name();
     var name2 = buttons[2].name();
     var name3 = buttons[3].name();
     var name4 = buttons[4].name();
     if (name0 == "Agenda list STAR ON")
     {
     target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[2].tap();
	 
     assertEquals(name0, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[0].name());
     assertEquals(name1, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[1].name());
     assertEquals(name2, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[2].name());
     assertEquals(name3, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[3].name());
     assertEquals(name4, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[4].name());
	 
     target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[4].tap();
     assertEquals(name0, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[0].name());
     assertEquals(name1, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[1].name());
     assertEquals(name2, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[2].name());
     assertEquals(name3, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[3].name());
     assertEquals(name4, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[4].name()); 		
     
     }
     else
     {
     target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[0].tap(); 
     if (target.frontMostApp().mainWindow().buttons()[0].isValid())			
     target.frontMostApp().mainWindow().buttons()[0].tap(); 		
     
     target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[2].tap(); 
     assertEquals(name0, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[0].name());
     assertEquals(name1, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[1].name());
     assertEquals(name2, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[2].name());
     assertEquals(name3, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[3].name());
     assertEquals(name4, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[4].name());
     
     target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[4].tap(); 
     assertEquals(name0, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[0].name());
     assertEquals(name1, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[1].name());
     assertEquals(name2, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[2].name());
     assertEquals(name3, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[3].name());
     assertEquals(name4, target.frontMostApp().mainWindow().scrollViews()[0].images()[2].buttons()[4].name());		
     }
     
     target.frontMostApp().navigationBar().leftButton().tap();
     target.frontMostApp().navigationBar().leftButton().tap();
	 
	 
	 });