var signup = new SignUp();
var settings = new Settings();


// Settings
function Settings()
{
	// check current view
	function isAtMainSettings()
	{
		return true;
	};
	
	function isAtProfileSettings()
	{
		return true;
	};
	
	function isAtWearingShineSettings()
	{
		return true;
	};
	
	
	// navigation
	function back()
	{
		
	};
	
	function check()
	{
		
	};
	
	function tapProfile()
	{
		
	};
	
	function tapWearingShine()
	{
		
	};
	
	function tapSignOut()
	{
		
	};
	
	
	// profile settings
	this.fillProfileForm = function(gender, birthday, height, weight)
	{
		var monthStrings = ["dummy", "January", "February", "March", "April", 
		                    "May", "June", "July", "August", "September",
							"October", "November", "December"];
		
		// gender
		if(gender != null)
		{
			if(gender = "Male" || gender == "male" || gender == true || gender == "m") 
				target.frontMostApp().mainWindow().buttons()[0].tap();
		
			if(gender = "Female" || gender == "female" || gender == false || gender == "f") 
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
				wheelPick(picker, 0, height[0] + (height[2] == "cm" ? "" : "'"));
				wheelPick(picker, 1, height[2] == "cm" ? "." + height[1] : height[1] + "\"");
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
				wheelPick(picker, 0, "." + weight[0]);
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

	
	// wearing shine settings 
	this.tapWearingAt(where)
	{
		
	};
	
}


// Sign up
function SignUp()
{
	// check current view
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
		log("Tap [Sign Up]");
		target.frontMostApp().mainWindow().buttons()["SIGN UP"].tap();
		wait();
	};
	
	this.next = function()target.frontMostApp()
	{
		log("Tap [Next]");
		target.frontMostApp().mainWindow().buttons()["arrow right indicator"].tap();
		wait();
	};
	
	this.back = function()
	{
		log("Tap [Back]")
		target.frontMostApp().mainWindow().buttons()["arrow left indicator"].tap();
		wait();
	}
	
	
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
			if(gender = "Male" || gender == "male" || gender == true || gender == "m") 
				target.frontMostApp().mainWindow().buttons()[0].tap();
		
			if(gender = "Female" || gender == "female" || gender == false || gender == "f") 
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
				wheelPick(picker, 0, height[0] + (height[2] == "cm" ? "" : "'"));
				wheelPick(picker, 1, height[2] == "cm" ? "." + height[1] : height[1] + "\"");
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
				wheelPick(picker, 0, "." + weight[0]);
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
	
	this.syncDevice(seconds)
	{
		target.frontMostApp().mainWindow().buttons()["logo small"].touchAndHold(seconds);
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
	
	// format string for step 2
	this.formatBirthday = function(birthday)
	{
		var monthStrings = ["dummy", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
							"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
		
		return monthStrings[birthday[1]] + " " + birthday[0] + ", " + birthday[2];
	};
	
	this.formatHeight = function(h1, h2, isUS)
	{
		if(isUS) return h1 + "'" + h2 + "\"";
		return h1 + "." + h2 + " m";
	};
	
	this.formatWeight = function(w1, w2, isUS)
	{
		return w1 + "." + w2 + (isUS ? " lbs" : " kg");
	};
	
	// check visible and instruction for step 4
	this.hasDontHaveShineButton = function()
	{
		return target.frontMostApp().mainWindow().buttons()["DON'T HAVE A SHINE?"].isValid()
		&& target.frontMostApp().mainWindow().buttons()["DON'T HAVE A SHINE?"].isVisible()
	};
	
	this.hasDetectionFailMessage = function()
	{
		return true;
	};
	
	this.hasDetectionPassMessage = function()
	{
		return true;
	};

}