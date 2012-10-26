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
		existedEmail: "testexisted@test.com",
		rightPwd: "a123456",
		wrongPwd: "asdaldjk1"
	}

//============================================================= //
// VERIFY
//============================================================= //
function verifyTranslition()
{
	signin = new SignIn();
	
	// verify cannot tap legal in sign in
	hr();
	print("<Tap SignIn tab>");
	signin.tapSignInTab();
	signin.tapLegal();
	assertTrue(signin.isVisible(), "Only Signup screen is visible");
	
	
	//Tap legal and comeback in Signup
	hr();
	print("<Tap legal and verify in Sign up>");
	signin.tapSignUpTab();
	signin.tapLegal();
	assertTrue(!signin.isVisible(), "Only Signup screen is visible");
	signin.tapCloseLegal();
	assertTrue(signin.isVisible(), "Only Signup screen is visible");
	
}

function verifyClientVerification()
{
	signin = new SignIn();
	
	// SignIn with invalid email
	
	hr();
	print("<SignIn with empty email>");
	signin.signIn("", "");
	assertTrue(signin.isInvalidEmailAlertShown(), "Empty email alert shown");

	
	// SignIn with empty password
	hr();
	print("<SignIn with empty password>");
	signin.signIn(SignInTD.existedEmail, "")
	wait(1);
	assertTrue(signin.isInvalidPasswordAlertShown(), "Invalid password alert shown");
	
	
	// SignIn with invalid email
	hr();
	print("<SignIn with invalid email>");
	signin.signIn("invalidEmail", SignInTD.rightPwd);
	assertTrue(signin.isInvalidEmailAlertShown(), "Invalid email alert shown");
	wait(10);	

	
	// SignIn with invalid password [length < 6]
	hr();
	print("<SignIn with password: " + SignInTD.wrongPwd + ">");
	signin.signIn(SignInTD.existedEmail, SignInTD.wrongPwd);
	wait(10);
	assertTrue(signin.isWrongSignInAlertShown(), "Invalid password alert shown");
	wait(10);
	
	// SignIn with invalid password [length == 6, all are letters]
	hr();
	print("<SignIn with password: " + SignInTD.wrongPwd + ">");
	signin.signIn(SignInTD.existedEmail, SignInTD.wrongPwd);
	wait(10);
	assertTrue(signin.isWrongSignInAlertShown(), "Invalid password alert shown");

	
	// SignIn with invalid password [length == 6, all are digits]
	hr();
	print("<SignIn with password: " + SignInTD.wrongPwd + ">");
	signin.signIn(SignInTD.existedEmail, SignInTD.wrongPwd);
	wait(10);
	assertTrue(signin.isWrongSignInAlertShown(), "Invalid password alert shown");

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
	wait(10);
	assertTrue(signin.isWrongSignInAlertShown(), "User not exist alert shown");

	// SignIn with wrong password
	hr();
	print("<SignIn with wrong password>");
	signin.signIn(SignInTD.existedEmail, SignInTD.wrongPwd);
	wait(10);
	assertTrue(signin.isWrongSignInAlertShown(), "Wrong password or email alert shown");
	
}

function verifyValidSignIn()
{
	signin = new SignIn();
	
	// SignIn with non-existed user
	hr();
	print("<SignIn with valid email and password>");
	signin.signIn(SignInTD.existedEmail, SignInTD.rightPwd);
	assertTrue(!signin.isVisible(), "SignIn screen is no more");
}
