#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"

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
		nav.toSettings(null, null, null, null, null);
		return;
	}
	
	nav.toSettings(loginTD.existedEmail, loginTD.rightPwd, null, null, null, true);
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
	
	h = new Home();
	assertTrue(h.isSignUpVisible(), "Home screen show at SignUp after tap [Sign up]");
}

function verifyValidUserLogout()
{
	s = new Settings();
	
	assertFalse(s.isSignUpBtnVisible(), "SignUp button is not visible");
	assertTrue(s.isSignOutBtnVisible(), "SignOut button is visible");
	
	s.signOut(true);
	
	h = new Home();
	assertTrue(h.isVisible(), "Home screen show at SignUp after [Sign out]");
}