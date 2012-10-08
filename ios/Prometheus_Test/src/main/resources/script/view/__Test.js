#import "_Navigator.js"
#import "_AlertHandler.js"
#import "_Prometheus.js"
#import "__TestArchive.js"
#import "../core/testcaseBase.js"
#import "MultiGoalChooser.js"
#import "Home.js"
#import "MusicSetting.js"
#import "Settings.js"

function testHistory()
{
	h = new History();
	
	h.isVisible();
	h.isNoHistory();
	
	info = h.getAllDates();
	h.getAllRecordsOfDate(info.dates[0]);
	h.getAllRecordsOfDate(info.dates[1]);
	
	//h.scrollToPlanner();
}
	

start("Demo");
//nav.toRunView(null, null, {age: 35, w1: 80, w2: ".5", wu:"kg", h1: 1, h2: ".65", hu: "meter", sex: "female", unit: "si"}, "Running", 22);
//listControls(app.mainWindow());
//logTree();

testHistory();

pass("Demo pass");
