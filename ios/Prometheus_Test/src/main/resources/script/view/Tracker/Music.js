#import "../MVPLibs.js"
/*
List of funcitons:
=========================================================================================
- assignControls()
- isVisible()
=========================================================================================
- switchMusic(flag = true)		:	[flag: true/false] turn on/off the music switch
- switchSmartDJ(flag = true)	:	[flag: true/false] turn on/off the smart DJ switch
=========================================================================================	
- start()						:	tap the [Start] button
=========================================================================================
- isSmartDJHidden()				:	check if SmartDJ is hidden
- isShuffleHidden()				:	check if Shuffle is hidden
=========================================================================================
*/

function Music()
{
	// Private fields
	var window;
	var mainView;
	
	var btnMusic;
	var btnSmartDJ;
	var btnShuffle;
	var btnStart;
	
	// Initalize
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.switchMusic = switchMusic;
	this.switchSmartDJ = switchSmartDJ;

	this.start = start;
	
	this.isSmartDJHidden = isSmartDJHidden;
	this.isShuffleHidden = isShuffleHidden;
	
	// Methods definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window.tableViews()[0];
		
		btnMusic = mainView.cells()["Music"].switches()["Music"];
		btnSmartDJ = mainView.cells()["Smart DJ"].switches()["Smart DJ"];
		btnShuffle = mainView.cells()["Shuffle"].switches()["Shuffle"];
		btnStart = window.buttons()[0];
	}
	
	function isVisible()
	{
		var cellSmartDJ = mainView.tableViews()[0].cells()["Smart DJ, ON, OFF"];
		
		exist = cellSmartDJ.isValid() && cellSmartDJ.isVisible();
		log("Music visible: " + exist);
		
		return exist;
	}

	function switchMusic(flag)
	{
		// default value
		if(typeof flag == "undefined")
			flag = true;
		
		value = flag ? 1 : 0;
		text = flag ? "On" : "Off";
		
		// switch
		btnMusic.setValue(value);
		log("Switch Music: " + text);
	}
	
	function switchSmartDJ(flag)
	{
		// default value
		if(typeof flag == "undefined")
			flag = true;
		
		value = flag ? 1 : 0;
		text = flag ? "On" : "Off";
		
		// switch
		btnSmartDJ.setValue(value);
		log("Switch Shuffle: " + text);
	}

	function start()
	{
		btnStart.tap();
		log("Tap [Start]");
	}
	
	function isSmartDJHidden()
	{
		hidden = !btnSmartDJ.isValid();
		log("SmartDJ is hidden: " + hidden);
		
		return hidden;
	}
	
	function isShuffleHidden()
	{
		hidden = !btnShuffle.isValid();
		log("Shuffle is hidden: " + hidden);
		
		return hidden;
	}
}