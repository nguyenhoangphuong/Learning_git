#import "SignUp.js"
#import "UserInfo.js"
#import "PlanChooser.js"
#import "GoalProgress.js"
#import "RunView.js"
#import "GoalPlan.js"
#import "About.js"
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
			wait(3);
		}
	}
	
}

tips = new Tips();
