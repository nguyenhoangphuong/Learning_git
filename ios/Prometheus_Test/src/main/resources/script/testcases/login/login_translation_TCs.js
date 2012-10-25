#import "loginFuncs.js"

//======================== Test logic ======================== //
start("StartHomeScreen: translation");

// navigate
toSignInScreen();

// verify
verifyTranslition();

pass("StartHomeScreen: translation");
