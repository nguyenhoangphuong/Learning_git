#import "_Navigator.js"
#import "_AlertHandler.js"
#import "_Prometheus.js"
#import "../core/testcaseBase.js"
#import "MultiGoalChooser.js"
#import "Home.js"
#import "MusicSetting.js"
#import "Settings.js"

function testSettings()
{
//	logTree();
	var target = UIATarget.localTarget();
	var app = target.frontMostApp();
	var window = app.mainWindow();
	var mainView = window.scrollViews()[0];
	
	var s = new Settings();
	
	//log(s.isVisible());
	//log(s.isSupportView());
	//log(s.isTroublemaker());
	//s.getResetButton().tap();
	//s.goToProfile();
	//s.rateApp();
	//s.tapSupport();
	//s.emailSupport();
	//s.likePage();
	//s.goToWebsite();
	//s.backToSettings();
	//s.tapFeedback();
	//s.resetPlan();
	s.resetPlan(true);
}

start("Demo");
testSettings();
pass("Demo pass");