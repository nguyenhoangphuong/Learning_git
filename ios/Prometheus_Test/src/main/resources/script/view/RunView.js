#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
RunView functions:
- isVisible()	: check if the current view is RunView

- canPause()	: check if the progress is running
- canResume()	: check if the progress is being paused
- canDone()		: check if the progress has finished and can be done
- musicIsPlaying()	: check if music is playing

- pause()		: pause the progress
- resume()		: resume the progress
- finish()		: finish the progress
- done()		: done after view the results
- pauseMusic()	: tap "pause music" button, do nothing when music is paused
- playMusic()	: tap "play music" button, do nothing when music is playing
- previousSong(): tap "previous song" button
- nextSong()	: tap "next song" button

- getCurrentInfo()	: get the current (percent, duration, distance)
- getResults()		: get the results (percent, duration, distance, pace)

- closeAlert()	:
- isCongratulationAlertVisible	:
*/

function RunView()
{
	// Private fields
	var window = app.mainWindow();
	
	var btnMusicToggle = window.buttons()[0];
	var btnMusicPrevious = window.buttons()[1];
	var btnMusicNext = window.buttons()[2];
	var btnDone = window.buttons()[3];
	var btnToggle = window.buttons()[4];
	var btnFinish = window.buttons()[5];
	
	// Methods
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
	
	this.closeAlert= closeAlert;
	this.isCongratulationAlertVisible = isCongratulationAlertVisible
	
	// Methods definition
	function isVisible()
	{
		return btnFinish.isValid() && btnFinish.isVisible();
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
		return btnDone.isValid() && btnDone.isVisible();
	}
	
	function musicIsPlaying() {
		return btnMusicToggle.name() == "bt music pause";
	}
	
	function pause()
	{
		if (canPause())
		{
			closeAlert();
			wait();
			btnToggle.tap();
		}
	}
	
	function resume()
	{
		if (canResume())
		{
			wait();
			btnToggle.tap();
		}
	}
	
	function finish()
	{
		closeAlert(true);
		wait(2);
		btnFinish.tap();
	}

	function done()
	{
		if (canDone())
		{
			wait();
			btnDone.tap();
		}
	}

	function pauseMusic()
	{
		if (musicIsPlaying())
			btnMusicToggle.tap();
	}
	
	function playMusic()
	{
		if (!musicIsPlaying())
			btnMusicToggle.tap();
	}
	
	function previousSong()
	{
		btnMusicPrevious.tap();
	}
	
	function nextSong()
	{
		btnMusicNext.tap();
	}

	function getCurrentInfo()
	{
		var info = {};
		info.percent = window.staticTexts()[2].name();
		info.duration = window.staticTexts()[4].name();
		info.distance = window.staticTexts()[6].name();
		
		log("info.percent: " + info.percent);
		log("info.duration: " + info.duration);
		log("info.distance: " + info.distance);
		
		return info
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
	
	function closeAlert(confirm) 
	{
		if (isCongratulationAlertVisible())
		{
			if (typeof confirm == "undefined")
				confirm = 0;
			if (confirm == true)
				confirm = 0;
			else
				confirm = 1;
				
			log("User has achieved week goal");
			alert.confirmCustomAlert(confirm);
		}
		else 
		{
			log("No alert");
		}
	}
	
	function isCongratulationAlertVisible() 
	{
		wait();
		info = alert.getCustomAlertInfo();
		
		if (info == null)
			return false;
		
		return info.title == alert.Congratulation;	
	}	
}