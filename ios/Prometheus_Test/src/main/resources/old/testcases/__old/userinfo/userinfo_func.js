#import "../../view/_Navigator.js"
#import "../../core/testcaseBase.js"

function GoToUserInfoScreen()
{
	nav.toUserInfo(null, null);
}

// check default values
function CheckDefaultValue()
{
	userinfo = new UserInfo();
	
	// check gender default
	//assertTrue(userinfo.isMale(), "Default sex is Male");
	
	// check unit default
	assertTrue(userinfo.isUS(), "Default unit is US");
}

function InputHeight()
{	
	userinfo = new UserInfo();
	
	// check height in US
	userinfo.setHeight("5'", "9\"", "feet");
	assertTrue(staticTextExist("5'9\"") && staticTextExist("(feet)"), "Height (plural) is display correctly in US");
	
	userinfo.setHeight("1'", "0\"", "feet");
	assertTrue(staticTextExist("1'0\"") && staticTextExist("(foot)"), "Height (single) is display correctly in US");
	
	// check height in SI
	userinfo.setHeight("1", ".70", "meters");
	assertTrue(staticTextExist("1.70") && staticTextExist("(meters)"), "Height (plural) is display correctly in SI");
	
	userinfo.setHeight("1", ".00", "meters");
	assertTrue(staticTextExist("1.00") && staticTextExist("(meter)"), "Height (single) is display correctly in SI");
}

function InputWeight()
{	
	userinfo = new UserInfo();
	
	// check weight in US
	userinfo.setWeight(199, ".5", "lbs");
	assertTrue(staticTextExist("199.5"), "Weight is display correctly in US");
	
	// check weight in SI
	userinfo.setWeight(91, ".7", "kg");
	assertTrue(staticTextExist("91.7"), "Weight is display correctly in SI");
}

function InputAge()
{	
	userinfo = new UserInfo();
	
	// check age
	userinfo.setAge(22);
	info = userinfo.getInfo();
	
	assertTrue(info.age == 22, "Age is display correctly");
}

function InputGender()
{	
	userinfo = new UserInfo();
	
	// check female
	userinfo.setSex("female");
	assertTrue(!userinfo.isMale(), "Gender female is display correctly");
	
	// check male
	userinfo.setSex("male");
	assertTrue(userinfo.isMale(), "Gender male is display correctly");
}

function VerifyInteruption()
{
	userinfo = new UserInfo();
	var isMale = userinfo.isMale();
	var isUS = userinfo.isUS();

	// lock app by press lock button
	lockApp("Lock", 2);

	assertTrue(staticTextExist("(years)") && isMale == userinfo.isMale() && isUS == userinfo.isUS(),
			"Interruption OK");

}

function VerifyOldState()
{
	userinfo = new UserInfo();
	var isMale = userinfo.isMale();
	var isUS = userinfo.isUS();

	// lock app by press lock button
	lockApp("Home", 2);

	assertTrue(staticTextExist("(years)") && isMale == userinfo.isMale() && isUS == userinfo.isUS(),
			"Old state OK");
}

function VerifySwipeToChangeWeight()
{
	ui = new UserInfo();
	ui.setWeight(90, ".5", "kg");
	
	a = ui.getInfo();
	ui.changeWeight(50);
	b = ui.getInfo();
	ui.changeWeight(-50);
	c = ui.getInfo();
	
	assertTrue(b.weight > a.weight, "Check increase weight");
	assertTrue(b.weight > c.weight, "Check decrease weight");
}

function VerifySwipeToChangeHeight()
{
	ui = new UserInfo();
	ui.setHeight(1, ".60", "meters");
	
	a = ui.getInfo();
	ui.changeHeight(50);
	b = ui.getInfo();
	ui.changeHeight(-50);
	c = ui.getInfo();
	
	a.height = parseFloat(a.height);
	b.height = parseFloat(b.height);
	c.height = parseFloat(c.height);
			
	assertTrue(b.height < a.height, "Check decrease height");
	assertTrue(b.height < c.height, "Check increase height");
}

function VerifyNextButton()
{
	userinfo = new UserInfo();
	userinfo.submit();
	
	// verify it will go to activity view
	a = new MultiGoalChooser();
	visible = a.isVisible();
	assertTrue(visible, "Verify Next button");
}