#import "loginFuncs.js"

//======================== Test logic ======================== //
start("LoginScreen: Login client verification");

// navigate
toStartScreen();

// verify
verifyClientVerification();

pass("LoginScreen: Login client verification");