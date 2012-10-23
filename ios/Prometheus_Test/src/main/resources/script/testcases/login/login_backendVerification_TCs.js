#import "loginFuncs.js"

//======================== Test logic ======================== //
start("LoginScreen: Login backend verification");

// navigate
toStartScreen();

// verify
verifyBackendVerification();
verifyValidSignIn();

pass("LoginScreen: Login backend verification");