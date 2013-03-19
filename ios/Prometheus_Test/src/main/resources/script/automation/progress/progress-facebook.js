#import "../../libs/libs.js"
#import "../helpers.js"
#import "../alerthandler.js"
#import "../views.js"


//test data
//---------------------------------------------------------------------------------------
var expect, actual;
var email = "mfwcqa@gmail.com";
var password = "misfitqc";

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


pass();