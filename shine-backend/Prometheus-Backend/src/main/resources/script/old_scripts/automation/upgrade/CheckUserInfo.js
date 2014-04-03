#import "UserInfo.js"
#import "../../core/common.js"

var target = UIATarget.localTarget();

// to userinfo in settings
target.frontMostApp().mainWindow().buttons()["I HAVE AN ACCOUNT"].tap();
target.frontMostApp().mainWindow().textFields()["email"].setValue(email);
target.frontMostApp().mainWindow().secureTextFields()["password"].setValue(password);
target.frontMostApp().keyboard().typeString("\n");
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.delay(5);
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.delay(1);
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.delay(1);
target.frontMostApp().mainWindow().buttons()["logo small"].touchAndHold(7.5);
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.delay(1);
target.frontMostApp().mainWindow().buttons()["btn settings"].tap();
target.delay(1);
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["User info"].tap();

// check
var aname = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Name"].textFields()[0].value();
assertEqual(aname, name, "Name");

/*
var abirthday = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[1].name();
	abirthday = abirthday.substring(abirthday.indexOf(',') + 2);
var aheight = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[3].name();
	aheight = aheight.substring(aheight.indexOf(',') + 2);								
var aweight = target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()[4].name();
	aweight = aweight.substring(aweight.indexOf(',') + 2);

assertEqual(abirthday, birthday, "Birthday");
assertEqual(aheight, height, "Height");
assertEqual(aweight, weight, "Weight");
*/