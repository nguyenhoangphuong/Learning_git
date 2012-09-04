#import "../../view/SignUp.js"
#import "../../view/UserInfo.js"
#import "../../view/PlanChooser.js"
#import "../../core/testcaseBase.js"

/**
 * Smoke test
 */



UIATarget.onAlert = function onAlert(alert){   
   var name = alert.name();
   UIALogger.logMessage("Alert "+name+" encountered");
   app.alert().defaultButton().tap();
   return true;
}

var target = UIATarget.localTarget();

UIALogger.logStart("Smoke test STARTED");
var window = UIATarget.localTarget().frontMostApp().mainWindow();

// inputUserInfo();
// choosePlan();
// logTree();
// wait(4);


pass("=== Smoke test END ===");


// /-----------------------------------------------

function signup() {
	wait(2);

	var signup = new SignUp();
	if (signup.isEmailTextFieldVisible() == 1) {
		log("Email is visible");
	}
	signup.pressLicenceAgreement();
	log("In licence agreement");
	wait();
	signup.closeLicenceAgreement();
	wait();
	signup.fillEmailAndSubmit("abcd@test.com");

	if (signup.isEmailTextFieldVisible() == 1) {
		UILogger.logFail("Should succeed");
	}
	
	// should be in user info now
} 

function inputUserInfo() {
	
	signup();
	var userinfo = new UserInfo();
	
	userinfo.submit();
	
}


function choosePlan() {
	inputUserInfo();
	log("After user info");
	wait();
	var planChooser = new PlanChooser();
	log("before plan chooser");
	planChooser.selectEasy();
	log("after plan chooser");
}



