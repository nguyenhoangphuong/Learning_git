#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"
#import "../views.js"


//test data
//---------------------------------------------------------------------------------------
var actual, expect;
var email = generateSignupAccount();
var password = "qwerty1";

var profile1 = 
{
	sex: "Male",
	birthday: [16, 9, 1991],
	height: [1, 70, "m"],
	weight: [68, 5, "kg"]
};

var profile2 = 
{
	sex: "Female",
	birthday: [18, 5, 1991],
	height: [5, 0, "ft in"],
	weight: [97, 0, "lbs"]
};


//test logic
//---------------------------------------------------------------------------------------
start();

// navigate to settings view
log("To Settings view");

signup.chooseSignUp();		
signup.submitSignUpForm(email, password, 4);
signup.fillProfileForm(profile1.sex, profile1.birthday, profile1.height, profile1.weight);
signup.next();
signup.setGoal(0);
signup.next();
signup.syncDevice(4);
wait(8);
signup.next();

wait(5);
home.closeTips();
home.tapSettings();
assertTrue(settings.isAtOverviewSettings(), "At Settings view");


// verify profile is saved correctly after signing up
log(" ---- Check overview profile ----");
actual = settings.getProfileOverviewInfo();
expect = helper.formatProfileOverviewInfo(profile1.sex, 
		profile1.height[0], profile1.height[1],
		profile1.weight[0], profile1.weight[1], false);

assertEqual(actual, expect, "Overview profile displays correctly");

log(" ---- Check detail profile ----");
settings.tapProfile();
assertTrue(settings.isAtProfileSettings(), "At Profile settings view");

actual = settings.getProfileForm();
expect = profile1;

assertEqual(actual.email, email, "Email displays correctly");
assertEqual(actual.birthday, helper.formatBirthday(profile1.birthday), "Birthday displays correctly");
assertEqual(actual.height, helper.formatHeight(profile1.height[0], profile1.height[1], false), "Height displays correctly");
assertEqual(actual.weight, helper.formatWeight(profile1.weight[0], profile1.weight[1], false), "Weight displays correctly");


// verify profile is saved correctly after editting in settings view
log(" ---- Input new profile ----");
settings.fillProfileForm(profile2.sex, profile2.birthday, profile2.height, profile2.weight);
settings.back(); wait();

log(" ---- Check overview profile ----");
actual = settings.getProfileOverviewInfo();
expect = helper.formatProfileOverviewInfo(profile2.sex, 
		profile2.height[0], profile2.height[1],
		profile2.weight[0], profile2.weight[1], true);

assertEqual(actual, expect, "Overview profile displays correctly");

log(" ---- Check detail profile ----");
settings.tapProfile();
actual = settings.getProfileForm();
expect = profile2;

assertEqual(actual.email, email, "Email displays correctly");
assertEqual(actual.birthday, helper.formatBirthday(profile2.birthday), "Birthday displays correctly");
assertEqual(actual.height, helper.formatHeight(profile2.height[0], profile2.height[1], true), "Height displays correctly");
assertEqual(actual.weight, helper.formatWeight(profile2.weight[0], profile2.weight[1], true), "Weight displays correctly");


// verify profile is saved correctly after signing out and signing in again
log(" ---- Signing out ----");
settings.back();
settings.tapSignOut();
wait();

assertTrue(signup.isAtInitView(), "Afer log out, app lead users to init view");

log(" ---- Signing in again ----");
signin.chooseSignIn();
signin.submitSignInForm(email, password, 4);

log(" ---- Check overview profile ----");
home.tapSettings();
actual = settings.getProfileOverviewInfo();
expect = helper.formatProfileOverviewInfo(profile2.sex, 
		profile2.height[0], profile2.height[1],
		profile2.weight[0], profile2.weight[1], false);

assertEqual(actual, expect, "Overview profile displays correctly");

log(" ---- Check detail profile ----");
settings.tapProfile();
actual = settings.getProfileForm();
expect = profile1;

assertEqual(actual.email, email, "Email displays correctly");
assertEqual(actual.birthday, helper.formatBirthday(profile2.birthday), "Birthday displays correctly");
assertEqual(actual.height, helper.formatHeight(profile2.height[0], profile2.height[1], true), "Height displays correctly");
assertEqual(actual.weight, helper.formatWeight(profile2.weight[0], profile2.weight[1], true), "Weight displays correctly");


pass();