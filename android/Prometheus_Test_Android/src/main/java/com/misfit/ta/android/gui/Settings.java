package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.utils.ShortcutsTyper;

public class Settings {
	// TODO need to adapt when there are more context menu options
	// These values are used in test with device Nexus 4, other devices may
	// need adaption.
	private static final int SETTINGS_CONTEXT_X = 555;
	private static final int SETTINGS_CONTEXT_Y = 195;
	private static final int SETTINGS_CONTEXT_INC = 95;
	
	
	/**
	 * Settings Menu
	 */
	public static void tapAdjustGoal() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y);
	}

	public static void tapShineSettings() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y
				+ SETTINGS_CONTEXT_INC);
	}

	public static void tapHelp() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y
				+ SETTINGS_CONTEXT_INC * 3);
	}
	
	public static void tapMyProfile() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y
				+ SETTINGS_CONTEXT_INC * 2);
	}

	public static void backToDayView() {
		// back from Settings, About, Adjust Goal ... screens
		ShortcutsTyper.delayTime(500);
		Gui.touchAView("TextView", "mID", "id/action_bar_subtitle");
	}

	public static boolean isProfileSettings() {
		return ViewUtils.findView("TextView", "mID", "id/dateOfBirthTextView",
				0) != null;
	}

	public static boolean isGoalSettings() {
		ShortcutsTyper.delayTime(1000);
		return ViewUtils.findView("TextView", "mID", "id/action_bar_subtitle",
				0) != null;
	}

	/**
	 * Settings Screen TODO edit value using picker
	 */
	public static void editName(String name) {
		Gui.touchAView("TextView", "mID", "id/nameTextView");
		ShortcutsTyper.delayTime(500);
		Gui.touch(Gui.getScreenWidth() / 2, Gui.getScreenHeight() / 2);
		Gui.clearTextbox(ViewUtils.findView("EditText", 0));
		Gui.type(name);
		Gui.pressBack();
		ShortcutsTyper.delayTime(1000);
		Gui.touch(Helper.getTouchPointOnPopup(
				ViewUtils.findView("Button", "mID", "id/button1", 0), 0, 20));
	}

	public static void editBirthDate(String day, String month, String year) {
		// For Galaxy Nexus only :(
		Gui.touchAView("TextView", "mID", "id/dateOfBirthTextView");
		ShortcutsTyper.delayTime(500);
		int[] monthPoint = Helper.getTouchPointOnPopup(ViewUtils.findView(
				"DatePicker",  0), 125, 240);
		int[] dayPoint = new int[]{monthPoint[0] + 130, monthPoint[1]};
		int[] yearPoint = new int[]{monthPoint[0] + 260, monthPoint[1]};
		System.out.println(monthPoint[0] + " " + monthPoint[1]);
		
		Gui.touch(dayPoint);
		Gui.type(day);
		Gui.pressBack();
		ShortcutsTyper.delayTime(500);
		Gui.touch(monthPoint);
		Gui.touch(monthPoint);
		Gui.type(month);
		Gui.pressBack();
		ShortcutsTyper.delayTime(1000);
		Gui.touch(yearPoint);
		Gui.type(year);
		Gui.pressBack();
		ShortcutsTyper.delayTime(500);
		Gui.touch(Helper.getTouchPointOnPopup(
				ViewUtils.findView("Button", "mID", "id/button1", 0), 0, 20));
	}

	public static void editGender(boolean isMale) {
		Gui.touchAView("TextView", "mID", "id/sexTextView");
		ShortcutsTyper.delayTime(500);
		if (isMale){
			Gui.touch(Helper.getTouchPointOnPopup(
					ViewUtils.findView("CheckedTextView", "mID", "id/text1", 0),
					0, 60));
		} else {
			Gui.touch(Helper.getTouchPointOnPopup(
					ViewUtils.findView("CheckedTextView", "mID", "id/text1", 1),
					0, 40));
		}
		// male 500, 550
		// female 500, 660
	}

	public static void editHeight(int digit, int fraction) {
		int[] digitPoint = new int[] { Gui.getScreenWidth() / 2 - 80,
				Gui.getScreenHeight() / 2 + 35 };
		int[] fractionPoint = new int[] { digitPoint[0] + 100, digitPoint[1] };
		Gui.touchAView("TextView", "mID", "id/heightTextView");
		ShortcutsTyper.delayTime(1000);
		Gui.touch(digitPoint);
		Gui.type(String.valueOf(digit));
		Gui.pressBack();
		ShortcutsTyper.delayTime(1000);

		Gui.touch(fractionPoint);
		Gui.type(String.valueOf(fraction));
		Gui.pressBack();
		Gui.touch(Helper.getTouchPointOnPopup(
				ViewUtils.findView("Button", "mID", "id/button1", 0), 0, 20));
		ShortcutsTyper.delayTime(1000);

	}

	public static void editWeight(int digit, int fraction) {
		int[] digitPoint = new int[] { Gui.getScreenWidth() / 2 - 80,
				Gui.getScreenHeight() / 2 + 35 };
		int[] fractionPoint = new int[] { digitPoint[0] + 100, digitPoint[1] };
		Gui.touchAView("TextView", "mID", "id/weightTextView");
		ShortcutsTyper.delayTime(1000);
		Gui.touch(digitPoint);
		Gui.type(String.valueOf(digit));
		Gui.pressBack();
		ShortcutsTyper.delayTime(1000);

		Gui.touch(fractionPoint);
		Gui.type(String.valueOf(fraction));
		Gui.pressBack();
		Gui.touch(Helper.getTouchPointOnPopup(
				ViewUtils.findView("Button", "mID", "id/button1", 0), 0, 20));
		ShortcutsTyper.delayTime(1000);
	}

	public static void editPreferredUnits() {
		// always choose U.S
		Gui.touchAView("TextView", "mID", "id/preferredUnitsTextView");
		ShortcutsTyper.delayTime(500);
		Gui.touch(Helper.getTouchPointOnPopup(
					ViewUtils.findView("CheckedTextView", "mID", "id/text1", 0),
					0, 60));
		ShortcutsTyper.delayTime(1000);
	}
	
	public static String getCurrentName() {
		return ViewUtils.findView("TextView", "mID", "id/nameTextView", 0).text;
	}
	
	public static String getCurrentBirthDate() {
		return ViewUtils.findView("TextView", "mID", "id/dateOfBirthTextView", 0).text;
	}
	
	public static String getCurrentHeight() {
		return ViewUtils.findView("TextView", "mID", "id/heightTextView", 0).text;
	}
	
	public static String getCurrentWeight() {
		return ViewUtils.findView("TextView", "mID", "id/weightTextView", 0).text;
	}
	
	public static String getCurrentGender() {
		return ViewUtils.findView("TextView", "mID", "id/sexTextView", 0).text;
	}
	
	public static String getCurrentUnit() {
		return ViewUtils.findView("TextView", "mID", "id/preferredUnitsTextView", 0).text;
	}

	public static void tapSignOut() {
		Gui.touchAView("TextView", "mID", "id/signoutTextView");
	}

	public static boolean hasNameField() {
		return ViewUtils.findView("TextView", "mID", "id/nameTextView", 0) != null;
	}

	public static boolean hasBirthDateField() {
		return ViewUtils.findView("TextView", "mID", "id/dateOfBirthTextView",
				0) != null;
	}

	public static boolean hasGenderField() {
		return ViewUtils.findView("TextView", "mID", "id/sexTextView", 0) != null;
	}

	public static boolean hasHeightField() {
		return ViewUtils.findView("TextView", "mID", "id/heightTextView", 0) != null;
	}

	public static boolean hasWeightField() {
		return ViewUtils.findView("TextView", "mID", "id/weightTextView", 0) != null;
	}

	public static boolean hasPreferredUnitsField() {
		return ViewUtils.findView("TextView", "mID",
				"id/preferredUnitsTextView", 0) != null;
	}

	/**
	 * Adjust Goal Screen
	 */
	public static void checkToConfirmGoal() {
	}


	public static void swipeDownGoalPicker(int fullScreenHeight,
			int fullScreenWidth, int popupHeight, int popupWidth,
			ViewNode viewOnPopup, int steps) {
		Gui.swipeDownViewOnPopup(fullScreenHeight, fullScreenWidth,
				popupHeight, popupWidth, viewOnPopup, steps);
	}

	public static void swipeUpGoalPicker(int fullScreenHeight,
			int fullScreenWidth, int popupHeight, int popupWidth,
			ViewNode viewOnPopup, int steps) {
		Gui.swipeUpViewOnPopup(fullScreenHeight, fullScreenWidth, popupHeight,
				popupWidth, viewOnPopup, steps);
	}
}
