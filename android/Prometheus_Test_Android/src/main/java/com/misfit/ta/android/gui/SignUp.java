package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class SignUp {

	public static void chooseSignUp() {
		Gui.touchAView("TextView", "mID", DefaultStrings.SignUpButtonTextId);
	}

	public static void pressBack() {
		Gui.touchAView("ImageButton", "mID", DefaultStrings.ImageBackButtonId);
	}

	public static void pressNext() {
		Gui.touchAView("ImageButton", "mID", DefaultStrings.ImageNextButtonId);
	}

	public static void fillSignUpForm(String email, String password) {
		if (email != null) {
			ViewNode emailNode = ViewUtils.findView("EditText", "mID",
					DefaultStrings.SignUpEmailTextViewId, 0);
			emailNode.text = "";

			Gui.touchAView("EditText", "mID",
					DefaultStrings.SignUpEmailTextViewId);
			Gui.clearTextbox(emailNode);
			Gui.type(email);
			Gui.pressBack();
		}

		if (password != null) {
			ViewNode passwordNode = ViewUtils.findView("EditText", "mID",
					DefaultStrings.SignUpPasswordTextViewId, 0);
			passwordNode.text = "";

			Gui.touchAView("EditText", "mID",
					DefaultStrings.SignUpPasswordTextViewId);
			Gui.clearTextbox(passwordNode);
			Gui.type(password);
			Gui.pressBack();
		}
	}

	public static void inputEmail(String email) {
		SignUp.fillSignUpForm(email, null);
	}

	public static void inputPassword(String password) {
		SignUp.fillSignUpForm(null, password);
	}

	/*
	 * Step 2: input profile TODO: edit fillProfileForm and getProfileForm
	 * methods to edit fields and get fields' values
	 */
	public static void fillProfileForm(Boolean isMale, String weight,
			String height, Integer[] birthday) {
		if (isMale != null) {
			inputSex(isMale);
		}

		// TODO: add logic code here
	}

	public static void linkShine() {
		Gui.longTouchAView("TextView", "mID",
				DefaultStrings.SignUpLinkShineTextViewId);
	}

	public static String formatDateString(int date, int month, int year) {
		String[] monthStrings = { "dummy", "Jan", "Feb", "Mar", "Apr", "May",
				"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

		return monthStrings[month] + " " + String.format("%02d", date) + ", "
				+ year;
	}

	public static String formatWeightString(float weight, boolean isUS) {
		return String.format("%.1f", weight) + (isUS ? " lbs" : " kg");
	}

	public static String formatHeightString(float height, boolean isUS) {
		if (isUS) {
			return String.format("%d", height / 12) + "'"
					+ String.format("%d", height % 12) + "\"";
		}

		return String.format("%.2f", height) + " m";
	}

	public static void inputSex(Boolean isMale) {
		if (isMale) {
			Gui.touchAView("RadioButton", "mID",
					DefaultStrings.SignUpMaleButtonId);
		} else {
			Gui.touchAView("RadioButton", "mID",
					DefaultStrings.SignUpFemaleButtonId);
		}
	}

	public static void inputBirthday(int date, int month, int year) {
	}

	public static void inputWeight(float weight, boolean isUS) {
	}

	public static void inputHeight(float height, boolean isUS) {
	}

	public static void inputUnits(boolean isUS) {
		if (isUS) {
			Gui.touchAView("RadioButton", "mID",
					DefaultStrings.SignUpUSButtonId);
		} else {
			Gui.touchAView("RadioButton", "mID",
					DefaultStrings.SignUpMetricButtonId);
		}
	}

	public static void chooseDefaultHeightValue(int fullScreenHeight,
			int fullScreenWidth) {
		Gui.touchAView("TextView", "mID", DefaultStrings.SignUpHeightTextViewId);
		dismissPopup(fullScreenHeight, fullScreenWidth, DefaultStrings.SetText);
	}

	public static void tapToSignOutAtProfilePage(int fullScreenHeight,
			int fullScreenWidth) {
		dismissPopup(fullScreenHeight, fullScreenWidth,
				DefaultStrings.SignOutText);
	}

	public static void dismissPopup(int fullScreenHeight, int fullScreenWidth,
			String buttonText) {
		int popupHeight = Gui.getHeight();
		int popupWidth = Gui.getWidth();
		ViewNode okButton = ViewUtils.findView("TextView", "mText", buttonText,
				0);
		Gui.touchViewOnPopup(fullScreenHeight, fullScreenWidth, popupHeight,
				popupWidth, okButton);
		// Magic line which makes ViewServer reload views after we dismiss popup
		ShortcutsTyper.delayTime(50);
	}

	public static void chooseDefaultWeightValue(int fullScreenHeight,
			int fullScreenWidth) {
		Gui.touchAView("TextView", "mID", DefaultStrings.SignUpWeightTextViewId);
		dismissPopup(fullScreenHeight, fullScreenWidth, DefaultStrings.SetText);
	}

	public static void chooseDefaultBirthdate(int fullScreenHeight,
			int fullScreenWidth) {
		Gui.touchAView("TextView", "mID",
				DefaultStrings.SignUpBirthdayTextViewId);
		dismissPopup(fullScreenHeight, fullScreenWidth, DefaultStrings.SetText);
	}
}
