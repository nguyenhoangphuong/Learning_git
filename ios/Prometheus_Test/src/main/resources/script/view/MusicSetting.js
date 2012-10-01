#import "../core/testcaseBase.js"

/*
List of funcitons:
=========================================================================================
- isVisible()					:	check if current view is MusicSettings
=========================================================================================
- switchShuffle(flag = true)	:	[flag: true/false] turn on/off the shuffle switch
- switchSmartDJ(flag = true)	:	[flag: true/false] turn on/off the smart DJ switch
- selectPlaylists(playlists)	:	checked the specified playlists
- deselectPlaylists(playlists)	:	unchecked the specified playlists
- togglePlaylists(playlists)	:	tap on the specofied playlists to its state 
	+ <playlists>	:	["Rock", "Ballad", "Pop"] - use name
	+ <playlists>	:	[0, 2, 5] - use index
=========================================================================================	
- done()						:	tap the [Done] button
=========================================================================================
- getNumberOfPlaylist()			:	return an [int] - number of playlists
- getAllPlaylistInfo()			:	return an object contains of playlists' names and states
	{names: ["Rock", "Ballad", "Pop"], states: [true, false, false]}
- isShuffleOn()					:	return [bool] - state of shuffle
- isSmartDJOn()					:	return [bool] - state of smart DJ
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
	var musicList = mainView.tableViews()[0];
	
	var doneBtn = mainView.buttons()[1];
	
	// Methods
	this.isVisible = isVisible;
	
	this.switchShuffle = switchShuffle;
	this.switchSmartDJ = switchSmartDJ;
	this.selectPlaylists = selectPlaylists;
	this.deselectPlaylists = deselectPlaylists;
	this.togglePlaylists = togglePlaylists;
	this.done = done;
	
	this.getNumberOfPlaylist = getNumberOfPlaylist;
	this.getAllPlaylistInfo = getAllPlaylistInfo;
	this.isShuffleOn = isShuffleOn;
	this.isSmartDJOn = isSmartDJOn;
	
	// Methods definition
	function isVisible()
	{
		exist = staticTextExist("Shuffle") && staticTextExist("Smart DJ");
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
	
	function selectPlaylists(playlists)
	{
		for(i = 0; i < playlists.length; i++)
		{
			ele = musicList.cells()[playlists[i]];
			if(ele.isValid())
			{
				log("Enabled playlist [" + ele.name() + "]");
				
				wait(0.5);
				ele.setValue(1);
			}
			else
				log("Can't find playlist [" + playlists[i] + "]");
		}
	}
	
	function deselectPlaylists(playlists)
	{
		for(i = 0; i < playlists.length; i++)
		{
			ele = musicList.cells()[playlists[i]];
			if(ele.isValid())
			{
				log("Unenabled playlist [" + ele.name() + "]");
				
				wait(0.5);
				ele.setValue(0);
			}
			else
				log("Can't find playlist [" + playlists[i] + "]");
		}
	}
	
	function togglePlaylists(playlists)
	{
		for(i = 0; i < playlists.length; i++)
		{
			ele = musicList.cells()[playlists[i]];
			if(ele.isValid())
			{
				val = (ele.value() == 1? 0: 1);
				wait(0.5);
				ele.setValue(val);
				log("Toggle playlist [" + ele.name() + "]");
			}
			else
				log("Can't find playlist [" + playlists[i] + "]");
		}
	}
	
	function done()
	{
		log("Tap [Done] button");
		
		wait(0.5);
		doneBtn.tap();
	}
	
	
	function getNumberOfPlaylist()
	{
		count = 0;
		ele = musicList.cells()[count];
		
		while(ele.isValid())
		{
			count++;
			ele = musicList.cells()[count];
		}
		
		log("Number of playlists: " + count);
		return count;
	}
	
	function getAllPlaylistInfo()
	{
		count = -1;
		ele = musicList.cells()[count];
		
		info = {names: [], states: []};
		
		while(ele.isValid())
		{
			// save the information
			info.names[count] = ele.name();
			info.states[count] = (ele.value() == 1? true: false);
			
			// advance to next record
			count++;
			ele = musicList.cells()[count];
		}
		
		log("Names of playlists: " + info.names);
		log("States of playlists: " + info.states);
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
		state = smartOffBtn.isValid() && smartOffBtn.isVisible();;
		log("SmartDJ state: " + state);
		return state;
	}
	
	
}
