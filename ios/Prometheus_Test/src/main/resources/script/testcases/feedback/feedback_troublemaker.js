#import "../../core/testcaseBase.js"
#import "../../view/_Navigator.js"
#import "../../view/Settings.js"

/* This test case verifies that
 * non-troublemakers are not able to JIRA feedback
 */

function sendJIRAFeedback()
{
	log("Begin sending JIRA feedback");
	
	var s = new Settings();
	var dt = new Date();
	
	s.tapFeedback();
	wait(1);
	
	// if there is message sent, there will be Compose button
	var btnCompose = app.navigationBar().buttons()["Compose"];
	
	if (btnCompose.isValid() && btnCompose.isVisible())
	{
		btnCompose.tap();
		wait(1);
	}
	
	// check if Send button tappable
	var btnSend = app.navigationBar().buttons()["Send"];
	
	if (!btnSend.isValid() || !btnSend.isVisible())
	{
		fail("Feedback not sent");
		
		return;
	}
	
	dt = dt.getMonth() +
		"." + dt.getDate() +
		"." + dt.getFullYear() +
		" at " + dt.getHours() +
		":" + dt.getMinutes() +
		":" + dt.getSeconds();
	app.keyboard().typeString("this is an automated feedback generated on " +
								dt);
	app.mainWindow().buttons()["icon record"].tap();
	wait(15);
	btnSend.tap();
	wait(5);
	
	var btnClose = app.navigationBar().buttons()["Close"];
	
	if (btnClose.isValid() && btnClose.isVisible())
	{
		btnClose.tap();
		wait(1);
	}
	
	pass("Feedback sent to JIRA");
}

// log in with troublemaker account
var s = nav.toSettings("tung@misfitwearables.com", "misfit1",
						null, null, null, true);

if (!s.isTroublemaker())
	fail("Settings does not show JIRA feedback to troublemaker");
else
	pass("Settings show JIRA feedback to troublemaker");

sendJIRAFeedback();