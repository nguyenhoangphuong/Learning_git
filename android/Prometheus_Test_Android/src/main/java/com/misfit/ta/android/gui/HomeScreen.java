package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.chimpchat.core.TouchPressType;
import com.misfit.ta.android.gui.Helper.Helper;

public class HomeScreen {

	// These values are used in test with device Galaxy Nexus, other devices may
	// need adaption.
	private static final int TIME_CONTEXT_WIDTH = 70;
	private static final int TIME_CONTEXT_HEIGHT = 210;
	private static final int TIME_CONTEXT_INC = 90;

	/**
	 * Manual Goal
	 */
	public static void tapManual() {
		Gui.touchAView("ActionMenuItemView", "mID", "id/manual_input");
	}

	public static void chooseRandomManual() {
		Gui.touchAView("Button", "mText", "Random");
	}

	public static void chooseDormantManual() {
		Gui.touchAView("Button", "mText", "Dormant");
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

	/**
	 * See the hints
	 */
	public static void tapLevelMild() {
		Helper.tapLevelMild();
	}

	public static void tapLevelDormant() {
		Helper.tapLevelDormant();
	}

	public static void tapLevelModerate() {
		Helper.tapLevelModerate();
	}

	public static void tapLevelActive() {
		Helper.tapLevelActive();
	}

	/**
	 * Choose time context like TODAY, WEEK, MONTH
	 */
	public static void changeTimeContext() {
		Gui.touchAView("CheckedTextView", "mID", "id/text1");
	}

	public static void chooseToday() {
		Gui.touch(TIME_CONTEXT_WIDTH, TIME_CONTEXT_HEIGHT);
	}

	public static void chooseWeek() {
		Gui.touch(TIME_CONTEXT_WIDTH, TIME_CONTEXT_HEIGHT + TIME_CONTEXT_INC);
	}

	public static void chooseMonth() {
		Gui.touch(TIME_CONTEXT_WIDTH, TIME_CONTEXT_HEIGHT + TIME_CONTEXT_INC
				* 2);
	}

}
