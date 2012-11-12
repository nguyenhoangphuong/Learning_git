#import "MVPLibs.js"


start("Demo");

ui = new UserInfo();
ui.setUnit("metric");
ui.setUnit("us");
ui.setGender("female");
ui.setGender("male");
ui.setWeight("191", ".5");
ui.setHeight("7'", "12\"");

pass("Demo pass");

target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Units"].buttons()["US"].tap();
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Units"].buttons()["Metric"].tap();
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Gender"].buttons()["Male"].tap();
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Gender"].buttons()["Female"].tap();
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Weight, 73.9 kg"].tap();
target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Height, 1.65 m"].tap();
target.frontMostApp().windows()[1].toolbar().buttons()["Done"].tap();
