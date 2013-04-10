package com.misfit.ta.gui;

import com.misfit.ta.utils.ShortcutsTyper;

public class Helper {
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
				&& Gui.getPopupContent().equals(DefaultStrings.InvalidEmailMessage);
	}
	
	public static boolean hasInvalidPasswordMessage() {
		return Gui.getPopupTitle().equals("Error")
				&& Gui.getPopupContent()
						.equals(DefaultStrings.InvalidPasswordMessage);
	}
}
