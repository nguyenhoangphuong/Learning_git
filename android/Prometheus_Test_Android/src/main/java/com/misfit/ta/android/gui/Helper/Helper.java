package com.misfit.ta.android.gui.Helper;

import com.misfit.ta.android.Gui;

public class Helper {
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
