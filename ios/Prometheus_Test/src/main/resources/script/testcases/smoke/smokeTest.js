#import "../../view/SignUp.js"
#import "../../view/UserInfo.js"
#import "../../view/PlanChooser.js"
#import "../../core/testcaseBase.js"

/**
 * Smoke test
 */




var target = UIATarget.localTarget();

UIALogger.logStart("Smoke test STARTED");
var window = UIATarget.localTarget().frontMostApp().mainWindow();

wait(2);
choosePlan();

UIATarget.onAlert = function onAlert(alert){   
   var name = alert.name();
   UIALogger.logMessage("Alert "+name+" encountered");
   app.alert().defaultButton().tap();
   return true;
}




////check signup
//var signup = new SignUp();
//if (signup.isEmailTextFieldVisible() == 1) {
//	log("Email is visible");
//}
//// view legal
//signup.pressLicenceAgreement();
//log("In licence agreement");
//wait();
//signup.closeLicenceAgreement();
//wait();
//signup.fillEmailAndSubmit("");
//var error = signup.getErrorMessage();
//
//if (error != signup.MsgEmpty) {
//	logFail("Empty email Failed");	
//} else {
//	logPass("Empty email OK");
//}
//
//
//if (signup.isEmailTextFieldVisible() == 1) {
//	UILogger.logFail("Should succeed");
//}


// inputUserInfo();
// choosePlan();
// logTree();
// wait(4);


pass("=== Smoke test END ===");



