#import "../../core/testcaseBase.js"
#import "../../view/_Navigator.js"
#import "../../view/Settings.js"

/* This test case verifies that when trying out
 * user is not able to JIRA feedback
 */

// try out
var s = nav.toSettings();

if (s.isTroublemaker())
	fail("Settings shows JIRA feedback when trying out");
else
	pass("Settings does not show JIRA feedback when trying out");