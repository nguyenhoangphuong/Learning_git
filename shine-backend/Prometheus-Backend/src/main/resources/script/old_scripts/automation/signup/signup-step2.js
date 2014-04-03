#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"
#import "../views.js"


// test data
// ---------------------------------------------------------------------------------------
var birthday = [16, 9, 1991];
var heightM = [1, 70, "m"];
var heightU = [5, 6, "ft in"];
var weightM = [68, 5, "kg"];
var weightU = [151, 0, "lbs"];

var usUnit = [null, null, "ft in"];
var metricUnit = [null, null, "m"];
var actual = null;

// test logic
// ---------------------------------------------------------------------------------------
start();

// navigate
signup.chooseSignUp();
signup.next();
assertTrue(signup.isAtStep2(), "Navigated to the Sign up - step 2");

// check change unit
signup.fillProfileForm(null, birthday, heightM, weightM);

signup.fillProfileForm(null, null, usUnit, null);
actual = signup.getProfileForm();
assertEqual(actual.birthday, helper.formatBirthday(birthday), "Birthday displays correctly");
assertEqual(actual.height, helper.formatHeight(heightU[0], heightU[1], true), "Height displays correctly");
assertEqual(actual.weight, helper.formatWeight(weightU[0], weightU[1], true), "Weight displays correctly");

signup.fillProfileForm(null, null, metricUnit, null);
actual = signup.getProfileForm();
assertEqual(actual.birthday, helper.formatBirthday(birthday), "Birthday displays correctly");
assertEqual(actual.height, helper.formatHeight(heightM[0], heightM[1], false), "Height displays correctly");
assertEqual(actual.weight, helper.formatWeight(weightM[0], weightM[1], false), "Weight displays correctly");

// check profile is saved when back from step 3
signup.next();
assertTrue(signup.isAtStep3(), "Navigated to the Sign up - step 3");
signup.back();
assertTrue(signup.isAtStep2(), "Navigated to the Sign up - step 2");

actual = signup.getProfileForm();
assertEqual(actual.birthday, helper.formatBirthday(birthday), "Birthday saves correctly");
assertEqual(actual.height, helper.formatHeight(heightM[0], heightM[1], false), "Height saves correctly");
assertEqual(actual.weight, helper.formatWeight(weightM[0], weightM[1], false), "Weight saves correctly");

pass();
