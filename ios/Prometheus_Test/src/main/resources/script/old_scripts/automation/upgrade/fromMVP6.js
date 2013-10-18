#import "../../libs/core/common.js"
#import "UserInfo.js"

// vars
var picker = null;
UIATarget.onAlert = function(_alert)
{
	_alert.defaultButton().tap();
	return true;
}

app.mainWindow().buttons()["I DON'T HAVE AN ACCOUNT"].tap();
wait();

log("Input name");
app.mainWindow().tableViews()["Empty list"].cells()["Name"].textFields()[0].tap();
app.keyboard().typeString(name);
app.windows()[1].toolbar().buttons()["Done"].tap();

log("Input Email");
app.mainWindow().tableViews()["Empty list"].cells()["Email"].textFields()[0].tap();
app.keyboard().typeString(email);
app.windows()[1].toolbar().buttons()["Done"].tap();

log("Input Password");
app.mainWindow().tableViews()["Empty list"].cells()["Password"].tap();
app.keyboard().typeString(password);
app.windows()[1].toolbar().buttons()["Done"].tap();
/*
log("Input Birthday");
app.mainWindow().tableViews()["Empty list"].cells()[4].tap();
var picker = app.windows()[1].pickers()[0];
dateWheelPick(picker, year, month, day);
app.windows()[1].toolbar().buttons()["Done"].tap();

log("Input Height");
app.mainWindow().tableViews()["Empty list"].cells()[6].tap();
picker = app.windows()[1].pickers()[0];
wheelPick(picker, 0, h1);
wheelPick(picker, 1, h2);
app.windows()[1].toolbar().buttons()["Done"].tap();

log("Input Weight");
app.mainWindow().tableViews()["Empty list"].cells()[7].tap();
picker = app.windows()[1].pickers()[0];
wheelPick(picker, 0, w1);
wheelPick(picker, 1, w2);
app.windows()[1].toolbar().buttons()["Done"].tap();
*/
app.mainWindow().buttons()["btn next"].tap();
wait(5);

pass();