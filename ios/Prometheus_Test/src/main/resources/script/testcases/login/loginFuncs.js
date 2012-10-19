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

SignInTD =
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
	print("<Tap SignIn and then return to home>");
	h.tapSignInTab();
	assertTrue(!h.isVisible() && h.isSignInVisible() && !h.isSignUpVisible() , "Only SignIn screen is visible");
	assertTrue(h.isEmailTextFieldVisible() && h.isPasswordTextFieldVisible(), "Email and Password fields exist");
	h.tapSignInTab(); 
	wait();
	assertTrue(h.isVisible() && !h.isSignInVisible() && !h.isSignUpVisible(), "Only Start screen is visible");
	
	// tap sign up and then return to home
	hr();
	print("<Tap signup and then return to home>");
	h.tapSignUp();
	assertTrue(!h.isVisible() && !h.isSignInVisible() && h.isSignUpVisible(), "Only Signup screen is visible");
	assertTrue(h.isEmailTextFieldVisible() && h.isPasswordTextFieldVisible(), "Email and Password fields exist");
	h.tapSignUp(); 
	wait();
	assertTrue(h.isVisible() && !h.isSignInVisible() && !h.isSignUpVisible(), "Only Start screen is visible");
}

function verifyClientVerification()
{
	h = new Home();
	
	// SignIn with invalid email
	hr();
	print("<SignIn with empty email>");
	h.signIn("", "");
	assertTrue(h.isInvalidEmailAlertShown(), "Empty email alert shown");
	h.tapSignInTab();
	
	// SignIn with empty password
	hr();
	print("<SignIn with empty password>");
	h.signIn(SignInTD.existedEmail, "")
	assertTrue(h.isInvalidPasswordAlertShown(), "Invalid password alert shown");
	h.tapSignInTab();
	
	// SignIn with invalid email
	hr();
	print("<SignIn with invalid email>");
	h.signIn("invalidEmail", SignInTD.rightPwd);
	assertTrue(h.isInvalidEmailAlertShown(), "Invalid email alert shown");
	h.tapSignInTab();
	
	// SignIn with invalid password [length < 6]
	hr();
	print("<SignIn with password: " + SignInTD.wrongPwd1 + ">");
	h.signIn(SignInTD.existedEmail, SignInTD.wrongPwd1);
	assertTrue(h.isInvalidPasswordAlertShown(), "Invalid password alert shown");
	h.tapSignInTab();
	
	// SignIn with invalid password [length == 6, all are letters]
	hr();
	print("<SignIn with password: " + SignInTD.wrongPwd2 + ">");
	h.signIn(SignInTD.existedEmail, SignInTD.wrongPwd2);
	assertTrue(h.isInvalidPasswordAlertShown(), "Invalid password alert shown");
	h.tapSignInTab();
	
	// SignIn with invalid password [length == 6, all are digits]
	hr();
	print("<SignIn with password: " + SignInTD.wrongPwd3 + ">");
	h.signIn(SignInTD.existedEmail, SignInTD.wrongPwd3);
	assertTrue(h.isInvalidPasswordAlertShown(), "Invalid password alert shown");
	h.tapSignInTab();
}

function verifyBackendVerification()
{
	h = new Home();
	
	// SignIn with non-existed user
	hr();
	print("<SignIn with non-existed email>");
	var genstring = generateRandomDigitString();
	var nonexistedEmail = "nonexisted" + genstring + "@test.com"
	h.signIn(nonexistedEmail, SignInTD.rightPwd);
	assertTrue(h.isWrongSignInAlertShown(), "User not exist alert shown");
	h.tapSignInTab();

	// SignIn with wrong password
	hr();
	print("<SignIn with wrong password>");
	h.signIn(SignInTD.existedEmail, SignInTD.wrongPwd);
	assertTrue(h.isWrongSignInAlertShown(), "Wrong password or email alert shown");
	h.tapSignInTab();
	
}

function verifyValidSignIn()
{
	h = new Home();
	
	// SignIn with non-existed user
	hr();
	print("<SignIn with valid email and password>");
	h.signIn(SignInTD.existedEmail, SignInTD.rightPwd);
	assertTrue(!h.isSignInVisible(), "SignIn screen is no more");
}
