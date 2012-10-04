#import "../../view/Home.js"
#import "../../view/UserInfo.js"
#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"
#import "../../core/common.js"
#import "../../view/Settings.js"
/**
 * This test cover: - Email validation - Email inputting
 */

var loginTD =
	{
		existedEmail: "existed@test.com",
		nonexistedEmail: "nonexisted@test.com",
		rightPwd: "123456",
		wrongPwd: "asdaldjk"
	}



function checkValidEmail(email, error)
{
	var signup = new Home();
	//fill email and correct password
	signup.signUp(email,"123456");
	wait(1);
	// validate in here
	assertTrue(!signup.isSignUpVisible(), "Check valid signup:" + error);
	signOutAfterSignUp();
	hr();
	
}

function signOutAfterSignUp()
{
	nav.toSettings();
	var setting = new Settings();
	setting.signOut();
}



function checkInvalidEmail(email)
{
	var signup = new Home();
	signup.signUp(email,"123456");
	assertTrue(signup.isInvalidEmailAlertShown(), "email: " + email + "----- Fail.Expected: not valid. Actual:  valid");
	signup.tapSignUp();
	hr();
}

function checkEmptyEmail()
{
	var signup = new Home();
	signup.signUp("","123456");
	assertTrue(signup.isEmptyEmailAlertShown(), "Empty email: -----Fail.Expected: not valid. Actual: valid");
	signup.tapSignUp();
	hr();
}

function checkEmptyPassword()
{
	var signup = new Home();
	signup.signUp("khoathai@yahoo.com","");
	assertTrue(signup.isEmptyPasswordAlertShown(), "Password empty-----Fail. expected : not valid . Actual : Valid");
	signup.tapSignUp();
	hr();
}

function checkDuplicatedUser()
{
	var signup = new Home();
	signup.signUp(loginTD.existedEmail,loginTD.rightPwd);
	assertTrue(signup.isExistedUserAlertShown(),"Duplicated user sign up-----Fail. expected : not valid . Actual : Valid");
	signup.tapSignUp();
	hr();
	
}


