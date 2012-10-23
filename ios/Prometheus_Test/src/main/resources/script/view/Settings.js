#import "../core/testcaseBase.js"
#import "_AlertHandler.js"
#import "_NavigationBar.js"

/*
Settings functions:
- isVisible()		: check if current view is Settings
- assignControls ()	: assign controls
- closeOtherWindow(): close other window (e.g. picker)
- isTroublemaker()	: check if current user is a troublemaker
- hasSignedIn()		: check if current user has signed in
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
- tapSupport()		: tap Email Support button
- tapLike()			: tap "Like our page" button
- tapWebsite()		: tap Website button
- tapRate()			: tap "Rate our App" button
- tapFeedback()		: tap "Trouble makers" (JIRA feedback) button
- sendJIRAFeedback(): send a JIRA feedback (w/ troublemaker)
- resetPlan(confirm = "yes"): tap Reset button and choose Yes/No when alert shown up
	+ resetPlan() or resetPlan("yes"): tap YES (case insensitive) when being asked
	+ resetPlan("no"): tap NO (case insensitive) when being asked
- signOut(yes = true)	: tap "Sign out" button (w/ user having logged in)
	+ signOut() or signOut(true)	: tap YES when being asked
	+ signOut(false)				: tap NO when being asked
- signUp()				: tap "Sign up" button (w/ user having not logged in)
- isSignOutBtnVisible()
- isSignUpBtnVisible()
- isNoMailAccountsAlertShown()
*/

function Settings()
{
	// Private fields
	var window;
	var mainView;
	
	var txtName;
	var btnMale;
	var btnFemale;
	var pckrBirthday;
	var pckrHeight;
	var pckrWeight;
	var btnUS;
	var btnMetric;
	
	var btnEmail;
	var btnLike;
	var btnWebsite;
	
	var btnRate;
	var btnTroubleMaker;
	
	var btnReset;
	var btnSignUp;
	var btnLogOut;
	
	// Methods
	this.isVisible = isVisible;
	this.assignControls = assignControls;
	this.closeOtherWindow = closeOtherWindow;
	
	this.isTroublemaker = isTroublemaker;
	this.hasSignedIn = hasSignedIn;
				
	this.setName = setName;
	this.setGender = setGender;
	this.setBirthday = setBirthday;
	this.setHeight = setHeight;
	this.setWeight = setWeight;
	this.setUnit = setUnit;
	
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
	this.isNoMailAccountsAlertShown = isNoMailAccountsAlertShown;
	
	// assign controls, this MUST be run
	assignControls();
	
	// Method definitions
	function isVisible()
	{		
		return navigationBar.settingsIsVisible();
	}
	
	function assignControls() {
		window = app.mainWindow();
		mainView = window.tableViews()["Empty list"];
		
		txtName = mainView.cells()["Name"];
		btnMale = mainView.cells()["Gender"].buttons()["Male"];
		btnFemale = mainView.cells()["Gender"].buttons()["Female"];
		pckrBirthday = mainView.cells()[3];
		pckrHeight = mainView.cells()[4];
		pckrWeight = mainView.cells()[5];
		btnUS = mainView.cells()["Unit"].buttons()["US"];
		btnMetric = mainView.cells()["Unit"].buttons()["Metric"];
		
		btnEmail = mainView.cells()["Email support"];
		btnLike = mainView.cells()["Like our page"];
		btnWebsite = mainView.cells()["Website"];
		
		btnRate = mainView.cells()["Rate our App"];
		btnTroubleMaker = mainView.cells()["Trouble makers"];
		
		btnReset = mainView.cells()["Reset plan"];
		btnSignUp = null; // TODO: change this
		btnLogOut = mainView.cells()["Log out"];
	}
	
	function closeOtherWindow()
	{
		mainView.dragInsideWithOptions({startOffset:{x:0.50, y:0.00},
										endOffset:{x:0.50, y:0.05}});
		wait();
		assignControls();
	}
	
	function isTroublemaker()
	{
		return btnTroubleMaker.checkIsValid();
	}
	
	function hasSignedIn()
	{
		return btnLogOut.checkIsValid();
	}
	
	function setName(name)
	{		
		if (name == null || name == "")
			name = "Love is in the air";
		
		txtName.tap();
		wait();
		
		// delete name if the field is not empty
		if (txtName.textFields()[0].value() != "") {
			txtName.textFields()[0].touchAndHold(2);
			app.editingMenu().elements()["Select All"].tap();
			app.keyboard().buttons()["Delete"].tap();
		}
		
		app.keyboard().typeString(name + "\n");
	}
	
	function setGender(gender)
	{
		if (gender == null)
			gender = "Female";
		
		if (gender.toLowerCase() == "female") {
			log("Set gender to Female");
			btnFemale.tap();
		}
		else {
			log("Set gender to Male");
			btnMale.tap();
		}
	}
	
	function setBirthday(year, monthString, day, yearIndex, monthIndex, dayIndex)
	{
		pckrBirthday.tap(); // open up Birthday picker
		wait();
		
		var picker = app.windows()[1].pickers()[0];
		
		log("Set birthday");
		dateWheelPick(picker, year, monthString, day, yearIndex, monthIndex, dayIndex);
		closeOtherWindow();
	}
	
	function setHeight(v1, v2)
	{
		pckrHeight.tap(); // open up Height picker
		wait();
		
		var picker = app.windows()[1].pickers()[0];
		
		log("Set height");
		wheelPick(picker, 0, v1);
		wheelPick(picker, 1, v2);
		closeOtherWindow();
	}
	
	function setWeight(v1, v2)
	{
		pckrWeight.tap(); // open up Weight picker
		wait();
		
		var picker = app.windows()[1].pickers()[0];
		
		log("Set weight");
		wheelPick(picker, 0, v1);
		wheelPick(picker, 1, v2);
		closeOtherWindow();
	}
	
	function setUnit(unit)
	{
		if (unit == null)
			unit = "us";
		
		assignControls();
		unit = unit.toLowerCase();
		
		if (unit == "us") {
			log("Set unit to US");
			btnUS.tap();
		}
		else {
			log("Set unit to Metric");
			btnMetric.tap();
		}
	}

	function tapSupport()
	{
		btnEmail.tap();
		wait();
	}
	
	function tapLike()
	{
		btnLike.tap();
	}
	
	function tapWebsite()
	{
		btnWebsite.tap();
	}
	
	function tapRate()
	{	
		btnRate.tap();
	}
	
	function tapFeedback()
	{
		if (isTroublemaker())
			btnTroubleMaker.tap();
	}
	
	function sendJIRAFeedback()
	{
		if (!isTroublemaker()) {
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
			window.buttons()[1].tap();
		else
			window.buttons()[0].tap();
	}
	
	function signOut(yes)
	{
		if (!hasSignedIn())
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
			app.mainWindow().buttons()[1].tap();
		}
		else
		{
			log("Tap NO to back to Settings");
			app.mainWindow().buttons()[0].tap();
		}
	}
	
	function signUp()
	{
		if (!hasSignedIn())
		{
			log("Tap Sign up");
			btnSignUp.tap();
		}
	}
	
	function isSignUpBtnVisible()
	{
		assignControls();
		
		return btnSignUp.chekcIsValid() && btnSignUp.isVisible();
	}
	
	function isSignOutBtnVisible()
	{
		assignControls();
		
		return btnLogOut.checkIsValid() && btnLogOut.isVisible();
	}
	
	function isNoMailAccountsAlertShown()
	{
		return alert.alertTitle != null &&
				alert.alertTitle == alert.NoEmailAccount;
	}
}