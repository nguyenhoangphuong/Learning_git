#import "../../view/SignUp.js"
#import "../../view/UserInfo.js"
#import "../../core/testcaseBase.js"

/**
 * Smoke test
 */

var target = UIATarget.localTarget();

UIALogger.logStart("Test email validation");

log("Swipe");
swipeVertically(168, 253, 100);
swipeHorizontally(168, 253, 200);

function signup() {
	wait(2);

	var signup = new SignUp();
	if (signup.isEmailTextFieldVisible() == 1) {
		log("Email is visible");
	}
	signup.pressLicenceAgreement();
	log("In licence agreement");
	wait();
	signup.closeLicenceAgreement();
	signup.fillEmailAndSubmit("");
	if (signup.getErrorMessage() != signup.MsgEmpty) {
		UIALogger.logFail("Wrong error message: " + signup.getErrorMessage() );
	}

	signup.fillEmailAndSubmit("dad@");
	log(signup.getErrorMessage() );
	if (signup.getErrorMessage() != signup.MsgInvalid) {
		UIALogger.logFail("Wrong error message: " + signup.getErrorMessage() );
	}
	signup.fillEmailAndSubmit("abcd@test.com");

	if (signup.isEmailTextFieldVisible() == 1) {
		UILogger.logFail("Should succeed");
	}
	
	// should be in user info now
} 


UIALogger.logPass("Pass");
