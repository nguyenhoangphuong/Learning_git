/*
 Cover:
 Signin > Signin:				2835, 2838, 2839, 2840, 2841, 2842, 2843
 Signin > ForgotPassword:		2852
 */

#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"

// test data
// -------------------------------------------------------------------------------------------------------
var validEmail = "qa@a.a";
var validPassword = "qwerty1";

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


// helpers
// -------------------------------------------------------------------------------------------------------
function inputEmail(email)
{
	log("Input Email");
	target.frontMostApp().mainWindow().textFields()["email"].setValue("");
	target.frontMostApp().mainWindow().textFields()["email"].tap();
	target.frontMostApp().keyboard().typeString(email + "\n");
}

function inputPassword(password)
{
	log("Input Password");
	target.frontMostApp().mainWindow().secureTextFields()["password"].tap();
	target.frontMostApp().keyboard().typeString(password + "\n");
	target.frontMostApp().keyboard().typeString("\n");
}

function submit(time)
{
	if(time === undefined) time = 1;

	target.frontMostApp().mainWindow().buttons()[2].tap();
	wait(time);
}

function verifyAlert(title, message)
{
	var info = {};
	info.title = target.frontMostApp().mainWindow().staticTexts()[4].name();
	info.message = target.frontMostApp().mainWindow().staticTexts()[5].name();
	
	assertEqual(info.title, title, "Alert title");
	assertEqual(info.message, message, "Alert message");
}


// start up - sign in - forgot password navigation
// -------------------------------------------------------------------------------------------------------
start();

log("To Sign in view");
target.frontMostApp().mainWindow().buttons()["I HAVE AN ACCOUNT"].tap();
wait();
assertTrue(target.frontMostApp().mainWindow().buttons()["Forgot password"].isVisible(), "Forgot password link is visible");

log("Back to Start up view");
target.frontMostApp().mainWindow().buttons()[1].tap();
wait();
assertTrue(target.frontMostApp().mainWindow().buttons()["I HAVE AN ACCOUNT"].isVisible(), "I have an account button is visible");

log("To Forgot password view");
target.frontMostApp().mainWindow().buttons()["I HAVE AN ACCOUNT"].tap();
wait();
target.frontMostApp().mainWindow().buttons()["Forgot password"].tap();
wait();
assertTrue(staticTextExist("Please enter the email you signed up with. We'll send you a link to reset your password."), "Instruction message is visible");

log("Back to Sign in view");
target.frontMostApp().mainWindow().buttons()["CANCEL"].tap();
wait();
assertTrue(target.frontMostApp().mainWindow().buttons()["Forgot password"].isVisible(), "Forgot password link is visible");


// invalid email test
// -------------------------------------------------------------------------------------------------------
for(var i = 0; i < invalidEmails.length; i++)
{
	inputEmail(invalidEmails[i]);
	inputPassword("");
	submit();
	verifyAlert(alert.Error, alert.InvalidEmailMsg);
}


// invalid password test
// -------------------------------------------------------------------------------------------------------
inputEmail(validEmail);

for(var i = 0; i < invalidPasswords.length; i++)
{
	inputPassword(invalidPasswords[i]);
	submit();
	verifyAlert(alert.Error, alert.InvalidPasswordMsg);
}


pass();