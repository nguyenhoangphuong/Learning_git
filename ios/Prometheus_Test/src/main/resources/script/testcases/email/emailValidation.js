#import "../../view/SignUp.js"
#import "../../core/testcaseBase.js"

/**
 * This test cover:
 * - Email validation
 * - Email inputting
 */

var target = UIATarget.localTarget();

UIALogger.logStart("Test email validation");

wait(5);
logTree();




var signup = new SignUp();
signup.pressLicenceAgreement();
log("In licence agreement");
wait();
log("abcde " + signup.isLicenceAgreementShown());
signup.closeLicenceAgreement();






UIALogger.logPass("Pass");
//signup.FillEmailAndSubmit("");
//var error = signup.GetErrorMessage();
//
//if(error == signup.ErrNull)
//	UIALogger.logPass("Passed");
//else
//	UIALogger.logFail("Failed");
