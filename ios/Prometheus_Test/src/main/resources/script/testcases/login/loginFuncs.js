#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"

//============================================================= //
// NAVIGATION
//============================================================= //
function toStartScreen()
{
	nav.toHome();
}

//============================================================= //
// DATA
//============================================================= //
loginTD =
	{
		existedEmail: "existed@test.com",
		nonexistedEmail: "nonexisted@test.com",
		rightPwd: "123456",
		wrongPwd: "asdaldjk"
	}

//============================================================= //
// VERIFY
//============================================================= //
function verifyTranslition()
{
	h = new Home();
	
	// tap log in and then return to home
	hr();
	print("<Tap login and then return to home>");
	h.tapLogin();
	assertTrue(!h.isVisible() && h.isLoginVisible() && !h.isSignupVisible() && !h.isTryoutVisible(), 
			"Only Login screen is visible");
	assertTrue(h.isEmailTextFieldVisible() && h.isPasswordTextFieldVisible(),
			"Email and Password fields exist");
	h.tapLogin();
	assertTrue(h.isVisible() && !h.isLoginVisible() && !h.isSignupVisible() && !h.isTryoutVisible(), 
			"Only Start screen is visible");
	
	// tap sign up and then return to home
	hr();
	print("<Tap signup and then return to home>");
	h.tapSignup();
	assertTrue(!h.isVisible() && !h.isLoginVisible() && h.isSignupVisible() && !h.isTryoutVisible(), 
			"Only Signup screen is visible");
	assertTrue(h.isEmailTextFieldVisible() && h.isPasswordTextFieldVisible(),
			"Email and Password fields exist");
	h.tapSignup();
	assertTrue(h.isVisible() && !h.isLoginVisible() && !h.isSignupVisible() && !h.isTryoutVisible(), 
			"Only Start screen is visible");	
	
	// tap try out and then return to home
	hr();
	print("<Tap tryout and then return to home>");
	h.tapTryout();
	assertTrue(!h.isVisible() && !h.isLoginVisible() && !h.isSignupVisible() && h.isTryoutVisible(), 
			"Only Tryout screen is visible");
	assertTrue(h.isEmailTextFieldVisible() && !h.isPasswordTextFieldVisible(),
			"Only Email field exist");
	h.tapTryout();
	assertTrue(h.isVisible() && !h.isLoginVisible() && !h.isSignupVisible() && !h.isTryoutVisible(), 
			"Only Start screen is visible");		
}

function verifyClientVerification()
{
	h = new Home();
	
	// login with empty email and password
	hr();
	print("<Login with empty email>");
	h.login("", "");
	assertTrue(h.isEmptyEmailAlertShown(), "Empty email alert shown");
	h.tapLogin();
	
	// login with empty password
	hr();
	print("<Login with empty password>");
	h.login(loginTD.existedEmail, "")
	assertTrue(h.isEmptyPasswordAlertShown(), "Empty password alert shown");
	h.tapLogin();
	
	// login with invalid email
	hr();
	print("<Login with invalid email>");
	h.login("invalidEmail", "somepass");
	assertTrue(h.isInvalidEmailAlertShown(), "Invalid email alert shown");
	h.tapLogin();
}

function verifyBackendVerification()
{
	h = new Home();
	
	// login with non-existed user
	hr();
	print("<Login with non-existed email>");
	h.login(loginTD.nonexistedEmail, loginTD.rightPwd);
	assertTrue(h.isInvalidUserAlertShown(), "User not exist alert shown");
	h.tapLogin();

	// login with wrong password
	hr();
	print("<Login with wrong password>");
	h.login(loginTD.existedEmail, loginTD.wrongPwd);
	assertTrue(h.isWrongLoginAlertShown(), "Wrong password or email alert shown");
	
	// signup with existed user
	hr();
	print("<Signup with existed email>");
	h.signup(loginTD.existedEmail, loginTD.rightPwd);
	assertTrue(h.isExistedUserAlertShown(), "Email has been used before alert shown");
	h.tapSignup();
}

function verifyValidLogin()
{
	h = new Home();
	
	// login with non-existed user
	hr();
	print("<Login with valid email and password>");
	h.login(loginTD.existedEmail, loginTD.rightPwd);
	assertTrue(!h.loginVisible(), "Login screen is nor more");
}
