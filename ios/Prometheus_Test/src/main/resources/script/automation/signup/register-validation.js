/*
 Cover:
 2805, 2806, 2807, 2808, 2809 
 */

#import "../../libs/libs.js"
#import "../alerthandler.js"

var existedEmail = "qa@a.a";
var validEmail = generateSignupAccount();
var invalidEmails = 
	[
		"",
		"wrong1.@a.a",
		"wrong2",
		"wrong3@",
		"wrong4@a.",
		".wrong5@a.a",
		"wrong6/@a.a",
		"wrong7@a..a"
	];

var invalidPasswords =
	[
		"",
		"allletters",
		"123456789",
		"tiny1",
	 	"1234a"
	];
var validPassword = "qwerty1";

start();

// INTERACTION
// -------------------------------------------------------------------------------------------------------
// input name, email, password
log("INTERACTION"); hr();

target.frontMostApp().mainWindow().buttons()["I DON'T HAVE AN ACCOUNT"].tap();
wait();


// VERIFY EMAIL
// -------------------------------------------------------------------------------------------------------
log("VERIFY EMAIL"); hr();

// -------------- input wrong email
for(var i = 0; i < invalidEmails.length; i++)
{
	log("Input Email");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Email"].textFields()[0].setValue("");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Email"].textFields()[0].tap();
	target.frontMostApp().keyboard().typeString(invalidEmails[i]);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
	
	target.frontMostApp().mainWindow().buttons()["btn next"].tap();
	wait();
	
	// check alert
	assertTrue(alert.isCustomAlertShown(), "Alert shown");
	var info = alert.getCustomAlertInfo();
	assertEqual(info.title, alert.Error, "Alert title");
	assertEqual(info.message, alert.InvalidEmailMsg, "Alert message");
	
	// confirm
	alert.confirmCustomAlert("OK");
	alert.reset();
}



// -------------- duplicated email
log("Input Duplicated Email");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Email"].textFields()[0].setValue("");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Email"].textFields()[0].tap();
target.frontMostApp().keyboard().typeString(existedEmail);
target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
	
	// input valid password
	log("Input Password");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Password"].tap();
	target.frontMostApp().keyboard().typeString(validPassword);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

	target.frontMostApp().mainWindow().buttons()["btn next"].tap();
	wait(3);
	
	// check alert
	assertTrue(alert.isCustomAlertShown(), "Alert shown");
	var info = alert.getCustomAlertInfo();
	assertEqual(info.title, alert.Error, "Alert title");
	assertEqual(info.message, alert.DuplicatedEmailMsg, "Alert message");

	// confirm
	alert.confirmCustomAlert("OK");
	alert.reset();


// VERIFY PASSWORD
// -------------------------------------------------------------------------------------------------------
log("VERIFY PASSWORD"); hr();

// --------------- input wrong password

// input valid email
	log("Input Email");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Email"].textFields()[0].setValue("");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Email"].textFields()[0].tap();
	target.frontMostApp().keyboard().typeString(validEmail);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Password"].secureTextFields()[0].setValue("");


for(var i = 0; i < invalidPasswords.length; i++)
{
	log("Input Password");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Password"].tap();
	target.frontMostApp().keyboard().typeString(invalidPasswords[i]);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
	
	target.frontMostApp().mainWindow().buttons()["btn next"].tap();
	wait();
	
	// check alert
	assertTrue(alert.isCustomAlertShown(), "Alert shown");
	var info = alert.getCustomAlertInfo();
	assertEqual(info.title, alert.Error, "Alert title");
	assertEqual(info.message, alert.InvalidPasswordMsg, "Alert message");
	
	// confirm
	alert.confirmCustomAlert("OK");
	alert.reset();
}


// ---------------- valid password (and email)
log("Input Password");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Password"].tap();
target.frontMostApp().keyboard().typeString(validPassword);
target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

target.frontMostApp().mainWindow().buttons()["btn next"].tap();
wait(5);

assertTrue(staticTextExist("WHAT DOES YOUR AVERAGE DAY OF ACTIVITY LOOK LIKE?"), "Navigated to Step 2");


pass();



