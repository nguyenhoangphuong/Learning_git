#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
UserInfo view function:
- isVisible()						:	check if the current view is visible
- setInfo(age, w1, w2, h1, h2)		:	set user information
	+ setInfo(18, 83, 0.3, "8'", "5\"") for US
	+ setInfo(18, 50, 0.4, 1 , 0.99) 	for SI
- setAge(age)						:	|
- setWeight(w1, w2)					:	| single function for setting values
- setHeight(h1, h2)					:	|
- changeHeight(dy)					:	change user height by dragging onto image
	+ dy > 0: make shorter
	+ dy < 0: make taller
- changeWeight(dx)					:	change user weight by dragging onto image
	+ dx > 0: make fatter
	+ dx < 0: make thinner
- setSex(sex)						:	set user sex
	+ setSex("male");
	+ setSex("female");
- setUnit(unit)						:	set user unit
	+ setUnit("us");
	+ setUnit("si");
- submit()							:	submit the form
- isMale()							:	check if sex switch is Male
- isUS()							:	check if unit switch is US
- getInfo()							:	return the info {age, weight, height}
	+ age		:	32 (int)
	+ weight	:	163.0 (float)
	+ height	:	"1.75" or "5'5"" (string)
*/

function UserInfo()
{
	// Private fields
	var mainView = app.mainWindow();

	var age = mainView.staticTexts()[1];
	var weight = mainView.staticTexts()[3];
	var height = mainView.staticTexts()[5];
	var cancel = mainView.buttons()[0];
	var done = mainView.buttons()[1];
		
	var male = mainView.staticTexts()[7];
	var female = mainView.staticTexts()[8];
	var us = mainView.staticTexts()[9];
	var si = mainView.staticTexts()[10];
	var next = mainView.buttons()[2];
		
	// Methods
	this.isVisible = isVisible;
	this.setInfo = setInfo;
	this.setAge = setAge;
	this.setHeight = setHeight;
	this.setWeight = setWeight;
	
	this.setSex = setSex;
	this.setUnit = setUnit;
	this.submit = submit;
	
	this.changeWeight = changeWeight;
	this.changeHeight = changeHeight;

	this.isMale = isMale;
	this.isUS = isUS;
	
	// Methods definition
	function isVisible()
	{
		return next.isVisible();
	}
	
	function setInfo(a, w1, w2, h1, h2)
	{
		// set age
		setAge(a);
		
		// set weight
		setWeight(w1, w2);
		
		// set height
		setHeight(h1, h2);
	}
	
	function setAge(a)
	{
		a = a.toString();
		
		wait();	
		age.tap();
		
		wait(0.5);
		picker = mainView.pickers()[0];
		wheelPick(picker, 0, a);
		
		done.tap();
	}
	
	function setWeight(w1, w2)
	{
		w1 = w1.toString(); w2 = w2.toString();
		
		wait();	
		weight.tap();
		
		wait(0.5);
		picker = mainView.pickers()[0];
		wheelPick(picker, 0, w1);
		wheelPick(picker, 1, w2);
		
		done.tap();
	}
	
	function setHeight(h1, h2)
	{
		h1 = h1.toString(); h2 = h2.toString();
		
		wait();
		height.tap();
		
		wait(0.5);
		picker = mainView.pickers()[0];
		wheelPick(picker, 0, h1);
		wheelPick(picker, 1, h2);
		
		done.tap();
	}
	
	
	function setSex(sex)
	{
		wait();
		if(sex == "male" && !isMale())
			female.tap();
		if(sex == "female" && isMale())
			male.tap();
	}
	
	function setUnit(unit)
	{
		wait();
		if(unit == "us" && !isUS())
			si.tap();
		if(unit == "si" && isUS())
			us.tap();
	}
	
	function submit()
	{
		next.tap();
	}
	

	function changeWeight(dx) 
	{
		wait();
		swipeHorizontally(160, 240, 160 + dx);
	}
	
	function changeHeight(dy) 
	{
		wait();
		swipeVertically(160, 240, 240 + dy);
	}
	
	function getInfo()
	{
		var info = {};
		info.age = parseInt(age.name());
		info.weight = parseFloat(weight.name());
		info.height = height.name();
		
		log("userinfo.age: " + info.age);
		log("userinfo.weight: " + info.weight);
		log("userinfo.height: " + info.height);
		
		return info;
	}
	
	
	// helpers
	function isMale()
	{
		wait(0.2);
		return female.rect().origin.x > 80;
	}
	
	function isUS()
	{
		// x > < 175
		wait(0.2);
		return staticTextExist("lbs");
	}
}