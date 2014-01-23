package com.misfit.ta.android.gui;

import java.util.HashMap;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.chimpchat.core.TouchPressType;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class SignUp {

	/*
	 * navigation and visible checking methods TODO: add visible checking method
	 * for step 2 3 4
	 */

	public static void chooseSignUp() {
		Gui.touchAView("TextView", "mID", DefaultStrings.SignUpButtonTextId);
	}

	public static void pressBack() {
		Gui.touchAView("ImageButton", "mID", DefaultStrings.ImageBackButtonId);
	}

	public static void pressNext() {
		Gui.touchAView("ImageButton", "mID", DefaultStrings.ImageNextButtonId);
	}

	public static String getAlertMessage() {
		ViewNode node = ViewUtils.findView("TextView", "mID", "id/message", 0);
		return node == null ? null : node.text;
	}

	public static void closeAlert() {
		ViewNode btn = ViewUtils.findView("Button", "mID", "id/button1", 0);
		ViewNode msg = ViewUtils.findView("TextView", "mID", "id/message", 0);
		if (btn != null) {
			int[] p = Helper.getTouchPointOnPopup(btn, msg.width / 2,
					btn.height / 2);
			Gui.touch(p[0], p[1]);
			Helper.wait1();
			Gui.getCurrentViews();
		} else
			Gui.makeToast("Error: alert should be shown");

	}

	public static boolean isAtStep1() {
		return ViewUtils.findView("EditText", "mID",
				"id/userinfo_passwordEdit", 0) != null;
	}

	public static boolean isAtStep2() {
		// to do: add logic code here
		return true;
	}

	public static boolean isAtStep3() {
		// to do: add logic code here
		return true;
	}

	public static boolean isAtStep4() {
		// to do: add logic code here
		return true;
	}

	/*
	 * Step 1: fill email and password
	 */
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
		 Gui.longTouchAView("TextView", "mID", DefaultStrings.SignUpLinkShineTextViewId);
	}

	public static String getProfileForm(String what) {
		// to do: add logic code here

		if (what == "sex")
			return null;

		if (what == "birthday")
			return null;

		if (what == "weight")
			return null;

		if (what == "height")
			return null;

		return null;
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
		Integer[] birthday = { date, month, year };
		fillProfileForm(null, null, null, birthday);
	}

	public static void inputWeight(float weight, boolean isUS) {
		String formatedWeight = formatWeightString(weight, isUS);
		fillProfileForm(null, formatedWeight, null, null);
	}

	public static void inputHeight(float height, boolean isUS) {
		String formatedHeight = formatHeightString(height, isUS);
		fillProfileForm(null, formatedHeight, null, null);
	}

	public static void inputUnits(boolean isUS) {
		if (isUS)
			Gui.touchAView("RadioButton", "mID",
					DefaultStrings.SignUpUSButtonId);
		else {
			Gui.touchAView("RadioButton", "mID",
					DefaultStrings.SignUpMetricButtonId);
		}
	}

	public static void chooseDefaultHeightValue(int fullScreenHeight,
			int fullScreenWidth) {
		Gui.touchAView("TextView", "mID", DefaultStrings.SignUpHeightTextViewId);
		dismissPopup(fullScreenHeight, fullScreenWidth, DefaultStrings.SetText);
	}

	public static void tapToSignOutAtProfilePage(int fullScreenHeight, int fullScreenWidth){
		dismissPopup(fullScreenHeight, fullScreenWidth, DefaultStrings.SignOutText);
	}
	public static void dismissPopup(int fullScreenHeight, int fullScreenWidth, String buttonText) {
		int popupHeight = Gui.getHeight();
		int popupWidth = Gui.getWidth();
		ViewNode okButton = ViewUtils.findView("TextView", "mText",
				buttonText, 0);
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
		Gui.touchAView("TextView", "mID", DefaultStrings.SignUpBirthdayTextViewId);
		dismissPopup(fullScreenHeight, fullScreenWidth, DefaultStrings.SetText);
	}

	public static String getSex() {
		return getProfileForm("sex");
	}

	public static String getBirthday() {
		return getProfileForm("birthday");
	}

	public static String getWeight() {
		return getProfileForm("weight");
	}

	public static String getHeight() {
		return getProfileForm("height");
	}

	/**
	 * Step 3: set goal
	 * 
	 * @param type
	 *            : 0 = Active, 1 = Very Active, 2 = Highly Active
	 */
	public static void setGoal(int type) {
		// to do: add logic code here

		if (type == 0) {
		}

		if (type == 1) {
		}

		if (type == 2) {
		}
	}

	public static HashMap<String, Object> getCurrentGoalInfo() {
		// to do: add logic code here

		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("title", null);
		info.put("activity", null);
		info.put("point", null);
		info.put("step", null);

		return info;
	}

	/**
	 * Step 4: set up device
	 */
	public static void startSyncing() {
		Gui.touch(Gui.getCoordinators("ImageButton", "mID", "id/buttonSetup"),
				TouchPressType.DOWN);
	}

	public static void stopSyncing() {
		Gui.touch(Gui.getCoordinators("ImageButton", "mID", "id/buttonSetup"),
				TouchPressType.UP);
	}

	public static boolean hasGetYoursNowButton() {
		return true;
	}

	public static boolean hasMagicHappening() {
		return ViewUtils.findView("Button", "mText", "MAGIC HAPPENING NOW", 0) != null;
	}

	public static boolean hasDetectedFailMessage() {
		return false;
	}

	public static boolean hasDectectedPassMessage() {
		return false;
	}

	/**
	 * New Goal
	 */
	public static void chooseActive() {
		Gui.touch(Gui.getScreenWidth() / 2, Gui.getScreenHeight() / 2 - 350);
	}

	public static void chooseVeryActive() {
		Gui.touch(Gui.getScreenWidth() / 2, Gui.getScreenHeight() / 2);
	}

	public static void chooseSuperActive() {
		Gui.touch(Gui.getScreenWidth() / 2, Gui.getScreenHeight() / 2 + 350);

	}

	public static String generateUniqueEmail() {
		return "test" + System.currentTimeMillis()
				+ TextTool.getRandomString(6, 6) + "@misfitqa.com";
	}

}
