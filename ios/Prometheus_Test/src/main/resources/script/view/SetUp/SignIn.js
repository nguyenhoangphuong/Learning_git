#import "../MVPLibs.js"

/*
List of functions:
================================================================================
- isVisible()					: check if current view is Sign up or sign in
- isSignInVisible()				: check if current view is "SignIn"
- isSignUpVisible()				: check if current view is "Sign up"
================================================================================
- tapSignInTab()				: tap SignIn button
- tapSignUpTab()				: tap "Sign up" button
- tapTryOut()					: tap "Try out" button
- tapLegal()					: tap "Legal" button
- tapCloseLegal()				: tap "Done" button to close Legal
================================================================================
- fillEmail(email)				: fill the email field with <email>
- fillPassword(pwd)				: fill the password field with <pwd>
- submit()						: submit the form
================================================================================
- isInvalidPasswordAlertShown()	: checking empty password alert is shown
- isInvalidEmailAlertShown()	: checking invalid email alert is shown
- isExistedUserAlertShown()		: checking if "existed user" alert is shown
- isWrongSignInAlertShown()		: checking if wrong password alert is shown
================================================================================
- signIn(email, pwd)			: shortcut for click "SignIn", fill form and submit
- signUp(email, pwd)			: shortcut for click "Sign up", fill form and submit
================================================================================
*/

function SignIn()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window;

	var textExist ;
	
	var btnSignUpTab ;
	var btnSignInTab ;
	var btnConfirm ;
	
	var btnTryout ;
//	var btnLegal ;
	
	var emailField ;
	var pwdField ;
	
	// Initalize
	assignControls();
	
	// Methods
	this.isVisible = isVisible;
	this.isSignInVisible = isSignInVisible;
	this.isSignUpVisible = isSignUpVisible;
	
	this.tapSignInTab = tapSignInTab;
	this.tapSignUpTab = tapSignUpTab;
	this.tapTryOut = tapTryOut;
	this.tapLegal = tapLegal;
	this.tapCloseLegal = tapCloseLegal;
	
	this.fillEmail = fillEmail;
	this.fillPassword = fillPassword;
	this.submit = submit;
	
	this.isInvalidEmailAlertShown = isInvalidEmailAlertShown;
	this.isInvalidPasswordAlertShown = isInvalidPasswordAlertShown;
	this.isExistedUserAlertShown = isExistedUserAlertShown;
	this.isWrongSignInAlertShown = isWrongSignInAlertShown;
	
	this.signIn = signIn;
	this.signUp = signUp;
	this.tryOut = tryOut;
	
	//---------------------------------------------
	// Method definition
	function assignControls()
	{
		 textExist = "Please Sign up or Sign in to continue";
	
		 btnSignUpTab = mainView.buttons()[0];
		 btnSignInTab = mainView.buttons()[1];
		 btnConfirm = mainView.buttons()[2];
		 
		 btnTryout = mainView.buttons()["tryout"];
//		 btnLegal = mainView.buttons()["legal"];
		 
		 emailField = mainView.textFields()[0];
		 pwdField = mainView.secureTextFields()[0];
	}
	
	// --------------- Visible method -------------
	function isVisible()
	{
		assignControls();
		return (btnSignInTab.isVisible());
	}
	
	function isSignInVisible() {
		return isVisible() && !isLegalVisible();
	}
	
	
	function isSignUpVisible() {
		return isVisible() && isLegalVisible();
	}
		

	function isLegalVisible() {
		assignControls();
		var legal = window.buttons()["legal"];
		return (legal != null && legal.isEnabled() && legal.isValid() && legal.isVisible());
	}
	
	// ----------------------Method tap action -------------------------
	function tapSignInTab()
	{
		log("Tap [SignIn] button");
		
		wait(0.5);
		btnSignInTab.tap();

	}
	
	function tapSignUpTab()
	{
		log("Tap [Sign up] button");
		
		wait(0.5);
		btnSignUpTab.tap();
	}
	
	function tapTryOut()
	{
		log("Tap [Try out] button");
		
		wait(0.5);
			btnTryout.tap();
		wait(0.5);
	}
	
	function tapLegal()
	{
		log("Tap [Legal] button");
		wait(0.5);
		
		
		var rect = btnConfirm.rect();
		//.mainWindow().buttons()["finish"].tap();
		x = rect.origin.x + rect.size.width / 2;
		y = rect.origin.y;
		target.tap({x:x, y:y + 100});
		
		log ("x= " + x + " - y=" + y);
		
		
		wait(0.5);
	}
	
	function tapCloseLegal()
	{
		log("Tap [CloseLegal] button");
		app.mainWindow().buttons()[0].tap();
		wait(0.5);
	}
	
	
	function fillEmail(email)
	{
		wait(0.5);
		emailField.tap();
		emailField.setValue(email);
		
		log("Fill email: " + email);
	}
	
	function fillPassword(pwd)
	{
		wait(0.5);
		pwdField.tap();
		pwdField.setValue(pwd);		
		log("Fill password: " + pwd);
	}
	
	function submit()
	{
		wait(0.5);
		btnConfirm.tap();
		
		// wait for alert
		wait();
	}
	
	// ---------------------- Check alert --------------------------------------
	function isInvalidEmailAlertShown()
	{
		shown = alert.alertTitle != null && 
				alert.alertTitle == alert.Error &&
				alert.alertMsg == alert.InvalidEmailMsg;
		
		log("Checking alert: [" +
				alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);
		alert.reset();
		
		return shown;
	}
	
	function isInvalidPasswordAlertShown()
	{
		shown = alert.alertTitle != null && 
				alert.alertTitle == alert.Error && 
				alert.alertMsg == alert.InvalidPasswordMsg;
		
		log("Checking alert: [" +
				alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);
		alert.reset();
	
		return shown;
	}
	
	function isExistedUserAlertShown()
	{
		shown = alert.alertTitle != null && 
				alert.alertTitle == alert.Error &&
				alert.alertMsg == alert.ExistedUserMsg;
		
		log("Checking alert: [" +
				alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);
		alert.reset();

		return shown;
	}
	
	function isWrongSignInAlertShown()
	{
		shown = alert.alertTitle != null && 
			alert.alertTitle == alert.Error && 
			alert.alertMsg == alert.WrongLoginMsg;
		log("Checking alert: [" + alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);

		alert.reset();

		return shown;	
	}
	
	
	function signIn(email, pwd)
	{
		tapSignInTab();
		fillEmail(email);
		fillPassword(pwd);
		submit();
	}
	
	function signUp(email, pwd)
	{
		tapSignUpTab();
		fillEmail(email);
		fillPassword(pwd);
		submit();
	}
	
	function tryOut()
	{
		tapTryOut();
	}
	
}
