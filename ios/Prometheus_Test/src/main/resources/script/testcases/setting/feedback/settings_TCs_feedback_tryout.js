#import "../../core/testcaseBase.js"
#import "../../view/_Navigator.js"
#import "settings_feedback_funcs.js"

/* This test case verifies that when trying out
 * user is not able to JIRA feedback
 */


start("Settings: Feedback")
// -----------------------------------------------------------------------
// try out account
log("3. Tryout account");

log("   3.1. Try out")
nav.toSettings(null, null, null, null, null);

log("   3.2. Check troublemaker is not shown")
verifyTroublemakerNotShown();

log("   3.3. Check sign out button is not visible")
verifySignOutButtonNotShown();

log("   3.4. Check sign up button is visible")
verifySignUpButtonShown();

log("   3.5. Check sign up button behaviour")
verifySigUpBtn();

//-----------------------------------------------------------------------
end();