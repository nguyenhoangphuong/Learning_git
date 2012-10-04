#import "loginFuncs.js"

//======================== Test logic ======================== //
start("LoginScreen: Login backend verification");

// navigate
toStartScreen();

// verify
verifyBackendVerification();
verifyValidLogin();

pass("LoginScreen: Login backend verification");