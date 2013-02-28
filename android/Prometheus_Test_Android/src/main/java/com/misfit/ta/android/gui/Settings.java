package com.misfit.ta.android.gui;

import com.misfit.ta.android.Gui;
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

	/**
	 * Settings Screen TODO edit value using picker
	 */
	public static void editName(String name) {
		Gui.touchAView("TextView", "mID", "id/nameTextView");
	}

	public static void editBirthDate(String birthDate) {
		Gui.touchAView("TextView", "mID", "id/dateOfBirthTextView");
	}

	public static void editGender(boolean isMale) {
		Gui.touchAView("TextView", "mID", "id/sexTextView");
	}

	public static void editHeight(String height) {
		Gui.touchAView("TextView", "mID", "id/heightTextView");
	}

	public static void editWidth(String weigth) {
		Gui.touchAView("TextView", "mID", "id/weigthTextView");
	}

	public static void editPreferredUnits(boolean isMetric) {
		Gui.touchAView("TextView", "mID", "id/preferredUnitsTextView");
	}

	public static void tapSignOut() {
		Gui.touchAView("TextView", "mID", "id/signoutTextView");
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
}
