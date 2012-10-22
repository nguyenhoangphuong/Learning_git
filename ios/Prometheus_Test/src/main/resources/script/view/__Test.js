/*#import "_Navigator.js"
#import "_AlertHandler.js"
#import "_Prometheus.js"
#import "../core/testcaseBase.js"*/
#import "../core/common.js"
/*#import "MultiGoalChooser.js"
#import "Home.js"
#import "MusicSetting.js"
#import "_NavigationBar.js"
#import "History.js"*/
#import "Settings.js"

function testSettings()
{
//	logTree();
	var target = UIATarget.localTarget();
	var app = target.frontMostApp();
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0];
	
	var s = new Settings();
	
/*	log("isVisible: " + s.isVisible());
	log("isTroublemaker: " + s.isTroublemaker());
	log("hasSignedIn: " + s.hasSignedIn());
	s.setGender("Male"); wait();
	s.setGender("Female"); wait();
	s.setGender("Male"); wait();
	s.setGender();*/
//	s.setName("My name is Slim Shady");
/*	s.setBirthday(1985, "may", 20);
	s.setHeight("2", ".50");
	s.setWeight("50", ".6");*/
/*	s.setUnit("metrIc");
	s.setUnit("US");*/
//	s.tapSupport();
//	s.tapLike();
//	s.tapFeedback();
//	s.tapRate();
//	s.tapWebsite();
//	s.resetPlan("yes");
//	s.signOut();
	s.sendJIRAFeedback();
}

start("Demo");
testSettings();
pass("Demo pass");
