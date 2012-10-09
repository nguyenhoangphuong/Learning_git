#import "../../core/testcaseBase.js"
#import "../../view/_Navigator.js"
#import "../../view/Settings.js"

/* This test case verifies that
 * non-troublemakers are not able to JIRA feedback
 */

// log in with non-troublemaker account
var s = nav.toSettings("t@t.t", "misfit1", null, null, null, true);

if (s.isTroublemaker())
	fail("Settings shows JIRA feedback to non-troublemaker");
else
	pass("Settings does not show JIRA feedback to non-troublemaker");