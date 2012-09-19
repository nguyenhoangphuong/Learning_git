#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"

// ============================================================= //
//	NAVIGATION
// ============================================================= //
function toGoalPlan(opt, val)
{
	nav.toPlanChooser();
	plan = new PlanChooser();

	if(opt == 0)
		plan.selectEasy();
	if(opt == 1)
		plan.selectNormal();
	if(opt == 2)
		plan.selectActive();
	if(opt == 3)
	{
		plan.selectOther();
		plan.setValue(val);
		plan.done();
	}
	
	gp = new GoalProgress();
	gp.scrollToDayGoal();
	
	wait(3);
	gp.scrollToGoalPlan();
	
	wait(2);
	tips.closeTips(5);
}

// ============================================================= //
//	VERIFY
// ============================================================= //
// e = none
function verifyAutoButton()
{
	// note: e is an array of float (7 elements) which stand for
	// 		 default value of 7 days plan number
	
	gp = new GoalPlan();
	
	day0 = gp.getDayInfoByIndex(0);
	day1 = gp.getDayInfoByIndex(1);
	day2 = gp.getDayInfoByIndex(2);
	day3 = gp.getDayInfoByIndex(3);
	day4 = gp.getDayInfoByIndex(4);
	day5 = gp.getDayInfoByIndex(5);
	day6 = gp.getDayInfoByIndex(6);

	gp.edit();		
	gp.planDayByIndex(1, 1.5, true);
	gp.planDayByIndex(2, 1.7, true);
	gp.planDayByIndex(3, 0.2, true);
	gp.save();
	
	gp.reset();
	
	day00 = gp.getDayInfoByIndex(0);
	assertEqual(day00.total, day0.total);
	
	day11 = gp.getDayInfoByIndex(1);
	assertEqual(day11.total, day1.total);

	day22 = gp.getDayInfoByIndex(2);
	assertEqual(day22.total, day2.total);
	
	day33 = gp.getDayInfoByIndex(3);
	assertEqual(day33.total, day3.total);

	day44 = gp.getDayInfoByIndex(4);
	assertEqual(day44.total, day4.total);
	
	day55 = gp.getDayInfoByIndex(5);
	assertEqual(day55.total, day5.total);
	
	day66 = gp.getDayInfoByIndex(6);
	assertEqual(day66.total, day6.total);
}

// e = none
function verifyEditButton()
{
	gp = new GoalPlan();
		
	gp.edit();
	assertTrue(gp.isInEditMode());
	
	gp.cancel();
}

// e = none
function verifyCancelButton()
{
	gp = new GoalPlan();

	b = gp.getDayInfoByIndex(2);
	gp.edit();
	gp.planDayByIndex(2, 5, true);
	gp.cancel();
	a = gp.getDayInfoByIndex(2);
	
	assertTrue(b.total == a.total);
	
	gp.reset();
}

// e = none
function verifyDoneButton()
{
	gp = new GoalPlan();

	b = gp.getDayInfoByIndex(2);
	gp.edit();
	gp.planDayByIndex(2, 5, true);
	t = gp.getDayInfoByIndex(2);
	gp.save();
	a = gp.getDayInfoByIndex(2);
	
	assertTrue(b.total != a.total);
	assertTrue(t.total == a.total);
	
	gp.reset();
}



// e = none
function verifySliderNotDraggable()
{
	gp = new GoalPlan();
	
	// checking in normal mode (any day)
	log("---- sliders : normal mode ----");
	b = gp.getDayInfoByIndex(2);
	gp.planDayByIndex(2, 0, true);
	a = gp.getDayInfoByIndex(2);

	assertTrue(b.total == a.total);
		
	// checking in edit mode (passed day)
	log("---- sliders : edit mode on today ----");
	gp.edit();
	b = gp.getDayInfoByIndex(0);
	gp.planDayByIndex(0, 0, true);
	a = gp.getDayInfoByIndex(0);
	gp.save();
	
	assertTrue(b.total == a.total);
	
	gp.reset();
}

// e = none
function verifySliderDraggable()
{
	gp = new GoalPlan();

	gp.edit();
	b = gp.getDayInfoByIndex(2);
	gp.planDayByIndex(2, 5, true);
	a = gp.getDayInfoByIndex(2);
	gp.save();
	
	assertTrue(b.total != a.total);
	
	gp.reset();
}




// e = none
function verifyHardAlert()
{
	gp = new GoalPlan();

	gp.edit();
	gp.planDayByIndex(2, 5);
	wait();
	
	assertTrue(gp.isHardAlertShown());
	
	gp.confirmHardAlert(true);
	wait();
	
	assertFalse(gp.isHardAlertShown());
	
	gp.cancel();
	gp.reset();
}

// e = none
function verifyEditModeInterruption()
{
	gp = new GoalPlan();

	// lock app for some seconds
	log("---- edit mode interruption : lock app check ----");
	gp.edit();
	lockApp("Lock", 5);
	
	assertTrue(gp.isInEditMode());
	
	// run app in background for some seconds
	log("---- edit mode interruption : deactive app check ----");
	lockApp("Home", 5);
	
	assertTrue(gp.isInEditMode());
	
	gp.cancel();
}




// e = {duration: "Sep 14 - Sep 20", goal: 5}
function verifyWeekInfo(e)
{	
	gp = new GoalPlan();
	info = gp.getWeekInfo();
	
	assertEqual(info.duration, e.duration);
	assertEqual(info.goal, e.goal);
}

// e = {total: 7, passed: 1, days: ["Today Sep 14th", "Sat Sep 15th", ...] }
function verifyDayList(e)
{
	gp = new GoalPlan();
	total = gp.getTotalDays();
	passed = gp.getPassedDays();
	
	assertEqual(e.total, total);
	assertEqual(e.passed, passed);
	
	for(i = 0; i < total; i++)
	{
		info = gp.getDayInfoByIndex(i);
		assertEqual(e.days[i], info.date);
	}
}

// e = {date: "Sat Sep 15th", temperature: "86 ºF", run: 0.34, total: 0.76}
function verifyDayData(i, e)
{
	gp = new GoalPlan();
	info = gp.getDayInfoByIndex(i);
	
	assertEqual(info.date, e.date);
	//assertEqual(info.temperature, e.temperature);
	assertEqual(info.run, e.run);
	assertEqual(info.total, e.total);
}

// e = {date: "Sat Sep 15th", temperature: "86 ºF", run: 0.34, total: 0.76}
function verifyTodayData(e)
{
	gp = new GoalPlan();
	info = gp.getTodayInfo();
	
	assertEqual(info.date, e.date);
	//assertEqual(info.temperature, e.temperature);
	assertEqual(info.run, e.run);
	assertEqual(info.total, e.total);
}




// e = none
function verifyTotalGoal()
{
	gp = new GoalPlan();
		
	total = gp.getWeekInfo().goal;
	remain = gp.getRemainPlanMiles();
	
	// default check: after reset
	log("---- total goal test: default ----");
	t = gp.getTotalPlanMiles();
	r = gp.getRemainPlanMiles();
	
	assertEqual(t, total);
	assertEqual(r, remain);
		
	// easy test: change only some day
	log("---- total goal test: easy ----");
	
	gp.edit();
	gp.planDayByIndex(2, 1.7, true);
	gp.planDayByIndex(3, 0.1, true);
	gp.save();
	
	t = gp.getTotalPlanMiles();
	r = gp.getRemainPlanMiles();
	
	assertEqual(t, gp.getWeekInfo().goal);
	assertEqual(r, remain);	
	
	// high test: change all days to high values
	log("---- total goal test: high ----");
	gp.reset();
	gp.edit();
	gp.planDayByIndex(1, 50, true);
	gp.planDayByIndex(2, 50, true);
	gp.planDayByIndex(3, 50, true);
	gp.planDayByIndex(4, 50, true);
	gp.planDayByIndex(5, 50, true);
	gp.planDayByIndex(6, 50, true);
	gp.save();
	
	info = gp.getDayInfoByIndex(0);
	t = gp.getTotalPlanMiles();
	r = gp.getRemainPlanMiles();
	
	expectTotal = parseFloat(maxMPD * 6 + info.total);
	expectTotal = expectTotal.toFixed(2);
	expectRemain = parseFloat(maxMPD * 6);
	expectRemain = expectRemain.toFixed(2);

	assertEqual(t, expectTotal);
	assertEqual(r, expectRemain);
	
	// low test: change all days to zeros
	log("---- total goal test: low ----");
	gp.reset();
	gp.edit();
	gp.planDayByIndex(1, 0, true);
	gp.planDayByIndex(2, 0, true);
	gp.planDayByIndex(3, 0, true);
	gp.planDayByIndex(4, 0, true);
	gp.planDayByIndex(5, 0, true);
	gp.planDayByIndex(6, 0, true);
	gp.save();
	
	info = gp.getDayInfoByIndex(0);
	t = gp.getTotalPlanMiles();
	r = gp.getRemainPlanMiles();
	
	assertEqual(t, info.total);
	assertEqual(r, 0);
	
	gp.reset();
}

// e = 2.95 (max value that you can set)
function verifyGoalRange(e)
{
	gp = new GoalPlan();
	
	// check max
	log("---- goal range: check max ----");	
	gp.edit();
	gp.planDayByIndex(2, e + 1, true);
	gp.save();
	
	v = gp.getDayInfoByIndex(2);
	
	assertEqual(v.total, e);
	
	// check min
	log("---- goal range: check min ----");	
	gp.reset();
	gp.edit();
	gp.planDayByIndex(2, 0, true);
	gp.save();
	
	v = gp.getDayInfoByIndex(2);
	
	assertEqual(v.total, 0);
	
	gp.reset();
}

