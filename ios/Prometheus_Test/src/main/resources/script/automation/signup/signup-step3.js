#import "../../libs/libs.js"
#import "../alerthandler.js"
#import "../views.js"


// test data
// ---------------------------------------------------------------------------------------
var actual = null;
var expect =
	[
		{
	 		title: "ACTIVE", activities: "1-2 activities per week",
	 		points: "2000 points per day or",
	 		steps: "10,000 steps per day"
	 	},
		{
	 		title: "VERY ACTIVE", activities: "2-4 activities per week",
	 		points: "3000 points per day or",
	 		steps: "15,000 steps per day"
	 	},
		{
	 		title: "SUPER ACTIVE", activities: "4+ activities per week",
	 		points: "5000 points per day or",
	 		steps: "30,000 steps per day"
	 	}
	]

// test logic
// ---------------------------------------------------------------------------------------
start();

// navigate
signup.chooseSignUp();
signup.next();
signup.next();
assertTrue(signup.isAtStep3(), "Navigated to the Sign up - step 3");

// check active info
for(var i = 0; i < 3; i++)
{
	actual = signup.getGoalInfo(i);
	assertEqual(actual.title, expect[i].title, "Title for level " + expect[i].title + " is correct");
	assertEqual(actual.activities, expect[i].activities, "Activities number for level " + expect[i].activities + " is correct");
	assertEqual(actual.points, expect[i].points, "Total points for level " + expect[i].points + " is correct");
	assertEqual(actual.steps, expect[i].steps, "Total steps for level " + expect[i].steps + " is correct");
}

// check can be move to step 2 and 4
signup.back();
assertTrue(signup.isAtStep2(), "Back to the Sign up - step 2");
signup.next();
assertTrue(signup.isAtStep3(), "Next to the Sign up - step 3");
signup.next();
assertTrue(signup.isAtStep4(), "Next to the Sign up - step 4");
signup.back();
assertTrue(signup.isAtStep3(), "Back to the Sign up - step 3");
				
pass();