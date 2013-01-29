/*
 Cover:
 
 */

#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"



// data
// -------------------------------------------------------------------------------------------------------
var unit = "US";
var name = "Tears";
var email = generateSignupAccount();
var password = "qwerty1";
var year = 1991;
var month = "September";
var day = 16;
var h1 = "5'", h2 = '6"', height = h1 + h2;
var w1 = '144', w2 = '.4', weight = w1 + w2 + " lbs";
var birthday = "Sep 16, 1991";
var sex = "MALE";

var newName = "Dandelion";
var newMonth = "December";
var newDay = 24;
var newYear = 1990;
var newBirthday = "Dec 24, 1990";



// navigation
// -------------------------------------------------------------------------------------------------------
start();

log("To Settings view");
target.frontMostApp().mainWindow().buttons()["I DON'T HAVE AN ACCOUNT"].tap();

fillRegisterForm(name, "", password, sex, year, day, month, unit, w1, w2, h1, h2);
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
wait(5);
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.frontMostApp().mainWindow().buttons()["logo small"].touchAndHold(6.8);
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.frontMostApp().mainWindow().buttons()["btn settings"].tap();
wait();



// verify profile is saved correctly after signing up
// -------------------------------------------------------------------------------------------------------
log("Settings > User Profile");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Your profile"].tap();
wait(2);

log("Verify user profile is saved");
var aname = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Name"].textFields()[0].value();
var abirthday = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[1].name();
	abirthday = abirthday.substring(abirthday.indexOf(',') + 2);
var aheight = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[3].name();
	aheight = aheight.substring(aheight.indexOf(',') + 2);								
var aweight = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[4].name();
	aweight = aweight.substring(aweight.indexOf(',') + 2);

assertEqual(aname, name, "Name");
assertEqual(abirthday, birthday, "Birthday");
assertEqual(aheight, height, "Height");
assertEqual(aweight, weight, "Weight");


// verify profile is saved correctly after editting in settings view
// -------------------------------------------------------------------------------------------------------
log("Input new user profile");
fillProfileInSettingsForm(newName, sex, newYear, newDay, newMonth, w1, w2, h1, h2);

log("Back to Settings view");
target.frontMostApp().mainWindow().buttons()[0].tap();
wait();

log("To User Profile again");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Your profile"].tap();
wait(2);

log("Check if User Profile is saved correctly");
aname = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Name"].textFields()[0].value();
abirthday = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[1].name();
abirthday = abirthday.substring(abirthday.indexOf(',') + 2);

assertEqual(aname, newName, "Name");
assertEqual(abirthday, newBirthday, "Birthday");



// verify Upload button is avaiable when user sign out
// ------------------------------------------------------------------------------------------------------
log("Back to Settings view");
target.frontMostApp().mainWindow().buttons()[0].tap();
wait();

log("Click sign out button");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Sign out"].tap();
// verify upload button in future


// verify sign out lead to Start up view
// ------------------------------------------------------------------------------------------------------
log("Verify sign out lead to Start up view");
assertTrue(target.frontMostApp().mainWindow().buttons()["I HAVE AN ACCOUNT"].isValid(), "I Have An Account button is visible");

log("Sign in again");
target.frontMostApp().mainWindow().buttons()["I HAVE AN ACCOUNT"].tap();
target.frontMostApp().mainWindow().textFields()["email"].setValue(email + "\n");
target.frontMostApp().mainWindow().secureTextFields()["password"].setValue(password);
target.frontMostApp().keyboard().typeString("\n");
wait();
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
wait(5);

log("To User Profile");
target.frontMostApp().mainWindow().buttons()["btn settings"].tap();
wait();
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Your profile"].tap();
wait(2);

log("Verify user profile is saved");
aname = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Name"].textFields()[0].value();
abirthday = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[1].name();
abirthday = abirthday.substring(abirthday.indexOf(',') + 2);
aheight = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[3].name();
aheight = aheight.substring(aheight.indexOf(',') + 2);								
aweight = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[4].name();
aweight = aweight.substring(aweight.indexOf(',') + 2);

assertEqual(aname, newName, "Name");
assertEqual(abirthday, newBirthday, "Birthday");
assertEqual(aheight, height, "Height");
assertEqual(aweight, weight, "Weight");


pass();