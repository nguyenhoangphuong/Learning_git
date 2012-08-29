#import "Agreement.js"
#import "SignUp.js"
#import "Legal.js"
#import "../core/testcaseBase.js"

function test1()
{
	UIALogger.logStart("Test Demo");
	var agreement = new Agreement();
	agreement.tapAgree();

	var signup = new SignUp();
	signup.fillEmailAndSubmit("");

	var error = signup.getErrorMessage();
	log(error);

	if(error == signup.MsgEmpty)
		UIALogger.logPass("Passed");
	else
		UIALogger.logFail("Failed");
}

function test2()
{
	start();
	
	legal = new Legal();	
	legal.setSex("male");
	legal.setSex("female");
	legal.setUnit("si");
	legal.setUnit("us");
	legal.setInfo(18, 43, 0.5, 2, 0.85);
	legal.setInfo(18, 145, 0.3, "7'", "3\"");
	
	pass();
}

test2()
