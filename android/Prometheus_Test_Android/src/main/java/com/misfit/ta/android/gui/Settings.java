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

	/**
	 * Settings Menu
	 */
	public static void tapSetActivityGoal() {
		Gui.touchAView("ShineSettingCheckbox", "mID",
				DefaultStrings.ActivityGoalViewId);
	}

	public static void tapSetSleepGoal() {
		Gui.touchAView("ShineSettingCheckbox", "mID",
				DefaultStrings.SleepGoalViewId);
	}

	public static void tapShineSettings() {
		Gui.touchAView("MenuMainTextView", "mID",
				DefaultStrings.SettingsMainMenuTextViewId);
	}

	public static void tapHelp() {
		Gui.touchAView("MenuMainTextView", "mID",
				DefaultStrings.HelpAndAboutMainMenuTextViewId);
	}

	public static void tapGoalsOnDashboard() {
		Gui.touchAView("MenuMainTextView", "mID",
				DefaultStrings.GoalsMainMenuTextViewId);
	}

	public static void tapMyProfile() {
		Gui.touchAView("TextView", "mText",
				DefaultStrings.MyInfoText);
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

	public static boolean isClockDisplayed() {
		return ViewUtils.findView("CheckBox", "mID",
				DefaultStrings.ShineSettingsDisplayClockCheckBoxId, 0).isChecked;
	}

	public static boolean isProgressShowedFirst() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId1, 0).isChecked;
	}

	public static boolean isClockShowedFirst() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId2, 0).isChecked;
	}

	public static boolean isMilesUnit() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId1, 0).isChecked;
	}

	public static boolean isKmUnit() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId2, 0).isChecked;
	}

	public static boolean isLbsUnit() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId1, 0).isChecked;
	}

	public static boolean isKgUnit() {
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId2, 0).isChecked;
	}
	
	public static boolean isAutomaticSleepTracking(){ 
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId1, 0).isChecked;
	}
	
	public static boolean isManualSleepTracking(){ 
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId2, 0).isChecked;
	}

	public static boolean isOffSleepTracking(){ 
		return ViewUtils.findView("RadioButton", "mID",
				DefaultStrings.ShineSettingsRadioButtonId3, 0).isChecked;
	}
	
	public static void openDistancePopup() {
		Gui.touchAView("ShineSettingCheckBox", "mID", DefaultStrings.ShineSettingsDistanceButtonId);
	}

	public static void openWeightPopup() {
		Gui.touchAView("ShineSettingCheckBox", "mID", DefaultStrings.ShineSettingsWeightButtonId);
	}
	
	public static void openSleepTrackingModePopup() {
		Gui.touchAView("ShineSettingCheckBox", "mID", DefaultStrings.ShineSettingsSleepTrackingModeButtonId);
	}
	
	public static void openDisplayOrderPopup() {
		Gui.touchAView("ShineSettingCheckBox", "mID", DefaultStrings.ShineSettingsDisplayOrderButtonId);
	}
	
	public static void showClock(boolean isShowed) {
		if (!(isClockDisplayed() && isShowed)) {
			Gui.touchAView("CheckBox", "mID",
					DefaultStrings.ShineSettingsDisplayClockCheckBoxId);
		}
	}
	
	public static void swipeDownValuePicker(int fullScreenHeight,
			int fullScreenWidth, int popupHeight, int popupWidth,
			ViewNode viewOnPopup, int steps) {
		Gui.swipeDownViewOnPopup(fullScreenHeight, fullScreenWidth,
				popupHeight, popupWidth, viewOnPopup, steps);
	}

	public static void swipeUpValuePicker(int fullScreenHeight,
			int fullScreenWidth, int popupHeight, int popupWidth,
			ViewNode viewOnPopup, int steps) {
		Gui.swipeUpViewOnPopup(fullScreenHeight, fullScreenWidth, popupHeight,
				popupWidth, viewOnPopup, steps);
	}

	public static String getDailyGoalSummary() {
		return ViewUtils.findView("TextView", "mID",
				DefaultStrings.GoalSummaryTextViewId, 0).text;
	}

	public static String getNightlyGoalSummary() {
		return ViewUtils.findView("TextView", "mID",
				DefaultStrings.GoalSummaryTextViewId, 1).text;
	}
}
