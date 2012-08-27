#import "Agreement.js"
#import "SignUp.js"

var target = UIATarget.localTarget();

UIALogger.logStart("Test Demo");
#var agreement = new Agreement(target);
#var signup = agreement.TabAgree();
#signup.FillEmailAndSubmit("");
#var error = signup.GetErrorMessage();

#if(error == signup.ErrNull)
#	UIALogger.logPass("Passed");
#else
	UIALogger.logFail("Failed");
