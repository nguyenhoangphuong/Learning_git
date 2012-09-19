#import "../../view/UserInfo.js"
#import "../../view/SignUp.js"
#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"

function GoToUserInfoScreen()
{
	var navi = new Navigator();
	navi.toUserInfo();
	UIALogger.logPass("GoToUserInfoScreen");
}

// check default values
function CheckDefaultValue()
{
	//UIALogger.logPass("CheckDefaultValue");

	userinfo = new UserInfo();
	// check gender default
	if(userinfo.isMale())
		UIALogger.logPass("Check gender defautl (male): Pass");
	else
		UIALogger.logFail("Check gender defautl (male): Fail");

	// check unit default
	if(userinfo.isUS())
		UIALogger.logPass("Check unit defautl (US): Pass");
	else
		UIALogger.logFail("Check unit defautl (US): Fail");
}

// current screen is User Info screen
function InputHeight()
{	
	//UIALogger.logPass("InputHeight");
	userinfo = new UserInfo();
	userinfo.setHeight("5'", "9\"");
	// check height is changed
	if(staticTextExist("5'9\""))
		UIALogger.logPass("Input height: Pass");
	else
		UIALogger.logFail("Input height: Fail");
}

function InputWeight()
{	
	//UIALogger.logPass("InputWeight");
	legal = new UserInfo();
	legal.setWeight(150, 0.8);
	if(staticTextExist("150.8"))
		UIALogger.logPass("Input weight: Pass");
	else
		UIALogger.logFail("Input weight: Fail");
}

function InputAge()
{	
	//UIALogger.logPass("InputAge");
	userinfo = new UserInfo();
	userinfo.setAge(10);
	wait(2);
	
	if(staticTextExist("10"))
		UIALogger.logPass("Input age: Pass");
	else
		UIALogger.logFail("Input age: Fail");
}

function InputGender()
{	
	//UIALogger.logPass("InputGender");
	userinfo = new UserInfo();
	var isMale = userinfo.isMale();
	if(isMale)
		userinfo.setSex("female");
	else
		userinfo.setSex("male");

	if(isMale != userinfo.isMale())
		UIALogger.logPass("Input gender: Pass");
	else
		UIALogger.logFail("Input gender: Fail");
}

function InputUnit()
{	
	//UIALogger.logPass("InputUnit");
	userinfo = new UserInfo();
	var isUs = userinfo.isUS();
	var tempStr = "lbs";
	if(isUs)
	{
		userinfo.setUnit("si");
		tempStr = "kg";
	}
	else
	{
		userinfo.setUnit("us");
		tempStr = "lbs";
	}

	wait();
	// check result
	if(isUs != userinfo.isUS() && staticTextExist(tempStr))
		UIALogger.logPass("Input unit: Pass " + tempStr);
	else
		UIALogger.logFail("Input unit: Fail " + tempStr);
}

function VerifyInteruption()
{
	//UIALogger.logPass("VerifyInteruption");
	userinfo = new UserInfo();
	var isMale = userinfo.isMale();
	var isUS = userinfo.isUS();

	// lock app by press lock button
	lockApp("Lock", 2);

	if(staticTextExist("yrs") && isMale == userinfo.isMale() && isUS == userinfo.isUS())
		UIALogger.logPass("Interuption: Pass");
	else
		UIALogger.logFail("Interuption: Fail");
}

function VerifyOldState()
{
	UIALogger.logPass("VerifyOldState");
	userinfo = new UserInfo();
	var isMale = userinfo.isMale();
	var isUS = userinfo.isUS();

	// lock app by press lock button
	lockApp("Home", 2);

	if(staticTextExist("yrs") && isMale == userinfo.isMale() && isUS == userinfo.isUS())
		UIALogger.logPass("Verify old state: Pass");
	else
		UIALogger.logFail("Verify old state: Fail");
}

function VerifyNextButton()
{
	userinfo = new UserInfo();
	userinfo.submit();
	
	// verify it will go to choose plan screen
	if(staticTextExist("Please set your plan"))
		UIALogger.logPass("Verify next button: Pass");
	else
		UIALogger.logFail("Verify next button: Fail");
}

// Go to User Info screen
UIALogger.logStart("---------- UserInfo view tests start ----------");

GoToUserInfoScreen();
CheckDefaultValue();
InputWeight();
InputHeight();
InputAge();
InputGender();
InputUnit();
VerifyInteruption();
VerifyOldState();
VerifyNextButton();

UIALogger.logPass("---------- UserInfo view tests end -----------");
	