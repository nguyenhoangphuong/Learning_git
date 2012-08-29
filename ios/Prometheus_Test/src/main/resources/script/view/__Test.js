#import "Agreement.js"
#import "SignUp.js"
#import "../core/testcaseBase.js"


UIALogger.logStart("Test Demo");


var agreement = new Agreement();
agreement.tapAgree();

var signup = new SignUp();
signup.fillEmailAndSubmit("");

wait(2);
dummy();
if(signup.isEmptyError())
{
	log("pass");
	UIALogger.logPass("Passed");
}
else
{
	log("fail");
	UIALogger.logFail("Failed");
}
dummy();
