#import "../../libs/libs.js"
#import "../alerthandler.js"
#import "../views.js"


// test data
// ---------------------------------------------------------------------------------------


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
wait(6);
assertTrue(signup.hasDontHaveShineButton(), "After 5 seconds [Dont Have A Shine] button appears");

// check detection fail
signup.syncDevice(3);
wait(0.5);
assertTrue(signup.hasDetectionFailMessage(), "Detection failed message is on screen");
wait(2);
assertTrue(signup.hasDetectionFailMessage(), "Detection failed message disappeared after 2 seconds");
assertTrue(signup.hasDontHaveShineButton(), "[Dont Have A Shine] button appears after that");

// check detection pass
signup.syncDevice(7);
wait(0.5);
assertTrue(signup.hasDetectionPassMessage(), "Detection passed message is on screen");

// check navigated to home screen
signup.next();
// -- assert something here

pass();