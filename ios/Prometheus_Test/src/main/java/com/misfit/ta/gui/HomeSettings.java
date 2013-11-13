package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class HomeSettings {

	/* Navigation */
	public static void tapBack() {
		Gui.touchAVIew("UIButton", DefaultStrings.BackButton);
	}

	public static void tapCancel() {
		Gui.touchAVIew("UIButton", DefaultStrings.CancelButton);
	}
	
	public static void tapMisfitLabs() {
		Gui.touchAVIew("UILabel", DefaultStrings.MisfitLabsButton);
	}

	public static void tapYourProfile() {
		Gui.touchAVIew("UILabel", DefaultStrings.MyProfileButton);
	}

	public static void tapFahrenheit() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.FahrenheitLabel);
	}

	public static void tapCelcius() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.CelciusLabel);
	}

	public static void tapMile() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.MileLabel);
	}

	public static void tapKm() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.KmLabel);
	}

	public static void tapLbs() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.LbsLabel);
	}

	public static void tapKg() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.KgLabel);
	}

	public static void tapLink() {
		Gui.touchAVIew("UILabel", DefaultStrings.LinkButton);
	}

	public static void tapUnlink() {
		Gui.touchAVIew("UILabel", DefaultStrings.UnlinkButton);
	}

	public static void tapSignOut() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.CaptializedShineOutButton);
	}

	public static void chooseSignOut() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.ShineOutButton);
	}

	/* Update profile */
	public static void updateGender(boolean isMale) {
		PrometheusHelper.enterGender(isMale);
	}

	public static void updateBirthDay(String year, String month, String day) {
		PrometheusHelper.enterBirthDay(year, month, day);
	}

	public static void updateHeight(String digit, String fraction,
			boolean isUSUnit) {
		PrometheusHelper.enterHeight(digit, fraction, isUSUnit);
	}

	public static void updateWeight(String digit, String fraction,
			boolean isUSUnit) {
		PrometheusHelper.enterWeight(digit, fraction, isUSUnit);
	}

	/* Set Daily Goal */
	public static void tapDoneAtNewGoal() {
		Gui.touchAVIew("UIButton", DefaultStrings.SaveButton);
	}

	public static void setSpinnerGoal(int newGoal, int currentGoal) {
		int buttonIndex = newGoal >= currentGoal ? 0 : 1;
		for (int i = 0; i < Math.abs(newGoal - currentGoal) / 100; i++) {
			Gui.touchAVIew("UIButton", buttonIndex);
			ShortcutsTyper.delayTime(500);
		}
	}

	public static int getSpinnerGoal() {
		return Gui.getSpinnerValue("PTNumberSpinner", 0);
	}

	public static void tapOKAtNewGoalPopup() {
		Gui.touchPopupButton(DefaultStrings.OKButton);
	}

	public static boolean hasNewGoalInstructionMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.NewGoalInstruction);
	}

	public static void tapHead() {
		Gui.touchAVIew("UIButton", 0);
	}

	public static void tapUpperArm() {
		Gui.touchAVIew("UIButton", 1);
	}

	public static void tapChest() {
		Gui.touchAVIew("UIButton", 2);
	}

	public static void tapWrist() {
		Gui.touchAVIew("UIButton", 3);
	}

	public static void tapWaist() {
		Gui.touchAVIew("UIButton", 4);
	}

	public static void tapFootAnkle() {
		Gui.touchAVIew("UIButton", 5);
	}
	
	public static void tapWearingShine() {
		
	}

	public static void tapSave() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.SaveButton);
	}
	
	/* Mode Debug */
	public static void tapDoneAtDebug() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.DoneButton);
	}

	/* Visible Check */
	public static boolean isAtSettings() {
		return ViewUtils.isExistedView("UILabel", DefaultStrings.SettingsTitle);
	}

	public static boolean isAtEditProfile() {
		return ViewUtils.isExistedView("UILabel", DefaultStrings.EditProfileTitle);
	}

	public static boolean isAtEditGoal() {
		return ViewUtils.isExistedView("UILabel", DefaultStrings.MyGoalTitle);
	}

	public static boolean isAtMyShine() {
		return ViewUtils.isExistedView("UILabel", DefaultStrings.MyShineTitle);
	}
}
