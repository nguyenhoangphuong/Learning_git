package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.chimpchat.core.TouchPressType;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.utils.ShortcutsTyper;

public class HomeScreen {

	public static void tapManual() {
		Gui.touchAView("TextView", "mText", "M");
	}

	public static void tapDebug() {
		Gui.touchAView("TextView", "mText", "D");
	}

	public static void saveManual() {
		Gui.touchAView("Button", "mText", DefaultStrings.SaveText);
	}

	public static void openDashboardMenu(int fullScreenHeight,
			int fullScreenWidth) {
		Gui.swipeLeftRight(0, fullScreenHeight / 2, fullScreenWidth,
				fullScreenHeight / 2, 1);
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
		Gui.longTouchAView("EditText", "mID", DefaultStrings.StepsEditTextId);
		Gui.type(steps);
		
		ShortcutsTyper.delayOne();
		Gui.longTouchAView("EditText", "mID", DefaultStrings.DurationEditTextId);
		Gui.type(duration);
	}

	public static void cancelManual() {
		Gui.touchAView("Button", "mText", DefaultStrings.CancelText);
	}

	public static void sleepManual() {
		Gui.touchAView("Button", "mText", DefaultStrings.SleepText);
	}

	public static Integer getCurrentGoalInPicker() {
		return Integer.valueOf(PrometheusHelper.getCurrentValueInPicker(0));
	}

	public static String[] getDebugValues() {
		String regex = "([\\r\\n\\t]+)";
		String text = ViewUtils.findView("TextView", "mID",
				DefaultStrings.DebugTextViewId, 0).text;
		System.out.println(text);
		String[] result = text.split(regex);
		return result;
	}

	public static String getTotalSleep() {
		return ViewUtils.findView("TextView", "mID",
				DefaultStrings.TotalSleepTextViewId, 0).text;
	}

}
