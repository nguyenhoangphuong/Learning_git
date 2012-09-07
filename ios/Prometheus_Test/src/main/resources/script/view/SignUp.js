#import "../core/testcaseBase.js"
#import "_AlertHandler.js"

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
	var email = window.textFields()["email"];
	
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
	
	function fillEmailAndSubmit(email)
	{
		wait();
		window.textFields()["email"].setValue(email);
		app.keyboard().typeString("\n");
	}
	function fillEmail(email)
	{
		wait();
		window.textFields()["email"].setValue(email);		
	}
	
	function getErrorMessage()
	{
		wait();
		
		if (window.staticTexts()[this.MsgEmpty].isValid())
        	return this.MsgEmpty;
	    else if (window.staticTexts()[this.MsgInvalid].isValid()) 
    	    return this.MsgInvalid;
    	else
    		return "";
	}
	
	
	function pressLicenceAgreement() 
	{
		window.buttons()["Licence Agreement"].tap();
	}
	
	function closeLicenceAgreement() 
	{
		window.buttons()["agree"].tap();
	}
	
	function isLicenceAgreementShown() 
	{
		return window.buttons()["agree"].isVisible();
	}
	
	
	function isNoInternetAlertShown()
	{
		return alert.alertTitle != null && alert.alertTitle == alert.NoInternet;
	}
}