#import "logoutFuncs.js"

//======================== Test logic ======================== //
start("Log out testcases");

// valid users log out
hr();
log("1. Valid users log out")
log("   1.1. Login with valid account");
toSettingScreen(1);
wait();
log("   1.2. Vefiry Sign out button and sign out");
verifyValidUserLogout();

// anonymous log out
hr();
log("2. Anonymoues users log out")
log("   2.1. Try out the app");
toSettingScreen();
wait();
log("   2.2. Vefiry Sign up button and sign up");
verifyAnonymousLogout();

pass("Log out testcases");