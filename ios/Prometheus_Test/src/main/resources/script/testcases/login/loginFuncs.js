#import "../../view/MVPLibs.js"
//============================================================= //
// NAVIGATION
//============================================================= //
function toSignInScreen()
{
	nav.toSignIn();
}

//============================================================= //
// DATA
//============================================================= //

SignInTD =
	{
		existedEmail: "abc@test.com",
		rightPwd: "qwerty1",
		wrongPwd: "asdaldjk1"
	}

//============================================================= //
// VERIFY
//============================================================= //
function verifyTranslition()
{
	signin = new SignIn();
	
	// tap legal and comeback in Signup
	hr();
	print("<Tap legal and verify in Sign up>");
	signin.tapSignUpTab();
	signin.tapLegal();
	assertTrue(!signin.isVisible(), "Current view isn't SignUp anymore");
	assertTrue(signin.isLegalShown(), "Legal info shows up");
	signin.tapCloseLegal();
	assertTrue(signin.isVisible(), "Only Signup screen is visible");
	assertTrue(!signin.isLegalShown(), "Legal info doesn't show");
}

function verifyClientVerification()
{
	signin = new SignIn();
	
	// SignIn with invalid email
	hr();
	print("<SignIn with empty email>");
	signin.signIn("", "");
	assertTrue(signin.isInvalidEmailAlertShown(), "Invalid email alert shown");
	
	// SignIn with empty password
	hr();
	print("<SignIn with empty password>");
	signin.signIn(SignInTD.existedEmail, "")
	assertTrue(signin.isInvalidPasswordAlertShown(), "Invalid password alert shown");
	
	// SignIn with invalid email
	hr();
	print("<SignIn with invalid email>");
	signin.signIn("invalidEmail", SignInTD.rightPwd);
	assertTrue(signin.isInvalidEmailAlertShown(), "Invalid email alert shown");
	
	// SignIn with invalid password [length < 6]
	hr();
	print("<SignIn with password: " + "ab12" + ">");
	signin.signIn(SignInTD.existedEmail, "ab12");
	assertTrue(signin.isInvalidPasswordAlertShown(), "Invalid password alert shown");
	
	// SignIn with invalid password [length == 6, all are letters]
	hr();
	print("<SignIn with password: " + "aaaaaa" + ">");
	signin.signIn(SignInTD.existedEmail, "aaaaaa");
	assertTrue(signin.isInvalidPasswordAlertShown(), "Invalid password alert shown");
	
	// SignIn with invalid password [length == 6, all are digits]
	hr();
	print("<SignIn with password: " + "123456" + ">");
	signin.signIn(SignInTD.existedEmail, "123456");
	assertTrue(signin.isInvalidPasswordAlertShown(), "Invalid password alert shown");
}

function verifyBackendVerification()
{
	signin = new SignIn();
	
	// SignIn with non-existed user
	hr();
	print("<SignIn with non-existed email>");
	var genstring = generateRandomDigitString();
	var nonexistedEmail = "nonexisted" + genstring + "@test.com"
	signin.signIn(nonexistedEmail, SignInTD.rightPwd);
	wait(5);
	assertTrue(signin.isWrongSignInAlertShown(), "User not exist alert shown");

	// SignIn with wrong password
	hr();
	print("<SignIn with wrong password>");
	signin.signIn(SignInTD.existedEmail, SignInTD.wrongPwd);
	wait(5);
	assertTrue(signin.isWrongSignInAlertShown(), "Wrong password or email alert shown");
	
}

function verifyValidSignIn()
{
	signin = new SignIn();
	
	// SignIn with non-existed user
	hr();
	print("<SignIn with valid email and password>");
	signin.signIn(SignInTD.existedEmail, SignInTD.rightPwd);
	wait(5);
	assertTrue(!signin.isVisible(), "SignIn screen is no more");
}
