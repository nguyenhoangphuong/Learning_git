#import "../MVPLibs.js"
/*
List of funcitons:
=========================================================================================
- isVisible()					:	check if current view is MusicSettings
=========================================================================================
- switchShuffle(flag = true)	:	[flag: true/false] turn on/off the shuffle switch
- switchSmartDJ(flag = true)	:	[flag: true/false] turn on/off the smart DJ switch
- selectPlaylists(id)			:	checked the specified playlist
	+ id: "MyMusic" - 	use string
	+ id: 0			-	use index
=========================================================================================	
- done()						:	tap the [Done] button
=========================================================================================
- getNumberOfPlaylist()			:	return an [int] - number of playlists
- getAllPlaylistInfo()			:	return an object contains of playlists' names and active one
	{all: ["Rock", "Ballad", "Pop"], active: "Rock"	}
- isShuffleOn()					:	return [bool] - state of shuffle
- isSmartDJOn()					:	return [bool] - state of smart DJ
- areOtherPartsHidden			:	check if other parts are hidden when Smart
									DJ is turned on
=========================================================================================
*/

function MusicSetting()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window;
	
	var shuffleOnBtn = mainView.staticTexts()[1];
	var shuffleOffBtn = mainView.staticTexts()[2];
	var smartOnBtn = mainView.staticTexts()[4];
	var smartOffBtn = mainView.staticTexts()[5];
	var musicList = mainView.tableViews()[1];
	
	var doneBtn = mainView.buttons()[1];
	
	// Methods
	this.isVisible = isVisible;
	
	this.switchShuffle = switchShuffle;
	this.switchSmartDJ = switchSmartDJ;
	this.selectPlaylists = selectPlaylists;
	this.done = done;
	
	this.getNumberOfPlaylist = getNumberOfPlaylist;
	this.getAllPlaylistInfo = getAllPlaylistInfo;
	this.isShuffleOn = isShuffleOn;
	this.isSmartDJOn = isSmartDJOn;
	this.areOtherPartsHidden = areOtherPartsHidden;
	
	// Methods definition
	function isVisible()
	{
		var cellSmartDJ = mainView.tableViews()[0].cells()["Smart DJ, ON, OFF"];
		
		exist = cellSmartDJ.isValid() && cellSmartDJ.isVisible();
		log("MusicSetting visible: " + (exist == 1? "true": "false"));
		
		return exist;
	}

	function switchShuffle(flag)
	{
		// default value
		if(typeof flag == "undefined")
		{
			flag = true;
		}
		
		
		// if current state is off and user want to turn on
		if(flag && !isShuffleOn())
		{
			log("Turning shuffle On");
			
			wait(0.5);
			shuffleOnBtn.tap();
		}
		
		// if current state is on and user want to turn off
		if(!flag && isShuffleOn())
		{
			log("Turning shuffle Off");
			
			wait(0.5);
			shuffleOffBtn.tap();
		}
	}
	
	function switchSmartDJ(flag)
	{
		// default value
		if(typeof flag == "undefined")
		{
			flag = true;
		}
		
		// if current state is off and user want to turn on
		if(flag && !isSmartDJOn())
		{
			log("Turning smart DJ On");
			
			wait(0.5);
			smartOnBtn.tap();
		}
		
		// if current state is on and user want to turn off
		if(!flag && isSmartDJOn())
		{
			log("Turning smart DJ Off");
			
			wait(0.5);
			smartOffBtn.tap();
		}
	}
	
	function selectPlaylists(id)
	{
		ele = musicList.cells()[id];
		if(ele.isValid())
		{
			log("Select playlist [" + ele.name() + "]");
			
			wait(0.5);
			ele.tap();
		}
		else
			log("Can't find playlist [" + id + "]");
	}
	
	function done()
	{
		log("Tap [Done] button");
		
		wait(0.5);
		doneBtn.tap();
	}
	
	
	function getNumberOfPlaylist()
	{
		count = musicList.cells().length;
		log("Number of playlists: " + count);
		
		return count;
	}
	
	function getAllPlaylistInfo()
	{
		count = 0;
		ele = musicList.cells()[count];
		
		info = {all: [], active: null};
		
		while(ele.isValid())
		{
			// save the information
			info.all[count] = ele.name();
			if(ele.value() == 1)
				info.active = ele.name();
			
			// advance to next record
			count++;
			ele = musicList.cells()[count];
		}
		
		log("All playlists: " + info.all);
		log("Active playlist: " + info.active);
		return info;
	}
	
	function isShuffleOn()
	{
		state = shuffleOffBtn.isValid() && shuffleOffBtn.isVisible();
		log("Shuffle state: " + state);
		return state;
	}
	
	function isSmartDJOn()
	{
		state = smartOffBtn.isValid() && smartOffBtn.isVisible();
		log("SmartDJ state: " + state);
		return state;
	}
	
	function areOtherPartsHidden()
	{
		return !shuffleOffBtn.isVisible() &&
				!musicList.isVisible();
	}
	
	
}
