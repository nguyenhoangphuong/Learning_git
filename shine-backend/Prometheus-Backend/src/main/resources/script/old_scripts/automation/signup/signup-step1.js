#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"
#import "../views.js"


// test data
// ---------------------------------------------------------------------------------------
var duplicatedEmail = "qa@a.a";
var invalidEmail = "invalidEmail";
var invalidPassword = "invalidPassword";
var validPassword = "qwerty1";


// test logic
// ---------------------------------------------------------------------------------------
start();

// navigate
signup.chooseSignUp();
assertTrue(signup.isAtStep1(), "Navigated the the Sign up - step 1");

// invalid email
signup.submitSignUpForm(invalidEmail, validPassword);
assertTrue(signup.isInvalidEmailAlertShown(), "Invalid email alert shown");

// invalid password
signup.submitSignUpForm(generateSignupAccount(), invalidPassword);
assertTrue(signup.isInvalidPasswordAlertShown(), "Invalid password alert shown");

// duplicated email
signup.submitSignUpForm(duplicatedEmail, validPassword, 5);
assertTrue(signup.isDuplicatedEmailAlertShown(), "Duplicated email alert shown");

// sign up successfully
signup.submitSignUpForm(generateSignupAccount(), validPassword, 5);
assertTrue(signup.isAtStep2(), "Register successfully, is at step 2");

pass();