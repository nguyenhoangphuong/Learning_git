#import "settings_funcs.js"

// TEST DATA ==============================================
var email = generateSignupAccount();
var pwd = "qwerty1";
var ui1 = 
	{
		name: "", unit: "metric", gender: "male", 
		w1: 68, w2: ".5", h1: 1, h2: ".70",

		weight: "68.5 kg", height: "1.70 m"
	};

var ui2 = 
	{
		name: "Tears", unit: "us", gender: "female", 
		w1: 140, w2: ".9", h1: "7'", h2: "9\"",
		
		weight: "140.9 lbs", height: "7'9\""		
	}


// TEST LOGIC =============================================

start("Settings: EmailSupport, ResetPlan");
// -------------------------------------------------------

log("1. Navigate to settings");
hr();
GoToSettingScreen();

log("2. Verify email support without email");
hr();
VerifyEmailSupportWithoutAccount();

log("3. Verify reset button");
hr();
VerifyResetButton();

log("4. Verify userinfo saved correctly");
hr();
FromSettingToSignIn();		// go to sign in screen
							// sign up new account
GoToSettingScreen(email, pwd, ui1, false);
VerifyUserInfoSaved(ui1);	// check if user info is saved
SetUserInfo(ui2);			// set different user info
FromSettingToSignIn();		// sign out
							// sign in with that account
GoToSettingScreen(email, pwd, ui1, true);
VerifyUserInfoSaved(ui2);	// check if user info is saved

// --------------------------------------------------------
pass("Settings: EmailSupport, ResetPlan");