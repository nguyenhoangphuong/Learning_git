#import "settings_feedback_funcs.js"

/* This test case verifies that
 * non-troublemakers are not able to JIRA feedback
 */


start("Settings: Feedback")
//-----------------------------------------------------------------------
log("2. Troublemaker account");

// log in with troublemaker account
log("   2.1. Log in")
nav.toSettings("tung@misfitwearables.com", "misfit1", null, null, true);

log("   2.2. Check troublemaker is shown")
verifyTroublemakerShown();

log("   2.3. Check sign out button visible")
verifySignOutButtonShown();

log("   2.4. Check sign up button is not visible")
verifySignUpButtonNotShown();

log("   2.5. Send JIRA feedback");
sendJIRAFeedback();

//-----------------------------------------------------------------------
pass("Settings: Feedback");	