#import "loginFuncs.js

//======================== Test logic ======================== //
start("StartScreen: Login backend verification");

// navigate
toStartScreen();

// verify
verifyBackendVerification();
verifyValidLogin();

pass("StartScreen: Login backend verification");