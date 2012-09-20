#import "_Navigator.js"
#import "_AlertHandler.js"
#import "_Prometheus.js"
#import "About.js"
#import "GoalPlan.js"
#import "../core/testcaseBase.js"

function testAll()
{
	testSignUp();
	signup = new SignUp();
	signup.fillEmailAndSubmit("abc@def.gh");

	testUserInfo()
	ui = new UserInfo();
	ui.submit();
	
	testPlanChooser()
	plan = new PlanChooser();
	wait(); tips.closeTips(1);
	
	testGoalProgress();
	gp = new GoalProgress();
}

function testSignUp()
{
	signup = new SignUp();
	signup.pressLicenceAgreement();
	if(!signup.isLicenceAgreementShown())
		log("no license shown");
	signup.closeLicenceAgreement();
	
	signup.fillEmailAndSubmit("");
	error1 = signup.getErrorMessage();
	log(error1);
	
	signup.fillEmailAndSubmit("x");
	error2 = signup.getErrorMessage();
	log(error2);
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


start("Demo");
nav.toPlanChooser();
pass("Demo pass");
