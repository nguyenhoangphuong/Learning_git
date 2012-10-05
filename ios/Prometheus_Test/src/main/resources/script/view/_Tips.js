#import "WhatsNew.js"
#import "Home.js"
#import "UserInfo.js"
#import "PlanChooser.js"
#import "GoalProgress.js"
#import "MusicSetting.js"
#import "RunView.js"
#import "GoalPlan.js"
#import "Settings.js"
#import "History.js"
#import "../core/testcaseBase.js"

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
			 	"To assist you with a good fitness plan, please input your age, weight and height",
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
			 	"Swipe left to settings.\nSwipe right for weekly plan"
			],
			
			"GoalPlan":
			[
			 	"Weekly Plan is where you can adjust the number of miles you want to run for each day",
			 	"With smart goal Assistant, the app will auto-suggesting an appropriate plan for your profile within 7 days",
			 	"Edit button will allow you to manually adjust the plan for each day",
			 	"This bar indicates the goal for each day",
			 	"The white bar indicates the progress"
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
