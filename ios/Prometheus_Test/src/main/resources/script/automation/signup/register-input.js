/*
 Cover:
 Signup > Register: 	2803, 2804 
 */

#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"

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

start();

// INTERACTION
// -------------------------------------------------------------------------------------------------------
log("INTERACTION"); hr();
target.frontMostApp().mainWindow().buttons()["I DON'T HAVE AN ACCOUNT"].tap();
wait();

// get sliders' range
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Units"].buttons()["METRIC"].tap();
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[6].tap();
picker = target.frontMostApp().windows()[1].pickers()[0];
var metricHRange1 = getWheelRange(picker, 0);
var metricHRange2 = getWheelRange(picker, 1);
target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[7].tap();
picker = target.frontMostApp().windows()[1].pickers()[0];
var metricWRange1 = getWheelRange(picker, 0);
var metricWRange2 = getWheelRange(picker, 1);
target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Units"].buttons()["US"].tap();
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[6].tap();
picker = target.frontMostApp().windows()[1].pickers()[0];
var usHRange1 = getWheelRange(picker, 0);
var usHRange2 = getWheelRange(picker, 1);
target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[7].tap();
picker = target.frontMostApp().windows()[1].pickers()[0];
var usWRange1 = getWheelRange(picker, 0);
var usWRange2 = getWheelRange(picker, 1);
target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();

// input name, email, password
fillRegisterForm(name, email, password, sex, year, day, month, unit, w1, w2, h1, h2)


// VERIFY
// -------------------------------------------------------------------------------------------------------
log("VERIFY"); hr();

var aunit = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Units"].buttons().firstWithPredicate("value == 1").name();
var aname = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Name"].textFields()[0].value();
var aemail = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Email"].textFields()[0].value();
var apassword = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Password"].secureTextFields()[0].value();
var abirthday = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[4].name();
	abirthday = abirthday.substring(abirthday.indexOf(',') + 2);
var aheight = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[6].name();
	aheight = aheight.substring(aheight.indexOf(',') + 2);								
var aweight = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[7].name();
	aweight = aweight.substring(aweight.indexOf(',') + 2);


log("Verify pickers");
assertEqual(metricHRange1.min, "0");
assertEqual(metricHRange1.max, "2");
assertEqual(metricHRange2.min, ".00");
assertEqual(metricHRange2.max, ".99");

assertEqual(metricWRange1.min, "10");
assertEqual(metricWRange1.max, "634");
assertEqual(metricWRange2.min, ".0");
assertEqual(metricWRange2.max, ".9");

assertEqual(usHRange1.min, "1'");
assertEqual(usHRange1.max, "8'");
assertEqual(usHRange2.min, '0"');
assertEqual(usHRange2.max, '11"');

assertEqual(usWRange1.min, "22");
assertEqual(usWRange1.max, "1399");
assertEqual(usWRange2.min, ".0");
assertEqual(usWRange2.max, ".9");


log("Verify input values");
assertEqual(aunit, unit, "Unit");
assertEqual(aname, name, "Name");
assertEqual(aemail, email, "Email");
assertEqual(apassword.length, password.length, "Password length");
assertEqual(abirthday, birthday, "Birthday");
assertEqual(aheight, height, "Height");
assertEqual(aweight, weight, "Weight");

pass();