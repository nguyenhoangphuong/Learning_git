#import "goalplan_func.js"
#import "goalplan_data.js"



/* ------------------ 
 -	To be note:
 	+ Remember to set the global "maxMPD" var value or you will end up
	sliding with wrong value.
 */

maxMPD = norm.maxMPD;

// ======================== Test logic ======================== //
start("GoalPlan: Min,Max range, goal adjustment");

// --- navigate ---
log("1 - Navigate to GoalPlan view");
toGoalPlan();

//--- goal adjustment ---
log("6 - Verify goal adjustment and edit mode");
log("   6.1 - Verify max and min range");
verifyGoalRange(maxMPD);
log("   6.2 - Verify total goal auto adjust");
verifyTotalGoal();

pass("GoalPlan: Min,Max range, goal adjustment");