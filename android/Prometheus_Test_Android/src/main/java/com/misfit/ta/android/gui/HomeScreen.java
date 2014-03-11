package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.chimpchat.core.TouchPressType;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.utils.ShortcutsTyper;

public class HomeScreen {

	public static boolean isHomeScreenVisible() {
		return ViewUtils.findView("ActionMenuItemView", "mID", DefaultStrings.SettingsActionMenuItemViewId, 0) != null;
	}

	/**
	 * Manual Goal
	 */
	public static void tapManual() {
		Gui.touchAView("ActionMenuItemView", "mID",
				DefaultStrings.ManualActionMenuItemViewId);
	}

	public static void saveManual() {
		Gui.touchAView("Button", "mText", DefaultStrings.SaveText);
	}

	/**
	 * Go to Settings screens
	 */
	public static void tapSettingsMenu() {
		Gui.touchAView("ActionMenuItemView", "mID", DefaultStrings.SettingsActionMenuItemViewId);
	}

	/**
	 * Place Shine to sync
	 */
	public static void sync() {
		Gui.touch(Gui.getCoordinators("ImageButton", "mID", "id/buttonSync"),
				TouchPressType.DOWN);
	}

	public static void inputManualTime(String hour, String minute) {
		Gui.touchAView("NumberPicker$CustomEditText", 0);
		if (hour != null) {
			Gui.type(hour);
		}
		Gui.touchAView("NumberPicker$CustomEditText", 1);
		if (minute != null) {
			Gui.type(minute);
		}
		Gui.pressBack();
	}
	
	public static void intputActivity(String duration, String steps) {
		Gui.longTouchAView("EditText", "mID", DefaultStrings.DurationEditTextId);
		Gui.type(duration);
		Gui.pressBack();
		
		ShortcutsTyper.delayOne();
		Gui.longTouchAView("EditText", "mID", DefaultStrings.StepsEditTextId);
		Gui.type(steps);
		Gui.pressBack();
	}
	
	public static void cancelManual() {
		Gui.touchAView("Button", "mText", DefaultStrings.CancelText);
	}
	
	public static void sleepManual() {
		Gui.touchAView("Button", "mText", DefaultStrings.SleepText);
	}
	
	public static Integer getCurrentGoalInPicker() {
		return Integer.valueOf(getCurrentValueInPicker(0));
	}
	
	public static String getCurrentValueInPicker(int index) {
		ViewNode currentGoalView = ViewUtils.findView("ShineCustomEditText", "mID", DefaultStrings.ShineCustomEditTextInPickerId, index);
		return currentGoalView.text;
	}
}
