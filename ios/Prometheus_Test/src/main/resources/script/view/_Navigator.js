#import "_TabBar.js"
#import "PlanBuilder/PlanBuilderData.js"
/*
  This file provides methods to navigate to specify view.
  =============================================================================
  
  - toSignIn() : to Home screen 
  
  - toUserInfo(email, pwd, login) : to UserInfo screen 
  			+ email, pwd, true  : use login
  			+ email, pwd, false : use signup
  			+ null, null, null  : use tryout
  			
  - toPlanPicker(email, pwd, userinfo, login) : go to PlanPicker screen 
  			+ userinfo = null : use default user info 
  			+ userinfo = {w1, w2, wu, h1, h2, hu, age, sex} 
  			ex: {100, 0.1, "kg", 1, 0.75, "meter", 20, "male" ("female")} 
  			
  - toPlanInfo(email, pwd, userinfo, planinfo, login) 
  			+ planinfo.type = Easy/Normal/Active/Custom
  			+ planinfo.name = The Starter's Plan
  
  - toPlanBuilder(email, pwd, userinfo, login)
  
  - toProgress(email, pwd, userinfo, pinfo, login)
  			+ planinfo.type = Easy/Normal/Active/Custom/New
  			+ planinfo.name = The Starter's Plan
  			+ planinfo.activities = 
  				[{name: Running, value: 10}, {name: Push-up, value: 50}]
  			note: activities is used only for New mode
  - toPlanner(email, pwd, userinfo, pinfo, login) 
  - toHistory(email, pwd, userinfo, pinfo, login) 
  - toSettings(email, pwd, userinfo, pinfo, login) 
  
  - toTracking()
  - toManualTracking()
  - toGPSTracking()
  - toMusicSetting()
 */

function Navigator()
{
	// =========================== Methods =====================
	this.toSignIn = toSignIn;
	this.toSignUp = toSignUp;
	this.toUserInfo = toUserInfo;
	
	this.toPlanPicker = toPlanPicker;
	this.toPlanInfo = toPlanInfo;
	this.toPlanBuilder = toPlanBuilder;
	
	this.toProgress = toProgress;
	this.toPlanner = toPlanner;
	this.toHistory = toHistory;
	this.toSettings = toSettings;
	
	this.toTracking = toTracking;
	this.toManualTracking = toManualTracking;
	this.toGPSTracking = toGPSTracking;
	this.toMusic = toMusic;
	
	// ====================== Method definitions ================
	function toSignUp()
	{	
		// skip the whats news if there is one
		var wn = new WhatsNew();
		if(wn.isVisible())
		{
			print("=> Skip the What news...");
			wn.tapButton();
			wait(1);
			print("=> Go to SignIn screen...");
		}
		
		// reached
		var h = new SignIn();
		return (h.isVisible()? h : null);
	}
	
	function toSignIn() {
		toSignUp();
		log("About to press sign in tab now...");
		var signIn = new SignIn();
		signIn.tapSignInTab();
	}
	
	function toUserInfo(email, password, login)
	{
		if(typeof login == "undefined")
			login = false;
		
		if(typeof password == "undefined")
			password = null;
		
		if(typeof email == "undefined")
			email = null;
		
		// go to Home first
		toSignUp();
		h = new SignIn();
		
		
		if(h.isVisible())
		{
			if(login)
			{
			
				// log in
				print("=> Go to PlanPicker screen by logging in ...");
				h.signIn(email, password);
				wait(10);
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
				wait(10);
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
	
	
	function toPlanPicker(email, password, uinfo, login)
	{
		// go to UserInfo first
		toUserInfo(email, password, login);
		ui = new UserInfo();
		
		// continue
		if (ui.isVisible())
		{
			print("=> Go to PlanPicker screen ...");
			
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
		pp = new PlanPicker();
		return pp.isVisible() ? pp : null;
	}
	
	function toPlanInfo(email, password, uinfo, pinfo, login)
	{
		// go to PlanPicker first
		toPlanPicker(email, password, uinfo, login);
		pp = new PlanPicker();
		
		if(pp.isVisible())
		{
			print("=> Go to PlanInfo screen...");
			
			if (pinfo != null)
			{
				// pick a plan
				pp.pickPlan(pinfo.type, pinfo.name);
			}
			else
				// pick default plan
				pp.pickPlan(pp.easy, pp.easyStarter);
		}
		
		// reached
		pi = new PlanInfo();	
		return (pi.isVisible() ? pi : null);
	}
	
	function toPlanBuilder(email, password, uinfo, login)
	{
		// go to PlanPicker first
		toPlanPicker(email, password, uinfo, login);
		pp = new PlanPicker();
		
		if(pp.isVisible())
		{
			print("=> Go to PlanBuilder...")
			
			// tap custom button
			pp.tapCustomPlan();
		}
	}
	
	
	function toProgress(email, password, uinfo, pinfo, login)
	{
		// to PlanPicker first
		toPlanPicker(email, password, uinfo, login);
		pp = new PlanPicker();
		
		// if current view is PlanPicker
		if(pp.isVisible())
		{
			if (pinfo == null) {
				pp.pickPlan(pp.easy, pp.easyStarter);
				pi = new PlanInfo();
				pi.tapGo();
			}
			else if(pinfo.type != "New") {
				// pick existed plan
				print("=> Go to 7DaysGoal screen by picking existed plan...");
				pp.pickPlan(pinfo.type, pinfo.name);
				
				pi = new PlanInfo();
				pi.tapGo();
			}
			// creacte new plan
			else
			{
				pp.tapCustomPlan();
				
				pb = new PlanBuilder();
				pb.setName(pinfo.name);
				
				for(i = 0; i < pinfo.activities.length; i++)
					pb.pickActivity(pinfo.activities[i].name.toLowerCase());
				
				for(i = 0; i < pinfo.activities.length; i++)
					pbActivityGoal(i, pinfo.activities[i].value);
				
				pb.save();
			}
			
			// wait for position alert
			wait();
		}
		// current view is any view in Home control
		else
		{
			if(tabBar.isVisible())
				tabBar.tapProgress();
		}
		// reached
		pg = new Progress();
		return (pg.isVisible() ? pg : null);
	}
	
	function toPlanner(email, password, uinfo, pinfo, login) 
	{
		// go to Progress first
		toProgress(email, password, uinfo, pinfo, login);
		tabBar.tapPlanner();
		
		// reach
		p = new Planner();
		return (p.isVisible() ? p : null);
	}
	
	function toHistory(email, password, uinfo, pinfo, login) 
	{
		// go to Progress first
		toProgress(email, password, uinfo, pinfo, login);
		tabBar.tapHistory();
		
		// reach
		h = new History();
		return (h.isVisible() ? h : null);
	}
	
	function toSettings(email, password, uinfo, pinfo, login)
	{
		// go to Progress first
		toProgress(email, password, uinfo, pinfo, login);
		tabBar.tapSettings();
		
		// reach
		s = new Settings();
		return (s.isVisible() ? s : null);
	}
	
	
	function toTracking(email, password, uinfo, activity, number, login)
	{

	}
	
	function toManualTracking()
	{
		
	}
	
	function toGPSTracking()
	{
		
	}
	
	function toMusic(email, password, uinfo, activity, number, login)
	{

	}
}

nav = new Navigator();
