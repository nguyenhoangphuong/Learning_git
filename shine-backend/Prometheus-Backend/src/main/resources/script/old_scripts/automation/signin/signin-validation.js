#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"
#import "../views.js"

// test data
// ---------------------------------------------------------------------------------------
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

var notExistEmail = generateSignupAccount();
var existEmail = "qa@a.a";
var wrongPassword = "wr0ngpass";
var rightPassword = "qwerty1";


// test logic
// ---------------------------------------------------------------------------------------
start();

// to sign in view
log("To Sign in view");
signin.chooseSignIn();
assertTrue(signin.isAtSignIn(), "At Sign in view");

// invalid email test
for(var i = 0; i < invalidEmails.length; i++)
{
	log("Sign in with: Email - " + invalidEmails[i] + ", Password - " + validPassword);
	signin.submitSignInForm(invalidEmails[i], validPassword);
	assertTrue(signin.isInvalidEmailAlertShown(), "Invalid email alert shown");
}

// invalid password test
for(var i = 0; i < invalidPasswords.length; i++)
{
	log("Sign in with: Email - " + validEmail + ", Password - " + invalidPasswords[i]);
	signin.submitSignInForm(validEmail, invalidPasswords[i]);
	assertTrue(signin.isInvalidPasswordAlertShown(), "Invalid password alert shown");
}

// right email, wrong password
log("Sign in with: Email - " + existEmail + ", Password - " + wrongPassword);
signin.submitSignInForm(existEmail, wrongPassword, 4);
assertTrue(signin.isWrongAccountAlertShown(), "Wrong log in info alert shown");


// wrong email
log("Sign in with: Email - " + notExistEmail + ", Password - " + rightPassword);
signin.submitSignInForm(notExistEmail, rightPassword, 4);
assertTrue(signin.isWrongAccountAlertShown(), "Wrong log in info alert shown");


// right email, right password
log("Sign in with: Email - " + existEmail + ", Password - " + rightPassword);
signin.submitSignInForm(existEmail, rightPassword, 4);
assertTrue(home.isAtDailyView(), "Log in successfully");


pass();
