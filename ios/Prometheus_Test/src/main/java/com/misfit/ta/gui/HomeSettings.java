package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;

public class HomeSettings {

	/* Navigation */
	public static void tapBack() {
		Gui.touchAVIew("UIButton", DefaultStrings.BackButton);
	}

	public static void tapCancel() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.CancelButton);
	}
	
	public static void tapTaggingAndSleep() {
		Gui.touchAVIew("UILabel", DefaultStrings.TaggingAndSleep);
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

	public static void tapSave() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.SaveButton);
	}
	
	public static void tapSignOut() {
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.SignoutButton);
	}
	
	public static void tapShowMyShine(){
		Gui.touchAVIew("UILabel", DefaultStrings.ShowMyShineLabel);
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
	
	public static void setActivityGoal(int points) {
		Gui.setPicker(0, points / 1000);
		Gui.setPicker(1, (points / 100) % 10);
	}
	
	public static void setSleepGoal(int hours, int minutes) {
		Gui.setPicker(0, hours - 1);
		Gui.setPicker(1, minutes);
	}
	
	public static void setWeightGoal(String digit, String fraction, boolean isUSUnit) {
		
		PrometheusHelper.enterWeight(digit, fraction, isUSUnit);
	}

	public static void tapActivityGoalTab() {
		
		Gui.touchAVIew("UITabBarButtonLabel", 0);
	}
	
	public static void tapSleepGoalTab() {
		
		Gui.touchAVIew("UITabBarButtonLabel", 1);
	}
	
	public static void tapWeightGoalTab() {
		
		Gui.touchAVIew("UITabBarButtonLabel", 2);
	}
	
	public static void tapOKAtNewGoalPopup() {
		Gui.touchPopupButton(DefaultStrings.OKButton);
	}

	public static boolean hasNewGoalInstructionMessage() {
		return Gui.getPopupContent().equals(DefaultStrings.NewGoalInstruction);
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

	public static boolean isAtMyShine() {
		return ViewUtils.isExistedView("UILabel", DefaultStrings.MyShineTitle);
	}
	
	public static boolean isAtMisfitLabs(){
		return ViewUtils.isExistedView("UILabel", DefaultStrings.MisfitLabsTitle);
	}

	public static boolean isAtEditActivityGoal() {
		return ViewUtils.isExistedView("UILabel", DefaultStrings.HowToHitActivityGoalLabel);
	}
	
	public static boolean isAtEditSleepGoal() {
		return ViewUtils.isExistedView("UILabel", DefaultStrings.HowToHitSleepGoalLabel);
	}
	
	public static boolean isAtEditWeightGoal() {
		return ViewUtils.isExistedView("PTWeightPickerView", 0);
	}
	
	public static boolean isAtDevicesSettingScreen(){
		return ViewUtils.isExistedView("UILabel", DefaultStrings.DevicesButton);
	}

}