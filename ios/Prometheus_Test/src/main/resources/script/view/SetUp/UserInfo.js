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
- getWidth()						:	return string in width cell
- getHeight()						:	return string in height cell
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
	
	this.getWidth = getWidth;
	this.getHeight = getHeight;
	
	// Methods definition
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window.tableViews()[0];
		cells = mainView.cells();
		
		usBtn = cells["Units"].buttons()["US"];
		siBtn = cells["Units"].buttons()["Metric"];
		maleBtn = cells["Gender"].buttons()["Male"];
		femaleBtn = cells["Gender"].buttons()["Female"];
		weightBtn = cells["Weight"];
		heightBtn = cells["Height"];
		
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
		wait(0.5);
		
		if(s == "male" || s == "m")
			maleBtn.tap();
		
		if(s == "female"|| s == "f")
			femaleBtn.tap();
		
		log("Set gender: " + s);
	}
	
	function setUnit(u)
	{
		wait(0.5);
		
		if(u == "us")
			usBtn.tap();
		else
			siBtn.tap();
		
		log("Set unit: " + u);
	}
	
	function submit()
	{
		wait(0.5);
		submitBtn.tap();
		
		log("Tap [Next]");
	}
	
	function getWidth()
	{
		var text = widthBtn.name();
		var w = text.substring(text.indexOf(",") + 2);
		
		log("Width: " + w);
		return w;
	}
	
	function getHeight()
	{
		var text = heightBtn.name();
		var h = text.substring(text.indexOf(",") + 2);
		
		log("Height: " + h);
		return h;
	}
}
