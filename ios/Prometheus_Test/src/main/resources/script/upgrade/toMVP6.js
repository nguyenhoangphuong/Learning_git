#import "../core/common.js"

var email = "5@1.1.1";
var password = "qwerty1";

// log in
app.mainWindow().buttons()["I HAVE AN ACCOUNT"].tap();
wait(0.5);
app.mainWindow().textFields()["email"].tap();
app.keyboard().typeString(email);
app.mainWindow().secureTextFields()["password"].tap();
app.keyboard().typeString(password + "\n");
app.mainWindow().buttons()["btn next"].tap();
wait(5);

// upgrade process
app.mainWindow().buttons()["btn next"].tap(); // to step 3
app.mainWindow().buttons()["btn next"].tap(); // to step 4
app.mainWindow().buttons()["logo small"].touchAndHold(8); // register device
app.mainWindow().buttons()["btn next"].tap();

// to settings > userinfo
app.mainWindow().buttons()["btn settings"].tap();
app.mainWindow().tableViews()["Empty list"].cells()["User info"].tap();

// verify
var cells = app.mainWindow().tableViews()["Empty list"].cells(); 
var actualName = cells["Name"].textFields()[0].value();
var actualBirthday = cells[1].value();
var actualSex = (cells["Sex"].buttons()["MALE"].value() == 1 ? "Male" : "Female");
var actualHeight = cells[3].value();
var actualWeight = cells[4].textFields()[0].value();

log(actualName);
log(actualBirthday);
log(actualSex);
log(actualHeight);
log(actualWeight);
