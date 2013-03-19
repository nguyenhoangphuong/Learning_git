#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"
#import "../views.js"



// test data
// ---------------------------------------------------------------------------------------
var email = generateSignupAccount();
var password = "qwerty1";
var gender = "Male";
var birthday = [16, 9, 1991];
var height = [1, 70, "cm"];
var weight = [68, 5, "kg"];


// test logic
// ---------------------------------------------------------------------------------------
start();

signup.chooseSignUp();
assertTrue(signup.isAtStep1(), "At Sign up - step 1");

signup.submitSignUpForm(email, password, 5);
assertTrue(signup.isAtStep2(), "At Sign up - step 2");

signup.fillProfileForm(gender, birthday, height, weight);
signup.next();
assertTrue(signup.isAtStep3(), "At Sign up - step 3");

signup.setGoal(0);
signup.next();
assertTrue(signup.isAtStep4(), "At Sign up - step 4");

signup.syncDevice(4);
wait(8);
assertTrue(signup.hasDetectionPassMessage(), "Detection passed message is on screen");

signup.next();
wait(6);
home.closeTips();
wait(3);
assertTrue(alert.title != null && alert.title == alert.AllowLocationMsg, "Location permission alert shown");
assertTrue(home.isAtDailyView(), "Navigated to Home screen");


pass();