#import "../core/testcaseBase.js"

function SignUp()
{
	// Private fields
	var mainView = app.mainWindow();
	var email = mainView.textFields()["email"];
	
	// Constants
	this.MsgEmpty = "Email must not be empty";
	this.MsgInvalid = "The email is invalid";
	
	// Methods
	this.fillEmailAndSubmit = fillEmailAndSubmit;
	this.getErrorMessage = getErrorMessage;
	
	// Method definition
	function fillEmailAndSubmit(email)
	{
		wait();
		app.keyboard().typeString(email + "\n");
	}
	
	function getErrorMessage()
	{
		wait();
		
		if (mainView.staticTexts()[this.MsgEmpty].isValid())
        	return this.MsgEmpty;
	    else if (mainView.staticTexts()[this.MsgInvalid].isValid()) 
    	    return this.MsgInvalid;
    	else
    		return "";
	}
}