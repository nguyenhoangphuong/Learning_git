// ===================== Navigate =======================================
function initStartView()
{
	// to history
	nav.toHistory(null, null, null, "Running", 10);
}

function goFromStartToHistory()
{
	// go to history
	tg = new GoalProgress();
	tg.scrollToPlanner();
	
	p = new GoalPlan();
	p.scrollToHistory();
}

function resetPlan(activity, amount)
{
	// go to settings and press reset plan
	h = new History();
	h.scrollToPlanner();
	
	p = new GoalPlan();
	p.scrollToTodaysGoal();
	
	tg = new GoalProgress();
	tg.scrollToSettings();
	
	s = new Settings();
	s.resetPlan(true);
	wait()
	
	// choose an activity and its amount
	a = new MultiGoalChooser();
	a.chooseActivityWithName(activity);
	
	pc = new PlanChooser();
	pc.selectOther();
	pc.setValue(amount);
	pc.done();
	wait();
}

// ==================== Verify ==========================================
function verifyNoHistory()
{
	h = new History();
	
	assertTrue(h.isNoHistory(), "No History");
}

function verify