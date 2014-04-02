package com.misfit.ta.android.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
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
	public static void tapSetActivityGoal() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y);
	}
	
	public static void tapSetSleepGoal() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y + SETTINGS_CONTEXT_INC);
	}

	public static void tapShineSettings() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y + SETTINGS_CONTEXT_INC * 2);
	}

	public static void tapHelp() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y + SETTINGS_CONTEXT_INC
				* 4);
	}

	public static void tapMyProfile() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y + SETTINGS_CONTEXT_INC
				* 3);
	}

	public static String getCurrentBirthDate() {
		return ViewUtils.findView("TextView", "mID",
				DefaultStrings.BirthdayTextViewId, 0).text;
	}

	public static String getCurrentHeight() {
		return ViewUtils.findView("TextView", "mID",
				DefaultStrings.HeightTextViewId, 0).text;
	}

	public static String getCurrentWeight() {
		return ViewUtils.findView("TextView", "mID",
				DefaultStrings.WeightTextViewId, 0).text;
	}

	public static boolean hasMaleGender() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.MaleButtonId, 0).isChecked;
	}

	public static boolean displayClock() {
		return ViewUtils.findView("CheckBox", "mID",
				DefaultStrings.ShineSettingsDisplayClockCheckBoxId, 0).isChecked;
	}

	public static boolean showProgressFirst() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId1, 0).isChecked;
	}

	public static boolean showClockFirst() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId2, 0).isChecked;
	}

	public static boolean useMiles() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId1, 1).isChecked;
	}

	public static boolean useKm() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId2, 1).isChecked;
	}

	public static boolean useLbs() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId1, 2).isChecked;
	}

	public static boolean useKg() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId2, 2).isChecked;
	}

	public static boolean trackSleep() {
		return ViewUtils.findView("Switch", "mID",
				DefaultStrings.ShineSettingsSleepTrackingSwitchId, 0).isChecked;
	}

	/**
	 * Adjust Goal Screen
	 */

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
