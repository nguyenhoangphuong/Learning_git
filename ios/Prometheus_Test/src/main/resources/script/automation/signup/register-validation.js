/*
 Cover:
 Signup > Register:		2805, 2806, 2807, 2808, 2809 
 */

#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"

// TEST DATA
// -------------------------------------------------------------------------------------------------------
var existedEmail = "qa@a.a";
var validEmail = generateSignupAccount();
var invalidEmails = 
	[
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

// HELPERS
// -------------------------------------------------------------------------------------------------------
function verifyAlert(title, message)
{
	// check alert
	assertTrue(alert.isCustomAlertShown(), "Alert shown");
	var info = alert.getCustomAlertInfo();
	assertEqual(info.title, title, "Alert title");
	assertEqual(info.message, message, "Alert message");
	
	// confirm
	alert.confirmCustomAlert("OK");
	alert.reset();
}

function inputEmail(email)
{
	log("Input Email");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Email"].textFields()[0].setValue(email);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
}

function inputPassword(password)
{
	log("Input Password");
	target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Password"].secureTextFields()[0].setValue(password);
	target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
}

function submit(time)
{
	if(time === undefined) time = 1;
	
	target.frontMostApp().mainWindow().buttons()["btn next"].tap();
	wait(1);
}


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

// wrong format email
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Password"].secureTextFields()[0].setValue(validPassword);
for(var i = 0; i < invalidEmails.length; i++)
{
	inputEmail(invalidEmails[i]);
	submit();
	
	verifyAlert(alert.Error, alert.InvalidEmailMsg);
}


// duplicated email
log("Input Duplicated Email");
inputEmail(existedEmail);
inputPassword(validPassword);
submit(10);

verifyAlert(alert.Error, alert.DuplicatedEmailMsg);



// VERIFY PASSWORD
// -------------------------------------------------------------------------------------------------------
log("VERIFY PASSWORD"); hr();

// input wrong format password
for(var i = 0; i < invalidPasswords.length; i++)
{
	inputPassword(invalidPasswords[i]);
	submit();
	
	verifyAlert(alert.Error, alert. InvalidPasswordMsg);
}


// valid password (and email)
inputEmail(validEmail);
inputPassword(validPassword);
submit(10);

assertTrue(staticTextExist("WHAT DOES YOUR AVERAGE DAY OF ACTIVITY LOOK LIKE?"), "Navigated to Step 2");


pass();



