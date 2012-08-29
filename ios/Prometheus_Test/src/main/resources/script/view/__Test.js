<<<<<<< HEAD
//#import "Agreement.js"
//#import "SignUp.js"
#import "../core/testcaseBase.js"
=======
#import "Agreement.js"
#import "SignUp.js"
>>>>>>> Update view wrapper for automation


UIALogger.logStart("Test Demo");
<<<<<<< HEAD
logTree();
=======
>>>>>>> Update view wrapper for automation

var agreement = new Agreement(target);
agreement.tapAgree();

var signup = new SignUp(target);
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
