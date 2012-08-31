#import "../core/testcaseBase.js"

function Legal()
{
	// Private fields
	var mainView = app.mainWindow();

	var age = mainView.staticTexts()[0];
	var weight = mainView.staticTexts()[2];
	var height = mainView.staticTexts()[4];
	var cancel = mainView.buttons()["Cancel"];
	var done = mainView.buttons()["Done"];
	
	var male = mainView.staticTexts()["Male"];
	var female = mainView.staticTexts()["Female"];
	var us = mainView.staticTexts()["US"];
	var si = mainView.staticTexts()["SI"];
	var next = mainView.buttons()["done"];
		
	// Methods
	this.setInfo = setInfo;
	this.changeBody = changeBody;

	this.setSex = setSex;
	this.setUnit = setUnit;
	this.submit = submit;
	this.changeWeight = changeWeight;
	this.changeHeight = changeHeight;
	
	// Methods definition
	function setInfo(age, w1, w2, h1, h2)
	{
		/*
		example: (18, 83, 0.3, 8, 5) for US
		         (18, 50, 0.4, 1 , 0.99) for SI
		*/
		
		// to string
		age = age.toString();
		w1 = w1.toString(); w2 = w2.toString();
		h1 = h1.toString(); h2 = h2.toString();
		log(w1);log(w2);log(h1);log(h2);		
		// set age
		// --- age is bug now ---
		
		// set weight
		wait();	
		weight.tap();
		picker = mainView.pickers()[0];
		
		wheelPick(picker, 0, w1);
		wheelPick(picker, 1, w2);
		done.tap();
		
		// set height
		wait();
		height.tap();
		picker = mainView.pickers()[0];

		wheelPick(picker, 0, h1);
		wheelPick(picker, 1, h2);
		done.tap();
	}
	
//	function changeBody(dx, dy) {
//		/*
//		Increase / Decrease weight and height value by dragging on the human image.
//		- dx: delta x value
//		- dy: delta y value
//		*/
//		
//	}
	
	function changeWeight(dx) {
		swipeVertically(168, 253, 168 + dx);
		swipeHorizontally(168, 253, 200);
	}
	
	function changeHeight(dy) {
		swipeHorizontally(168, 253, 253 + dy);
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
	
	// helpers
	function isMale()
	{
		return female.rect().origin.x > 80;
	}
	
	function isUS()
	{
		staticTextExist("lbs");
	}
}