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



function checkInvalidEmail(email)
{
	var signup = new SignUp();
	signup.fillEmailAndSubmit(email);
	var msg = signup.getErrorMessage();
	if (msg == signup.MsgInvalid)
		UIALogger.logPass("Pass");
	else
		UIALogger.logFail("Fail. The email is valid");
}

function checkEmptyEmail(email)
{
	var signup = new SignUp();
	signup.fillEmailAndSubmit(email);
	var msg = signup.getErrorMessage();
	if (msg == signup.MsgEmpty)
		UIALogger.logPass("Pass");
	else
		UIALogger.logFail("Fail. The email is valid");
}
