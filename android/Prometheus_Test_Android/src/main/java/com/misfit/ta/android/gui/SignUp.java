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
			PrometheusHelper.editGender(isMale);
		}

		// TODO: add logic code here
	}

	public static void linkShine() {
		Gui.touchAView("TextView", "mText", DefaultStrings.ShineText);
		Gui.longTouchAView("TextView", "mID",
				DefaultStrings.SignUpLinkShineTextViewId);
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
	
	public static void handleSignUpTutorial() {
		Gui.swipeLeft(2);
		Gui.touchAView("Button", "mID", DefaultStrings.SignUpTutorialGotItButtonId);
	}

	public static void chooseDefaultHeightValue(int fullScreenHeight,
			int fullScreenWidth) {
		Gui.touchAView("TextView", "mID", DefaultStrings.HeightTextViewId);
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth, DefaultStrings.SetText);
	}

	public static void tapToSignOutAtProfilePage(int fullScreenHeight,
			int fullScreenWidth) {
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth,
				DefaultStrings.SignOutText);
	}


	public static void chooseDefaultWeightValue(int fullScreenHeight,
			int fullScreenWidth) {
		Gui.touchAView("TextView", "mID", DefaultStrings.WeightTextViewId);
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth, DefaultStrings.SetText);
	}

	public static void chooseDefaultBirthdate(int fullScreenHeight,
			int fullScreenWidth) {
		Gui.touchAView("TextView", "mID",
				DefaultStrings.BirthdayTextViewId);
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth, DefaultStrings.SetText);
	}
}
