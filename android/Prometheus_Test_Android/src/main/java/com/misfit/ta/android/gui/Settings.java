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
	public static void tapAdjustGoal() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y);
	}

	public static void tapShineSettings() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y + SETTINGS_CONTEXT_INC);
	}

	public static void tapHelp() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y + SETTINGS_CONTEXT_INC
				* 3);
	}

	public static void tapMyProfile() {
		Gui.touch(SETTINGS_CONTEXT_X, SETTINGS_CONTEXT_Y + SETTINGS_CONTEXT_INC
				* 2);
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

	public static String getCurrentUnit() {
		return ViewUtils.findView("TextView", "mID",
				"id/preferredUnitsTextView", 0).text;
	}

	public static void tapSignOut() {
		Gui.touchAView("TextView", "mID", "id/signoutTextView");
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
