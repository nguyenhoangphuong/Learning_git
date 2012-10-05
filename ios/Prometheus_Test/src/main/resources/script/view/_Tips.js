#import "WhatsNew.js"
#import "Home.js"
#import "UserInfo.js"
#import "PlanChooser.js"
#import "GoalProgress.js"
#import "MusicSetting.js"
#import "RunView.js"
#import "GoalPlan.js"
#import "Settings.js"
#import "../core/testcaseBase.js"

/*
This handles the tips
...
*/

function Tips()
{
	// =========================== Methods =====================
	this.closeTips=closeTips
	
	// ====================== Method definition ================

	function closeTips(numberOfTips) {
		log("Closing tips");
		for (i=0; i< numberOfTips; i++) {
			target.tap({x:154.00, y:200.00});
			wait(4);
		}
		wait(2);
	}
	
}

tips = new Tips();
