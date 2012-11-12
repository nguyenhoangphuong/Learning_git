#import "../../view/MVPLibs.js"

//============================================================= //
//DATA
//============================================================= //
loginTD =
	{
		existedEmail: "abc@test.com",
		rightPwd: "qwerty1"
	}

//============================================================= //
// NAVIGATION
//============================================================= //
function toSettingScreen(opt)
{
	
	if(typeof opt == "undefined")
	{
		// to setting by try out
		log("To Settings screen by check out");
		nav.toSettings(null, null, null, pinfodefault);
	}
	
	if(opt == 1)
	{
		// to setting by sign in
		log("To Settings screen by sign in");
		nav.toSettings(loginTD.existedEmail, loginTD.rightPwd, null, null, true);
	}
	
	if(opt == 2)
	{
		// to setting by sign up
		log("To Settings screen by sign up");
		var genstring = generateRandomDigitString();
		nav.toSettings("qaGeneratedEmail" + genstring + loginTD.existedEmail, loginTD.rightPwd, null, null, false);	
	}
}

//============================================================= //
// VERIFY
//============================================================= //
function verifyAnonymousLogout()
{
	s = new Settings();
	
	assertTrue(s.isSignUpBtnVisible(), "SignUp button is visible");
	assertFalse(s.isSignOutBtnVisible(), "but SignOut button is not visible");
	
	s.signUp(true);
	
	h = new SignIn();
	assertTrue(h.isVisible(), "SignIn screen show at SignUp after tap [Sign up]");
}

function verifyValidUserLogout()
{
	s = new Settings();
	
	assertFalse(s.isSignUpBtnVisible(), "SignUp button is not visible");
	assertTrue(s.isSignOutBtnVisible(), "but SignOut button is visible");
	
	s.signOut(true);
	
	h = new SignIn();
	assertTrue(h.isVisible(), "SignIn screen show at SignUp after [Sign out]");
}