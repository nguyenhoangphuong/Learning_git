#import "_Navigator.js"
#import "_AlertHandler.js"
#import "_Prometheus.js"
#import "About.js"
#import "../core/testcaseBase.js"


function testSignUp()
{
	signup = new SignUp();
	signup.pressLicenceAgreement();
	if(!signup.closeLicenceAgreement())
		log("no license shown");
	signup.closeLicenceAgreement();
	
	signup.fillEmailAndSubmit("");
	error1 = signup.getErrorMessage();
	log(error1);
	
	signup.fillEmailAndSubmit("x");
	error2 = signup.getErrorMessage();
	log(error2);
	
	if(error1 != signup.MsgEmpty || error2 != signup.MsgInvalid)
		log("no error message shown");
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

function toWeekGoal()
{
	wait(3);
	target.frontMostApp().keyboard().typeString("bbb@bb.bb\n");
	wait();
	target.tap({x:171.00, y:263.50});
	wait(2);
	target.tap({x:160.50, y:266.00});
	wait(2);
	target.tap({x:168.50, y:271.00});
	wait(2);
	target.tap({x:168.50, y:271.00});
	target.frontMostApp().mainWindow().buttons()[2].tap();
	wait();
	target.frontMostApp().mainWindow().buttons()[0].tap();
	wait(2);
	target.frontMostApp().mainWindow().scrollViews()[0].tapWithOptions({tapOffset:{x:0.43, y:0.52}});
	wait();	
}

start("Demo");

	plan = new GoalPlan();
	plan.getTotalDays();
	plan.getPassedDays();
	plan.getTotalPlanMiles();
	plan.getRunMiles();
	plan.getRemainPlanMiles();

pass("Demo pass");

	