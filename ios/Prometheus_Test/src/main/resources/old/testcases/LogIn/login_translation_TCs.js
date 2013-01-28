#import "loginFuncs.js"
listAllStaticTexts(target.frontMostApp().mainWindow());

//======================== Test logic ======================== //
start("StartHomeScreen: translation");

// navigate
toSignInScreen();

// verify
verifyTranslition();

pass("StartHomeScreen: translation");

