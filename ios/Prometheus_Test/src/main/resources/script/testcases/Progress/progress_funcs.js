#import "../../view/MVPLibs.js"

function GoToProgress(pinfo)
{
	nav.toProgress(null, null, null, pinfo);
}

function AddRecord(activity)
{
	tabBar.tapTracker();
	t = new Tracking();
	t.tapManual();
	t.tapActivity(activity);
	
	m = new ManualTracking();
	m.save();
}

function VerifyNoActivityProgress(activitiesExpect)
{
	// activitiesExpect = ["Swimming", "Running"];

	// to progress and get activities list
	p = new Progress();
	actual = p.getActivities();
	
	// check the activities' name
	assertEqual(actual.activities.length, activitiesExpect.length, "Number of activities is the same");
	for(var i = 0; i < actual.activities.length; i++)
	{
		actualAct = actual.activities;;
		expectAct = activitiesExpect[i];
		assertTrue(actualAct.indexOf(expectAct) >= 0, "Activity is in list");
	}
	
	// check progress of each day
	for(var i = 0; i < 7; i++)
	{
		p.tapDate(i);
		progress = p.getActivities();
		
		for(var j = 0; j < progress.activities.length; j++)
			assertEqual(progress.percent[j], "0%", "Starting percent is 0%" +
					", Date " + i +  
					", " + progress.activities[j]);
	}
	
	p.tapToday();
}

function VerifyNoActivityRecord(activitiesExpect)
{
	// to progress
	p = new Progress();
	
	// tap on each activity and check No activity is shown
	for(var act = 0; act < activitiesExpect.length; act++)
	{
		p.tapActivity(0);
		p.tapActivity(act);
		
		assertTrue(p.isHistoryRecordsShown(), "Current records is shown");
		assertTrue(p.isNoActivity(), "Current records is No activity");
		
		records = p.getCurrentHistory();
		assertEqual(records.name, activitiesExpect[act], "Current records name is correct");
		assertEqual(records.percent, "0%", "Current records percent is 0%");
		
		p.tapOutside();
		assertTrue(!p.isHistoryRecordsShown(), "Current records doesn't show up anymore");
	}
}

function VerifyProgressIsUpdated()
{
	// to progress
	p = new Progress();
	p.tapToday();
	
	// check percent is updated (!= 0%)
	progress = p.getActivities();
	for(var j = 0; j < progress.activities.length; j++)
		assertTrue(progress.percent[i] != "0%", "Percent isn't 0%" +
				", " + progress.activities[i]);
}

function VerifyRecordsAreUpdated()
{
	// to progress
	p = new Progress();
	p.tapToday();
	
	// check records are update
	progress = p.getActivities();
	for(var i = 0; i < progress.activities.length; i++)
	{
		p.tapActivity(i);
		assertTrue(!p.isNoActivity(), "Current records isn't No activity anymore");
		
		records = p.getCurrentHistory();
		assertTrue(records.percent != "0%", "Current records percent isn't 0% anymore");
		
		p.tapOutside();
	}
}