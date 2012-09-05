/*
This file provides methods to navigate to specify view.
This will go from nothing to the specify view, so kill the app first.

--- Usage:
nav.toSignUp();
nav.toUserInfo();
...
*/

#import "SignUp.js"
#import "UserInfo.js"
#import "PlanChooser.js"
#import "GoalProgress.js"
#import "GoalStart.js"
#import "History.js"
#import "About.js"
#import "../core/testcaseBase.js"

function Navigator()
{
	// =========================== Methods =====================
	this.toSignUp = toSignUp;
	this.toUserInfo = toUserInfo;
	this.toWeekGoal = toWeekGoal;
	this.toTodayGoal = toTodayGoal;
	this.toGoalStart = toGoalStart;
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
		signup = new SignUp();
		signup.fillEmailAndSubmit("test@misfit.com");
		wait();
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
			goal.scrollToDayProgress();
		}			
		
		// since second time the app will
		// automatic open this page from start
	}
	
	function toGoalStart()
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
		toHistory();
		history = new History();
		history.scrollToAbout();
		wait();
	}
}

nav = new Navigator();
