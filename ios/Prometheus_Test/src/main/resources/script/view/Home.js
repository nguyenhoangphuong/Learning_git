#import "../core/testcaseBase.js"

/*
List of functions:
================================================================================
- isVisible()					: check if current view is Home
- isLoginVisible()				: check if current view is "Login"
- isSignUpVisible()				: check if current view is "Sign up"

- isEmailTextFieldVisible()		: check if email field is visible
- isPasswordTextFieldVisible()	: check if password field is visible
================================================================================
- tapLogin()					: tap Login button
- tapSignUp()					: tap "Sign up" button
- tapTryOut()					: tap "Try out" button
- fillEmail(email)				: fill the email field with <email>
- fillPassword(pwd)				: fill the password field with <pwd>
- submit()						: submit the form
================================================================================
- isEmptyEmailAlertShown()		: checking empty email alert is shown
- isEmptyPasswordAlertShown()	: checking empty password alert is shown
- isInvalidEmailAlertShown()	: checking invalid email alert is shown
- isInvalidUserAlertShown()		: checking user not found alert is shown
- isExistedUserAlertShown()		: checking if "existed user" alert is shown
- isWrongLoginAlertShown()		: checking if wrong password alert is shown
================================================================================
- login(email, pwd)				: shortcut for click "Login", fill form and submit
- signUp(email, pwd)			: shortcut for click "Sign up", fill form and submit
- tryOut()						: shortcut for click "Try out"
================================================================================
*/

function Home()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window;
	
	
	
	var titleLogin = "Login";
	var titleSignup = "Signup";
	var titleTryout = "Tryout"
	
	
	var emailField = mainView.textFields()[0];
	var pwdField = mainView.secureTextFields()[0];
	
	// Methods
	this.isVisible = isVisible;
	this.isLoginVisible = isLoginVisible;
	this.isSignUpVisible = isSignUpVisible;
	this.isTryoutVisible = isTryoutVisible;
	
	this.isEmailTextFieldVisible = isEmailTextFieldVisible;
	this.isPasswordTextFieldVisible = isPasswordTextFieldVisible;
	
	this.tapLogin = tapLogin;
	this.tapSignUp = tapSignUp;
	this.tapTryOut = tapTryOut;
	this.fillEmail = fillEmail;
	this.fillPassword = fillPassword;
	this.submit = submit;
	
	this.isEmptyEmailAlertShown = isEmptyEmailAlertShown;
	this.isEmptyPasswordAlertShown = isEmptyPasswordAlertShown;
	this.isInvalidEmailAlertShown = isInvalidEmailAlertShown;
	this.isInvalidUserAlertShown = isInvalidUserAlertShown;
	this.isExistedUserAlertShown = isExistedUserAlertShown;
	this.isWrongLoginAlertShown = isWrongLoginAlertShown;
	
	this.login = login;
	this.signUp = signUp;
	this.tryOut = tryOut;
	
	// Method definition
	function isVisible()
	{	
		exist = staticTextExist("No data is saved if you just try out");
		
		log("Home visible: " + exist);		
		return exist;
	}
	
	function isLoginVisible() 
	{

		exist = mainView.buttons()[titleLogin].isVisible() && !mainView.buttons()[titleSignup].isVisible() && !mainView.buttons()[titleTryout].isVisible();
				

		log("Login visible: " + exist);
		return exist;
	}
	
	function isSignUpVisible()
	{

		exist = !mainView.buttons()[titleLogin].isVisible() && mainView.buttons()[titleSignup].isVisible() && !mainView.buttons()[titleTryout].isVisible();
				
		log("SignUp visible: " + exist);
		return exist;
	}
	
	
	function isTryoutVisible()
	{
		exist = !mainView.buttons()[titleLogin].isVisible() && !mainView.buttons()[titleSignup].isVisible() && mainView.buttons()[titleTryout].isVisible();
				
		log("Tryout visible: " + exist);
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
	
	
	function tapLogin()
	{
		log("Tap [Login] button");
		
		wait(0.5);
		mainView.buttons()[titleLogin].tap();
		wait(0.5);
	}
	
	function tapSignUp()
	{
		log("Tap [Sign up] button");
		
		wait(0.5);
		mainView.buttons()[titleSignup].tap();
		wait(0.5);
	}
	
	function tapTryOut()
	{
		log("Tap [Try out] button");
		
		wait(0.5);
		mainView.buttons()[titleTryout].tap();
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
		
		if(isPasswordTextFieldVisible())
			pwdField.tap();
		else
			emailField.tap();
		
		app.keyboard().typeString("\n");
		log("Submiting form...");
		
		// wait for alert
		wait();
	}
	
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
	
	function isInvalidUserAlertShown()
	{
		shown = alert.alertTitle != null && 
				alert.alertTitle == alert.Error && 
				alert.alertMsg == alert.InvalidUserMsg;
		
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
	
	function isWrongLoginAlertShown()
	{
		shown = alert.alertTitle != null && 
			alert.alertTitle == alert.Error && 
			alert.alertMsg == alert.WrongLoginMsg;
		log("Checking alert: [" + alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);

		alert.reset();

		return shown;	
	}
	
	
	function login(email, pwd)
	{
		//skipWhatsNew();
		tapLogin();
		fillEmail(email);
		fillPassword(pwd);
		submit();
		
		// wait additional time for sync
		wait(3);	
	}
	
	function signUp(email, pwd)
	{
		//skipWhatsNew();
		tapSignUp();
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
