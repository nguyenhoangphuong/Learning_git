#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"
#import "../views.js"


// test data
// ---------------------------------------------------------------------------------------
logTree();

// test logic
// ---------------------------------------------------------------------------------------
start();

// navigate
signup.chooseSignUp();
signup.next();
signup.next();
signup.setGoal(0);
signup.next();
assertTrue(signup.isAtStep4(), "Navigated to the Sign up - step 4");

// check init view
assertTrue(signup.hasDontHaveShineButton() == false, "Init view don't have [Dont Have A Shine] button");

// check view after 5 seconds
wait(8);
assertTrue(signup.hasDontHaveShineButton(), "After 5 seconds [Dont Have A Shine] button appears");

/* ------ this feature in this MVP is disabled
 
// check detection fail
signup.syncDevice(3);
wait(0.5);
assertTrue(signup.hasDetectionFailMessage(), "Detection failed message is on screen");target.dragFromToForDuration({x:156.50, y:223.50}, {x:315.00, y:376.00}, 3.3);

wait(2);
assertTrue(signup.hasDetectionFailMessage(), "Detection failed message disappeared after 2 seconds");
assertTrue(signup.hasDontHaveShineButton(), "[Dont Have A Shine] button appears after that");
 
 ------- */

// check detection pass
signup.syncDevice(4);
wait(8);
assertTrue(signup.hasDetectionPassMessage(), "Detection passed message is on screen");

// check navigated to home screen
signup.next();

// wait for tips to finish animation and close tips
wait(6);
home.closeTips();

// wait for alert (location permission)
wait(3);
assertTrue(alert.title != null && alert.title == alert.AllowLocationMsg, "Location permission alert shown");
assertTrue(home.isAtDailyView(), "Navigated to Home screen");
						  
pass();