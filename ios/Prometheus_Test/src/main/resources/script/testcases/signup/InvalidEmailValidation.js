#import "../../view/SignUp.js"
#import "../../core/testcaseBase.js"

/**
 * This test cover: - Email validation - Email inputting
 */

var target = UIATarget.localTarget();

UIALogger.logStart("Test email validation");

checkEmptyEmail("");
checkInvalidEmail("@domain.com");
checkInvalidEmail("email@@domain.com");
checkInvalidEmail(".email@domain.com");
checkInvalidEmail("email.@domain.com");
checkInvalidEmail("email@domain");
checkInvalidEmail("email@domain..com");
checkInvalidEmail("email@***$$$.com");
checkInvalidEmail("em%^ail@domain.com");
checkInvalidEmail("email@-domain.com");
checkInvalidEmail("emaildomain.com");
checkInvalidEmail("email@.com");
checkInvalidEmail("$$$$$$");
checkEmptyPassword();


function checkInvalidEmail(email)
{
	var signup = new Home();
	signup.fillEmail(email);
	signup.fillPassword("123456")	;
	signup.submit()	;
	if (signup.isInvalidEmailAlertShown())
		UIALogger.logPass("Pass");
	else
		UIALogger.logFail("email: " + email + "----- Fail.Expected: not valid. Actual:  valid");
}

function checkEmptyEmail(email)
{
	var signup = new Home();
	signup.fillEmail(email);
	signup.fillPassword("123456")	;
	signup.submit()	;
	if (signup.isEmptyEmailAlertShown())
		UIALogger.logPass("Pass");
	else
		UIALogger.logFail("email: " + email + "-----Fail.Expected: not valid. Actual: valid");
}

function checkEmptyPassword()
{
	var signup = new Home();
	signup.fillEmail("khoathai@yahoo.com");
	signup.fillPassword("")	;
	signup.submit()	;
	if (signup.isEmptyPasswordAlertShown())
		UIALogger.logPass("Pass");
	else
		UIALogger.logFail("Password empty-----Fail. expected : not valid . Actual : Valid");
}

