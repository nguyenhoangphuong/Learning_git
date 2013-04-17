package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class SignIn {

	public static void tapLogIn() {
		Gui.touchAVIew("UIButtonLabel", "LOG IN");
	}

	public static void tapLogInWithFacebook() {
		Gui.touchAVIew("UIButtonLabel", "LOG IN WITH FACEBOOK");
	}

	public static void enterEmailPassword(String email, String password) {
		Gui.touchAVIew("UITextField", 0);
		String txtEmail = Gui.getProperty(
				ViewUtils.findView("UITextField", 0), "text");
		for (int i = 0; i < txtEmail.length(); i++) {
			Gui.pressDelete();
		}
		ShortcutsTyper.delayTime(300);
		Gui.type(email);
		ShortcutsTyper.delayTime(300);
		Gui.pressNext();
		ShortcutsTyper.delayTime(300);
		Gui.pressDelete();
		Gui.type(password);
		ShortcutsTyper.delayTime(300);
		Gui.pressDone();
	}

	public static void enterEmail(String email) {
		Gui.type(email);
	}

	public static void enterPassword(String password) {
		Gui.type(password);
	}

	public static boolean hasInvalidEmailMessage() {
		return Helper.hasInvalidEmailMessage();
	}

	public static boolean hasInvalidPasswordMessage() {
		return Helper.hasInvalidPasswordMessage();
	}

	public static boolean hasIncorrectLoginMessage() {
		return Gui.getPopupTitle().equals("Error")
				&& Gui.getPopupContent().equals(
						DefaultStrings.SignInWrongAccountMessage);
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

	public static void tapNext() {
		Gui.tapNext();
	}

	public static void tapPrevious() {
		Gui.tapPrevious();
	}

	public static boolean isLoginView() {
		return ViewUtils.isExistedView("UILabel", "LOG IN");
	}

	/**
	 * Forgot Password
	 */
	public static void tapForgotPassword() {
		Gui.touchAVIew("UIButtonLabel", "Forgot password");
	}

	public static boolean isForgotPasswordView() {
		return "SUBMIT".equals(Gui.getProperty("UIButtonLabel", 0, "text"))
				&& "CANCEL".equals(Gui.getProperty("UIButtonLabel", 1, "text"));
//		"Enter your email".equals(Gui.getProperty(
//				"UITextFieldCenteredLabel", 0, "text"))
				
	}

	public static void tapCancel() {
		Gui.touchAVIew("UIButtonLabel", "CANCEL");
	}

	public static void tapSubmit() {
		Gui.touchAVIew("UIButtonLabel", "SUBMIT");
	}

	public static boolean hasEmailSentPassword() {
		return Gui.getPopupTitle().equals("Email Sent")
				&& Gui.getPopupContent().equals(
						DefaultStrings.ForgotPasswordEmailSentMessage);
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

	public static boolean hasSuccessNewPasswordMessage() {
		return Gui.getPopupTitle().equals("Success!")
				&& Gui.getPopupContent().equals(
						DefaultStrings.ForgotPasswordNewPasswordMessage);
	}

	public static boolean hasNoMatchPasswordMessage() {
		return Gui.getPopupTitle().equals("Error")
				&& Gui.getPopupContent().equals(
						DefaultStrings.ForgotPasswordWrongConfirmMessage);
	}

	public static boolean hasNotAssociatedEmailMessage() {
		return Gui.getPopupTitle().equals("Incorrect Email")
				&& Gui.getPopupContent().equals(
						DefaultStrings.ForgotPasswordEmailNotExistMessage);
	}

}
