#import "../../view/SignUp.js"
#import "../../core/testcaseBase.js"
#import "../../core/common.js"

if(isKeyboardVisible())
	pass();
else
	fail();


var signup = new Home();
signup.fillEmail(email);
signup.fillPassword("123456");
signup.submit()	;
if (signup.isInvalidEmailAlertShown())
	UIALogger.logPass("Pass");
else
{
	log("current msg: " + msg);
	fail("Fail. The email is valid");
}

signup.fillEmail("tvkhoa@domain.com");
wait(1);
msg = signup.getErrorMessage();

if (msg != null)
{
	if(msg == "")
		pass("Delete invalid msg");
	else
	{
		log("current msg: " + msg);
		fail("Not delete invalid msg");
	}
}
else
	pass("Delete invalid msg");