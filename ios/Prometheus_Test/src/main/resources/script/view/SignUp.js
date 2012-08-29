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
	
	this.isEmptyError = isEmptyError;
	this.isInvalidError = isInvalidError;
	
	// Method definition
	function fillEmailAndSubmit(email)
	{
		wait();
		app.keyboard().typeString(email + "\n");
	}
	
	function getErrorMessage()
	{
		wait();
		return app.staticTexts()[0].value();
	}
	
	function isEmptyError()
	{
		wait(2);
    	if (mainView.staticTexts()[this.MsgEmpty].isValid())
        	return true;
	    else
    	    return false;
    }
    
    function isInvalidError()
    {
    	wait();
    	if (mainView.staticTexts()[this.MsgInvalid].isValid()) 
        	return true;
	    else
    	    return false;
    }
}