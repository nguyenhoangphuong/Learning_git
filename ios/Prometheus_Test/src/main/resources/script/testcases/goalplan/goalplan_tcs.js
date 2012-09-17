#import "goalplan_func.js"
#import "goalplan_data.js"

/* ------------------
 SUMMARY:
 - 	This automation script cover all the testcases in GoalAdjustment
 	section except the following:
 	4266, 4267, 4271, 4277, 4286
 
 -	To be improve:
 	+ Dynamic test data base on current day
 
 -	To be note:
 	+ Remember to set the global "maxMPD" var value or you will end up
	sliding with wrong value.
 */

// ======================== Test data ======================== //
norm = 
{
	e51 :	e51(10),
	e52	:	e52(),
	e53	:
	{
		e531 :	{i: 0, e: e53(0)},
		e532 :	{i: 1, e: e53(1)},
		e533 :	{i: 4, e: e53(4)},
	},
};
maxMPD = 4.37;

// ======================== Test logic ======================== //
start("-------- GoalPlan test ---------");

// --- navigate ---
log("1 - Navigate to GoalPlan view");
//toGoalPlan(1);


// --- buttons verify ---
log("2 - Verify buttons");
log("   2.1 - Auto suggest button");
verifyAutoButton();
log("   2.2 - Edit button");
verifyEditButton();
log("   2.3 - Cancel button");
verifyCancelButton();
log("   2.4 - Done button");
verifyDoneButton();


// --- sliders verify ---
log("3 - Verify sliders");
log("   3 - Verify sliders not draggable");
verifySliderNotDraggable();
log("   3 - Verify sliders draggable");
verifySliderDraggable();


// --- alert and interruption ---
log("4 - Verify alert and interruption");
log("   4.1 - Verify hard alert");
verifyHardAlert();
log("   4.2 - Verify interruption in edit mode");
verifyEditModeInterruption();


// --- week and days information ---
log("5 - Verify week and days information");
log("   5.1 - Verify week information");
verifyWeekInfo(norm.e51);
log("   5.2 - Verify day lists");
verifyDayList(norm.e52);
log("   5.3 - Verify day data");
verifyDayData(norm.e53.e531.i, norm.e53.e531.e);
verifyDayData(norm.e53.e532.i, norm.e53.e532.e);
verifyDayData(norm.e53.e533.i, norm.e53.e533.e);
log("   5.4 - Verify today data");
verifyTodayData(norm.e53.e531.e);


// --- goal adjustment ---
log("6 - Verify goal adjustment and edit mode");
log("   6.1 - Verify max and min range");
verifyGoalRange(maxMPD);
log("   6.2 - Verify total goal auto adjust");
verifyTotalGoal();

pass("-------- GoalPlan test ---------");