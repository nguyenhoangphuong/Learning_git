#import "../../libs/libs.js"
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
signup.submitSignUpForm(email, password, 5);
signup.fillProfileForm(gender, birthday, height, weight);
signup.next();
signup.setGoal(0);
signup.next();
signup.syncDevice(7);
signup.next();
wait(3);

// ----- assert something here

pass();