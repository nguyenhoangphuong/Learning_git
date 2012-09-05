#import "_Navigator.js"
#import "_AlertHandler.js"
#import "../core/testcaseBase.js"


function testSignUp()
{
	signup = new SignUp();
	signup.pressLicenceAgreement();
	if(!signup.closeLicenceAgreement())
		fail();
	signup.closeLicenceAgreement();
	
	signup.fillEmailAndSubmit("");
	error = signup.getErrorMessage();
	
	log(error);
}	

function testUserInfo()
{
	ui = new UserInfo();	
	ui.setSex("male");
	ui.setSex("female");
	ui.setUnit("si");
	ui.setUnit("us");
	ui.setInfo(18, 46, 0.5, 1, 0.55);
	ui.setInfo(18, 95, 0.3, "6'", "8\"");
	ui.changeWeight(-100);
	ui.changeHeight(100);
}

function testGoalProgress()
{
	goal = new GoalProgress();
	goal.scrollToDayProgress();
	goal.scrollToWeekProgress();
	goal.scrollToHistory();
}

function testAbout()
{
	about = new About();
	about.getSupport();
	if(about.isNoEmailAlertShown())
		pass();
	else
		fail();
}

start("Demo");

testAbout();

pass("Demo pass");