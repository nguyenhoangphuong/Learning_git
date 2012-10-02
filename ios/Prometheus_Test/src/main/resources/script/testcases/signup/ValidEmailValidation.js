#import "../../view/Home.js"
#import "../../view/UserInfo.js"
#import "../../core/testcaseBase.js"
#import "../../core/common.js"
/**
 * This test cover: - Email validation - Email inputting
 */




function checkValidEmail(email, error)
{
	var signup = new Home();
	signup.fillEmail(email);
	signup.fillPassword("123456");
	signup.submit();
	wait(1);
	// validate in here
}