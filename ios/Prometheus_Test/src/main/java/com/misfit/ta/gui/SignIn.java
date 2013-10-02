package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;

public class SignIn {

	/* lauch screen */
    public static void tapLogIn() {
        Gui.touchAVIew("PTRichTextLabel", DefaultStrings.LogInButton);
    }

    public static void tapLogInWithFacebook() {
        Gui.touchAVIew("UIButtonLabel", DefaultStrings.LogInFacebookButton);
    }

    
    /* log in screen */
    public static boolean isLoginView() {
        return ViewUtils.isExistedView("UILabel", DefaultStrings.SignInTitle);
    }
    
    public static void tapForgotPassword() {
        Gui.touchAVIew("UIButtonLabel", DefaultStrings.ForgotPasswordButton);
    }
    
    public static void tapNext() {
    	Gui.touchAVIew("UIButtonLabel", DefaultStrings.NextButton);
    }

    public static void tapPrevious() {
    	Gui.touchAVIew("UIButtonLabel", DefaultStrings.BackButton);
    }

    public static void enterEmailPassword(String email, String password) {
        PrometheusHelper.enterEmailPassword(email, password);
    }

    
    /* pop up messages */
    public static boolean hasSignInInvalidEmailMessage() {
        return PrometheusHelper.hasSignInInvalidEmailMessage();
    }
    
    public static boolean hasForgotPasswordInvalidEmailMessage() {
    	return PrometheusHelper.hasForgotPasswordInvalidEmailMessage();
    }

    public static boolean hasInvalidPasswordMessage() {
        return PrometheusHelper.hasInvalidPasswordMessage();
    }

    public static boolean hasIncorrectLoginMessage() {
        return PrometheusHelper.hasIncorrectLoginMessage();
    }

    public static void tapIForgot() {
        // popup Sorry your email or password is not correct
        Gui.touchPopupButton(Gui.isRunningOnIOS7() ? 0 : 1);
    }

    public static void tapTryAgain() {
        // popup Sorry your email or password is not correct
        Gui.touchPopupButton(Gui.isRunningOnIOS7() ? 1 : 0);
    }

    public static void tapOK() {
        // popup error message
        Gui.touchPopupButton(0);
    }
    
    public static void tapCancel() {
        // popup on forgot password view
        Gui.touchPopupButton(0);
    }


    /* forgot password screen */
    public static boolean isForgotPasswordView() {
        return DefaultStrings.ForgotPasswordSubmitButton.equals(Gui.getProperty("UIButtonLabel", 0, "text"))
                && DefaultStrings.ForgotPasswordCancelButton.equals(Gui.getProperty("UIButtonLabel", 1, "text"));

    }

    public static void enterEmailForResetPassword(String email) {
     	
    	Gui.setText("PTPaddingTextField", 0, email);
    }
    
    public static void tapCancelResetPassword() {
        Gui.touchAVIew("UIButtonLabel", DefaultStrings.ForgotPasswordCancelButton);
    }

    public static void tapSubmitResetPassword() {
        Gui.touchAVIew("UIButtonLabel", DefaultStrings.ForgotPasswordSubmitButton);
    }
    
    public static boolean hasEmailSentMessage() {
        return Gui.getPopupContent().equals(DefaultStrings.ForgotPasswordEmailSentMessage);
    }
    
    public static boolean hasSuccessNewPasswordMessage() {
        return Gui.getPopupContent().equals(DefaultStrings.ForgotPasswordNewPasswordMessage);
    }

    public static boolean hasNoMatchPasswordMessage() {
        return Gui.getPopupContent().equals(DefaultStrings.ForgotPasswordWrongConfirmMessage);
    }

    public static boolean hasNotAssociatedEmailMessage() {
        return Gui.getPopupContent().equals(DefaultStrings.ForgotPasswordEmailNotExistMessage);
    }

}
