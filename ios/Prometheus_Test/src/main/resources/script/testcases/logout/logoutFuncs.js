#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"

//============================================================= //
//DATA
//============================================================= //
loginTD =
	{
		existedEmail: "existed@test.com",
		nonexistedEmail: "nonexisted@test.com",
		rightPwd: "123456",
		wrongPwd: "asdaldjk"
	}

//============================================================= //
// NAVIGATION
//============================================================= //
function toSettingScreen(opt)
{
	if(typeof opt == "undefined")
	{
		nav.toSettings("tryout@test.com", null, null, "Running", 10);
		return;
	}
	
	nav.toSettings("existed@test.com", "123456", null, "Running", 10);
}

//============================================================= //
// VERIFY
//============================================================= //
function verifyAnonymousLogout()
{
	s = new Settings();
	assertTrue(s.hasSignedIn(), "No Logout button");
}

function verifyValidUserLogout()
{
	s = new Settings();
	assertTrue(s.hasSignedIn(), "Logout button existed");
	s.signOut();
	
	h = new Home();
	assertTrue(h.isVisible(), "Home screen shown after log out");
}