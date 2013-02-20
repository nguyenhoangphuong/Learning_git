#import "../core/common.js"

// generate email and define userinfo
var email = "upgrade-" + generateRandomNumber(100) + "@misfit.mvp6";
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

log("Start the upgrade test");

app.mainWindow().buttons()["I DON'T HAVE AN ACCOUNT"].tap();
app.mainWindow().tableViews()["Empty list"].cells()["Name"].textFields()[0].tap();
app.keyboard().typeString("Mvp6");
app.mainWindow().tableViews()["Empty list"].cells()["Email"].textFields()[0].tap();
app.keyboard().typeString(email);
app.windows()[1].toolbar().buttons()["Done"].tap();

log("Email: " + email);

app.mainWindow().tableViews()["Empty list"].cells()["Password"].secureTextFields()[0].tap();
app.keyboard().typeString("misfit1");
app.windows()[1].toolbar().buttons()["Done"].tap();

app.mainWindow().buttons()[1].tap();
wait(3);
log("now choose level");
// choose level
app.mainWindow().buttons()["btn next"].tap();
wait(3);
log("now choose goal");
// choose goal
app.mainWindow().buttons()["btn next"].tap();

// setup
app.mainWindow().buttons()["logo small"].touchAndHold(13.5);

log("Set name ok");
pass("Test ok");
