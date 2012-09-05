#import "../../view/SignUp.js"
#import "../../core/testcaseBase.js"
#import "../../core/common.js"

if(isKeyboardVisible())
	pass();
else
	fail();


var signup = new SignUp();

signup.fillEmailAndSubmit("@@domain.com");
var msg = signup.getErrorMessage();
if (msg == "The email is invalid")
	pass();
else
	fail("Fail. The email is valid");

signup.fillEmail("tvkhoa@domain.com");
msg = signup.getErrorMessage();
if (msg == "")
	pass();
else
	fail();