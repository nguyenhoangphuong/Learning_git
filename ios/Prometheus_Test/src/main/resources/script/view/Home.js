#import "../core/testcaseBase.js"

/*
List of functions:
=========================================================================================
- isVisible()					:	check if current view is Home
- isLoginVisible()				:	check if current view is LogIn
- isSignupVisible()				:	check if current view is SignUp
- isTryoutVisible()				:	check if current view is TryOut
- isEmailTextFieldVisible()		:	check if email field is visible
- isPasswordTextFieldVisible()	:	check if password field is visible
=========================================================================================
- tapLogin()					:	tap [Login] button
- tapSignup()					:	tap [Sign up] button
- tapTryout()					:	tap [Try out] button
- fillEmail(email)				:	fill the email field with <email>
- fillPassword(pwd)				:	fill the password field with <pwd>
- submit()						:	submit the form
=========================================================================================
- isEmptyEmailAlertShown()		:	<bool> checking empty email alert is shown
- isEmptyPasswordAlertShown()	:	<bool> checking empty password alert is shown
- isInvalidEmailAlertShown()	:	<bool> checking invalid email alert is shown
- isInvalidUserAlertShown()		:	<bool> checking user not found alert is shown
- isExistedUserAlertShown()		:	<bool> checking if user existed when sign up alert shown
=========================================================================================
- login(email, pwd)				:	shortcut for click [Login], fill form, and submit
- signup(email, pwd)			:	shortcut for click [Sign up], fill form, and submit
- tryout(email)					:	shortcut for click [Try out], fill form, and submit
=========================================================================================
*/

function Home()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window;
	
	var loginBtn = mainView.buttons()[0];
	var signupBtn = mainView.buttons()[1];
	var tryoutBtn = mainView.buttons()[2];
	
	var emailField = mainView.textFields()[0];
	var pwdField = mainView.secureTextFields()[0];
	
	// Methods
	this.isVisible = isVisible;
	this.isLoginVisible = isLoginVisible;
	this.isSignupVisible = isSignupVisible;
	this.isTryoutVisible = isTryoutVisible;
	this.isEmailTextFieldVisible = isEmailTextFieldVisible;
	this.isPasswordTextFieldVisible = isPasswordTextFieldVisible;
	
	this.tapLogin = tapLogin;
	this.tapSignup = tapSignup;
	this.tapTryout = tapTryout;
	this.fillEmail = fillEmail;
	this.fillPassword = fillPassword;
	this.submit = submit;
	
	this.isEmptyEmailAlertShown = isEmptyEmailAlertShown;
	this.isEmptyPasswordAlertShown = isEmptyPasswordAlertShown;
	this.isInvalidEmailAlertShown = isInvalidEmailAlertShown;
	this.isInvalidUserAlertShown = isInvalidUserAlertShown;
	this.isExistedUserAlertShown = isExistedUserAlertShown;
	
	this.login = login;
	this.signup = signup;
	this.tryout = tryout;
	
	// Method definition
	function isVisible()
	{
		exist = loginBtn.isVisible() && signupBtn.isVisible() && tryoutBtn.isVisible() &&
				loginBtn.name() == "Login";
		
		log("[" + loginBtn.name() + "] [" + signupBtn.name() + "] [" + tryoutBtn.name() + "]");
		log("Home visible: " + exist);
		return exist;
	}
	
	function isLoginVisible() 
	{
		exist = loginBtn.isVisible() && !signupBtn.isVisible() && !tryoutBtn.isVisible() &&
				loginBtn.name() == "Login";

		log("LogIn visible: " + exist);
		return exist;
	}
	
	function isSignupVisible()
	{
		exist = !loginBtn.isVisible() && signupBtn.isVisible() && !tryoutBtn.isVisible() &&
				signupBtn.name() == "Sign up";

		log("SignUp visible: " + exist);
		return exist;
	}
	
	function isTryoutVisible()
	{
		exist = !loginBtn.isVisible() && !signupBtn.isVisible() && tryoutBtn.isVisible() &&
				tryoutBtn.name() == "Try out";

		log("TryOut visible: " + exist);
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
		loginBtn.tap();
		wait(0.5);
	}
	
	function tapSignup()
	{
		log("Tap [Sign up] button");
		
		wait(0.5);
		signupBtn.tap();
		wait(0.5);
	}
	
	function tapTryout()
	{
		log("Tap [Try out] button");
		
		wait(0.5);
		tryoutBtn.tap();
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
		log("Checking alert: [" + alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);
		
		alert.reset();
		
		return shown;
	}
	
	function isEmptyPasswordAlertShown()
	{
		shown = alert.alertTitle != null && 
			alert.alertTitle == alert.Error &&
			alert.alertMsg == alert.EmptyPasswordMsg;
		log("Checking alert: [" + alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);
		
		alert.reset();
		
		return shown;
	}
	
	function isInvalidEmailAlertShown()
	{
		shown = alert.alertTitle != null && 
			alert.alertTitle == alert.Error && 
			alert.alertMsg == alert.InvalidEmailMsg;
		log("Checking alert: [" + alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);
		
		alert.reset();
		
		return shown;
	}
	
	function isInvalidUserAlertShown()
	{
		shown = alert.alertTitle != null && 
			alert.alertTitle == alert.Error && 
			alert.alertMsg == alert.InvalidUserMsg;
		log("Checking alert: [" + alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);
	
		alert.reset();
	
		return shown;
	}
	
	function isExistedUserAlertShown()
	{
		shown = alert.alertTitle != null && 
			alert.alertTitle == alert.Error && 
			alert.alertMsg == alert.ExistedUserMsg;
		log("Checking alert: [" + alert.alertTitle + "] - [" + alert.alertMsg + "]: " + shown);

		alert.reset();

		return shown;
	}
	
	
	function login(email, pwd)
	{
		tapLogin();
		fillEmail(email);
		fillPassword(pwd);
		submit();
		
		// wait additional time for sync
		wait(3);	
	}
	
	function signup(email, pwd)
	{
		tapSignup();
		fillEmail(email);
		fillPassword(pwd);
		submit();
		
		// wait additional time for sync
		wait(3);
	}
	
	function tryout(email)
	{
		tapTryout();
		fillEmail(email);
		submit();
	}
}