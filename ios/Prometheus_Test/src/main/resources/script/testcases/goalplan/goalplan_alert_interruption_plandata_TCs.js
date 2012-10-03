#import "goalplan_func.js"
#import "goalplan_data.js"

/* ------------------ 
 -	To be note:
 	+ Remember to set the global "maxMPD" var value or you will end up
	sliding with wrong value.
 */

maxMPD = norm.maxMPD;

// ======================== Test logic ======================== //
start("GoalPlan: Alert, Interruption, Data consitent");

// --- navigate ---
log("1 - Navigate to GoalPlan view");
toGoalPlan();

//--- alert and interruption ---
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

pass("GoalPlan: Alert, Interruption, Data consitent");