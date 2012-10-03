#import "../core/testcaseBase.js"

/*
List of functions:
================================================================================
- isVisible()					: check if current view is Home
- assignControls()				: assign buttons, textboxes, ...
- isWhatsNewVisible()			: check if current view is "What's new"
- isLoginVisible()				: check if current view is Login
- isSignUpVisible()				: check if current view is "Sign up"
- isTryOutVisible()				: check if current view is "Try out"
- isEmailTextFieldVisible()		: check if email field is visible
- isPasswordTextFieldVisible()	: check if password field is visible
- isSkipButtonVisible()			: check if Skip button (when trying out) is
									visible
================================================================================
- skipWhatsNew()				: skip "What's New"
- tapLogin()					: tap Login button
- tapSignUp()					: tap "Sign up" button
- tapTryOut()					: tap "Try out" button
- tapSkip()						: tap Skip button (when trying out)
- fillEmail(email)				: fill the email field with <email>
- fillPassword(pwd)				: fill the password field with <pwd>
- submit()						: submit the form
================================================================================
- isEmptyEmailAlertShown()		: checking empty email alert is shown
- isEmptyPasswordAlertShown()	: checking empty password alert is shown
- isInvalidEmailAlertShown()	: checking invalid email alert is shown
- isInvalidUserAlertShown()		: checking user not found alert is shown
- isExistedUserAlertShown()		: checking if "existed user" alert is shown
================================================================================
- login(email, pwd)				: shortcut for click Login, fill form and submit
- signUp(email, pwd)			: shortcut for click "Sign up", fill form and
									submit
- tryOut()						: shortcut for click "Try out"
================================================================================
*/

function Home()
{
	// Private fields
	var window = app.mainWindow();
	var mainView = window;
	
	var loginBtn = mainView.buttons()[0];
	var signUpBtn = mainView.buttons()[1];
	var tryOutBtn = mainView.buttons()[2];
	
	var emailField = mainView.textFields()[0];
	var pwdField = mainView.secureTextFields()[0];
	
	// Methods
	this.isVisible = isVisible;
	this.assignControls = assignControls;
	this.isWhatsNewVisible = isWhatsNewVisible;
	this.isLoginVisible = isLoginVisible;
	this.isSignUpVisible = isSignUpVisible;
	this.isTryOutVisible = isTryOutVisible;
	this.isEmailTextFieldVisible = isEmailTextFieldVisible;
	this.isPasswordTextFieldVisible = isPasswordTextFieldVisible;
	this.isSkipButtonVisible = isSkipButtonVisible;
	
	this.skipWhatsNew = skipWhatsNew;
	this.tapLogin = tapLogin;
	this.tapSignUp = tapSignUp;
	this.tapTryOut = tapTryOut;
	this.tapSkip = tapSkip;
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
		exist = loginBtn.isVisible() &&
				signUpBtn.isVisible() &&
				tryOutBtn.isVisible() &&
				loginBtn.name() == "Login";
		
		log("[" + loginBtn.name() +
				"] [" + signUpBtn.name() +
				"] [" + tryOutBtn.name() + "]");
		log("Home visible: " + exist);
		
		return exist;
	}
	
	function assignControls()
	{
		window = app.mainWindow();
		mainView = window;
		
		loginBtn = mainView.buttons()[0];
		signUpBtn = mainView.buttons()[1];
		tryOutBtn = mainView.buttons()[2];
		
		emailField = mainView.textFields()[0];
		pwdField = mainView.secureTextFields()[0];	
	}
	
	function isWhatsNewVisible()
	{
		return mainView.buttons().length == 1 && // there is only 1 button
				mainView.buttons()["Skip"].isValid() &&
				mainView.buttons()["Skip"].isVisible(); // the button is Skip
	}
	
	function isLoginVisible() 
	{
		exist = loginBtn.isVisible() &&
				!signUpBtn.isVisible() &&
				!tryOutBtn.isVisible() &&
				loginBtn.name() == "Login";

		log("LogIn visible: " + exist);
		
		return exist;
	}
	
	function isSignUpVisible()
	{
		exist = !loginBtn.isVisible() &&
				signUpBtn.isVisible() &&
				!tryOutBtn.isVisible() &&
				signUpBtn.name() == "Sign up";

		log("SignUp visible: " + exist);
		return exist;
	}
	
	function isTryOutVisible()
	{
		exist = !loginBtn.isVisible() &&
				!signUpBtn.isVisible() &&
				tryOutBtn.isVisible() &&
				tryOutBtn.name() == "Try out";

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
	
	function isSkipButtonVisible()
	{
		exist = tryOutBtn.isVisible() && tryOutBtn.name() == "Skip";
		
		log("Skip visible: " + exist);
		
		return exist;
	}

	function skipWhatsNew()
	{
		wait(1);
		
		if (isWhatsNewVisible())
		{
			log('Skip "What\'s New" screen');
			mainView.buttons()["Skip"].tap();
			wait(3);
			assignControls();
		}
	}
	
	function tapLogin()
	{
		log("Tap [Login] button");
		
		wait(0.5);
		loginBtn.tap();
		wait(0.5);
	}
	
	function tapSignUp()
	{
		log("Tap [Sign up] button");
		
		wait(0.5);
		signUpBtn.tap();
		wait(0.5);
	}
	
	function tapTryOut()
	{
		log("Tap [Try out] button");
		
		wait(0.5);
		tryOutBtn.tap();
		wait(0.5);
	}
	
	function tapSkip()
	{
		log("Tap [Skip] button");
		
		wait(0.5);
		skipBtn = mainView.buttons()[0];
		skipBtn.tap();
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
		skipWhatsNew();
		tapLogin();
		fillEmail(email);
		fillPassword(pwd);
		submit();
		
		// wait additional time for sync
		wait(3);	
	}
	
	function signUp(email, pwd)
	{
		skipWhatsNew();
		tapSignUp();
		fillEmail(email);
		fillPassword(pwd);
		submit();
		
		// wait additional time for sync
		wait(3);
	}
	
	function tryOut()
	{
		skipWhatsNew();
		tapTryOut();
	}
}
