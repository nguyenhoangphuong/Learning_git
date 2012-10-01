#import "_Navigator.js"
#import "_AlertHandler.js"
#import "_Prometheus.js"
#import "../core/testcaseBase.js"

#import "Home.js"
#import "MusicSetting.js"


function testHome()
{
	h = new Home();

	// tap log in and then return to home
	hr();
	h.tapLogin();
	h.isVisible();
	h.isLoginVisible();
	h.isSignupVisible();
	h.isTryoutVisible();
	h.isEmailTextFieldVisible();
	h.isPasswordTextFieldVisible();
	h.tapLogin();
	h.isVisible();
	h.isLoginVisible();
	
	// tap sign up and then return to home
	hr();
	h.tapSignup();
	h.isVisible();
	h.isLoginVisible();
	h.isSignupVisible();
	h.isTryoutVisible();
	h.isEmailTextFieldVisible();
	h.isPasswordTextFieldVisible();
	h.tapSignup();
	h.isVisible();
	h.isSignupVisible();
	
	// tap try out and then return to home
	hr();
	h.tapTryout();
	h.isVisible();
	h.isLoginVisible();
	h.isSignupVisible();
	h.isTryoutVisible();
	h.isEmailTextFieldVisible();
	h.tapTryout();
	h.isVisible();
	h.isTryoutVisible();

	// login with empty email
	hr();
	h.tapLogin();
	h.fillEmail("");
	h.submit();
	h.isEmptyEmailAlertShown();
	h.tapLogin();
	
	// login with empty password
	hr();
	h.tapLogin();
	h.fillEmail("abc@test.com");
	h.fillPassword("");
	h.submit();
	h.isEmptyPasswordAlertShown();
	h.tapLogin();
	
	// login with invalid email
	hr();
	h.tapLogin();
	h.fillEmail("asda");
	h.submit();
	h.isInvalidEmailAlertShown();
	h.tapLogin();
	
	// login with non-existed user
	hr();
	h.tapLogin();
	h.fillEmail("non_existed_user@nowhere.mar");
	h.fillPassword("123456");
	h.submit();
	wait(3);
	h.isInvalidUserAlertShown();
	h.tapLogin();

	// signup with existed user
	hr();
	h.tapSignup();
	h.fillEmail("abc@test.com");
	h.fillPassword("123456");
	h.submit();
	wait(3);
	h.isExistedUserAlertShown();
	h.tapSignup();

	// some shortcut
	h.login("dump@sol.earth", "123");
	h.tapLogin();
	h.signup("abc@test.com", "123456");
	h.tapSignup();
	
	// try out
	// becareful: you won't go back to old view after this
	//h.tryout("tryout@test.com");
}	

function testUserInfo()
{
	ui = new UserInfo();
	
	ui.setSex("female");
	ui.setSex("male");
	ui.setSex("male");
	ui.setSex("female");
	ui.setUnit("si");
	ui.setUnit("us");
	ui.setUnit("us");
	ui.setUnit("si");
	
	ui.setInfo(18, 46, 0.5, 1, 0.55);
	ui.getInfo();
	
	ui.setUnit("us");
	ui.setInfo(18, 95, 0.3, "6'", "8\"");
	ui.getInfo();
	
	ui.changeWeight(-100);
	ui.getInfo();
	
	ui.changeHeight(100);
	ui.getInfo();
}

function testPlanChooser()
{
	plan = new PlanChooser();
	
	plan.getPlanAmounts();
	plan.selectOther()
	plan.setValue(17);
	plan.getPickerValue();
	plan.back();
	
	plan.selectOther()
	plan.setValue(17);
	plan.getPickerValue();
	plan.done();
}

function testGoalProgress()
{
	goal = new GoalProgress();
	goal.scrollToDayGoal();
	goal.scrollToWeekGoal();
	//goal.scrollToAbout();
	//goal.scrollToGoalPlan();
	
	goal.getWeekInfo();
	goal.getTodayInfo();
	goal.getWeatherInfo();
	goal.getGPSSignal();
	goal.getQuote();
}

function testRunView()
{
	start = new RunView();
	start.pause();
	start.getCurrentInfo();
	start.resume();
	start.finish();
	start.getResults();
	start.done();
}

function testGoalPlan()
{
	plan = new GoalPlan();
	plan.getTotalDays();
	plan.getPassedDays();
	plan.getTotalPlanMiles();
	plan.getRunMiles();
	plan.getRemainPlanMiles();
	plan.getWeekInfo();
	plan.getDayInfoByIndex(0);
	plan.getDayInfoByIndex(1);
	plan.getDayInfoByIndex(12);
	plan.getDayInfoByName("Mon Sep 10th");
	plan.getDayInfoByName("Sep Mon 10th");
	plan.getTodayInfo();

	plan.edit();
	plan.planDayByIndex(1, 4.5);
	plan.planDayByIndex(2, 0.5, "No");
	plan.planDayByIndex(12, 4.5);
	plan.planDayByName("Mon Sep 10th", 4.5);
	plan.planDayByName("Sep Mon 1oth", 4.5);
	plan.reset();
	plan.save();
}

function testAbout()
{
	about = new About();
	about.rateApp();
	target.pushTimeout(5);
	if(!prometheus.isActive())
		pass();
	else
		fail();
	target.popTimeout();
}

function testGPS(e)
{	
	gp = new GoalProgress;
	
	log("---------- checking with error =  " + e.toString() + " -----------");
	error = {};
	error.AH = error.BH = error.AV = error.BV = e;
	gp.simulateARun(3, error, true);
	wait();
}

function testMusicSetting()
{
	ms = new MusicSetting();
	ms.isVisible();	
	ms.isShuffleOn();
	ms.isSmartDJOn();
	ms.switchShuffle(false);
	ms.switchShuffle(true);
	ms.switchShuffle(true);
	ms.switchSmartDJ(false);
	ms.switchSmartDJ(true);
	ms.switchSmartDJ(true);
	ms.selectPlaylists(["Love", "Sad"]);
	ms.deselectPlaylists(["Sad"]);
	ms.togglePlaylists(["Love", "Sad", "MyMusic"]);
	ms.getNumberOfPlaylist();
	ms.getAllPlaylistInfo();
	ms.done();
}

start("Demo");
testHome();
pass("Demo pass");
