#import "../view/UserInfo.js"
#import "../view/SignUp.js"
#import "../core/testcaseBase.js"
//#import "smoke/smokeTest.js"

function GoToUserInfoScreen()
{
	UIALogger.logPass("GoToUserInfoScreen");
	if(staticTextExist("GOAL PLANNER"))
	{
		wait(2);

		var signup = new SignUp();
		if (signup.isEmailTextFieldVisible() == 1) {
			log("Email is visible");
		}
		signup.pressLicenceAgreement();
		log("In licence agreement");
		wait();
		signup.closeLicenceAgreement();
		// signup.fillEmailAndSubmit("");
		// if (signup.getErrorMessage() != signup.MsgEmpty) {
		// 	UIALogger.logFail("Wrong error message: " + signup.getErrorMessage() );
		// }

		// signup.fillEmailAndSubmit("dad@");
		// log(signup.getErrorMessage() );
		// if (signup.getErrorMessage() != signup.MsgInvalid) {
		// 	UIALogger.logFail("Wrong error message: " + signup.getErrorMessage() );
		// }
		signup.fillEmailAndSubmit("abcd@test.com");

		if (signup.isEmailTextFieldVisible() == 1) {
			UILogger.logFail("Should succeed");
		}
	}
}

// check default values
function CheckDefaultValue()
{
	UIALogger.logPass("CheckDefaultValue");

	legal = new UserInfo();
	// check gender default
	if(legal.isMale())
		UIALogger.logPass("Check gender defautl (male): Pass");
	else
		UIALogger.logFail("Check gender defautl (male): Fail");

	// check unit default
	if(legal.isUS())
		UIALogger.logPass("Check unit defautl (US): Pass");
	else
		UIALogger.logFail("Check unit defautl (US): Fail");
}

// current screen is User Info screen
function InputHeight()
{	
	UIALogger.logPass("InputHeight");
	legal = new UserInfo();
	legal.setHeight(5, 9);
	// check height is changed
	if(staticTextExist("5'9\""))
		UIALogger.logPass("Input height: Pass");
	else
		UIALogger.logFail("Input height: Fail");
}

function InputWeight()
{	
	UIALogger.logPass("InputWeight");
	legal = new UserInfo();
	legal.setWeight(122, 0.4);
	if(staticTextExist("122.4"))
		UIALogger.logPass("Input weight: Pass");
	else
		UIALogger.logFail("Input weight: Fail");
}

function InputAge()
{	
	UIALogger.logPass("InputAge");
	legal = new UserInfo();
	legal.setAge(25);
	if(staticTextExist("25"))
		UIALogger.logPass("Input age: Pass");
	else
		UIALogger.logFail("Input age: Fail");
}

function InputGender()
{	
	UIALogger.logPass("InputGender");
	legal = new UserInfo();
	var isMale = legal.isMale();
	if(isMale)
		legal.setSex("female");
	else
		legal.setSex("male");

	if(isMale != legal.isMale())
		UIALogger.logPass("Input gender: Pass");
	else
		UIALogger.logFail("Input gender: Fail");
}

function InputUnit()
{	
	UIALogger.logPass("InputUnit");
	legal = new UserInfo();
	var isUs = legal.isUS();
	var tempStr = "lbs";
	if(isUs)
	{
		legal.setUnit("si");
		tempStr = "kg";
	}
	else
	{
		legal.setUnit("us");
		tempStr = "lbs";
	}

	wait();
	// check result
	if(isUs != legal.isUS() && staticTextExist(tempStr))
		UIALogger.logPass("Input unit: Pass " + tempStr);
	else
		UIALogger.logFail("Input unit: Fail " + tempStr);
}

function VerifyInteruption()
{
	UIALogger.logPass("VerifyInteruption");
	legal = new UserInfo();
	var isMale = legal.isMale();
	var isUS = legal.isUS();

	// lock app by press lock button
	lockApp("Lock", 2);

	if(staticTextExist("yrs") && isMale == legal.isMale() && isUS == legal.isUS())
		UIALogger.logPass("Interuption: Pass");
	else
		UIALogger.logFail("Interuption: Fail");
}

function VerifyOldState()
{
	// skip because of issue with HOME button action: it don't reopen app after minimizing

	// UIALogger.logPass("VerifyOldState");
	// legal = new UserInfo();
	// var isMale = legal.isMale();
	// var isUS = legal.isUS();

	// // lock app by press lock button
	// lockApp("Home", 2);

	// if(staticTextExist("yrs") && isMale == legal.isMale() && isUS == legal.isUS())
	// 	UIALogger.logPass("Verify old state: Pass");
	// else
	// 	UIALogger.logFail("Verify old state: Fail");
}

function VerifyNextButton()
{
	UIALogger.logPass("VerifyNextButton");
	legal = new UserInfo();
	legal.submit();
	// verify it will go to choose plan screen
	if(staticTextExist("Please set your plan"))
		UIALogger.logPass("Verify next button: Pass");
	else
		UIALogger.logFail("Verify next button: Fail");
}

// Go to User Info screen
UIALogger.logPass("START TEST");
GoToUserInfoScreen();

//CheckDefaultValue();
//InputWeight();
//InputHeight();
InputAge();
//InputGender();
//InputUnit();
//VerifyInteruption();
//VerifyOldState();
//VerifyNextButton();
UIALogger.logPass("END OF TEST");