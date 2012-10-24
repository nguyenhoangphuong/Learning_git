#import "muntigoalsFuncs.js"
#import "../../core/common.js"
/**
This test verifies that user can:
- Choose different activities and goals 
- Units of activities are different
- User can reset the activity to choose another one
*/


function runMultiGoalTest() {

	start("Start a test");
	hr();
	
	goToMultiGoalChooser();
	hr();
	
	checkBackButtonInTheFirstTime();
	hr();
	
	checkCorrectData();
	hr();

	pass("Test done");
}




runMultiGoalTest();