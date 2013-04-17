package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class SignUp {
	public static void tapSignUp() {
		Gui.touchAVIew("UIButtonLabel", "SIGN UP");
	}

	/* STEP 1: ENTER EMAIL AND PASSWORD */
	public static void enterEmailPassword(String email, String password) {
		Gui.touchAVIew("UITextField", 0);
		Gui.type(email);
		Gui.pressNext();
		Gui.type(password);
		Gui.pressDone();
	}

	/* STEP 2: PROFILE */
	public static void enterGender(boolean isMale) {
		Helper.enterGender(isMale);
	}

	public static void enterBirthDay(String year, String month, String day) {
		Helper.enterBirthDay(year, month, day);
	}

	public static void enterHeight(String digit, String fraction,
			boolean isUSUnit) {
		Helper.enterHeight(digit, fraction, isUSUnit);
	}

	public static void enterWeight(String digit, String fraction,
			boolean isUSUnit) {
		Helper.enterWeight(digit, fraction, isUSUnit);
	}

	/* STEP 3: WHAT ARE POINTS? */
	/* STEP 4: SET YOUR GOAL */

	public static void tapNext() {
		Gui.touchAVIew("UIButton", 0);
	}

	public static void tapPrevious() {
		Gui.touchAVIew("UIButton", 1);
	}

	public static int getCurrentGoal() {
		int level = 0;
		String text = Gui.getText("UILabel", 0);
		if ("ACTIVE".equals(text)) {
			level = 0;
		} else if ("VERY ACTIVE".equals(text)) {
			level = 1;
		} else {
			level = 2;
		}
		return level;
	}

	public static void setGoal(int level) {
		int currentLevel = getCurrentGoal();
		if (level >= currentLevel) {
			for (int i = 0; i < level - currentLevel; i++) {
				Gui.swipeUp(300);
			}
		} else {
			for (int i = 0; i < currentLevel - level; i++) {
				Gui.swipeDown(300);
			}
		}
	}

	public static void sync() {
		Helper.sync();
	}

	public static void tapAllowUseCurrentLocation() {

	}

	public static boolean isSignUpStep1View() {
		return "Email".equals(Gui.getProperty(
				ViewUtils.findView("UITextFieldCenteredLabel", 0), "text"))
				&& "Password".equals(Gui.getProperty(
						ViewUtils.findView("UITextFieldCenteredLabel", 1),
						"text"));
	}

	public static boolean isSignUpStep2View() {
		return "PROFILE".equals(Gui.getProperty(
				ViewUtils.findView("UILabel", 8), "text"));
	}

	public static boolean isSignUpStep3View() {
		return "WHAT ARE POINTS?".equals(Gui.getProperty(
				ViewUtils.findView("UILabel", 0), "text"));
	}

	public static boolean isSignUpStep4View() {
		return "SET YOUR GOAL".equals(Gui.getProperty(
				ViewUtils.findView("UILabel", 4), "text"));
	}

	public static boolean isSignUpStep5View() {
		return "PAIRING".equals(Gui.getProperty(
				ViewUtils.findView("UILabel", 16), "text"));
	}
	
	public static boolean hasInvalidEmailMessage() {
		return Helper.hasInvalidEmailMessage();
	}
	
	public static boolean hasInvalidPasswordMessage() {
		return Helper.hasInvalidPasswordMessage();
	}
	
	public static boolean hasExistedEmailMessage() {
		return Gui.getPopupTitle().equals("Error")
				&& Gui.getPopupContent()
				.equals(DefaultStrings.SignUpDuplicatedEmailMessage);
	}
}
