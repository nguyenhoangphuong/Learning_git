#import "../core/testcaseBase.js"
#import "_Tips.js"

/*
List of functions:
================================================================================
- isVisible()					: check if current view is Sign up or sign in
- isSignInVisible()				: check if current view is "SignIn"
- isSignUpVisible()				: check if current view is "Sign up"
- isEmailTextFieldVisible()		: check if email field is visible
- isPasswordTextFieldVisible()	: check if password field is visible
================================================================================
- tapSignInTab()					: tap SignIn button
- tapSignUpTab()					: tap "Sign up" button
- tapTryOut()					: tap "Try out" button
- tapLegal()					: tap "Legal" button
- fillEmail(email)				: fill the email field with <email>
- fillPassword(pwd)				: fill the password field with <pwd>
- submit()						: submit the form
================================================================================
- isInvalidPasswordAlertShown()	: checking empty password alert is shown
- isInvalidEmailAlertShown()	: checking invalid email alert is shown
- isExistedUserAlertShown()		: checking if "existed user" alert is shown
- isWrongSignInAlertShown()		: checking if wrong password alert is shown
================================================================================
- signIn(email, pwd)				: shortcut for click "SignIn", fill form and submit
- signUp(email, pwd)			: shortcut for click "Sign up", fill form and submit
- tryOut()						: shortcut for click "Try out"
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
	var btnLegal ;
	var emailField ;
	var pwdField ;
	
	assignControls();
	
	// Methods
	this.isVisible = isVisible;
	this.isSignInVisible = isSignInVisible;
	this.isSignUpVisible = isSignUpVisible;
	this.isEmailTextFieldVisible = isEmailTextFieldVisible;
	this.isPasswordTextFieldVisible = isPasswordTextFieldVisible;
	
	this.tapSignInTab = tapSignInTab;
	this.tapSignUpTab = tapSignUpTab;
	this.tapTryOut = tapTryOut;
	this.tapLegal = tapLegal;
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
		 textExist = "Try out first";
	
		 btnSignUpTab = mainView.buttons()[0];
		 btnSignInTab = mainView.buttons()[1];
		 btnConfirm = mainView.buttons()[2];
		 btnTryout = mainView.buttons()[4];
		 btnLegal = mainView.buttons()["Legal"];
		 emailField = mainView.textFields()[0];
		 pwdField = mainView.secureTextFields()[0];
	}
	
	// --------------- Visible method -------------
	function isVisible()
	{
		// TODO	
		exist = staticTextExist(textExist);
		//
		log("Sign in visible: " + exist);		
		return exist;
	}
	
	function isSignInVisible() 
	{
		
		// TODO
		btnSignInTab.isVisible();
		//
		log("SignIn visible: " + exist);
		return exist;
	}
	
	function isSignUpVisible()
	{
		
		// TO DO
		exist = btnSignUpTab.isVisible();
		//		
		log("SignUp visible: " + exist);
		return exist;
	}
		
		
	function isEmailTextFieldVisible()
	{
		exist = emailField.isValid() && emailField.isVisible();
		
		log("EmailField visible: " + exist);		
		return exist;
	}
	
	function isPasswordTextFieldVisible()
	{
		exist = pwdField.isValid() && pwdField.isVisible();
		
		log("PasswordField visible: " + exist);
		
		return exist;
	}
	
	// -----------------------------------------------------------------
	
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
		// TO DO --- Update later
			//btnLegal.tap();
		//
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
		
		// TO DO
		if(isPasswordTextFieldVisible())
			pwdField.tap();
		else
			emailField.tap();	
		app.keyboard().typeString("\n");
		log("Submiting form...");
		//
		// wait for alert
		wait(10);
	}
	
	// -------------------------------------------------------------------------
	// ---------------------- Check alert --------------------------------------
	function isEmptyEmailAlertShown()
	{
		shown = alert.alertTitle != null && 
				alert.alertTitle == alert.Error && 
				alert.alertMsg == alert.EmptyEmailMsg;
		
		log("Checking alert: [" +
				alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);
		alert.reset();
		
		return shown;
	}
	
	function isEmptyPasswordAlertShown()
	{
		shown = alert.alertTitle != null && 
				alert.alertTitle == alert.Error &&
				alert.alertMsg == alert.EmptyPasswordMsg;
		
		log("Checking alert: [" +
				alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);
		alert.reset();
		
		return shown;
	}
	
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
		//skipWhatsNew();
		tapSignInTab();
		fillEmail(email);
		fillPassword(pwd);
		submit();
		// wait additional time for sync
		wait(3);	
	}
	
	function signUp(email, pwd)
	{
		//skipWhatsNew();
		tapSignUpTab();
		fillEmail(email);
		fillPassword(pwd);
		submit();	
		// wait additional time for sync
		wait(3);
	}
	
	function tryOut()
	{
		tapTryOut();
	}
}
