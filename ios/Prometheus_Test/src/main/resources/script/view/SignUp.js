#import "../core/testcaseBase.js"
#import "_AlertHandler.js"
#import "_Tips.js"


/*
SignUp function
- isEmailTextFieldVisible
- fillEmailAndSubmit(email)
	+ fillEmailAndSubmit("") : enter null email
	+ fillEmailAndSubmit("test@yahoo.com") : enter valid email
- getErrorMessage()
	+ errMsg = getErrorMessage()
	+ errMsg == signup.MsgEmpty
	+ errMsg == signup.MsgInvalid
*/

function SignUp()
{
	// Private fields
	var window = app.mainWindow();
	var email = window.textFields()[0];
	
	// Constants
	this.MsgEmpty = "Email must not be empty";
	this.MsgInvalid = "The email is invalid";
	
	// Methods
	this.isVisible = isVisible;
	this.isEmailTextFieldVisible = isEmailTextFieldVisible;
	this.fillEmailAndSubmit = fillEmailAndSubmit;
	this.getErrorMessage = getErrorMessage;
	
	this.pressLicenceAgreement = pressLicenceAgreement;
	this.closeLicenceAgreement = closeLicenceAgreement;
	this.isLicenceAgreementShown = isLicenceAgreementShown;
	
	this.isNoInternetAlertShown = isNoInternetAlertShown;
	this.fillEmail = fillEmail;
	
	// Method definition
	function isVisible()
	{
		return isEmailTextFieldVisible();
	}
	
	function isEmailTextFieldVisible() 
	{
		wait();
		return email.isVisible();
	}
	
	function fillEmailAndSubmit(_email)
	{
		wait();
		email.setValue(_email);
		app.keyboard().typeString("\n");
		wait(2);
		
		if(!isEmailTextFieldVisible())
			tips.closeTips(4);
	}
	
	function fillEmail(_email)
	{
		wait();
		email.setValue(_email);		
	}
	
	function getErrorMessage()
	{
		wait();
		msg = window.staticTexts()[1].name();
		return msg;
	}
	
	
	function pressLicenceAgreement() 
	{
		wait();
		window.buttons()[0].tap();
	}
	
	function closeLicenceAgreement() 
	{
		wait();
		window.buttons()[0].tap();
	}
	
	function isLicenceAgreementShown() 
	{
		wait();	
		return window.buttons()[0].isVisible();
	}
	
	
	function isNoInternetAlertShown()
	{
		return alert.alertTitle != null && alert.alertTitle == alert.NoInternet;
	}
}