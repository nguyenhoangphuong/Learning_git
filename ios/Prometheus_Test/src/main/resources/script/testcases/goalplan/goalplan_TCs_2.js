#import "goalplan_func.js"
#import "goalplan_data.js"



// ======================== Test logic ======================== //
start("GoalPlan: Min,Max range, goal adjustment, data consitent");

// --- navigate ---
log("1 - Navigate to GoalPlan view");
toGoalPlan();

//--- week and days information ---
log("5 - Verify week and days information");
log("   5.1 - Verify week information");
verifyWeekInfo(norm.e51);
log("   5.2 - Verify day lists");
verifyDayList(norm.e52);
/*
log("   5.3 - Verify day data");
//since plan is depent on weather, this may fail
verifyDayData(norm.e53.e531.i, norm.e53.e531.e);
verifyDayData(norm.e53.e532.i, norm.e53.e532.e);
verifyDayData(norm.e53.e533.i, norm.e53.e533.e);
*/
log("   5.4 - Verify today data");
verifyTodayData(norm.e53.e531.e);

//--- goal adjustment ---
log("6 - Verify goal adjustment and edit mode");
log("   6.1 - Verify max and min range");
verifyGoalRange(maxMPD);
log("   6.2 - Verify total goal auto adjust");
verifyTotalGoal();

pass("GoalPlan: Min,Max range, goal adjustment, data consistent");