#import "../../core/testcaseBase.js"
#import "../../view/_Navigator.js"

/* This test case verifies that the screen displays:
 * - Smart DJ switcher
 * - Shuffle switcher
 * - Playlist(s)
 * - Done button
 * The test also verifies that when switching Smart DJ on/off the other
 * components are displayed/hidden accordingly
 */

/* Navigate to Music Settings screen. This may be removed when the
 * _Navigator.js has been fully coded
 */
function toMusicSettings()
{
	nav.toTodaysGoal();

	var btnMusicSettings = target.frontMostApp().mainWindow().
							scrollViews()[0].buttons()["bt music 2"];

	btnMusicSettings.tap();
	wait(1);
	
	var ms = new MusicSetting();
	
	return ms.isVisible() ? ms : null;
}

function runTest()
{
	var ms = toMusicSettings();
	
	if (ms == null) return; 

	if (ms.getNumberOfPlaylist() < 1)
		fail("No playlist is displayed while actually there is at least 1");
	
	ms.switchSmartDJ(false);
	
	if (!ms.areOtherPartsHidden())
		fail("Other parts are not hidden when Smart DJ is on");
	
	ms.switchSmartDJ();
	ms.switchSmartDJ(false);
	ms.switchSmartDJ();
	ms.switchShuffle();
	ms.switchShuffle(false);
	ms.switchShuffle();
	ms.switchShuffle(false);
}

runTest();