#import "../core/common.js"

// generate email and define userinfo
var email = "upgrade-" + generateRandomDigitString() + "@misfit.mvp3";
var password = "qwerty1";
var w1 = "132", w2 = ".3";
var h1 = "5'", h2 = '6"';
var birthyear = 1991;
var birthmonth = "September";
var birthdate = 16;

// vars
var picker = null;
UIATarget.onAlert = function(_alert)
{
	_alert.defaultButton().tap();
	return false;
}

// skip whats new
app.mainWindow().buttons()["Skip"].tap();
wait();

// sign up
app.mainWindow().textFields()["email"].tap();
app.keyboard().typeString(email);
app.mainWindow().secureTextFields()["password"].tap();
app.keyboard().typeString(password);
app.mainWindow().buttons()["finish"].tap();
app.mainWindow().buttons()["finish"].waitForInvalid();
wait();

// input userinfo
// - weight
app.mainWindow().tableViews()["Empty list"].cells()[2].tap();
wait();
picker = app.windows()[1].pickers()[0];
wheelPick(picker, 0, w1);
wheelPick(picker, 1, w2);
app.windows()[1].toolbar().buttons()["Done"].tap();
wait();
// - height
app.mainWindow().tableViews()["Empty list"].cells()[3].tap();
wait();
picker = app.windows()[1].pickers()[0];
wheelPick(picker, 0, h1);
wheelPick(picker, 1, h2);
app.windows()[1].toolbar().buttons()["Done"].tap();
wait();
// - birthday
app.mainWindow().tableViews()["Empty list"].cells()[4].tap();
wait();
picker = app.windows()[1].pickers()[0];
dateWheelPick(picker, birthyear, birthmonth, birthdate);
app.windows()[1].toolbar().buttons()["Done"].tap();
app.mainWindow().buttons()["next"].tap();
wait();

// pick plan
app.mainWindow().scrollViews()[0].tableViews()[0].cells()["Normal"].tap();
app.mainWindow().scrollViews()[0].tableViews()[0].cells()["The Mover's Plan"].tap();
app.mainWindow().buttons()["Go for it"].tap();
wait(2);

// go to setting and set name
app.tabBar().buttons()["Settings"].tap();
app.mainWindow().tableViews()["Empty list"].cells()["Name"].tap();
app.keyboard().typeString("Tears");
app.windows()[1].toolbar().buttons()["Done"].tap();
app.tabBar().buttons()["Progress"].tap();
wait();
