package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.chimpchat.core.TouchPressType;
import com.misfit.ta.android.gui.Helper.Helper;

public class HomeScreen {

	// These values are used in test with device Galaxy Nexus, other devices may
	// need adaption.
	private static final int TIME_CONTEXT_WIDTH = 70;
	private static final int TIME_CONTEXT_HEIGHT = 210;
	private static final int TIME_CONTEXT_INC = 90;

	
	public static boolean isHomeScreenVisible()
	{
		return ViewUtils.findView("ImageButton", "mID", "id/overflow", 0) != null;
	}
	
	/**
	 * Manual Goal
	 */
	public static void tapManual() {
		Gui.touchAView("ActionMenuItemView", "mID", DefaultStrings.ManualActionMenuItemViewId);
	}


	public static void saveManual() {
		Gui.touchAView("Button", "mText", "Save");
	}
	
	/**
	 * Go to Settings screens
	 */
	public static void tapSettingsMenu() {
		Gui.touchAView("ImageButton", "mID", "id/overflow");
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
		Gui.pressEnter();
	}
}
