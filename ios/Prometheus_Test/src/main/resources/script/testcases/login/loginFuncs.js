#import "../../view/_Navigator.js"
#import "../../view/Home.js"
#import "../../core/testcaseBase.js"
#import "../../core/common.js"
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
		existedEmail: "testexisted@test.com",
		rightPwd: "a123456",
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
	assertTrue(!h.isVisible() && h.isLoginVisible() && !h.isSignUpVisible() && !h.isTryoutVisible(), "Only Login screen is visible");
	assertTrue(h.isEmailTextFieldVisible() && h.isPasswordTextFieldVisible(), "Email and Password fields exist");
	h.tapLogin();
	wait(1);
	assertTrue(h.isVisible() && !h.isLoginVisible() && !h.isSignUpVisible() && !h.isTryoutVisible(), 
			"Only Start screen is visible");
	
	// tap sign up and then return to home
	hr();
	print("<Tap signup and then return to home>");
	h.tapSignUp();
	assertTrue(!h.isVisible() && !h.isLoginVisible() && h.isSignUpVisible() && !h.isTryoutVisible(), 
			"Only Signup screen is visible");
	assertTrue(h.isEmailTextFieldVisible() && h.isPasswordTextFieldVisible(),
			"Email and Password fields exist");
	h.tapSignUp();
	assertTrue(h.isVisible() && !h.isLoginVisible() && !h.isSignUpVisible() && !h.isTryoutVisible(), 
			"Only Start screen is visible");	
	
	// tap try out and then return to home
	hr();
	print("<Tap tryout and then return to home>");
	h.tapTryOut();
	assertTrue(!h.isVisible() && !h.isLoginVisible() && !h.isSignUpVisible() && !h.isTryoutVisible(), 
			"Only Tryout screen is visible");
		
}

function verifyClientVerification()
{
	h = new Home();
	
	// login with empty email and password
	hr();
	print("<Login with empty email>");
	h.login("", "");
	assertTrue(h.isInvalidEmailAlertShown(), "Empty email alert shown");
	h.tapLogin();
	
	// login with empty password
	hr();
	print("<Login with empty password>");
	h.login(loginTD.existedEmail, "")
	assertTrue(h.isInvalidPasswordAlertShown(), "Empty password alert shown");
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
	var genstring = generateRandomDigitString();
	var nonexistedEmail = "nonexisted" + genstring + "@test.com"
	h.login(nonexistedEmail, loginTD.rightPwd);
	assertTrue(h.isWrongLoginAlertShown(), "User not exist alert shown");
	h.tapLogin();

	// login with wrong password
	hr();
	print("<Login with wrong password>");
	h.login(loginTD.existedEmail, loginTD.wrongPwd);
	assertTrue(h.isWrongLoginAlertShown(), "Wrong password or email alert shown");
	h.tapLogin();
	
}

function verifyValidLogin()
{
	h = new Home();
	
	// login with non-existed user
	hr();
	print("<Login with valid email and password>");
	h.login(loginTD.existedEmail, loginTD.rightPwd);
	assertTrue(!h.isLoginVisible(), "Login screen is nor more");
}
