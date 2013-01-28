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
#import "Tracker/Music.js"

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
			"Planner":
				[
				 "Tap to change your goals",
				 "Tap here to make changes.",
				 "This will automatically adjust your goals based on your performance."
				]
		};
	
	// ====================== Method definition ================
	function isTipsDisplay(view, win)
	{
		if(typeof win == "undefined")
			win = app.mainWindow();
		
		wait();
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
		if(typeof numberOfTips == "undefined")
			numberOfTips = 1;
		
		log("Closing tips");
		
		wait();
		for (i=0; i< numberOfTips; i++) 
		{
			target.tap({x:154.00, y:200.00});
			wait();
		}
		wait();
	}
	
}

tips = new Tips();
