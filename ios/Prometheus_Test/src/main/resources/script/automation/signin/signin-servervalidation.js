/*
 Cover:
 Signin > Signin:	 2844, 2845, 2846, 2847
 */

#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"

// test data
// -------------------------------------------------------------------------------------------------------
var validEmail = "qa@a.a";
var validEmailNoPlan = "automation_signin_noplan@qa.com";
var validEmailHavePlan = "automation_signin_haveplan@qa.com";
var validPassword = "qwerty1";


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
	target.frontMostApp().keyboard().typeString(password);
	target.frontMostApp().keyboard().typeString("\n");
	wait();
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

// navigate
// -------------------------------------------------------------------------------------------------------
log("To Sign in view");
target.frontMostApp().mainWindow().buttons()["I HAVE AN ACCOUNT"].tap();
wait();


// right email, wrong password / wrong email, right password
// -------------------------------------------------------------------------------------------------------
log("Right email, wrong password");
inputEmail(validEmail);
inputPassword("asdasdasdasd12");
submit(5);
verifyAlert(alert.Error, alert.WrongAccountMsg);


log("Wrong email, right password");
inputEmail(generateSignupAccount());
inputPassword(validPassword);
submit(5);
verifyAlert(alert.Error, alert.WrongAccountMsg);


// right email, right passwords, have plans
// -------------------------------------------------------------------------------------------------------
log("Right email, wrong password, have plans");
inputEmail(validEmailHavePlan);
inputPassword(validPassword);
submit(5);
assertTrue(target.frontMostApp().mainWindow().buttons()["btn settings"].isValid(), "Button Settings is avaiable");

log("Sign out");
target.frontMostApp().mainWindow().buttons()["btn settings"].tap();
wait(5);
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Sign out"].tap();
wait();


// right email, right passwords, no plans
// -------------------------------------------------------------------------------------------------------
log("To Sign in view");
target.frontMostApp().mainWindow().buttons()["I HAVE AN ACCOUNT"].tap();
wait();

log("Right email, wrong password, no plans");
inputEmail(validEmailNoPlan);
inputPassword(validPassword);
submit(5);
assertTrue(staticTextExist("WHAT DOES YOUR AVERAGE DAY OF ACTIVITY LOOK LIKE?"), "User is directed to Step 2 to complete plan");


pass();