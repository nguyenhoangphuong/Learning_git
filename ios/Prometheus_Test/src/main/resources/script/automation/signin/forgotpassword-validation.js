#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"
#import "../views.js"
listAllStaticTexts(target.frontMostApp().mainWindow().scrollViews()[2]);
listAllButtons(target.frontMostApp().mainWindow().scrollViews()[2]);
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


// test logic
// -------------------------------------------------------------------------------------------------------
start();

// to forgot password by tapping on the link
log("To Forgot password view");
signin.chooseSignIn();
signin.tapForgotPassword();
assertTrue(signin.isAtForgotPassword(), "At Forgot password by tapping on the link");

// back to sign in view
signin.cancelForgotPassword();
assertTrue(signin.isAtSignIn(), "At Sign in view");

// sign in wrong account and tap I forgot
alert.alertChoice = "I forgot";
signin.submitSignInForm(notRegisteredEmail, "qwerty1", 5);
assertTrue(signin.isAtForgotPassword(), "At Forgot password by tapping on I forgot alert");
assertEqual(signin.getCurrentForgotPasswordEmail(), notRegisteredEmail, "Default email is the same with input email");

// input wrong format email
for(var i = 0; i < invalidEmails.length; i++)
{
	signin.fillForgotPasswordEmail(invalidEmails[i]);
	signin.submitForgotPassword();
	
	assertTrue(signin.isInvalidEmailAlertShown(), "Invalid email alert shown");
}

// not registered email
signin.fillForgotPasswordEmail(notRegisteredEmail);
signin.submitForgotPassword();
wait(3);

assertTrue(signin.isIncorrectEmailAlertShown(), "Not existed email alert shown");

// registered email
signin.fillForgotPasswordEmail(registeredEmail);
signin.submitForgotPassword();
wait(3);

assertTrue(signin.isEmailSentAlertShown(), "Email sent alert shown");
assertTrue(signin.isAtSignIn(), "Users is redirected to sign in");


pass();