#import "_Tips.js"
#import "../core/testcaseBase.js"
#import "Home.js"
#import "UserInfo.js"

/*
This file provides methods to navigate to specify views.
This will go from nothing to a specify view, therefore you need to
kill the app first.

--- Functions:
- toHome()
- toUserInfo(email, pwd)			: go to UserInfo by trying out
- toMultiGoalChooser(userinfo)
	+ userinfo = null	: use default user info
	+ userinfo = {h1, h2, w1, w2, age, sex, unit}
		ex: {100, 0.1, 1, 0.75, 20, "male" ("female"), "si" ("us")}
- toPlanChooser(email, pwd, userinfo, activity)
	+ activity			: name (string) or index (int)
		ex: "Swimming" or 3
- to7DayGoal(email, pwd, userinfo, activity, number)
	+ number			: number of unit / miles / ...
		ex: 30
- toTodaysGoal(email, pwd, userinfo, activity, number)
- toRunView(email, pwd, userinfo, activity, number)
- toPlanner(email, pwd, userinfo, activity, number)
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
		var h = new Home();
		
		log("Go to Home screen ...");
		h.skipWhatsNew();
		
		return h.isVisible() ? h : null;
	}
	
	function toUserInfo()
	{
		var h = new Home();
		
		log("Go to UserInfo screen by trying out ...");
		h.tryOut();
		wait(2);
		
		var ui = new UserInfo();
		
		return ui.isVisible() ? ui : null;
	}
	
	function toMultiGoalChooser(uinfo)
	{
		// go to UserInfo first
		var ui = toUserInfo();
		
		if (ui == null)
		{
			log("Cannot go to UserInfo screen");
			return null;
		}
		
		log("Go to MultiGoalChooser screen ...");
		
		// pick value
		if (uinfo != null)
		{
			ui.setInfo(uinfo.age, uinfo.w1, uinfo.w2, uinfo.h1, uinfo.h2);
			ui.setSex(uinfo.sex);
			ui.setUnit(uinfo.unit);
		}
		
		ui.submit();
		wait(1);
		
		a = new MultiGoalChooser();
		
		return a.isVisible() ? a : null;
	}
	
	function toPlanChooser(email, password, uinfo, activity)
	{
		// go to Activity first
		a = toActivity(email, password, uinfo);
		print("=> Go to PlanChooser screen...");
		
		// pick activity type
		a.pickActivity(activity);
		
		// return PlanChooser view object
		pc = new PlanChooser();
		if(pc.isVisible())
			return pc;
		
		return null;
	}
	
	function to7DayGoal(email, password, uinfo, activity, number)
	{
		// wait for the app to load
		wait(3);
		h = new Home();
		
		// first time running
		if(h.isVisible())
		{
			// to PlanChooser first
			pc = toPlanChooser(email, password, uinfo, activity);
			print("=> Go to WeekGoal screen...");
			
			// select plan
			pc.selectOther();
			pc.setValue(number);
			pc.done();
			
			// close tips
			wait();
			tips.closeTips(1);
		}
		// second time and on
		else
		{
			goal = new GoalProgress();
			goal.scrollToWeekProgress();
		}
		
		// return GoalProgress view object
		gp = new GoalProgress();
		if(gp.isWeekGoalVisible())
			return gp;
		
		return null;
	}
	
	function toTodaysGoal(email, password, uinfo, activity, number)
	{
		// wait for app to load
		wait(3);
		h = new Home();

		// first time running
		if(h.isVisible())
		{
			// go to WeekGoal first
			gp = to7DayGoal(email, password, uinfo, activity, number);
			print("=> Go to TodayGoal screen...");
			 
			// swipe down
			gp.scrollToDayGoal();
			
			return gp;
		}			
		
		// since second time the app will
		// automatic open this page from start
		
		// return GoalProgress view object
		gp = new GoalProgress();
		if(gp.isTodayGoalVisible())
			return gp;
		
		return null;
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
	
	function toSettings()
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