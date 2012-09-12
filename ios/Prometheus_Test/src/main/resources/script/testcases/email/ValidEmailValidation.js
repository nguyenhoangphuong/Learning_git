#import "../../view/SignUp.js"
#import "../../view/UserInfo.js"
#import "../../core/testcaseBase.js"
#import "../../core/common.js"
/**
 * This test cover: - Email validation - Email inputting
 */




function checkValidEmail(email, error)
{
	var signup = new SignUp();
	signup.fillEmailAndSubmit(email);
	wait(1);
	var user = new UserInfo();
	if (user.isVisible())
		UIALogger.logPass("Pass" + "with : " + email);
	else
		UIALogger.logFail("Fail" + error + "with : " + email);
}