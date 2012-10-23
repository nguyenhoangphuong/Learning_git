#import "../../view/SignIn.js"
#import "../../view/UserInfo.js"
#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"
#import "../../core/common.js"
#import "../../view/Settings.js"
/**
 * This test cover: - Email validation - Email inputting
 */
var signupTD =
	{
		existedEmail: "abc@test.com",		
		rightPwd: "qwerty1"
	}



function checkValidEmail(email, error)
{
	var signup = new SignIn();
	//fill email and correct password
	log("begin");
	signup.signUp(email,signupTD.rightPwd);
	wait(1);
	// validate in here
	assertTrue(!signup.isVisible(), "Check valid signup:" + error);
	signOutAfterSignUp();
	hr();
	
}

function signOutAfterSignUp()
{
	nav.toSettings();
	wait();
	var setting = new Settings();
	setting.signOut();
}



function checkInvalidEmail(email)
{
	var signup = new SignIn();
	signup.signUp(email,signupTD.rightPwd);
	assertTrue(signup.isInvalidEmailAlertShown(), "email: " + email + "-----.Expected: not valid. Actual:  valid");
	signup.tapSignUpTab();
	hr();
}

function checkEmptyEmail()
{
	var signup = new SignIn();
	signup.signUp("",signupTD.rightPwd);
	assertTrue(signup.isInvalidEmailAlertShown(), "Empty email: -----.Expected: not valid. Actual: valid");
	signup.tapSignUpTab();
	hr();
}

function checkPassword(password)
{
	var signup = new SignIn();
	var genstring = generateRandomDigitString();
	var email = "nonexisted" + genstring + "@test.com";
	signup.signUp(email,password);
	assertTrue(signup.isInvalidPasswordAlertShown(), "Password validate format-----. expected : not valid . Actual : Valid");
	signup.tapSignUpTab();
	hr();
}



function checkDuplicatedUser()
{
	var signup = new SignIn();
	signup.signUp(signupTD.existedEmail,signupTD.rightPwd);
	assertTrue(signup.isExistedUserAlertShown(),"Duplicated user sign up-----. expected : not valid . Actual : Valid");
	signup.tapSignUpTab();
	hr();
	
}


