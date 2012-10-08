#import "../../view/Home.js"
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
		existedEmail: "testexisted@test.com",		
		rightPwd: "a123456"
	}



function checkValidEmail(email, error)
{
	var signup = new Home();
	//fill email and correct password
	log("begin");
	signup.signUp(email,signupTD.rightPwd);
	wait(1);
	// validate in here
	assertTrue(!signup.isSignUpVisible(), "Check valid signup:" + error);
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
	var signup = new Home();
	signup.signUp(email,signupTD.rightPwd);
	assertTrue(signup.isInvalidEmailAlertShown(), "email: " + email + "----- Fail.Expected: not valid. Actual:  valid");
	signup.tapSignUp();
	hr();
}

function checkEmptyEmail()
{
	var signup = new Home();
	signup.signUp("",signupTD.rightPwd);
	assertTrue(signup.isInvalidEmailAlertShown(), "Empty email: -----Fail.Expected: not valid. Actual: valid");
	signup.tapSignUp();
	hr();
}

function checkPassword(password)
{
	var signup = new Home();
	var genstring = generateRandomDigitString();
	var email = "nonexisted" + genstring + "@test.com";
	signup.signUp(email,password);
	assertTrue(signup.isInvalidPasswordAlertShown(), "Password validate format-----Fail. expected : not valid . Actual : Valid");
	signup.tapSignUp();
	hr();
}



function checkDuplicatedUser()
{
	var signup = new Home();
	signup.signUp(signupTD.existedEmail,signupTD.rightPwd);
	assertTrue(signup.isExistedUserAlertShown(),"Duplicated user sign up-----Fail. expected : not valid . Actual : Valid");
	signup.tapSignUp();
	hr();
	
}


