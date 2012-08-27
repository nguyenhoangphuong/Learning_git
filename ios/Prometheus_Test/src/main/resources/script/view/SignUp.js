#import "../testcaseBase.js"


function SignUp(var target)
{
	// Target
	this.Target = target;
	this.App = target.frontMostApp();

	// Fields
	this.MainView = App.mainWindow();
	this.Email = MainView.textFields()["email"];
	
	this.ErrNull = "Email must not be empty";
	this.ErrInvalid = "The email is invalid";
	
	// Methods
	this.FillEmailAndSubmit = FillEmailAndSubmit;
	this.GetErrorMessage = GetErrorMessage;
	
	// Method definition
	function FillEmailAndSubmit(String email)
	{
		wait();
		TypeText(Email, email + "\n");
		alert("fill");
	}
	
	function GetErrorMessage()
	{
		wait(0.2);
		return App.staticTexts()[0];
	}
}