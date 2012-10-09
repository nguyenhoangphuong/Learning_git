#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

/*
UserInfo view function:
=========================================================================================
- isVisible()						:	check if the current view is visible
=========================================================================================
- setInfo(age, w1, w2, h1, h2)		:	set user information
	+ setInfo(18, 83, 0.3, "8'", "5\"") for US
	+ setInfo(18, 50, 0.4, 1 , 0.99) 	for SI
- setAge(age)						:	|
- setWeight(w1, w2)					:	| single function for setting values
- setHeight(h1, h2)					:	|
- setSex(sex)						:	set user sex ("male" / "female")
- submit()							:	submit the form
=========================================================================================
- changeHeight(dy)					:	change user height by dragging onto image
	+ dy > 0: make shorter
	+ dy < 0: make taller
- changeWeight(dx)					:	change user weight by dragging onto image
	+ dx > 0: make fatter
	+ dx < 0: make thinner
=========================================================================================
- isMale()							:	check if sex switch is Male
- isUS()							:	check if unit switch is US
- getInfo()							:	return the info {age, weight, height}
	+ age		:	32 (int)
	+ weight	:	163.0 (float)
	+ height	:	"1.75" or "5'5"" (string)
=========================================================================================

MAY BE CHANGES
- setInfo	: base on new design
- isBackButtonExist: back button may exist in some case
- isMale : since there will be no Unit switch
*/

function UserInfo()
{
	// Private fields
	var mainView = app.mainWindow();

	var age = mainView.staticTexts()[1];
	var weight = mainView.staticTexts()[3];
	var height = mainView.staticTexts()[5];
		
	var next = mainView.buttons()[3];
	var sex = mainView.buttons()[2];
	var cancel = mainView.buttons()[0];
	var done = mainView.buttons()[1];
		
	// Methods
	this.isVisible = isVisible;
	
	this.setInfo = setInfo;
	this.setAge = setAge;
	this.setHeight = setHeight;
	this.setWeight = setWeight;
	this.setSex = setSex;
	this.submit = submit;
	
	this.changeWeight = changeWeight;
	this.changeHeight = changeHeight;
	this.getInfo = getInfo;
	
	this.isMale = isMale;
	this.isUS = isUS;
	
	// Methods definition
	function isVisible()
	{
		exist = (staticTextExist("(years)") && staticTextExist("(lbs)") && staticTextExist("(feet)")) ||
				(staticTextExist("(years)") && staticTextExist("(kg)") && staticTextExist("(meters)")) ||
				tips.isTipsDisplay("UserInfo");
		
		log("UserInfo visible: " + exist);
		return exist;
	}
	
	
	function setInfo(a, w1, w2, wu, h1, h2, hu)
	{
		// set age
		setAge(a);
		
		// set weight
		setWeight(w1, w2, wu);
		
		// set height
		setHeight(h1, h2, hu);
	}
	
	function setAge(a)
	{
		a = a.toString();
		
		wait(0.5);
		age.tap();
		log("Tap [Age]");
		
		wait(0.5);
		picker = mainView.pickers()[0];
		wheelPick(picker, 0, a);
		log("Pick value: " + a);
		
		done = mainView.buttons()[1];
		done.tap();
	}
	
	function setWeight(w1, w2, wu)
	{
		w1 = w1.toString(); w2 = w2.toString();
		
		wait(0.5);	
		weight.tap();
		log("Tap [Weight]");
		
		wait(0.5);
		picker = mainView.pickers()[0];
		wheelPick(picker, 2, wu);
		wheelPick(picker, 0, w1);
		wheelPick(picker, 1, w2);
		log("Pick value: " + w1 + " - " + w2);
		
		done = mainView.buttons()[1];
		done.tap();
	}
	
	function setHeight(h1, h2, hu)
	{
		h1 = h1.toString(); h2 = h2.toString();
		
		wait(0.5);
		height.tap();
		log("Tap [Height]");
		
		wait(0.5);
		picker = mainView.pickers()[0];
		wheelPick(picker, 2, hu);
		wheelPick(picker, 0, h1);
		wheelPick(picker, 1, h2);
		log("Pick value: " + h1 + " - " + h2);
		
		done = mainView.buttons()[1];
		done.tap();
	}
	
	
	function setSex(s)
	{
		wait();
		if(s == "male" && !isMale())
		{
			sex.tap();
			log("Set sex: Male");
		}
		if(s == "female" && isMale())
		{
			sex.tap();
			log("Set sex: Female");
		}
	}
	
	function submit()
	{
		wait(0.5);
		next.tap();
		
		log("Tap [Next]");
	}
	

	function changeWeight(dx) 
	{
		wait();
		swipeHorizontally(160, 240, 160 + dx);
		
		log("Change weight by swiping: " + dx);
	}
	
	function changeHeight(dy) 
	{
		wait();
		swipeVertically(160, 240, 240 + dy);
		
		log("Change height by swiping: " + dy);
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
		return sex.isVisible() && sex.name() == "Male";
	}
	
	function isUS()
	{
		return staticTextExist("(lbs)");
	}
}
