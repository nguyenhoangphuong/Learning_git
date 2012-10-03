#import "goalplan_func.js"
#import "goalplan_data.js"

/* ------------------ 
 -	To be note:
 	+ Remember to set the global "maxMPD" var value or you will end up
	sliding with wrong value.
 */

maxMPD = norm.maxMPD;

// ======================== Test logic ======================== //
start("GoalPlan: Buttons, Sliders behaviour");

// --- navigate ---
log("1 - Navigate to GoalPlan view");
toGoalPlan();


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

//--- sliders verify ---
log("3 - Verify sliders");
log("   3 - Verify sliders not draggable");
verifySliderNotDraggable();
log("   3 - Verify sliders draggable");
verifySliderDraggable();

pass("GoalPlan: Buttons, Sliders behaviour");