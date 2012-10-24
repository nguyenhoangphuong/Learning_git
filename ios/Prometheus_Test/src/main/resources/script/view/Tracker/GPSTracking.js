#import "../MVPLibs.js"

/*
List of functions:
=========================================================================================
- assignControls()
- isVisible()
=========================================================================================
- canPause()	: check if the progress is running
- canResume()	: check if the progress is being paused
- canDone()		: check if the progress has finished and can be done
- musicIsPlaying()	: check if music is playing
=========================================================================================
- pause()		: pause the progress
- resume()		: resume the progress
- finish()		: finish the progress
- done()		: done after view the results
=========================================================================================
- pauseMusic()	: tap "pause music" button, do nothing when music is paused
- playMusic()	: tap "play music" button, do nothing when music is playing
- previousSong(): tap "previous song" button
- nextSong()	: tap "next song" button
=========================================================================================
- getCurrentInfo()	: get the current (percent, duration, distance)
- getResults()		: get the results (percent, duration, distance, pace)
=========================================================================================

to be update: GetResults
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
	
	// Initalize
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.canPause = canPause;
	this.canResume = canResume;
	this.canDone = canDone;
	this.musicIsPlaying = musicIsPlaying;
	
	this.pause = pause;
	this.resume = resume;
	this.finish = finish;
	this.done = done;
	
	this.pauseMusic = pauseMusic;
	this.playMusic = playMusic;
	this.previousSong = previousSong;
	this.nextSong = nextSong;
		
	this.getCurrentInfo = getCurrentInfo;
	this.getResults = getResults;
	
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
	
	function isVisible()
	{
		visible = btnFinish.isVisible() && btnFinish.name() == "Finish";
		
		log("GPSTracking is visible: " + visible);
		return visible;
	}
	
	function canPause()
	{
		return btnToggle.name() == "Pause";
	}
	
	function canResume()
	{
		return btnToggle.name() == "Resume";
	}
	
	function canDone()
	{
		btnDone = app.mainWindow().buttons()["Done"];
		return btnDone.isValid() && btnDone.isVisible();
	}
	
	function musicIsPlaying() 
	{
		return btnMusicToggle.name() == "bt music pause";
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
	}

	function done()
	{
		if (canDone())
		{
			btnDone = app.mainWindow().buttons()["Done"];
			wait(0.5);
			btnDone.tap();
			log("Tap [Done]");
		}
	}

	function pauseMusic()
	{
		if (musicIsPlaying())
			btnMusicToggle.tap();
		
		log("Tap [Pause Music]");
	}
	
	function playMusic()
	{
		if (!musicIsPlaying())
			btnMusicToggle.tap();
		
		log("Tap [Play Music]");
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

	function getCurrentInfo()
	{
		var info = {};
		info.distance = window.staticTexts()[1].name();
		info.duration = window.staticTexts()[4].name();
		info.speed = window.staticTexts()[5].name();

		log("Current tracking info: " + JSON.stringify(info));
		
		return info;
	}
	
	function getResults()
	{
		var info = {};
		info.percent = window.staticTexts()[2].name();
		info.duration = window.staticTexts()[3].name();
		info.distance = window.staticTexts()[5].name();
		info.speed = window.staticTexts()[7].name();
		
		log("result.percent: " + info.percent);
		log("result.duration: " + info.duration);
		log("result.distance: " + info.distance);
		log("result.speed: " + info.speed);
		
		return info;
	}
}