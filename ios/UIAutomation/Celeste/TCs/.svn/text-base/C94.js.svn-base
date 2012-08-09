//#import "/Users/misfit/Desktop/tuneup_js/tuneup.js"

#import "/Users/jenkins/qa/Projects/tuneup_js/tuneup.js"

var target = UIATarget.localTarget();
var app = UIATarget.localTarget().frontMostApp();

test("C94", function(target, app) {

	target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.98, y:0.40}, endOffset:{x:0.21, y:0.42}});
	target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.98, y:0.40}, endOffset:{x:0.11, y:0.43}});
	 
	 
	 target.delay(2);
	 
	 
	if(target.frontMostApp().mainWindow().scrollViews()[0].buttons()[6].label()=="Feedback Star off" && target.frontMostApp().mainWindow().scrollViews()[0].buttons()[11].label()=="Feedback Star off" && target.frontMostApp().mainWindow().scrollViews()[0].buttons()[16].label() =="Feedback Star off")
	 {
	 		target.frontMostApp().mainWindow().scrollViews()[0].buttons()[6].tap();
	 		target.frontMostApp().mainWindow().scrollViews()[0].buttons()[11].tap();
	 		target.frontMostApp().mainWindow().scrollViews()[0].buttons()[16].tap();
	 
	 		target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.02, y:0.43}, endOffset:{x:0.87, y:0.62}});
	 		target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.02, y:0.43}, endOffset:{x:0.63, y:0.51}});
	 		target.frontMostApp().mainWindow().scrollViews()[0].buttons()["Agenda"].scrollToVisible();
	 		target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.91, y:0.60}, endOffset:{x:0.34, y:0.58}});
	 
	 		var isAlert = 0;
	 		
	 
	 		if(target.frontMostApp().mainWindow().scrollViews()[0].buttons()[6].label()=="Feedback Star on" && target.frontMostApp().mainWindow().scrollViews()[0].buttons()[11].label()=="Feedback Star on" && target.frontMostApp().mainWindow().scrollViews()[0].buttons()[16].label() =="Feedback Star on")
	 
	 		{
	 				UIALogger.logPass("The ratings still remain after switching to Menu screen and switching back to Feedbacks screen");
	 				
	 				target.frontMostApp().mainWindow().scrollViews()[0].buttons()[8].tap();
	 				target.frontMostApp().mainWindow().scrollViews()[0].buttons()[13].tap();
	 				target.frontMostApp().mainWindow().scrollViews()[0].buttons()[18].tap();
	 
	 				
	 				if(target.frontMostApp().mainWindow().scrollViews()[0].buttons()[8].label()=="Feedback Star off" && target.frontMostApp().mainWindow().scrollViews()[0].buttons()[13].label()=="Feedback Star off" && target.frontMostApp().mainWindow().scrollViews()[0].buttons()[18].label() =="Feedback Star off")
	 				
	 				{
	 					UIALogger.logPass("You can't rating again");
	 
	 					target.deactivateAppForDuration(2);
	 
	 					var timeout = 20;
     					var isMenuScreen = false;
     
     					target.pushTimeout(timeout);
	 					target.popTimeout();
	 					
	 					if(target.frontMostApp().mainWindow().scrollViews()[0].buttons()[6].label()=="Feedback Star on" && target.frontMostApp().mainWindow().scrollViews()[0].buttons()[11].label()=="Feedback Star on" && target.frontMostApp().mainWindow().scrollViews()[0].buttons()[16].label() =="Feedback Star on")
	 
				 		{
	 						UIALogger.logPass("The ratings still remain after pressing button Home");
	 
	 
						}
						else
						{
	 						UIALogger.logPass("The ratings don't be save after pressing button Home");
						}	 
	 					
	
	 				}
	 				else
	 				{
	 					UIALogger.logFail("You still rating again");
	 				}
	 
	 
	 							
	 							
	 			
	 		}
	 		else
	 		{
	 				UIALogger.logFail("The ratings still don't remain");
	 		}
	 		
	 
	 }
	 
	 
	
	 
	 
	 
	 
	 
	 
	target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.17, y:0.17}, endOffset:{x:0.81, y:0.17}});
	target.frontMostApp().mainWindow().scrollViews()[0].dragInsideWithOptions({startOffset:{x:0.17, y:0.08}, endOffset:{x:0.93, y:0.01}});
	 
	 
	 
	 
 });
	 
