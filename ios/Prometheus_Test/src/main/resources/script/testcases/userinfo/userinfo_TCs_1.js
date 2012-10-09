#import "userinfo_func.js"

// ======================== Test logic ======================== //
start("UserInfo: Input, Flow");

// --- navigate ---
log("1 - Navigate to UserInfo view");
GoToUserInfoScreen();

// --- input verify ---
log("2 - Verify Input");
log("   2.1 - Default Values")
CheckDefaultValue();
log("   2.2 - Input Age");
InputAge();
log("   2.3 - Input Weight");
InputWeight();
log("   2.4 - Input Height");
InputHeight();
log("   2.5 - Input Gender");
InputGender();

//--- next button verify ---
log("3 - Verify next button");
VerifyNextButton();

pass("UserInfo: Input, Flow");
