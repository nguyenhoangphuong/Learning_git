#import "../../core/testcaseBase.js"
#import "../../view/_Navigator.js"
#import "settings_feedback_funcs.js"

/* This test case verifies that
 * non-troublemakers are not able to JIRA feedback
 */

start("Settings: Feedback")
// -----------------------------------------------------------------------
// log in with non-troublemaker account
log("1. Non-troublemaker account");

log("   1.1. Log in")
nav.toSettings("t@t.t", "misfit1", null, null, null, true);

log("   1.2. Check troublemaker is not shown")
verifyTroublemakerNotShown();

log("   1.3. Check sign out button is visible")
verifySignOutButtonShown();

log("   1.4. Check sign up button is not visible")
verifySignUpButtonNotShown();

log("   1.5. Check sign out button behaviour")
verifySigOutBtn();

//-----------------------------------------------------------------------
end();