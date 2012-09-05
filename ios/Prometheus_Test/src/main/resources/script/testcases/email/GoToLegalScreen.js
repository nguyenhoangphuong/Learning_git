#import "../../view/SignUp.js"
#import "../../core/testcaseBase.js"
#import "../../core/common.js"

var signup = new SignUp();
signup.pressLicenceAgreement();

if(signup.isLicenceAgreementShown())
	pass();
else
	fail();

signup.closeLicenceAgreement();
if(signup.isEmailTextFieldVisible())
	pass();
else
	fail();
	