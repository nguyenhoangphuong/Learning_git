#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"
#import "../views.js"


//test data
//---------------------------------------------------------------------------------------
var expect, actual;
var email = "mfwcqa@gmail.com";
var password = "misfitqc";

var goal = {level: 1, points: 2500};
var records = [];
var total = {steps: 0, duration: 0, miles: 0, points: 0, percent: 0};


//test logic
//---------------------------------------------------------------------------------------
start();

// navigate to progress view
log("To Progress view");

signin.chooseFacebookSignIn();
wait(10);
home.closeTips();
wait(10);

// assert default values
assertTrue(home.isAtDailyView(), "At Home view - daily mode");

// check swiping ok
expect = "TODAY";

for(var i = 0; i < 4; i++)
{
	home.swipeRight();
	actual = home.getCurrentDay();
	
	assertTrue(actual != expect, "Swipe right ok");
	expect = actual;
}

wait();
for(var i = 0; i < 4; i++)
	home.swipeLeft();

assertTrue(home.getCurrentDay() == "TODAY", "Swipe left ok");

// change debug source to input form
home.tapSettings();
settings.setInputFormAsDebugSource();
settings.back();


// input some records
for(var i = 0; i < 3; i++)
{
	// input
	var duration = randInt(5, 10);
	var steps = duration * randInt(100, 180);
	var rec = home.inputRecord(duration, steps); 
	wait(5);
	
	// assert the record is saved
	var parent = target.frontMostApp().mainWindow().scrollViews()[2];
	assertTrue(parent.staticTexts()[rec.time].isValid(), "Start time displays correctly");
	assertTrue(parent.staticTexts()[rec.duration].isValid(), "Duration displays correctly");
	assertTrue(parent.staticTexts()[rec.steps].isValid(), "Steps displays correctly");
	assertTrue(parent.staticTexts()[rec.points].isValid(), "Points displays correctly");
	
	// assert the summarize info is correct
	total.steps += steps;
	total.duration += duration;
	total.points += parseInt(rec.points);
	total.percent = (100 * total.points / goal.points).toFixed(0);
	
	var sum = home.getProgressSummary();
	assertEqual(sum.goal, goal.points, "Goal's total points");
	assertEqual(sum.steps, total.steps, "Total steps");
	assertEqual(sum.duration, (total.duration / 60.0).toFixed(2), "Total duration");
	assertEqual(sum.points, total.points, "Total points");
	assertEqual(sum.percent, total.percent, "Total percent");
	
	records.push(rec);
}

// sign out and sign in again, check if record still saved
log("Sign out and sign in again");
wait(10);
home.tapSettings();
settings.tapSignOut();
wait();
signin.chooseFacebookSignIn();
wait(20);

log("Check if records are saved");
for(var i = 0; i < records.length; i++)
{
	// assert the record is saved
	var parent = target.frontMostApp().mainWindow().scrollViews()[2];
	assertTrue(parent.staticTexts()[records[i].time].isValid(), "Start time saves correctly");
	assertTrue(parent.staticTexts()[records[i].duration].isValid(), "Duration saves correctly");
	assertTrue(parent.staticTexts()[records[i].steps].isValid(), "Steps saves correctly");
	assertTrue(parent.staticTexts()[records[i].points].isValid(), "Points displays correctly");
}

var sum = home.getProgressSummary();
assertEqual(sum.goal, goal.points, "Goal's total points");
assertEqual(sum.steps, total.steps, "Total steps");
assertEqual(sum.duration, (total.duration / 60.0).toFixed(2), "Total duration");
assertEqual(sum.points, total.points, "Total points");
assertEqual(sum.percent, total.percent, "Total percent");


pass();