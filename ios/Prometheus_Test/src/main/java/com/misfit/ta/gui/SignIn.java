package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class SignIn {

	/* lauch screen */
    public static void tapLogIn() {
        Gui.touchAVIew("UIButton", 0);
    }

    public static void tapLogInWithFacebook() {
        Gui.touchAVIew("UIButtonLabel", " SIGN IN");
    }

    
    /* log in screen */
    public static boolean isLoginView() {
        return ViewUtils.isExistedView("UILabel", "LOG IN");
    }
    
    public static void tapForgotPassword() {
        Gui.touchAVIew("UIButtonLabel", "Umm... What's my password again?");
    }
    
    public static void tapNext() {
        Gui.tapNext();
    }

    public static void tapPrevious() {
        Gui.tapPrevious();
    }

    public static void enterEmailPassword(String email, String password) {
        PrometheusHelper.enterEmailPassword(email, password);
    }

    
    /* pop up messages */
    public static boolean hasInvalidEmailMessage() {
        return PrometheusHelper.hasInvalidEmailMessage();
    }

    public static boolean hasInvalidPasswordMessage() {
        return PrometheusHelper.hasInvalidPasswordMessage();
    }

    public static boolean hasIncorrectLoginMessage() {
        return Gui.getPopupContent().equals(DefaultStrings.SignInWrongAccountMessage);
    }

    public static void tapIForgot() {
        // popup Sorry your email or password is not correct
        Gui.touchPopupButton(0);
    }

    public static void tapTryAgain() {
        // popup Sorry your email or password is not correct
        Gui.touchPopupButton(1);
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
        return "SUBMIT".equals(Gui.getProperty("UIButtonLabel", 0, "text"))
                && "CANCEL".equals(Gui.getProperty("UIButtonLabel", 1, "text"));

    }

    public static void enterEmailForResetPassword(String email) {
    	
    	Gui.longTouch(150, 100, 200);
		if(ViewUtils.isExistedView("PTEmailVerifyingTextField", 0))
		{
			Gui.touchAVIew("PTEmailVerifyingTextField", 0);
			String txtEmail = Gui.getProperty("PTEmailVerifyingTextField", 0, "text");
			
			System.out.println("Deleting: " + txtEmail);
			Gui.moveCursorInCurrentTextViewTo(-1);
			for (int i = 0; i < txtEmail.length(); i++)
				Gui.pressDelete();
		}
		else if(ViewUtils.isExistedView("PTPaddingTextField", 0));
		{
			Gui.touchAVIew("PTPaddingTextField", 0);
			String txtEmail = Gui.getProperty("PTPaddingTextField", 0, "text");
			
			System.out.println("Deleting: " + txtEmail);
			Gui.moveCursorInCurrentTextViewTo(-1);
			for (int i = 0; i < txtEmail.length(); i++)
				Gui.pressDelete();
		}
		
        ShortcutsTyper.delayTime(800);
        Gui.type(email);
    }
    
    public static void changePassword(String newPassword) {
        Gui.touchAVIew("PTBlackTextField", 0);
        ShortcutsTyper.delayTime(10000);
        Gui.type(newPassword);
        ShortcutsTyper.delayTime(10000);
        Gui.pressNext();
        ShortcutsTyper.delayTime(10000);
        Gui.type(newPassword);
        ShortcutsTyper.delayTime(10000);
        Gui.pressDone();
    }
    
    public static void tapCancelResetPassword() {
        Gui.touchAVIew("UIButtonLabel", "CANCEL");
    }

    public static void tapSubmitResetPassword() {
        Gui.touchAVIew("UIButtonLabel", "SUBMIT");
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
