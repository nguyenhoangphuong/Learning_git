#import "logoutFuncs.js"

//======================== Test logic ======================== //
start("Log out testcases");

// anonymous log out
hr();
log("1. Anonymoues users log out")
log("   1.1. Try out the app");
toSettingScreen();
wait();
log("   1.2. Vefiry Sign up button and sign up");
verifyAnonymousLogout();

// valid users log out
hr();
log("2. Valid users log out")
log("   2.1. Login with valid account");
toSettingScreen(1);
wait();
log("   2.2. Vefiry Sign out button and sign out");
verifyValidUserLogout();


pass("Log out testcases");

// auto login user log out
hr();
log("3. Auto login users log out")
log("   3.1. Sign up the app");
toSettingScreen(2);
wait();
log("   3.2. Vefiry Sign out button and sign out");
verifyValidUserLogout();

pass("Log out testcases");
