#import "loginFuncs.js"

//======================== Test logic ======================== //
start("LoginScreen: Login client verification");

// navigate
toSignInScreen();

// verify
verifyClientVerification();

pass("LoginScreen: Login client verification");

