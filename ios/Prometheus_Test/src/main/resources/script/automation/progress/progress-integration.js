#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"
#import "../views.js"


//test data
//---------------------------------------------------------------------------------------
var email = generateSignupAccount();
var password = "qwerty1";
var goal = {level: 1, points: 2500};

var records = [];
var total = {steps: 0, duration: 0, miles: 0, points: 0, percent: 0};


//test logic
//---------------------------------------------------------------------------------------
start();

// navigate to progress view
log("To Progress view");

signup.chooseSignUp();		
signup.submitSignUpForm(email, password, 4);
signup.next();
signup.setGoal(goal.level);
signup.next();
signup.syncDevice(4);
wait(8);
signup.next();

wait(5);
home.closeTips();
wait(5);

// assert default values
assertTrue(home.isAtDailyView(), "At Home view - daily mode");
assertTrue(home.hasPlaceShineHere(), "No record, display Place Shine here instead");

// change debug source to input form
home.tapSettings();
settings.setInputFormAsDebugSource();
settings.back();


// input some records
for(var i = 0; i < 5; i++)
{
	// input
	var duration = randInt(5, 10);
	var steps = duration * randInt(100, 180);
	var rec = home.inputRecord(duration, steps); wait();
	
	// assert the record is saved
	var parent = target.frontMostApp().mainWindow().scrollViews()[2];
	assertTrue(parent.staticTexts()[rec.time].isValid(), "Start time displays correctly");
	assertTrue(parent.staticTexts()[rec.duration].isValid(), "Duration displays correctly");
	assertTrue(parent.staticTexts()[rec.steps].isValid(), "Steps displays correctly");
	assertTrue(parent.staticTexts()[rec.points].isValid(), "Points displays correctly");
	
	// assert the summarize info is correct
	total.steps += steps;
	total.duration += duration;
	total.points += point;
	total.percent = parseInt(total.points / goal.points);
	
	var sum = home.getProgressSummary();
	assertEqual(sum.goal, goal.points, "Goal's total points");
	assertEqual(sum.steps, total.steps, "Total steps");
	assertEqual(sum.duration, (total.duration / 60).toFixed(2), "Total duration");
	assertEqual(sum.points, total.points, "Total points");
	assertEqual(sum.percent, toal.percent, "Total percent");
	
	records.push(rec);
}

// sign out and sign in again, check if record still saved
log("Sign out and sign in again");
wait(5);
home.tapSettings();
settings.tapSignOut();
signin.chooseSignIn();
signin.submitSignInForm(email, password, 10);

log("Check if records are saved");
for(var i = 0; i < records.length; i++)
{
	// assert the record is saved
	var parent = target.frontMostApp().mainWindow().scrollViews()[2];
	assertTrue(parent.staticTexts()[records[i].time].isValid(), "Start time saves correctly");
	assertTrue(parent.staticTexts()[records[i].duration].isValid(), "Duration saves correctly");
	assertTrue(parent.staticTexts()[records[i].steps].isValid(), "Steps saves correctly");
	assertTrue(parent.staticTexts()[records[i].points].isValid(), "Points displays correctly");
	
	// assert the summarize info is correct
	total.steps += steps;
	total.duration += duration;
	total.points += point;
	total.percent = parseInt(total.points / goal.points);
}

var sum = home.getProgressSummary();
assertEqual(sum.goal, goal.points, "Goal's total points");
assertEqual(sum.steps, total.steps, "Total steps");
assertEqual(sum.duration, (total.duration / 60).toFixed(2), "Total duration");
assertEqual(sum.points, total.points, "Total points");
assertEqual(sum.percent, toal.percent, "Total percent");

pass();