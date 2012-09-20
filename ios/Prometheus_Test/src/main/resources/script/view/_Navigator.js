#import "SignUp.js"
#import "UserInfo.js"
#import "PlanChooser.js"
#import "GoalProgress.js"
#import "RunView.js"
#import "GoalPlan.js"
#import "About.js"
#import "_Tips.js"
#import "../core/testcaseBase.js"

/*
This file provides methods to navigate to specify view.
This will go from nothing to the specify view, so kill the app first.

--- Usage:
nav.toSignUp();
nav.toUserInfo();
...
*/

function Navigator()
{
	// =========================== Methods =====================
	this.toSignUp = toSignUp;
	this.toUserInfo = toUserInfo;
	this.toWeekGoal = toWeekGoal;
	this.toPlanChooser = toPlanChooser;
	this.toTodayGoal = toTodayGoal;
	this.toRunView = toRunView;
	this.toHistory = toHistory;
	this.toAbout = toAbout;
	
	// ====================== Method definition ================
	function toSignUp()
	{
		// just wait for the app to load ^^
		wait(3);
	}
	
	function toUserInfo()
	{
		toSignUp();
		if(staticTextExist("SHINE"))
		{
			var signup = new SignUp();
			if (signup.isEmailTextFieldVisible() == 1) {
				log("Email is visible");
			}
			else
				log("No email visible");

			signup.fillEmailAndSubmit("abcd@test.com");

			if (signup.isEmailTextFieldVisible() == 1) {
				UILogger.logFail("Should succeed");
			}
		}
		else
			log("Can't find SHINE title!");
		
	}
	
	function toPlanChooser()
	{
		toUserInfo();
		userinfo = new UserInfo();
		userinfo.submit();
		wait();
	}
	
	function toWeekGoal()
	{
		wait(3);
		signup = new SignUp();
		
		// first time running
		if(signup.isEmailTextFieldVisible())
		{
			toPlanChooser();
			plan = new PlanChooser();
			plan.selectEasy();
		}
		// second time
		else
		{
			goal = new GoalProgress();
			goal.scrollToWeekProgress();
		}
		
		wait();
		tips.closeTips(1);
	}
	
	function toTodayGoal()
	{
		wait(3);
		signup = new SignUp();

		// first time running
		if(signup.isEmailTextFieldVisible())
		{
			toWeekGoal();
			goal = new GoalProgress();
//			goal.scrollToDayProgress();
			goal.scrollToDayGoal();
		}			
		
		// since second time the app will
		// automatic open this page from start
	}
	
	function toRunView()
	{
		toTodayGoal();
		goal = new GoalProgress();
		goal.start();
		
		wait();
	}
	
	function toHistory()
	{
		toTodayGoal();
		goal = new GoalProgress();
		goal.scrollToHistory();
		wait();
	}
	
	function toAbout()
	{
		toTodayGoal();
		goal = new GoalProgress();
		goal.scrollToAbout();
		wait();
	}
}

nav = new Navigator();
