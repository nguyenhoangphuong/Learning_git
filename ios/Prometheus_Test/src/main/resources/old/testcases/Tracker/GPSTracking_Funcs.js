#import "../../view/MVPLibs.js"
/**
 * This test covers: - Do an activity w/ GPS tracking, test the functionality of
 * buttons while tracking
 */

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
	
	log("pause");
	g.pause();
	
	var info1 = g.getCurrentInfo();
	wait(10);
	
	var info2 = g.getCurrentInfo();
	
	if (info1.distance != info2.distance ||
		info1.duration != info2.duration ||
		info1.pace != info2.pace)
		fail("Info of the run is not kept while being paused");
	
	log("resume");
	g.resume();
	
	log("finish");
	g.finish();
	wait(1);
	
	log("tapMap");
	g.tapMap();
	wait(3);
	
	if (!g.mapIsFullscreen())
		fail("Map cannot be fullscreen");
	else
		g.closeMap();

	wait(1);
	g.done();
}