package com.misfit.ta.gui;

import com.misfit.ta.utils.ShortcutsTyper;

public class PrometheusHelper {
	public static void enterGender(boolean isMale) {
		if (isMale) {
			Gui.touchAVIew("UIButtonLabel", "Male");
		} else {
			Gui.touchAVIew("UIButtonLabel", "Female");
		}
	}

	public static void enterBirthDay(String year, String month, String day) {
		Gui.touchAVIew("PTDatePickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(0, month);
		ShortcutsTyper.delayTime(5000);
		Gui.setPicker(1, day);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(2, year);
		ShortcutsTyper.delayTime(1000);
		Gui.dismissPicker();
	}

	public static void enterHeight(String digit, String fraction,
			boolean isUSUnit) {
		/* Example for ft in Gui.setPicker(1, "9\\\""); */
		Gui.touchAVIew("PTHeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		if (isUSUnit) {
			Gui.setPicker(2, "ft in");
		} else {
			Gui.setPicker(2, "m");
		}
		ShortcutsTyper.delayTime(5000);
		Gui.setPicker(0, digit);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(1, fraction);
		ShortcutsTyper.delayTime(1000);
		Gui.dismissPicker();
	}

	public static void enterWeight(String digit, String fraction,
			boolean isUSUnit) {
		Gui.touchAVIew("PTWeightPickerControl", 0);
		ShortcutsTyper.delayTime(1000);
		if (isUSUnit) {
			Gui.setPicker(2, "lbs");
		} else {
			Gui.setPicker(2, "kg");
		}
		ShortcutsTyper.delayTime(5000);
		Gui.setPicker(0, digit);
		ShortcutsTyper.delayTime(1000);
		Gui.setPicker(1, fraction);
		ShortcutsTyper.delayTime(1000);
		Gui.dismissPicker();
	}

	public static boolean hasInvalidEmailMessage() {
		return Gui.getPopupTitle().equals("Error")
				&& (Gui.getPopupContent().equals(
						DefaultStrings.InvalidEmailMessage) || Gui
						.getPopupContent().equals(
								DefaultStrings.ForgotInvalidEmailMessage));
	}

	public static boolean hasInvalidPasswordMessage() {
		return Gui.getPopupTitle().equals("Error")
				&& Gui.getPopupContent().equals(
						DefaultStrings.InvalidPasswordMessage);
	}

	public static void sync() {
		Gui.touchAVIew("UIButton", DefaultStrings.SyncPlaceShineHere);
		Gui.drag("UIButton", DefaultStrings.SyncPlaceShineHere, 300, 0);
	}

	public static void enterEmailPassword(String email, String password) {
		Gui.touchAVIew("UITextField", 0);
		String txtEmail = Gui.getProperty("UITextField", 0, "text");
		for (int i = 0; i < txtEmail.length(); i++) {
			Gui.pressDelete();
		}
		ShortcutsTyper.delayTime(800);
		Gui.type(email);
		ShortcutsTyper.delayTime(300);
		Gui.pressNext();
		ShortcutsTyper.delayTime(300);
		Gui.pressDelete();
		Gui.type(password);
		ShortcutsTyper.delayTime(300);
		Gui.pressDone();
	}
}
