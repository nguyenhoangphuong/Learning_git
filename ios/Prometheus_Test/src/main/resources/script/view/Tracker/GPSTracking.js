#import "../MVPLibs.js"

/*
Function list:
================================================================================
- assignControls()
- assignControlsForResultsScreen()
- isVisible()
================================================================================
- canPause()		: check if the progress is running
- canResume()		: check if the progress is being paused
- canPlayMusic()	: check if music control is enabled for playing
- musicIsPlaying()	: check if music is playing
================================================================================
- pause()			: pause the progress
- resume()			: resume the progress
- finish()			: finish the progress
- getCurrentInfo()	: get the current (distance, duration, pace)
================================================================================
- pauseMusic()		: tap "pause music" button, do nothing when music is paused
- playMusic()		: tap "play music" button, do nothing when music is playing
- previousSong()	: tap "previous song" button
- nextSong()		: tap "next song" button
================================================================================
- isAtResultsScreen()
- canDone()			: check if the progress has finished and can be done
- tapMap()			: tap on map (to make it fullscreen)
- closeMap()		: close map when it is fullscreen
- mapIsFullscreen()
- done()			: done after view the results
- getResults()		: get the results (distance, duration, pace)
================================================================================
*/

function GPSTracking()
{
	// Private fields
	var window;
	var mainView;
	var infoView;
	
	var btnMusicToggle;
	var btnMusicPrevious;
	var btnMusicNext;
	
	var btnToggle;
	var btnFinish;
	
	var btnDone;
	var map;
	var btnCloseMap;
		
	// Methods
	this.assignControls = assignControls;
	this.assignControlsForResultsScreen = assignControlsForResultsScreen;
	this.isVisible = isVisible;
	
	this.canPause = canPause;
	this.canResume = canResume;
	this.canPlayMusic = canPlayMusic;
	this.musicIsPlaying = musicIsPlaying;
	
	this.pause = pause;
	this.resume = resume;
	this.finish = finish;
	this.getCurrentInfo = getCurrentInfo;
	
	this.pauseMusic = pauseMusic;
	this.playMusic = playMusic;
	this.previousSong = previousSong;
	this.nextSong = nextSong;
	
	this.isAtResultsScreen = isAtResultsScreen;
	this.canDone = canDone;
	this.tapMap = tapMap;
	this.closeMap = closeMap;
	this.mapIsFullscreen = mapIsFullscreen;
	this.done = done;
	this.getResults = getResults;
	
	// Initialize
	assignControls();
	
	// Methods definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window;
		infoView = mainView.scrollViews()[0];
		
		btnMusicToggle = mainView.buttons()[0];
		btnMusicPrevious = mainView.buttons()[1];
		btnMusicNext = mainView.buttons()[2];
		
		btnToggle = mainView.buttons()[3];
		btnFinish = mainView.buttons()[4];
	}
	
	function assignControlsForResultsScreen()
	{
		if (app.mainWindow().buttons()["Done"].checkIsValid())
			btnDone = app.mainWindow().buttons()["Done"];
		else
			btnDone = null;
		
		if (app.mainWindow().elements()[2].checkIsValid())
			map = app.mainWindow().elements()[2];
		else
			map = null;
		
		if (app.mainWindow().buttons()["btn close"].checkIsValid())
			btnCloseMap = app.mainWindow().buttons()["btn close"];
		else
			btnCloseMap = null;
	}
	
	function isVisible()
	{
		visible = btnFinish.isVisible() && btnFinish.name() == "Finish" &&
				btnToggle.isVisible();
		
		log("GPS Tracking visibility: " + visible);
		return visible;
	}
	
	function canPause()
	{
		return btnToggle.name().toLowerCase() == "pause";
	}
	
	function canResume()
	{
		return btnToggle.name().toLowerCase() == "resume";
	}
	
	function canPlayMusic()
	{
		return btnMusicToggle.checkIsValid() &&
				btnMusicToggle.isVisible();
	}
	
	function musicIsPlaying() 
	{
		wait(0.5);
		
		return btnMusicToggle.name() == "img pause";
	}
	
	function pause()
	{
		if (canPause())
		{
			wait(0.5);
			btnToggle.tap();
			log("Tap [Pause]");
		}
	}
	
	function resume()
	{
		if (canResume())
		{
			wait(0.5);
			btnToggle.tap();
			log("Tap [Resume]");
		}
	}
	
	function finish()
	{
		wait(0.5);
		btnFinish.tap();
		log("Tap [Finish]");
		
		// TODO: tap Resume and Finish at confirmation
	}
	
	function getCurrentInfo()
	{
		var info = {};
		
		info.distance = infoView.staticTexts()[2].name();
		info.duration = infoView.staticTexts()[5].name();
		info.pace = infoView.staticTexts()[6].name();
		
		log("Current tracking info: " + JSON.stringify(info));
		
		return info;
	}

	function pauseMusic()
	{
		if (musicIsPlaying()) {
			btnMusicToggle.tap();
			log("Tap [Pause Music]");
		}
	}
	
	function playMusic()
	{
		if (!musicIsPlaying()) {
			btnMusicToggle.tap();
			log("Tap [Play Music]");
		}
	}
	
	function previousSong()
	{
		btnMusicPrevious.tap();
		log("Tap [Previous]");
	}
	
	function nextSong()
	{
		btnMusicNext.tap();
		log("Tap [Next]");
	}
	
	function isAtResultsScreen()
	{
		assignControlsForResultsScreen();
		
		return btnDone != null ||
				map != null ||
				btnCloseMap != null;
	}

	function canDone()
	{
		return (isAtResultsScreen() && btnDone != null);
	}
	
	function tapMap()
	{
		if (isAtResultsScreen() && map != null) {
			wait(0.5);
			map.tap();
			log("Tap map");
		}
	}
	
	function closeMap()
	{
		if (mapIsFullscreen()) {
			wait(0.5);
			btnCloseMap.tap();
			log("Close map");
		}
	}
	
	function mapIsFullscreen() {
		return (isAtResultsScreen() &&
				btnCloseMap != null &&
				btnCloseMap.checkIsValid() &&
				btnCloseMap.isVisible());
	}
	
	function done()
	{
		if (canDone())
		{			
			wait(0.5);
			btnDone.tap();
			log("Tap [Done]");
		}
	}
	
	function getResults()
	{
		var info = {};
		// TODO: update this when possible (currently it is impossible)
		/*
		info.distance = infoView.staticTexts()[2].name();
		info.duration = infoView.staticTexts()[5].name();
		info.pace = infoView.staticTexts()[6].name();
		
		log("result.distance: " + info.distance);
		log("result.duration: " + info.duration);
		log("result.pace: " + info.pace);
		*/
		return info;
	}
}
