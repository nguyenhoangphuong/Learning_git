#import "goalplan_func.js"
#import "goalplan_data.js"

/*
	+ 4.2 Verify interruption by pressing Home may not run on some device
 	+ 5.3 Pass only when there is no weather information
 */

// ======================== Test logic ======================== //
start("GoalPlan: Buttons, Sliders behaviour, Alert, Interruption");

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

//--- alert and interruption ---
log("4 - Verify alert and interruption");
log("   4.1 - Verify hard alert");
verifyHardAlert();
log("   4.2 - Verify interruption in edit mode");
verifyEditModeInterruption();

pass("GoalPlan: Buttons, Sliders behaviour, Alert, Interruption");