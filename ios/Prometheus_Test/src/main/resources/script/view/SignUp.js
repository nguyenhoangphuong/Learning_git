#import "../core/testcaseBase.js"

function SignUp()
{
	// Private fields
	var window = app.mainWindow();
	var email = window.textFields()["email"];
	
	// Constants
	this.MsgEmpty = "Email must not be empty";
	this.MsgInvalid = "The email is invalid";
	
	// Methods
	this.isEmailTextFieldVisible = isEmailTextFieldVisible;
	this.fillEmailAndSubmit = fillEmailAndSubmit;
	this.getErrorMessage = getErrorMessage;
	this.pressLicenceAgreement = pressLicenceAgreement;
	this.closeLicenceAgreement = closeLicenceAgreement;
	this.isLicenceAgreementShown = isLicenceAgreementShown;
	
	
	// Method definition
	
	function isEmailTextFieldVisible() {
		return window.textFields()["email"].isVisible();

	}
	function fillEmailAndSubmit(email)
	{
		wait();
		window.textFields()["email"].setValue(email);
		app.keyboard().typeString("\n");
	}
	
	function getErrorMessage() {
		wait();
		
		if (window.staticTexts()[this.MsgEmpty].isValid())
        	return this.MsgEmpty;
	    else if (window.staticTexts()[this.MsgInvalid].isValid()) 
    	    return this.MsgInvalid;
    	else
    		return "";
	}
	
	
	function pressLicenceAgreement() {
		window.buttons()["Licence Agreement"].tap();
	}
	
	function closeLicenceAgreement() {
		window.buttons()["agree"].tap();
	}
	
	function isLicenceAgreementShown() {
		return window.buttons()["agree"].isVisible();
	}
}