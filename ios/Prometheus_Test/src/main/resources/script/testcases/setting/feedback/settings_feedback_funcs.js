#import "../../../core/testcaseBase.js"
#import "../../../view/_Navigator.js"

function verifyTroublemakerShown()
{
	s = new Settings();
	assertTrue(s.isTroublemaker(), "Settings JIRA feedback is shown");
	
	/*
	if (s.isTroublemaker())
		fail("Settings shows JIRA feedback to non-troublemaker");
	else
		log("Settings does not show JIRA feedback to non-troublemaker");
	*/
}

function verifyTroublemakerNotShown()
{
	s = new Settings();
	assertTrue(!s.isTroublemaker(), "Settings JIRA feedback is not shown");
	
	/*
	if (!s.isTroublemaker())
		fail("Settings does not show JIRA feedback to troublemaker");
	else
		log("Settings show JIRA feedback to troublemaker");
	*/
}

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
	
	app.keyboard().typeString("this is an automated feedback generated on " + dt);
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
	
	log("Feedback sent to JIRA");
}

function verifySignOutButtonShown()
{
	s = new Settings();
	assertTrue(s.isSignOutBtnVisible(), "Sign out button is shown");
}

function verifySignOutButtonNotShown()
{
	s = new Settings();
	assertTrue(!s.isSignOutBtnVisible(), "Sign out button is not shown");
}

function verifySignUpButtonShown()
{
	s = new Settings();
	assertTrue(s.isSignUpBtnVisible(), "Sign up button is shown");
}

function verifySignUpButtonNotShown()
{
	s = new Settings();
	assertTrue(!s.isSignUpBtnVisible(), "Sign up button is not shown");
}

function verifySignOutBtn()
{
	s = new Settings();
	s.signOut();
	
	h = new Home();
	assertTrue(h.isVisible(), "At Home view after sign out");
}

function verifySignUpBtn()
{
	s = new Settings();
	s.signUp();
	
	h = new Home();
	assertTrue(h.isSignUpVisible(), "At Home->SignUp view after sign up");
}