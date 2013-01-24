var target = UIATarget.localTarget();

target.frontMostApp().mainWindow().buttons()["I HAVE AN ACCOUNT"].tap();
target.frontMostApp().mainWindow().textFields()["email"].setValue("upgrade-26@misfit.mvp5");
target.frontMostApp().mainWindow().secureTextFields()["password"].setValue("qwerty1");
target.frontMostApp().keyboard().typeString("\n");
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.delay(5);
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.delay(5);
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.delay(5);
target.frontMostApp().mainWindow().buttons()["logo small"].touchAndHold(8.9);
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.frontMostApp().mainWindow().buttons()["btn settings"].tap();
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["User info"].tap();