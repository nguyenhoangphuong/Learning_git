#import "_Tips.js"
#import "MultiGoalChooser.js"
#import "../core/testcaseBase.js"

/*
This file provides methods to navigate to specify view.
This will go from nothing to the specify view, so kill the app first.

--- Functions:
- toHome()						:	to Home screen
- toUserInfo(email, pwd, login)	:	to UserInfo screen
	+ null, null				:	go to UserInfo by choosing [Try out] without email
	+ email, null				:	go to UserInfo by [Try out] with email
	+ email, pwd				:	go to UserInfo by [Log in]
- toMultiGoalChooser(email, pwd, userinfo, login)	:	go to ActivitiyChooser screen
	+ userinfo = null			:	use default user info
	+ userinfo = {w1, w2, wu, h1, h2, hu, age, sex}
		ex:	     {100, 0.1, "kg", 1, 0.75, "meter", 20, "male" ("female")}
- toPlanChooser(email, pwd, userinfo, activity, login)
	+ activity			: name (string) or index (int)
		ex: "Swimming" or 3
- toWeekGoal(email, pwd, userinfo, activity, number, login)
	+ number: number of unit / miles / ...
		ex: 30
- toTodayGoal(email, pwd, userinfo, activity, number, login)
- toRunView(email, pwd, userinfo, activity, number, login)
- toGoalPlan(email, pwd, userinfo, activity, number, login)
- toHistory(email, pwd, userinfo, activity, number, login)
- toSettings(email, pwd, userinfo, activity, number, login)
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
	this.toMusic = toMusic;
	this.toRunView = toRunView;
	this.toPlanner = toPlanner;
	this.toHistory = toHistory;
	this.toSettings = toSettings;
	
	// ====================== Method definitions ================
	function toHome()
	{	
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
	
	function toUserInfo(email, password, login)
	{
		if(typeof login == "undefined")
			login = false;
		
		// go to Home first
		toHome();
		h = new Home();
		
		if(h.isVisible())
		{
			if(login)
			{
				// log in
				print("=> Go to MultiGoalChooser screen by logging in ...");
				h.login(email, password);
				wait(2);
			}
			else if(email == null || (typeof email == "undefined"))
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
		if(ui.isVisible())
		{
			// if there are any tips, close it
			if(tips.isTipsDisplay("UserInfo"))
				tips.closeTips(4);
			return ui;
		}
		else
			return null;
	}
	
	function toMultiGoalChooser(email, password, uinfo, login)
	{
		// go to UserInfo first
		toUserInfo(email, password, login);
		ui = new UserInfo();
		
		// continue
		if (ui.isVisible())
		{
			print("=> Go to MultiGoalChooser screen ...");
			
			// pick value
			if (uinfo != null)
			{
				ui.setSex(uinfo.sex);
				ui.setInfo(uinfo.age, uinfo.w1, uinfo.w2, uinfo.wu, uinfo.h1, uinfo.h2, uinfo.hu);
			}
			
			ui.submit();
			wait(1);
		}
		
		// reached
		a = new MultiGoalChooser();
		return a.isVisible() ? a : null;
	}
	
	function toPlanChooser(email, password, uinfo, activity, login)
	{
		// go to Activity first
		toMultiGoalChooser(email, password, uinfo, login);
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
	
	function to7DayGoal(email, password, uinfo, activity, number, login)
	{
		// to PlanChooser first
		toPlanChooser(email, password, uinfo, activity, login);
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
		if(gp.isWeekGoalVisible())
		{
			if(tips.isTipsDisplay("WeekGoal", app.mainWindow().scrollViews()[0]))
				tips.closeTips(1);
			return gp;
		}
		else
			return null;
	}
	
	function toTodaysGoal(email, password, uinfo, activity, number, login)
	{
		// to WeekGoal first
		to7DayGoal(email, password, uinfo, activity, number, login);
		goal = new GoalProgress();
		
		if(goal.isWeekGoalVisible())
		{
			print("=> Go to TodayGoal screen...");
			goal.scrollToTodaysGoal();
		}
		
		// reached
		if(goal.isTodaysGoalVisible())
		{
			if(tips.isTipsDisplay("TodayGoal", app.mainWindow().scrollViews()[0]))
				tips.closeTips(1);
			return goal;
		}
		else
			return null;
	}
	
	function toRunView(email, password, uinfo, activity, number, login)
	{
		// go to TodaysGoal first
		toTodaysGoal(email, password, uinfo, activity, number, login);
		goal = new GoalProgress();
		
		if(goal.isTodaysGoalVisible())
		{
			print("=> Go to RunView screen...");
			
			// click Start
			goal.start();
			wait();
		}
		
		// return RunView view object
		rv = new RunView();
		return rv.isVisible() ? rv : null;
	}
	
	function toMusic(email, password, uinfo, activity, number, login)
	{
		// go to TodaysGoal first
		toTodaysGoal(email, password, uinfo, activity, number, login);
		goal = new GoalProgress();
		
		if(goal.isTodaysGoalVisible())
		{
			print("=> Go to Music screen...");
			
			// tap music
			goal.tapMusic();
			wait();
		}
		
		// return RunView view object
		rv = new RunView();
		return rv.isVisible() ? rv : null;
	}
	
	function toPlanner(email, password, uinfo, activity, number, login)
	{
		// go to TodayGoal first
		toTodaysGoal(email, password, uinfo, activity, number, login);
		goal = new GoalProgress();
		
		if(goal.isTodaysGoalVisible())
		{
			print("=> Go to Planner screen...");
			
			// swipe right
			goal.scrollToPlanner();
			wait();
		}
		
		// return RunView view object
		p = new GoalPlan();
		if(p.isVisible())
		{
			if(tips.isTipsDisplay("GoalPlan", app.mainWindow().scrollViews()[0]))
				tips.closeTips(5);
			return p;
		}
		else
			return null;
	}
	
	function toHistory(email, password, uinfo, activity, number, login)
	{
		// go to Planner first
		toPlanner(email, password, uinfo, activity, number, login);
		p = new GoalPlan();
		
		if(p.isVisible())
		{
			print("=> Go to History screen...");
			
			// swipe down
			p.scrollToHistory();
			wait();
		}
		
		// return RunView view object
		h = new History();
		return h.isVisible() ? h : null;
	}
	
	function toSettings(email, password, uinfo, activity, number, login)
	{
		// go to TodayGoal first
		toTodaysGoal(email, password, uinfo, activity, number, login);
		goal = new GoalProgress();
		
		if(goal.isTodaysGoalVisible())
		{
			print("=> Go to Setting screen...");
			
			// swipe right
			gp.scrollToSettings();
			wait();
		}
		
		// return RunView view object
		s = new Settings();
		return s.isVisible() ? s : null;
	}
}

nav = new Navigator();
