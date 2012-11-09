#import "../../view/MVPLibs.js"

// ===================== Navigate =======================================
function initStartView()
{
	// to history
	nav.toHistory(null, null, null, pinfodefault , null);
}


function resetPlan(type, name)
{
	var tabbar = new TabBar();
	tabbar.tapSettings();
	
	s = new Settings();
	s.resetPlan("yes");
	wait();
	
	// choose an activity tyoe and name
	var planpicker = new PlanPicker();
	planpicker.pickPlan(type,name);
	
	//accept to go for it
	var planinfo = new PlanInfo()
	planinfo.tapGo();
	wait();
	tabbar.tapHistory();
}

// ===================== Add records ====================================
function addEntryByManualTracking()
{
	var tabbar = new TabBar();
	tabbar.tapTracker();
	
	var tracking = new Tracking();
	tracking.tapManual();
	tracking.tapActivity("Treadmill");
	
	var manualtracking = new ManualTracking();
	manualtracking.save();
	wait(2);
	tabbar.tapHistory();
}

function addNumberOfEntryByManualTracking(numofentry)
{
	for (i=0; i < numofentry ; i++)
	{
		addEntryByManualTracking();
	}
}
// ==================== Verify goal plan history ========================
function verifyHistoryVisible()
{
	var history = new History();
	assertTrue(history.isVisible(), "History Visible");
}

function verifyNoHistory()
{
	var history = new History();
	history.groupByActivity();
	assertTrue(history.isNoHistory(), "No History");
	history.groupByDate();
	assertTrue(history.isNoHistory(), "No History");
}

function verifyNumberOfHistory(numexpected)
{
	var history = new History();
	history.groupByDate();
	var numactual = history.getNumberOfEntries();
	assertEqual(numactual, numexpected, "Number of Entry");
}




