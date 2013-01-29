/*
 Cover:
 Settings:					2894
 Settings > Adjust goal:	2923, 2924, 2925
 Settings > Wearing Shine:	2900, 2926
 Settings > Profile:		2921, 2896
 */

#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"


// verify navigation
// -------------------------------------------------------------------------------------------------------
start();

log("To Progress view");
target.frontMostApp().mainWindow().buttons()["I DON'T HAVE AN ACCOUNT"].tap();
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
target.frontMostApp().mainWindow().buttons()["logo small"].touchAndHold(7);
target.frontMostApp().mainWindow().buttons()["btn next"].tap();
// -------------------------------------------------------------------------------------------------------
log("Progress > Settings");
target.frontMostApp().mainWindow().buttons()["btn settings"].tap();
wait();
assertTrue(staticTextExist("SETTINGS"), "Navigated to Settings");
// -------------------------------------------------------------------------------------------------------
log("Settings > User Profile");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Your profile"].tap();
wait(2);
assertTrue(staticTextExist("YOUR PROFILE"), "Navigated to User Profile");
// -------------------------------------------------------------------------------------------------------
log("User Profile > Settings");
target.frontMostApp().mainWindow().buttons()[0].tap();
wait();
assertTrue(staticTextExist("SETTINGS"), "Navigated to Settings");
// -------------------------------------------------------------------------------------------------------
log("Settings > Adjust your goal");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Adjust your goal"].tap();
wait();
assertTrue(staticTextExist("SET YOUR ACTIVITY GOAL"), "Cancel and Back to Settings");
// -------------------------------------------------------------------------------------------------------
log("Adjust your goal > Cancel > Settings");
target.frontMostApp().mainWindow().buttons()[4].tap();
wait();
assertTrue(staticTextExist("SETTINGS"), "Navigated to Settings");
// -------------------------------------------------------------------------------------------------------
log("Settings > Adjust your goal");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Adjust your goal"].tap();
wait();
assertTrue(staticTextExist("SET YOUR ACTIVITY GOAL"), "Cancel and Back to Settings");
// -------------------------------------------------------------------------------------------------------
log("Adjust your goal > Confirm > Settings");
target.frontMostApp().mainWindow().buttons()["btn check"].tap();
wait();
assertTrue(staticTextExist("SETTINGS"), "Navigated to Settings");
// -------------------------------------------------------------------------------------------------------
log("Settings > Wearing Shine");
target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Wearing Shine"].tap();
wait();
assertTrue(staticTextExist("WHERE WILL YOU WEAR YOUR SHINE?"), "Navigated to Wearing Shine");
// -------------------------------------------------------------------------------------------------------
log("Wearing Shine > Settings");
target.frontMostApp().mainWindow().buttons()[0].tap();
wait();
assertTrue(staticTextExist("SETTINGS"), "Navigated to Settings");
// -------------------------------------------------------------------------------------------------------
log("Settings > Progress");
target.frontMostApp().mainWindow().buttons()[0].tap();
wait();
assertTrue(staticTextExist("LEVEL 1"), "Level 1 label existed");


pass();