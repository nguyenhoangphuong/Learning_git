package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.chimpchat.core.TouchPressType;

public class HomeScreen {
	public static void tapManual() {
		Gui.touchAView("ActionMenuItemView", "mID", "id/manual_input");
	}

	public static void tapSettingsMenu() {
		Gui.touchAView("ImageButton", "mID", "id/overflow");
	}

	public static void sync() {
		Gui.touch(Gui.getCoordinators("ImageButton", "mID", "id/buttonSync"),
				TouchPressType.DOWN);
	}
	
	public static void tapLevelMild() {
		Gui.touchAView("ImageButton", "mID", "id/buttonLevelMild");
	}
	
	public static void tapLevelDormant() {
		Gui.touchAView("ImageButton", "mID", "id/buttonLevelDormant");
	}
	
	public static void tapLevelModerate() {
		Gui.touchAView("ImageButton", "mID", "id/buttonLevelModerate");
	}
	
	public static void tapLevelActive() {
		Gui.touchAView("ImageButton", "mID", "id/buttonLevelActive");
	}
	
	public static void changeTimeContext() {
		Gui.touchAView("CheckedTextView", "mID", "id/text1");
	}

}
