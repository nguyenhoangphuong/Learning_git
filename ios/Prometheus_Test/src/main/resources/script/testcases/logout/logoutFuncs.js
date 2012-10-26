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
		// to setting
		
		nav.toSettings(null, null, null, pinfodefault , null);
			
		return;
	}
	
	var genstring = generateRandomDigitString();
	
	nav.toSettings(genstring + loginTD.existedEmail, loginTD.rightPwd, null, pinfodefault, false);
}

//============================================================= //
// VERIFY
//============================================================= //
function verifyAnonymousLogout()
{
	s = new Settings();
	
	assertTrue(s.isSignUpBtnVisible(), "SignUp button is visible");
	assertFalse(s.isSignOutBtnVisible(), "SignOut button is not visible");
	
	s.signUp();
	
	h = new SignIn();
	assertTrue(h.isSignUpVisible(), "SignIn screen show at SignUp after tap [Sign up]");
}

function verifyValidUserLogout()
{
	s = new Settings();
	
	assertFalse(s.isSignUpBtnVisible(), "SignUp button is not visible");
	assertTrue(s.isSignOutBtnVisible(), "SignOut button is visible");
	
	s.signOut(true);
	
	h = new SignIn();
	assertTrue(h.isVisible(), "SignIn screen show at SignUp after [Sign out]");
}