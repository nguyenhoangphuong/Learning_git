#import "../MVPLibs.js"

/*
UserInfo view function:
=========================================================================================
- assignControls()
- isVisible()
=========================================================================================
- setWeight(w1, w2)					:	setWeight(86, ".2")
- setHeight(h1, h2)					:	setHeight("5'", "9\"") or setHeight(1, ".75")
- setGender(gender)					:	set user gender ("male" / "female")
- setUnit(unit)						:	set user unit ("us" / "metric")
- submit()							:	submit the form
=========================================================================================
- getInfo()							:	return {weight, height, unit, gender}
=========================================================================================
*/

function UserInfo(view)
{
	// Private fields
	var window;
	var mainView;
	
	var usBtn;
	var siBtn;
	var maleBtn;
	var femaleBtn;
	var weightBtn;
	var heightBtn;
	
	var submitBtn;
	
	// Initalize
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
	
	this.setHeight = setHeight;
	this.setWeight = setWeight;
	this.setGender = setGender;
	this.setUnit = setUnit;
	this.submit = submit;
	
	this.getInfo = getInfo;
	
	// Methods definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window.tableViews()[0];
		cells = mainView.cells();
		
		usBtn = cells[0].buttons()[0];
		siBtn = cells[0].buttons()[1];
		maleBtn = cells[1].buttons()[0];
		femaleBtn = cells[1].buttons()[1];
		weightBtn = cells[2];
		heightBtn = cells[3];
		
		submitBtn = window.buttons()[0];
	}
	
	function isVisible() 
	{
        var visible = ("About me" == app.navigationBar().name());
 		log("Personal Info is visible: " + visible);

 		return visible;
    }
	
	function setWeight(w1, w2)
	{
		assignControls();
		
		w1 = w1.toString(); w2 = w2.toString();
		
		wait(0.5);	
		weightBtn.tap();
		log("Tap [Weight]");
		
		wait(0.5);
		pickWin = app.windows()[1];
		picker = pickWin.pickers()[0];

		wheelPick(picker, 0, w1);
		wheelPick(picker, 1, w2);
		log("Pick value: " + w1 + " - " + w2);
		
		done = pickWin.toolbar().buttons()[0];
		done.tap();
	}
	
	function setHeight(h1, h2)
	{
		assignControls();
		
		h1 = h1.toString(); h2 = h2.toString();
		
		wait(0.5);
		heightBtn.tap();
		log("Tap [Height]");
		
		wait(0.5);
		pickWin = app.windows()[1];
		picker = pickWin.pickers()[0];
		
		wheelPick(picker, 0, h1);
		wheelPick(picker, 1, h2);
		log("Pick value: " + h1 + " - " + h2);
		
		done = pickWin.toolbar().buttons()[0];
		done.tap();
	}
	
	function setGender(s)
	{
		assignControls();
		
		wait(0.5);
		
		if(s == "male" || s == "m")
			maleBtn.tap();
		
		if(s == "female"|| s == "f")
			femaleBtn.tap();
		
		log("Set gender: " + s);
	}
	
	function setUnit(u)
	{
		assignControls();
		
		wait(0.5);
		
		if(u == "us")
			usBtn.tap();
		else
			siBtn.tap();
		
		log("Set unit: " + u);
	}
	
	function submit()
	{
		assignControls();
		
		wait(0.5);
		submitBtn.tap();
		
		log("Tap [Next]");
	}
	
	function getInfo()
	{
		assignControls();
		
		var info = {};
		text = weightBtn.name();
		info.weight = text.substring(text.indexOf(",") + 2);
		text = heightBtn.name();
		info.height = text.substring(text.indexOf(",") + 2);
		info.unit = (usBtn.value() == 1 ? "us" : "metric");
		info.gender = (maleBtn.value() == 1 ? "male" : "female");
		
		log("About me: " + JSON.stringify(info));
		return info;
	}
	
}
