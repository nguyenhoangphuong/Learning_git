#import "../core/testcaseBase.js"

#import "SetUp/WhatsNew.js"
#import "SetUp/SignIn.js"
#import "SetUp/UserInfo.js"

#import "PlanBuilder/PlanPicker.js"
#import "PlanBuilder/PlanInfo.js"
#import "PlanBuilder/PlanBuilder.js"

#import "Home/Progress.js"
#import "Home/Planner.js"
#import "Home/Settings.js"
#import "Home/History.js"

#import "Tracker/Tracking.js"
#import "Tracker/ManualTracking.js"
#import "Tracker/GPSTracking.js"
#import "Tracker/MusicSetting.js"

/*
This handles the tips
...
*/

function Tips()
{
	// =========================== Methods =====================
	this.isTipsDisplay = isTipsDisplay;
	this.closeTips=closeTips;
	
	// ========================= Constants =====================
	this.Tips = 
		{
			"UserInfo":
			[
			 	//"To assist you with a good fitness plan, please input your age, weight and height",
			 	"Please help \"Shine\" build your personalized plan by inputting your age, weight & height. ",
			 	"Swipe up or down to adjust height",
			 	"Swipe left or right to adjust weight",
			 	"Tap here for manual input"
			],
			
			"WeekGoal":
			[
			 	"Swipe up or down to switch view"
			],
	
			"TodayGoal":
			[
			 	//"Swipe left to settings.\nSwipe right for weekly plan"
			 	"Swipe right for settings.\nSwipe left for seven day plan."
			],
			
			"GoalPlan":
			[
			 	"You can adjust how much you want to train each day in your seven day plan.",
			 	"\"Shine\" automatically builds a personalized plan across the next 7 days for you.",
			 	"Edit button allows you to build your own weekly plan",
			 	"This bar indicates each day’s goal",
			 	"The white bar shows that day’s progress."
			],
		};
	
	// ====================== Method definition ================
	function isTipsDisplay(view, win)
	{
		wait(2);
		thetips = this.Tips[view];
		for(i = 0; i < thetips.length; i++)
		{
			if(staticTextExist(thetips[i], win))
			{
				log("Tips on " + view + " is shown up: " + thetips[i]);
				return true;
			}
		}
		
		log("Tips on " + view + " is shown up: false");
		return false;
	}
	
	function closeTips(numberOfTips) 
	{
		log("Closing tips");
		
		wait();
		for (i=0; i< numberOfTips; i++) 
		{
			target.tap({x:154.00, y:200.00});
			wait(2);
		}
		wait();
	}
	
}

tips = new Tips();
