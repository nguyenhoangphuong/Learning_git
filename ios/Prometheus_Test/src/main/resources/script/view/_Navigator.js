#import "_Tips.js"
#import "MultiGoalChooser.js"
#import "../core/testcaseBase.js"

/*
This file provides methods to navigate to specify view.
This will go from nothing to the specify view, so kill the app first.

--- Functions:
- toHome()					:	to Home screen
- toUserInfo(email, pwd)	:	to UserInfo screen
	+ null, null			:	go to UserInfo by choosing [Try out] without email
	+ email, null			:	go to UserInfo by [Try out] with email
	+ email, pwd			:	go to UserInfo by [Log in]
- toActivity(email, pwd, userinfo)	:	go to ActivitiyChooser screen
	+ userinfo = null		:	use default user info
	+ userinfo = {h1, h2, w1, w2, age, sex, unit}
		ex:	     {100, 0.1, 1, 0.75, 20, "male" ("female"), "si" ("us")}
- toPlanChooser(email, pwd, userinfo, activity)
	+ activity			: name (string) or index (int)
		ex: "Swimming" or 3
- toWeekGoal(email, pwd, userinfo, activity, number)
	+ number: number of unit / miles / ...
		ex: 30
- toTodayGoal(email, pwd, userinfo, activity, number)
- toRunView(email, pwd, userinfo, activity, number)
- toGoalPlan(email, pwd, userinfo, activity, number)
- toHistory(email, pwd, userinfo, activity, number)
- toSettings(email, pwd, userinfo, activity, number)
*/

function Navigator()
{
	// =========================== Methods =====================
	this.toHome = toHome;	
	this.toUserInfo = toUserInfo;
	this.toMultiGoalChooser = toMultiGoalChooser;
	this.toPlanChooser = toPlanChooser;	
	this.to7DayGoal = to7DayGoal;
	this.toTodaysGoal = toTodaysGoal;
	this.toRunView = toRunView;
	this.toPlanner = toPlanner;
	this.toHistory = toHistory;
	this.toSettings = toSettings;
	
	// ====================== Method definitions ================
	function toHome()
	{
		// wait for app to load
		wait(3);
		
		// skip the whats news if there is one
		var wn = new WhatsNew();
		if(wn.isVisible())
		{
			print("=> Skip the What news...");
			wn.tapButton();
			wait(1);
			print("=> Go to Home screen...");
		}
		
		// reached
		var h = new Home();
		return (h.isVisible()? h : null);
	}
	
	function toUserInfo(email, password)
	{
		// go to Home first
		toHome();
		h = new Home();
		
		if(h.isVisible())
		{
			if(email == null || (typeof email == "undefined"))
			{
				// try out
				print("=> Go to UserInfo screen by trying out ...");
				h.tryOut();
				wait(2);
			}
			else
			{
				// sign up
				print("=> Go to UserInfo screen by signing up ...");
				h.signUp(email, password);
				wait(2);
			}
		}
		
		// reached
		var ui = new UserInfo();
		return ui.isVisible() ? ui : null;
	}
	
	function toMultiGoalChooser(email, password, uinfo)
	{
		// go to UserInfo first

		toUserInfo(email, password);
		ui = new UserInfo();
		
		if (ui.isVisible())
		{
			print("=> Go to MultiGoalChooser screen ...");
			
			// pick value
			if (uinfo != null)
			{
				ui.setSex(uinfo.sex);
				ui.setUnit(uinfo.unit);
				ui.setInfo(uinfo.age, uinfo.w1, uinfo.w2, uinfo.h1, uinfo.h2);
			}
			
			ui.submit();
			wait(1);
		}
		
		// reached
		a = new MultiGoalChooser();
		return a.isVisible() ? a : null;
	}
	
	function toPlanChooser(email, password, uinfo, activity)
	{
		// go to Activity first
		toMultiGoalChooser(email, password, uinfo);
		a = new MultiGoalChooser();
		
		if(a.isVisible())
		{
			print("=> Go to PlanChooser screen...");
			
			if (activity!=null)
			{
				// pick activity type
				a.chooseActivityWithIndex(activity);
			}
			else 
				a.chooseActivityWithIndex(0);
		}
		
		// reached
		pc = new PlanChooser();	
		return (pc.isVisible() ? pc : null);
	}
	
	function to7DayGoal(email, password, uinfo, activity, number)
	{
		// to PlanChooser first
		toPlanChooser(email, password, uinfo, activity);
		pc = new PlanChooser();
		
		// if current view is PlanChooser
		if(pc.isVisible())
		{
			print("=> Go to WeekGoal screen from UserInfo...");
			
			// select plan
			pc.selectOther();
			wait();
			if (number !=null)
				pc.setValue(number);
			
			pc.done();
			
			// close tips
			wait();
			tips.closeTips(1);
		}
		// else: then it must be TodayGoal
		else
		{
			goal = new GoalProgress();
			
			if(goal.isTodaysGoalVisible())
			{
				print("=> Go to WeekGoal screen from TodayGoal...");
				goal.scrollToWeekGoal();
			}
		}
		
		// reached
		gp = new GoalProgress();
		return (gp.isWeekGoalVisible() ? gp : null);
	}
	
	function toTodaysGoal(email, password, uinfo, activity, number)
	{
		// to WeekGoal first
		to7DayGoal(email, password, uinfo, activity, number);
		goal = new GoalProgress();
		
		if(goal.isWeekGoalVisible())
		{
			print("=> Go to TodayGoal screen...");
			goal.scrollToTodaysGoal();
		}
		
		return goal.isTodaysGoalVisible() ? goal : null;
	}
	
	function toRunView(email, password, uinfo, activity, number)
	{
		// go to TodaysGoal first
		gp = toTodaysGoal(email, password, uinfo, activity, number);
		print("=> Go to RunView screen...");
		
		// click Start
		goal.start();
		wait();
		
		// return RunView view object
		rv = new RunView();
		if(rv.isVisible())
			return rv;
		
		return null;
	}
	
	function toPlanner(email, password, uinfo, activity, number)
	{
		// go to TodayGoal first
		gp = toTodaysGoal(email, password, uinfo, activity, number);
		print("=> Go to Planner screen...");
		
		// swipe right
		gp.scrollToPlanner();
		wait();
		
		// return RunView view object
		p = new Planner();
		if(p.isVisible())
			return p;
		
		return null;
	}
	
	function toHistory(email, password, uinfo, activity, number)
	{
		// go to Planner first
		p = toPlanner(email, password, uinfo, activity, number);
		print("=> Go to History screen...");
		
		// swipe down
		p.scrollToHistory();
		wait();
		
		// return RunView view object
		h = new History();
		if(h.isVisible())
			return h;
		
		return null;
	}
	
	function toSettings(email, password, uinfo, activity, number)
	{
		// go to TodayGoal first
		gp = toTodaysGoal(email, password, uinfo, activity, number);
		print("=> Go to Setting screen...");
		
		// swipe right
		gp.scrollToSettings();
		wait();
		
		// return RunView view object
		s = new Settings();
		if(s.isVisible())
			return s;
		
		return null;
	}
}

nav = new Navigator();
