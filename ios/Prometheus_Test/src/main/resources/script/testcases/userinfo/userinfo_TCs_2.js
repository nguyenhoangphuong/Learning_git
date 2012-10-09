#import "userinfo_func.js"

// ======================== Test logic ======================== //
start("UserInfo: Swiping, Oldstate and Interruption");

// --- navigate ---
log("1 - Navigate to UserInfo view");
GoToUserInfoScreen();

// --- swiping verify ---
log("4 - Verify Swiping");
log("   4 - Verify swipe horizontal");
VerifySwipeToChangeWeight();
log("   4 - Verify swipe vertical");
VerifySwipeToChangeHeight();

// --- old state and interruption ---
log("5 - Verify old state and interruption");
log("   5.1 - Verify interruption");
VerifyInteruption();
log("   5.2 - Verify old state");
VerifyOldState();

pass("UserInfo: Swiping, Oldstate and Interruption");