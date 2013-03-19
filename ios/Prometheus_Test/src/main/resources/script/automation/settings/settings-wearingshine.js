#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"
#import "../views.js"


//test data
//---------------------------------------------------------------------------------------
var actual, expect;
var email = generateSignupAccount();
var password = "qwerty1";
var wear =
	{
		on: ["Head", "Arm", "Chest", "Wrist", "Waist", "Foot"],
		title: [settings.HeadTitle, settings.ArmTitle, settings.ChestTitle, settings.WristTitle, 
		        settings.WaistTitle, settings.FootTitle],
		description: [settings.HeadDescription, settings.ArmDescription, settings.ChestDescription, 
		              settings.WristDescription, settings.WaistDescription, settings.FootDescription]
	}

//test logic
//---------------------------------------------------------------------------------------
start();

// navigate to settings view
log("To Settings view");

signup.chooseSignUp();		
signup.submitSignUpForm(email, password, 4);
signup.next();
signup.setGoal(0);
signup.next();
signup.syncDevice(4);
wait(8);
signup.next();

wait(5);
home.closeTips();
home.tapSettings();


//verify default wearing position
log(" ---- Check default wearing position ----");
actual = settings.getCurrentWearingPosition();
expect = "";

assertEqual(actual, expect, "Default wearing position displays correctly");


// verify each position can be tapped and display right info
settings.tapWearingShine();
assertTrue(settings.isAtWearingShineSettings(), "At Wearing Shine view");

for(var i = 0; i < wear.on.length; i++)
{
	settings.tapWearOn(wear.on[i]);
	
	assertTrue(settings.hasTip(wear.title[i], wear.description[i]), "Tip for [" + wear.on[i] + "] displays correctly");
	
	if(wear.on[i] == "Chest")
		assertTrue(settings.hasBuyNecklaceButton(), "Buy necklace button is visible");
	
	if(wear.on[i] == "Wrist")
		assertTrue(settings.hasBuyWristbandButton(), "Buy wristband button is visible");
	
	settings.closeDescription();
}


// verify wearing position is saved correctly after editting in settings view
log(" ---- Set new wearing position ----");
settings.tapWearOn(wear.on[3]);
settings.back();
wait(2);

log(" ---- Check new wearing position ----");
actual = settings.getCurrentWearingPosition();
expect = wear.on[3];
assertEqual(actual, expect, "New wearing position displays correctly");


// verify wearing position is saved correctly after signing out and signing in again
log(" ---- Signing out ----");
settings.tapSignOut();
wait();

log(" ---- Signing in again ----");
signin.chooseSignIn();
signin.submitSignInForm(email, password, 4);

log(" ---- Check new wearing position ----");
home.tapSettings();
actual = settings.getCurrentWearingPosition();
expect = wear.on[3];
assertEqual(actual, expect, "New wearing position displays correctly");

pass();