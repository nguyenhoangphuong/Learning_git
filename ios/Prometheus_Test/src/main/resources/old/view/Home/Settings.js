#import "../MVPLibs.js"

/*
Settings functions:
================================================================================
- assignControls ()	: assign controls
- isVisible()		: check if current view is Settings
================================================================================
- setName(name)
- setGender(gender = "Female")	: set gender ("Female" (default) or "Male")
- setBirthday(year, monthString, day, yearIndex = 2, monthIndex = 0, dayIndex = 1)
	+ year: year value (number);
	+ monthString: month value (string, case insensitive);
	+ day: day value (number);
	+ yearIndex: index of year wheel, default = 2;
	+ monthIndex: index of month wheel, default = 0;
	+ dayIndex: index of day wheel, default = 1;
- setHeight(wheel1value, wheel2value)
	+ example:	setWeight("6'", "5\""); // US
				setWeight("1", ".70"); // Metric
- setWeight(wheel1value, wheel2value)
	+ example:	setWeight("70", ".5");
- setUnit(unit = "US")	: set unit (default = "US")
	+ unit: "US" (default) or "Metric", case insensitive 
- getUserInfo()		: get the current user info on Setting view
================================================================================
- tapSupport()		: tap Email Support button
- tapLike()			: tap "Like our page" button
- tapWebsite()		: tap Website button
================================================================================
- tapRate()			: tap "Rate our App" button
- tapFeedback()		: tap "Trouble makers" (JIRA feedback) button
- sendJIRAFeedback(): send a JIRA feedback (w/ troublemaker)
================================================================================
- resetPlan(confirm = "yes"): tap Reset button and choose Yes/No when alert shown up
	+ resetPlan() or resetPlan("yes"): tap YES (case insensitive) when being asked
	+ resetPlan("no"): tap NO (case insensitive) when being asked
- signOut(yes = true)	: tap "Sign out" button (w/ user having logged in)
	+ signOut() or signOut(true)	: tap YES when being asked
	+ signOut(false)				: tap NO when being asked
- signUp()				: tap "Sign up" button (w/ user having not logged in)
================================================================================
- isSignOutBtnVisible()
- isSignUpBtnVisible()
- isTroublemakerBtnVisisble()
- isNoMailAccountsAlertShown()
================================================================================
*/

function Settings()
{
	// Private fields
	var window;
	var mainView;
	
	var btnUS;
	var btnMetric;
	
	var txtName;
	var btnMale;
	var btnFemale;
	var pckrBirthday;
	var pckrHeight;
	var pckrWeight;
	
	var btnEmail;
	var btnLike;
	var btnWebsite;
	
	var btnRate;
	var btnTroubleMaker;
	
	var btnReset;
	var btnSignUp;
	var btnLogOut;
	
	// Initalize
	assignControls();
	
	// Methods
	this.assignControls = assignControls;
	this.isVisible = isVisible;
				
	this.setUnit = setUnit;
	this.setName = setName;
	this.setGender = setGender;
	this.setBirthday = setBirthday;
	this.setHeight = setHeight;
	this.setWeight = setWeight;
	this.getUserInfo = getUserInfo;
	
	this.tapSupport = tapSupport;
	this.tapLike = tapLike;
	this.tapWebsite = tapWebsite;
	
	this.tapRate = tapRate;
	this.tapFeedback = tapFeedback;
	this.sendJIRAFeedback = sendJIRAFeedback;
	
	this.resetPlan = resetPlan;
	this.signOut = signOut;
	this.signUp = signUp;
	
	this.isSignOutBtnVisible = isSignOutBtnVisible;
	this.isSignUpBtnVisible = isSignUpBtnVisible;
	this.isTroublemakerBtnVisible = isTroublemakerBtnVisible;
	this.isNoMailAccountsAlertShown = isNoMailAccountsAlertShown;
	
	// assign controls, this MUST be run
	assignControls();
	
	// Method definitions
	function isVisible()
	{		
		return navigationBar.settingsIsVisible();
	}
	
	function assignControls() 
	{
		window = app.mainWindow();
		mainView = window.tableViews()[0];
		
		btnUS = mainView.cells()["Units"].buttons()["US"];
		btnMetric = mainView.cells()["Units"].buttons()["Metric"];
		
		txtName = mainView.cells()["Name"].textFields()[0];
		btnMale = mainView.cells()["Gender"].buttons()["Male"];
		btnFemale = mainView.cells()["Gender"].buttons()["Female"];
		pckrBirthday = mainView.cells()[3];
		pckrHeight = mainView.cells()[4];
		pckrWeight = mainView.cells()[5];
		
		btnEmail = mainView.cells()["Email support"];
		btnLike = mainView.cells()["Like our page"];
		btnWebsite = mainView.cells()["Website"];
		
		btnRate = mainView.cells()["Rate our app"];
		btnTroubleMaker = mainView.cells()["Trouble makers"];
		
		btnReset = mainView.cells()["Reset Plan"];
		btnSignUp = mainView.cells()["Sign up"];
		btnLogOut = mainView.cells()["Sign out"];
	}
	
	
	function setName(name)
	{		
		assignControls();
		
		if (name == null || name == "")
			name = "Love is in the air";
		
		txtName.scrollToVisible();
		txtName.tap();
		txtName.setValue(name);
		
		app.windows()[1].toolbar().buttons()["Done"].tap();
	}
	
	function setGender(gender)
	{
		assignControls();
		
		if (typeof gender == "undefined")
			gender = "Female";
		
		if (gender.toLowerCase() == "female") 
		{
			log("Set gender to Female");
			btnFemale.tap();
		}
		else 
		{
			log("Set gender to Male");
			btnMale.tap();
		}
	}
	
	function setBirthday(year, monthString, day, yearIndex, monthIndex, dayIndex)
	{
		assignControls();
		
		pckrBirthday.tap(); // open up Birthday picker
		wait();
		
		var picker = app.windows()[1].pickers()[0];
		
		log("Set birthday");
		dateWheelPick(picker, year, monthString, day, yearIndex, monthIndex, dayIndex);
		
		app.windows()[1].toolbar().buttons()["Done"].tap();
	}
	
	function setHeight(v1, v2)
	{
		assignControls();
		
		pckrHeight.tap(); // open up Height picker
		wait();
		
		var picker = app.windows()[1].pickers()[0];
		
		log("Set height");
		wheelPick(picker, 0, v1.toString());
		wheelPick(picker, 1, v2.toString());
		
		app.windows()[1].toolbar().buttons()["Done"].tap();
	}
	
	function setWeight(v1, v2)
	{
		assignControls();
		
		pckrWeight.tap(); // open up Weight picker
		wait();
		
		var picker = app.windows()[1].pickers()[0];
		
		log("Set weight");
		wheelPick(picker, 0, v1.toString());
		wheelPick(picker, 1, v2.toString());
		
		app.windows()[1].toolbar().buttons()["Done"].tap();
	}
	
	function setUnit(unit)
	{
		assignControls();
		
		if (typeof unit == "undefined")
			unit = "us";
		
		assignControls();
		unit = unit.toLowerCase();
		
		if (unit == "us") 
		{
			log("Set unit to US");
			btnUS.tap();
		}
		else 
		{
			log("Set unit to Metric");
			btnMetric.tap();
		}
	}
	
	function getUserInfo()
	{
		assignControls();
		
		var info = {};
		text = pckrWeight.name();
		info.weight = text.substring(text.indexOf(",") + 2);
		text = pckrHeight.name();
		info.height = text.substring(text.indexOf(",") + 2);
		info.unit = (btnUS.value() == 1 ? "us" : "metric");
		info.gender = (btnMale.value() == 1 ? "male" : "female");
		info.name = txtName.value();
		
		log("About me: " + JSON.stringify(info));
		return info;
	}

	
	function tapSupport()
	{
		btnEmail.tap();
		wait(0.5);
		log("Tap [Support]");
	}
	
	function tapLike()
	{
		btnLike.tap();
		wait(0.5);
		log("Tap [Like]");
	}
	
	function tapWebsite()
	{
		btnWebsite.tap();
		wait(0.5);
		log("Tap [Website]");
	}
	
	
	function tapRate()
	{	
		btnRate.tap();
		wait(0.5);
		log("Tap [Rate]");
	}
	
	function tapFeedback()
	{
		if (isTroublemakerBtnVisible())
		{
			btnTroubleMaker.tap();
			wait(0.5);
			log("Tap [Behind the scenes]");
			return;
		}
	}
	
	function sendJIRAFeedback()
	{
		if (!isTroublemakerBtnVisible()) 
		{
			log("Oops... babe you are not a troublemaker");
			return;
		}
		
		var dt = new Date();
		
		log("Begin sending JIRA feedback");
		tapFeedback();
		wait();
		
		// if there is message sent, there will be Compose button
		var btnCompose = app.navigationBar().buttons()["Compose"];
		
		if (btnCompose.isValid() && btnCompose.isVisible())
		{
			btnCompose.tap();
			wait();
		}
		
		// check if Send button tappable
		var btnSend = app.navigationBar().buttons()["Send"];
		
		if (!btnSend.isValid() || !btnSend.isVisible())
		{
			fail("Feedback not sent");
			return;
		}
		
		dt = dt.getMonth() +
			"." + dt.getDate() +
			"." + dt.getFullYear() +
			" at " + dt.getHours() +
			":" + dt.getMinutes() +
			":" + dt.getSeconds();
		
		app.keyboard().typeString("this is an automated feedback generated on " + dt);
		app.mainWindow().buttons()["icon record"].tap();
		wait(15);
		btnSend.tap();
		wait(5);
		
		var btnClose = app.navigationBar().buttons()["Close"];
		
		if (btnClose.isValid() && btnClose.isVisible())
		{
			btnClose.tap();
			wait();
		}
		
		log("Feedback sent to JIRA");
	}
	
	
	function resetPlan(confirm)
	{
		if (confirm == null)
			confirm  = "yes";
		
		btnReset.tap();
		wait();
		
		if (confirm.toLowerCase() == "yes")
			app.actionSheet().buttons()[0].tap();
		else
			app.actionSheet().buttons()[1].tap();
		
		wait(0.5);
	}
	
	function signOut(yes)
	{
		if (!isSignOutBtnVisible())
		{
			log("User has not logged in. No logging out at this time :P");
			return;
		}
		
		log("Tap Sign out");
		btnLogOut.tap();
		wait();
		
		if (typeof yes == "undefined")
			yes = true;
		
		if (yes)
		{
			log("Tap YES to log out");
			app.actionSheet().buttons()[0].tap();
		}
		else
		{
			log("Tap NO to back to Settings");
			app.actionSheet().buttons()[1].tap();
		}
		
		wait(0.5);
	}
	
	function signUp(yes)
	{
		if (!isSignUpBtnVisible())
		{
			log("User signed in, can only sign out");
			return;
		}
		
		log("Tap Sign up");
		btnSignUp.tap();
		wait();
		
		if (typeof yes == "undefined")
			yes = true;
		
		if (yes)
		{
			log("Tap YES to log out");
			app.actionSheet().buttons()[0].tap();
		}
		else
		{
			log("Tap NO to back to Settings");
			app.actionSheet().buttons()[1].tap();
		}
		
		wait(0.5);
	}
	
	
	function isSignUpBtnVisible()
	{
		var visible = btnSignUp.checkIsValid();
		log("[Sign up] visible: " + visible);
		return visible;
	}
	
	function isSignOutBtnVisible()
	{
		var visible = btnLogOut.checkIsValid();
		log("[Sign out] visible: " + visible);
		return visible;
	}

	function isTroublemakerBtnVisible()
	{		
		var visible = btnTroubleMaker.checkIsValid();
		log("[Troublemaker] visible: " + visible);
		return visible;
	}
	
	function isNoMailAccountsAlertShown()
	{
		return alert.alertTitle != null &&
				alert.alertTitle == alert.NoEmailAccount;
	}

}