/*
 Cover:
 Signup > Register:		2810, 2811
 Signup > Step1:		2813, 2814, 2817, 2916
 Signup > Step2:		2822, 2823
 Signup > Step3:		2829, 2830
 Signup > Step4:		2831, 2832, 2833
 */

#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"

// DATA
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


// INTERACTION
// -------------------------------------------------------------------------------------------------------
start();

log("INTERACTION"); hr();
target.frontMostApp().mainWindow().buttons()["I DON'T HAVE AN ACCOUNT"].tap();
wait();

// VERIFY
// -------------------------------------------------------------------------------------------------------
// back at register
target.frontMostApp().mainWindow().buttons()["btn back"].tap();
wait();
assertTrue(target.frontMostApp().mainWindow().buttons()["I DON'T HAVE AN ACCOUNT"].isValid(), "Back to StartUp");

// fill form and next to step 2
target.frontMostApp().mainWindow().buttons()["I DON'T HAVE AN ACCOUNT"].tap();
fillRegisterForm(name, email, password, sex, year, day, month, unit, w1, w2, h1, h2);
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
wait(3);
assertTrue(staticTextExist("WHAT DOES YOUR AVERAGE DAY OF ACTIVITY LOOK LIKE?"), "Next to Step 2");
 
// back to step 1
log("Back to Step 1");
target.frontMostApp().mainWindow().buttons()["btn back"].tap();
wait();
assertTrue(target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Name"].textFields()[0].isValid(), "Name field is valid");
assertFalse(target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Email"].isValid(), "Email field is not valid");
assertFalse(target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Password"].isValid(), "Password field is not valid");

// data from register is saved
var aunit = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Units"].buttons().firstWithPredicate("value == 1").name();
var aname = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Name"].textFields()[0].value();
var abirthday = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[2].name();
	abirthday = abirthday.substring(abirthday.indexOf(',') + 2);
var aheight = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[4].name();
	aheight = aheight.substring(aheight.indexOf(',') + 2);								
var aweight = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[5].name();
	aweight = aweight.substring(aweight.indexOf(',') + 2);

log("Verify input values");
assertEqual(aunit, unit, "Unit");
assertEqual(aname, name, "Name");
assertEqual(abirthday, birthday, "Birthday");
assertEqual(aheight, height, "Height");
assertEqual(aweight, weight, "Weight");
 
// back from step 1 is not possible
assertTrue(target.frontMostApp().mainWindow().buttons()["btn back"].isEnabled() == 0, "Back button is disabled");
target.frontMostApp().mainWindow().buttons()["btn back"].tap();
wait();
assertTrue(target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Name"].textFields()[0].isValid(), "Name field is valid");
assertFalse(target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Email"].textFields()[0].isValid(), "Email field is not valid");

// next to step 2
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
wait();
assertTrue(staticTextExist("WHAT DOES YOUR AVERAGE DAY OF ACTIVITY LOOK LIKE?"), "Next to Step 2");

// next to step 3
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
wait();
assertTrue(target.frontMostApp().mainWindow().staticTexts().firstWithName("01").isValid(), "Next to Step 3");

// back to step 2
target.frontMostApp().mainWindow().buttons()["btn back"].tap();
wait();
assertTrue(staticTextExist("WHAT DOES YOUR AVERAGE DAY OF ACTIVITY LOOK LIKE?"), "Back to Step 2");

// next to step 3
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
wait();
assertTrue(target.frontMostApp().mainWindow().staticTexts().firstWithName("01").isValid(), "Next to Step 3");

// next to step 4
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
wait(8);
assertTrue(staticTextExist("PLACE SHINE HERE"), "Next to Step 4");

// at the beginning, no back or next buttons, but have get yours now button
assertFalse(target.frontMostApp().mainWindow().buttons()["btn back"].isVisible(), "Back button is not valid");
assertFalse(target.frontMostApp().mainWindow().buttons()["btn next"].isVisible(), "Next button is not valid");
assertTrue(target.frontMostApp().mainWindow().buttons()["GET YOURS NOW"].isVisible(), "Get Yours Now button is visible");

// detection fail
target.frontMostApp().mainWindow().buttons()["logo small"].touchAndHold(4);
assertTrue(staticTextExist("UNDETECTED!\nPLEASE TRY AGAIN"), "Undetected message shown");
assertFalse(staticTextExist("PLACE SHINE HERE"), "Place Shine here message is hidden");
assertFalse(target.frontMostApp().mainWindow().buttons()["GET YOURS NOW"].isVisible(), "Get Yours Now button is hidden");

// after fail, get yours now is avaiable again
wait(5);
assertTrue(target.frontMostApp().mainWindow().buttons()["GET YOURS NOW"].isVisible(), "Get Yours Now button is avaiable again after fail detection");

// detection success
target.frontMostApp().mainWindow().buttons()["logo small"].touchAndHold(7.5);
assertTrue(staticTextExist("GO.\nALWAYS WEAR YOUR SHINE."), "Detected message shown");
assertTrue(staticTextExist("To sync just place Shine on the logo below"), "Instruction message shown");
assertFalse(staticTextExist("PLACE SHINE HERE"), "Place Shine here message is hidden");
assertFalse(target.frontMostApp().mainWindow().buttons()["GET YOURS NOW"].isVisible(), "Get Yours Now button is hidden");
assertTrue(target.frontMostApp().mainWindow().buttons()[3].isValid(), "Next button is valid");

// next to home screen
target.frontMostApp().mainWindow().buttons()[3].tap();
wait(3);
assertTrue(target.frontMostApp().mainWindow().buttons()["btn settings"].isVisible(), "Settings button is avaiable");


pass();