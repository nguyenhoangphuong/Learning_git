package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.utils.ShortcutsTyper;

public class Settings {
	// TODO need to adapt when there are more context menu options
	// These values are used in test with device Galaxy Nexus, other devices may need adaption.
	private static final int SETTINGS_CONTEXT_WIDTH = 540;
	private static final int SETTINGS_CONTEXT_HEIGHT = 195;
	private static final int SETTINGS_CONTEXT_INC = 75;

	/**
	 * Settings Menu
	 */
	public static void tapAdjustGoal() {
		ShortcutsTyper.delayTime(300);
		Gui.touch(SETTINGS_CONTEXT_WIDTH, SETTINGS_CONTEXT_HEIGHT);
	}

	public static void tapSettings() {
		ShortcutsTyper.delayTime(300);
		Gui.touch(SETTINGS_CONTEXT_WIDTH, SETTINGS_CONTEXT_HEIGHT + SETTINGS_CONTEXT_INC);
	}

	public static void tapAbout() {
		ShortcutsTyper.delayTime(300);
		Gui.touch(SETTINGS_CONTEXT_WIDTH, SETTINGS_CONTEXT_HEIGHT + SETTINGS_CONTEXT_INC * 2);
	}

	public static void backToDayView() {
		// back from Settings, About, Adjust Goal ... screens
		ShortcutsTyper.delayTime(500);
		Gui.touchAView("TextView", "mID", "id/action_bar_subtitle");
	}
	
	public static boolean isProfileSettings() {
		return ViewUtils.findView("TextView", "mID", "id/dateOfBirthTextView", 0) != null;
	}
	
	public static boolean isGoalSettings() {
		ShortcutsTyper.delayTime(1000);
		return ViewUtils.findView("TextView", "mID", "id/action_bar_subtitle", 0) != null;
	}

	/**
	 * Settings Screen TODO edit value using picker
	 */
	public static void editName(String name) {
		Gui.touchAView("TextView", "mID", "id/nameTextView");
		ShortcutsTyper.delayTime(500);
		Gui.touch(Gui.getScreenWidth() / 2, Gui.getScreenHeight() / 2);
    	Gui.type(name);
    	Gui.pressBack();
    	ShortcutsTyper.delayTime(1000);
		Gui.touch(Helper.getTouchPointOnPopup(ViewUtils.findView("Button", "mID", "id/button1", 0), 0, 20));
	}

	public static void editBirthDate(String day, String month, String year) {
		Gui.touchAView("TextView", "mID", "id/dateOfBirthTextView");
		ShortcutsTyper.delayTime(500);
		int[] dayPoint = 
				Helper.getTouchPointOnPopup(ViewUtils.findView("EditText", "mID", "id/numberpicker_input", 0), 0, 0);
		int[] monthPoint =
				Helper.getTouchPointOnPopup(ViewUtils.findView("EditText", "mID", "id/numberpicker_input", 1), 0, 0);
		int[] yearPoint =
				Helper.getTouchPointOnPopup(ViewUtils.findView("EditText", "mID", "id/numberpicker_input", 2), 0, 0);
		Gui.touch(dayPoint);
		Gui.type(day);
		Gui.pressBack();
		Gui.touch(monthPoint);
		Gui.type(month);
		Gui.pressBack();
		Gui.touch(yearPoint);
		Gui.type(year);
		Gui.pressBack();
		Gui.touch(Helper.getTouchPointOnPopup(ViewUtils.findView("Button", "mID", "id/button1", 0), 0, 20));
		//500, 886
	}

	public static void editGender(boolean isMale) {
		Gui.touchAView("TextView", "mID", "id/sexTextView");
		ShortcutsTyper.delayTime(500);
		if (isMale && ViewUtils.findView("CheckedTextView", "mID", "id/text1", 1).isChecked) { // current gender is female and want to change to male
			Gui.touch(Helper.getTouchPointOnPopup(ViewUtils.findView("CheckedTextView", "mID", "id/text1", 0), 0, -20));
		} else if (!ViewUtils.findView("CheckedTextView", "mID", "id/text1", 1).isChecked) {
			Gui.touch(Helper.getTouchPointOnPopup(ViewUtils.findView("CheckedTextView", "mID", "id/text1", 1), 0, 20));
		}
		Gui.touch(Helper.getTouchPointOnPopup(ViewUtils.findView("Button", "mID", "id/button1", 0), 0, 20));
		// male 500, 550
		// female 500, 660
	}

	public static void editHeight(int digit, int fraction) {
		int[] digitPoint = new int[]{Gui.getScreenWidth() / 2 - 30, Gui.getScreenHeight() / 2 + 35};
		//Helper.getTouchPointOnPopup(ViewUtils.findView("NumberPicker$CustomEditText", "mID", "id/numberpicker_input", 0), 0, 0);
int[] fractionPoint =
		new int[]{digitPoint[0] + 40, digitPoint[1]};
		System.out.println("1");
		Gui.touchAView("TextView", "mID", "id/heightTextView");
		Gui.pressBack();
		ShortcutsTyper.delayTime(500);
		System.out.println("2");
		Gui.touch(digitPoint);
		Gui.type(String.valueOf(digit));
		System.out.println("3");
		Gui.pressBack();
		ShortcutsTyper.delayTime(1000);
		//System.out.println(fractionPoint[0] + " " + fractionPoint[1]);
		System.out.println("4");
		Gui.touch(fractionPoint);
		Gui.type(String.valueOf(fraction));
		Gui.pressBack();
		Gui.touch(Helper.getTouchPointOnPopup(ViewUtils.findView("Button", "mID", "id/button1", 0), 0, 20));
		
	}

	public static void editWeight(int digit, int fraction) {
		Gui.touchAView("TextView", "mID", "id/weightTextView");
		ShortcutsTyper.delayTime(500);
		int[] digitPoint = 
				Helper.getTouchPointOnPopup(ViewUtils.findView("NumberPicker$CustomEditText", "mID", "id/numberpicker_input", 0), 0, 0);
		int[] fractionPoint =
				Helper.getTouchPointOnPopup(ViewUtils.findView("NumberPicker$CustomEditText", "mID", "id/numberpicker_input", 1), 0, 0);
		Gui.touch(digitPoint);
		Gui.type(String.valueOf(digit));
		ShortcutsTyper.delayTime(500);
		Gui.pressBack();
		ShortcutsTyper.delayTime(500);
		Gui.touch(fractionPoint);
		Gui.type(String.valueOf(fraction));
		ShortcutsTyper.delayTime(500);
		Gui.pressBack();
		Gui.touch(Helper.getTouchPointOnPopup(ViewUtils.findView("Button", "mID", "id/button1", 0), 0, 20));
	}

	public static void editPreferredUnits(boolean isMetric) {
		Gui.touchAView("TextView", "mID", "id/preferredUnitsTextView");
	}

	public static void tapSignOut() {
		Gui.touchAView("TextView", "mID", "id/signoutTextView");
	}
	
	public static boolean hasNameField() {
		return ViewUtils.findView("TextView", "mID", "id/nameTextView", 0) != null;
	}

	public static boolean hasBirthDateField() {
		return ViewUtils.findView("TextView", "mID", "id/dateOfBirthTextView", 0) != null;
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
		return ViewUtils.findView("TextView", "mID", "id/preferredUnitsTextView", 0) != null;
	}


	/**
	 * About Screen
	 */
	public static void chooseSuport() {
		Gui.touchAView("TextView", "mID", "id/aboutSupportContainer");
	}

	public static void chooseLikeFacebook() {
		Gui.touchAView("TextView", "mID", "id/aboutLikeContainer");
	}

	public static void chooseWebsite() {
		Gui.touchAView("TextView", "mID", "id/aboutWebsiteContainer");
	}

	public static void chooseRateOurApp() {
		Gui.touchAView("TextView", "mID", "id/aboutRateContainer");
	}

	/**
	 * Adjust Goal Screen
	 */
	public static void checkToConfirmGoal() {
		Gui.touchAView("ActionMenuItemView", "mID", "id/check");
	}
	
	public static Integer getCurrentLevel() {
		return Integer.valueOf(ViewUtils.findView("TextView", "mID", "id/textCurrentLevel", 0).text)  ;
	}
	public static void setGoalUp(int steps) {
		for (int i = 0; i < steps; i++) {
			Gui.touchAView("Button", 1);
		}
	}

	public static void setGoalDown(int steps) {
		for (int i = 0; i < steps; i++) {
			Gui.touchAView("Button", 0);
		}
	}
}
