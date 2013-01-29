/*
 Cover:
 Signin > ForgotPassword:		2848, 2849, 2850, 2851
 */

#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"

// test data
// -------------------------------------------------------------------------------------------------------
var notRegisteredEmail = generateSignupAccount();
var registeredEmail = "qa@a.a";
var invalidEmails = 
	[
		"",
		"wrong1.@a.a",
		"wrong2",
		"wrong3@",
		"wrong4@a.",
		".wrong5@a.a",
		"wrong6/@a.a",
		"wrong7@a..a",
	];


// helpers
// -------------------------------------------------------------------------------------------------------
function inputEmail(email)
{
	log("Input Email");
	target.frontMostApp().mainWindow().textFields()["email"].setValue("");
	target.frontMostApp().mainWindow().textFields()["email"].tap();
	target.frontMostApp().keyboard().typeString(email);
}

function submit(time)
{
	if(time === undefined) time = 1;
	target.frontMostApp().mainWindow().buttons()["SUBMIT"].tap();
	
	wait(time);
}

function verifyAlert(title, message, index1, index2)
{
	if(index1 === undefined) index1 = 1;
	if(index2 === undefined) index2 = 2;
	
	var info = {};
	info.title = target.frontMostApp().mainWindow().staticTexts()[index1].name();
	info.message = target.frontMostApp().mainWindow().staticTexts()[index2].name();
	
	assertEqual(info.title, title, "Alert title");
	assertEqual(info.message, message, "Alert message");
}



// navigate
// -------------------------------------------------------------------------------------------------------
start();

log("To Sign in view");
target.frontMostApp().mainWindow().buttons()["I HAVE AN ACCOUNT"].tap();
wait();
assertTrue(target.frontMostApp().mainWindow().buttons()["Forgot password"].isVisible(), "Forgot password link is visible");
target.frontMostApp().mainWindow().buttons()["Forgot password"].tap();


// input, wrong format email
// -------------------------------------------------------------------------------------------------------
log("Wrong format email");
for(var i = 0; i < invalidEmails.length; i++)
{
	inputEmail(invalidEmails[i]);
	submit();
	verifyAlert(alert.Error, alert.InvalidEmailMsg);
}


// not registered email
// -------------------------------------------------------------------------------------------------------
log("Not registered email");
inputEmail(notRegisteredEmail);
submit(5);
verifyAlert(alert.IncorrectEmail, alert.IncorrectEmailMsg);


// registered email
// -------------------------------------------------------------------------------------------------------
log("Registered email");
inputEmail(registeredEmail);
submit(5);
verifyAlert(alert.EmailSent, alert.EmailSentMsg, 4, 5);

log("Check if user had redirected to Sign in");
assertTrue(target.frontMostApp().mainWindow().buttons()["Forgot password"].isValid(), "Forgot password link is valid");


pass();