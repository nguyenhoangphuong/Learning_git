//#import "_Navigator.js"
//#import "_AlertHandler.js"
//#import "_Prometheus.js"
//#import "../core/testcaseBase.js"
//#import "../core/common.js"
//#import "MultiGoalChooser.js"
//#import "Home.js"
//#import "MusicSetting.js"
//#import "_NavigationBar.js"
//#import "History.js"
//#import "Settings.js"
//#import "../testcases/setting/feedback/settings_feedback_funcs.js"
//#import "../testcases/setting/feedback/settings_TCs_feedback_non-troublemaker_tryout.js"
#import "Tracker/GPSTracking.js"

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

function testGPSTracking() {
	var g = new GPSTracking();
	
	log("isVisible: " + g.isVisible());
	
	if (g.canPlayMusic())
	{
		if (!g.musicIsPlaying()) {
			log("playMusic");
			g.playMusic();
		}
		
		log("musicIsPlaying: " + g.musicIsPlaying());
		wait(5);
		log("nextSong")
		g.nextSong();
		wait(5);
		log("previousSong");
		g.previousSong();
		wait(5);
		log("pauseMusic");
		g.pauseMusic();
	}
	
	log("Current info: " + g.getCurrentInfo());
	log("pause");
	g.pause();
	wait(5);
	log("Current info: " + g.getCurrentInfo());
	log("resume");
	g.resume();
	log("finish");
	g.finish();
	wait(1); 
	log("tapMap");
	g.tapMap();
	wait(3);
	log("mapIsFullscreen: " + g.mapIsFullscreen());
	g.closeMap();
	wait(1);
	g.done();
	//log("mapIsFullscreen: " + g.mapIsFullscreen());
}

start("Demo");
//testSettings();
//logTree();

// repeating tracking
/*
for (var x = 1; x <= 10; x++) {
	start("Run: " + x);
	target.frontMostApp().mainWindow().buttons()["tracker button"].tap();
	wait(1);
	target.tap({x:154.50, y:267.00});
	wait(1);
	target.frontMostApp().mainWindow().buttons()["Start"].tap();
	wait(1);
	testGPSTracking();
	wait(1);
}
*/
testGPSTracking();



pass("Demo pass");
