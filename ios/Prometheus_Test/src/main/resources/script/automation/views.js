var signup = new SignUp();
var home = new Home();
var settings = new Settings();
var signin = new SignIn();

// Home
function Home()
{
	// check current view
	this.isAtDailyView = function()
	{
		return target.frontMostApp().mainWindow().buttons()["TODAY"].isValid() &&
			target.frontMostApp().mainWindow().buttons()["TODAY"].isVisible();
	};
	
	this.hasPlaceShineHere = function()
	{
		return target.frontMostApp().mainWindow().scrollViews()[2].staticTexts()["PLACE\nSHINE\nHERE"].isValid() &&
				target.frontMostApp().mainWindow().scrollViews()[2].staticTexts()["PLACE\nSHINE\nHERE"].isVisible();
	};
	
	
	// navigation
	this.closeTips = function()
	{
		target.frontMostApp().mainWindow().scrollViews()[2].tapWithOptions({tapOffset:{x:0.5, y:0.5}});
		wait();
		target.frontMostApp().mainWindow().scrollViews()[2].tapWithOptions({tapOffset:{x:0.5, y:0.5}});
		wait();
	};
	
	this.tapSettings = function()
	{
		target.frontMostApp().mainWindow().buttons()["ico settings"].tap();
		wait();
	};
	
	this.swipeRight = function()
	{
		swipeHorizontally(50, 150, 270, 1);
		wait();
	};
	
	this.swipeLeft = function()
	{
		swipeHorizontally(150, 50, 270, 1);
		wait();
	};
	
	
	// input forms		
	this.inputRecord = function(duration, steps)
	{
		target.frontMostApp().mainWindow().buttons()["Manual"].tap();
		wait();
		target.frontMostApp().mainWindow().buttons()["Random"].tap();
		
		target.frontMostApp().mainWindow().textFields()[2].setValue(duration);
		target.frontMostApp().mainWindow().textFields()[3].setValue(steps);
		target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
		
		var info = {};
		var timeString = target.frontMostApp().mainWindow().textFields()[1].value();
		var duration = parseInt(target.frontMostApp().mainWindow().textFields()[2].value());
		var steps = parseInt(target.frontMostApp().mainWindow().textFields()[3].value());
		var v = steps / duration;
		var points = (0.25 + (v > 115.0 ? 0 : (v - 115.0) * 0.01)) * steps;
		
		info.time = helper.formatStartTime(timeString);
		info.duration = helper.formatDuration(duration);
		info.steps = helper.formatStep(steps);
		info.points = helper.formatPoint(points.toFixed(0));
		
		target.frontMostApp().mainWindow().buttons()["Save"].tap();
		return info;
	};
	
	this.randomRecord = function()
	{
		target.frontMostApp().mainWindow().buttons()["Manual"].tap();
		wait();
		target.frontMostApp().mainWindow().buttons()["Random"].tap();
		
		var info = {};
		var timeString = target.frontMostApp().mainWindow().textFields()[1].value();
		var duration = parseInt(target.frontMostApp().mainWindow().textFields()[2].value());
		var steps = parseInt(target.frontMostApp().mainWindow().textFields()[3].value());
		var v = steps / duration;
		var points = (0.25 + (v > 115.0 ? 0 : (v - 115.0) * 0.01)) * steps;
		
		info.time = helper.formatStartTime(timeString);
		info.duration = helper.formatDuration(duration);
		info.steps = helper.formatStep(steps);
		info.points = helper.formatPoint(points.toFixed(0));
		
		target.frontMostApp().mainWindow().buttons()["Save"].tap();
		return info;
	};
	
	// getting info and utilities	
	this.getProgressSummary = function()
	{		
		var progress = target.frontMostApp().mainWindow().scrollViews()[2].staticTexts()[5].name();
		var info = {};
		
		info.goal = parseInt(progress.substring(progress.indexOf("/") + 1));
		info.percent = parseInt(target.frontMostApp().mainWindow().scrollViews()[2].staticTexts()[0].name());
		info.points = parseInt(progress.substring(0, progress.indexOf("/")));
		info.steps = parseInt(target.frontMostApp().mainWindow().scrollViews()[2].staticTexts()[2].name());
		info.miles = parseFloat(target.frontMostApp().mainWindow().scrollViews()[2].staticTexts()[4].name());
		info.duration = parseFloat(target.frontMostApp().mainWindow().scrollViews()[2].staticTexts()[6].name());
		
		log(JSON.stringify(info));
		return info;
	};
				
	this.getCurrentDay = function()
	{
		return target.frontMostApp().mainWindow().buttons()[3].name();
	};
	
}


// Sign in
function SignIn()
{
	// check current view
	this.isAtSignIn = function()
	{
		return staticTextExist("LOG IN") && staticTextExist("Enter your account details");
	}
	
	this.isAtForgotPassword = function()
	{
		return staticTextExist("Please enter the email you signed up with. We'll send you a link to reset your password.");
	}
	
	
	// navigation
	this.chooseSignIn = function()
	{
		if(!target.frontMostApp().mainWindow().buttons()["LOG IN"].isValid())
		{
			target.frontMostApp().alert().defaultButton().tap();
			wait();
		}
		
		target.frontMostApp().mainWindow().buttons()["LOG IN"].tap();
		wait();
	}
	
	this.chooseFacebookSignIn = function()
	{
		if(!target.frontMostApp().mainWindow().buttons()["LOG IN WITH FACEBOOK"].isValid())
		{
			target.frontMostApp().alert().defaultButton().tap();
			wait();
		}
		
		target.frontMostApp().mainWindow().buttons()["LOG IN WITH FACEBOOK"].tap();
		wait();
	};
	
	this.back = function()
	{
		log("Tap [Back]")
		target.frontMostApp().mainWindow().buttons()[1].tap();
		wait();
	};
	
	this.tapForgotPassword = function()
	{
		target.frontMostApp().mainWindow().buttons()["Forgot password"].tap();
		wait();
	};
	
	this.cancelForgotPassword = function()
	{
		target.frontMostApp().mainWindow().buttons()["CANCEL"].tap();
		wait();
	};
	
	this.submitForgotPassword = function()
	{
		target.frontMostApp().mainWindow().buttons()["SUBMIT"].tap();
		wait();
	};
	
	// input forms
	this.submitSignInForm = function(email, password, waitTime)
	{
		if(waitTime === undefined)
			waitTime = 1;
		
		target.frontMostApp().mainWindow().textFields()["email"].setValue(email);
		target.frontMostApp().mainWindow().secureTextFields()["password"].setValue(password);
		target.frontMostApp().keyboard().typeString("\n");
		wait(waitTime);
	};
	
	this.fillForgotPasswordEmail = function(email)
	{
		if(this.isAtForgotPassword())
			target.frontMostApp().mainWindow().textFields()["email"].setValue(email);
	};
	
	this.getCurrentForgotPasswordEmail = function()
	{
		if(this.isAtForgotPassword())
			return target.frontMostApp().mainWindow().textFields()["email"].value();	
	};
	
	
	// alert check
	this.isInvalidEmailAlertShown = function()
	{
		return alert.message != null && alert.message == alert.InvalidEmailMsg;
	};
	
	this.isInvalidPasswordAlertShown = function()
	{
		return alert.message != null && alert.message == alert.InvalidPasswordMsg;
	};

	this.isWrongAccountAlertShown = function()
	{
		return alert.message != null && alert.message == alert.WrongAccountMsg;
	};
	
	this.isIncorrectEmailAlertShown = function()
	{
		return alert.message != null && alert.message == alert.IncorrectEmailMsg;
	};
	
	this.isEmailSentAlertShown = function()
	{
		return alert.message != null && alert.message == alert.EmailSentMsg;
	};
	
}


// Sign up
function SignUp()
{
	// check current view
	this.isAtInitView = function()
	{
		return buttonExist("SIGN UP") && buttonExist("LOG IN");
	};
	
	this.isAtStep1 = function()
	{
		return staticTextExist("SIGN UP") && staticTextExist("Create a Misfit account");
	};
	
	this.isAtStep2 = function()
	{
		return staticTextExist("PROFILE") && staticTextExist("To help us understand you better");
	}

	this.isAtStep3 = function()
	{
		return staticTextExist("SET YOUR GOAL") && staticTextExist("How active do you want to be?");
	}
	
	this.isAtStep4 = function()
	{
		return staticTextExist("LINK YOUR SHINE") && staticTextExist("Place Shine below to get started");;
	}
	
	// navigation methods
	this.chooseSignUp = function()
	{
		if(!target.frontMostApp().mainWindow().buttons()["SIGN UP"].isValid())
		{			
			target.frontMostApp().alert().defaultButton().tap();
			wait();
		}
		
		log("Tap [Sign Up]");
		target.frontMostApp().mainWindow().buttons()["SIGN UP"].tap();
		wait();
	};
	
	this.next = function()
	{
		log("Tap [Next]");
		
		var id = 1;
		if(this.isAtStep2() || this.isAtStep4()) 
			id = 3;
		
		target.frontMostApp().mainWindow().buttons()[id].tap();
		wait();
	};
	
	this.back = function()
	{
		log("Tap [Back]")
		
		var id = 0;
		if(this.isAtStep2() || this.isAtStep4()) 
			id = 2;
		
		target.frontMostApp().mainWindow().buttons()[id].tap();
		wait();
	};
	
	
	// forms methods
	this.submitSignUpForm = function(email, password, waitTime)
	{
		if(waitTime === undefined)
			waitTime = 1;
		
		target.frontMostApp().mainWindow().textFields()["email"].setValue(email);
		target.frontMostApp().mainWindow().secureTextFields()["password"].setValue(password)
		target.frontMostApp().keyboard().typeString("\n");	
		wait(waitTime);
	};
	
	this.fillProfileForm = function(gender, birthday, height, weight)
	{
		var monthStrings = ["dummy", "January", "February", "March", "April", "May", "June", "July", "August", "September",
							"October", "November", "December"];
		
		// gender
		if(gender != null)
		{
			if(gender == "Male" || gender == "male" || gender == true || gender == "m") 
				target.frontMostApp().mainWindow().buttons()[0].tap();
		
			if(gender == "Female" || gender == "female" || gender == false || gender == "f") 
				target.frontMostApp().mainWindow().buttons()[1].tap();
		}
		
		// birthday
		if(birthday != null)
		{
			target.frontMostApp().mainWindow().staticTexts()[5].tap();
			dateWheelPick(target.frontMostApp().windows()[1].pickers()[0], birthday[2], monthStrings[birthday[1]], birthday[0]);
			target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
		}
		
		// height
		if(height != null)
		{
			target.frontMostApp().mainWindow().staticTexts()[7].tap();
			var picker = target.frontMostApp().windows()[1].pickers()[0];
			
			wheelPick(picker, 2, height[2]);	// ft or m
			if(height[0] != null)
			{
				wheelPick(picker, 0, height[0] + (height[2] == "m" ? "" : "'"));
				wheelPick(picker, 1, height[2] == "m" ? "." + height[1] : height[1] + "\"");
			}
			
			target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
		}

		// weight
		if(weight != null)
		{
			target.frontMostApp().mainWindow().staticTexts()[9].tap();
			var picker = target.frontMostApp().windows()[1].pickers()[0];
			
			wheelPick(picker, 2, weight[2]);	// lbs or kg
			if(weight[0] != null)
			{
				wheelPick(picker, 0, weight[0].toString());
				wheelPick(picker, 1, "." + weight[1]);
			}
			
			target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
		}		
	};
	
	this.getProfileForm = function()
	{
		var info = {};
		info.birthday = target.frontMostApp().mainWindow().staticTexts()[5].name();
		info.height = target.frontMostApp().mainWindow().staticTexts()[7].name();
		info.weight = target.frontMostApp().mainWindow().staticTexts()[9].name();
		
		return info;
	};
	
	this.setGoal = function(goalLevel)
	{
		if(goalLevel == 0) target.frontMostApp().mainWindow().staticTexts()["ACTIVE"].tap();
		if(goalLevel == 1) target.frontMostApp().mainWindow().staticTexts()["VERY ACTIVE"].tap();
		if(goalLevel == 2) target.frontMostApp().mainWindow().staticTexts()["HIGHLY ACTIVE"].tap();
	};
	
	this.getGoalInfo = function(goalLevel)
	{
		var info = {};
		info.title = target.frontMostApp().mainWindow().staticTexts()[goalLevel * 4 + 2].name();
		info.activities = target.frontMostApp().mainWindow().staticTexts()[goalLevel * 4 + 3].name();
		info.points = target.frontMostApp().mainWindow().staticTexts()[goalLevel * 4 + 4].name();
		info.steps = target.frontMostApp().mainWindow().staticTexts()[goalLevel * 4 + 5].name();
		
		return info;
	};
	
	this.syncDevice = function(seconds)
	{
		target.frontMostApp().mainWindow().buttons()["progresscircle"].touchAndHold(seconds);
	};
	
	
	// checking alert at step 1
	this.isInvalidEmailAlertShown = function()
	{
		return alert.message != null && alert.message == alert.InvalidEmailMsg;
	};
	
	this.isInvalidPasswordAlertShown = function()
	{
		return alert.message != null && alert.message == alert.InvalidPasswordMsg;
	};
	
	this.isDuplicatedEmailAlertShown = function()
	{
		return alert.message != null && alert.message == alert.DuplicatedEmailMsg;
	};
	
	
	// check visible and instruction for step 4
	this.hasDontHaveShineButton = function()
	{
		return  target.frontMostApp().mainWindow().buttons()["DON'T HAVE A SHINE?"].isValid() && 
				target.frontMostApp().mainWindow().buttons()["DON'T HAVE A SHINE?"].isVisible()
	};
	
	this.hasDetectionFailMessage = function()
	{
		return staticTextExist("Detection fail");
	};
	
	this.hasDetectionPassMessage = function()
	{
		return staticTextExist("Done.");
	};

}


// Settings Screen
function Settings()
{
	
	// default strings
	this.HeadTitle = "HEAD";
	this.ArmTitle = "UPPER ARM";
	this.ChestTitle = "CHEST";
	this.WristTitle = "WRIST";
	this.WaistTitle = "WAIST";
	this.FootTitle = "FOOT/ANKLE";
	this.HeadDescription = "Clip Shine to a hat or pair of glasses with the clasp for an eye-catching look.";
	this.ArmDescription = "Using the clasp, you can clip shine to a short-sleeved shirt or blouse.";
	this.ChestDescription = "Use the clasp to clip shine to a shirt collar, shirt pocket, or button-up shirt. Or, get the Shine necklace accessory to make Shine the front and center of your outfit.";
	this.WristDescription = "If you want to use Shine as a watch, there’s no better place than the wrist. Just make sure you use the official wristband or leather wristband to keep Shine secure!";
	this.WaistDescription = "Use the clasp to clip shine to your belt, belt loop, pocket, or the hem of a shirt. Wearing Shine here results in very accurate activity reporting.";
	this.FootDescription = "Use the clasp to clip shine to a shoe, shoelace or sock.";
	
	
	// check current view
	this.isAtOverviewSettings = function()
	{
		return staticTextExist("SETTINGS");
	};
	
	this.isAtProfileSettings = function()
	{
		return staticTextExist("USER INFO");
	};
	
	this.isAtWearingShineSettings = function()
	{
		return staticTextExist("WHERE WILL YOU WEAR YOUR SHINE?");
	};
	
	
	// navigation
	this.back = function()
	{
		log("Tap [Back]");
		
		if(this.isAtProfileSettings())
			target.frontMostApp().mainWindow().buttons()[2].tap();
		
		else if(this.isAtWearingShineSettings())
			target.tap({x:10, y:450});
		
		else if(this.isAtOverviewSettings())
			target.frontMostApp().mainWindow().buttons()[0].tap();
		
		wait();
	};
	
	this.tapProfile = function()
	{
		log("Tap [Profile]");
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[0].tap();
		wait();
	};
	
	this.tapWearingShine = function()
	{
		log("Tap [Wearing Shine]");
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[1].tap();
		wait();
	};
	
	this.tapSignOut = function()
	{
		log("Tap [Sign out]");
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["SIGN OUT"].tap();
		wait();
	};
	
	
	// wearing Shine methods
	this.tapWearOn = function(where)
	{		
		if(where == 0 || where == "Head")
			target.frontMostApp().mainWindow().buttons()[0].tap();
		
		if(where == 1 || where == "Arm")
			target.frontMostApp().mainWindow().buttons()[1].tap();
		
		if(where == 2 || where == "Chest")
			target.frontMostApp().mainWindow().buttons()[2].tap();
		
		if(where == 3 || where == "Wrist")
			target.frontMostApp().mainWindow().buttons()[3].tap();
		
		if(where == 4 || where == "Waist")
			target.frontMostApp().mainWindow().buttons()[4].tap();
		
		if(where == 5 || where == "Foot" || where == "Ankle")
			target.frontMostApp().mainWindow().buttons()[5].tap();
		
		wait();
	};
	
	this.closeDescription = function()
	{
		target.tap({x:300, y:40});
	};
	
	this.hasTip = function(title, description)
	{
		return staticTextExist(title) && staticTextExist(description);
	};
	
	this.hasBuyNecklaceButton = function()
	{
		return buttonExist("BUY NECKLACE");
	};
	
	this.hasBuyWristbandButton = function()
	{
		return buttonExist("BUY WRISTBAND");
	};

	
	// profile settings methods
	this.fillProfileForm = function(gender, birthday, height, weight)
	{
		var monthStrings = ["dummy", "January", "February", "March", "April", "May", "June", "July", "August", "September",
							"October", "November", "December"];
		
		// gender
		if(gender != null)
		{
			if(gender == "Male" || gender == "male" || gender == true || gender == "m") 
				target.frontMostApp().mainWindow().buttons()[0].tap();
		
			if(gender == "Female" || gender == "female" || gender == false || gender == "f") 
				target.frontMostApp().mainWindow().buttons()[1].tap();
		}
		
		// birthday
		if(birthday != null)
		{
			target.frontMostApp().mainWindow().staticTexts()[5].tap(); 
			dateWheelPick(target.frontMostApp().windows()[1].pickers()[0], birthday[2], monthStrings[birthday[1]], birthday[0]);
			target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
		}
		
		// height
		if(height != null)
		{
			target.frontMostApp().mainWindow().staticTexts()[7].tap();
			var picker = target.frontMostApp().windows()[1].pickers()[0];
			
			wheelPick(picker, 2, height[2]);	// ft or m
			if(height[0] != null)
			{
				wheelPick(picker, 0, height[0] + (height[2] == "m" ? "" : "'"));
				wheelPick(picker, 1, height[2] == "m" ? "." + height[1] : height[1] + "\"");
			}
			
			target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
		}

		// weight
		if(weight != null)
		{
			target.frontMostApp().mainWindow().staticTexts()[9].tap();
			var picker = target.frontMostApp().windows()[1].pickers()[0];
			
			wheelPick(picker, 2, weight[2]);	// lbs or kg
			if(weight[0] != null)
			{
				wheelPick(picker, 0, weight[0].toString());
				wheelPick(picker, 1, "." + weight[1]);
			}
			
			target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
		}
	};
	
	this.getProfileForm = function()
	{
		var info = {};
		info.email = target.frontMostApp().mainWindow().staticTexts()[1].name();
		info.birthday = target.frontMostApp().mainWindow().staticTexts()[5].name();
		info.height = target.frontMostApp().mainWindow().staticTexts()[7].name();
		info.weight = target.frontMostApp().mainWindow().staticTexts()[9].name();
		
		return info;
	};
	
	
	// getting info and utilities
	this.getProfileOverviewInfo = function()
	{
		var text = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[0].name();
		return text.substring(text.indexOf(",") + 2);
	};
	
	this.getCurrentWearingPosition = function()
	{
		var text = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[1].name();
		if(text.indexOf("," < 0))
			return "";
		
		return text.substring(text.indexOf(",") + 2);
	};
	

	// set debug datasource
	function setDebugSource(source)
	{
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["DEBUG"].tap();
		target.frontMostApp().mainWindow().textFields()[0].tap();
		
		var picker = target.frontMostApp().windows()[1].pickers()[0];
		wheelPick(picker, 0, source);
		
		target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
		target.frontMostApp().mainWindow().buttons()["Done"].tap();
		wait();
	};
	
	this.setAccelerometerAsDebugSource = function()
	{
		setDebugSource("Accelerometer");
	};
	
	this.setInputFormAsDebugSource = function()
	{
		setDebugSource("Input form");
	};
	
	this.setFitbitAsDebugSource = function()
	{
		setDebugSource("Fitbit");
	};
	
}