package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;

public class HomeSettings {
	
	public static void tapBackAtSettings() {
        Gui.touchAVIew("UIButton", "Back");
    }
	
    public static void tapAdjustGoal() {
        Gui.touchAVIew("UILabel", "ADJUST GOAL");
    }

    public static void tapYourProfile() {
        Gui.touchAVIew("UILabel", "YOUR PROFILE");
    }

    public static void tapWearingShine() {
        Gui.touchAVIew("UILabel", "WEARING SHINE");
    }
    
    public static void tapUnlink() {
        Gui.touchAVIew("UILabel", "UNLINK");
    }

    public static void tapSupport() {
        Gui.touchAVIew("UILabel", "SUPPORT");
    }

    public static void tapLikeUs() {
        Gui.touchAVIew("UILabel", "LIKE US");
    }

    public static void tapWebsite() {
        Gui.touchAVIew("UILabel", "WEBSITE");
    }

    public static void tapStore() {
        Gui.touchAVIew("UILabel", "STORE");
    }

    public static void tapRateOurApp() {
        Gui.touchAVIew("UILabel", "RATE OUR APP");
    }

    public static void tapDebug() {
        Gui.touchAVIew("UILabel", "DEBUG");
    }

    public static void tapBehindTheScenes() {
        Gui.touchAVIew("UILabel", "BEHIND THE SCENES");
    }

    public static void tapSignOut() {
        Gui.touchAVIew("UILabel", "SIGN OUT");
    }

    /* Update profile */

    public static void updateGender(boolean isMale) {
        PrometheusHelper.enterGender(isMale);
    }

    public static void updateBirthDay(String year, String month, String day) {
        PrometheusHelper.enterBirthDay(year, month, day);
    }

    public static void updateHeight(String digit, String fraction, boolean isUSUnit) {
        PrometheusHelper.enterHeight(digit, fraction, isUSUnit);
    }

    public static void updateWeight(String digit, String fraction, boolean isUSUnit) {
        PrometheusHelper.enterWeight(digit, fraction, isUSUnit);
    }
    
    public static void tapBackAtEditProfile() {
    	Gui.touchAVIew("UIButton", "Back");
    }

    /* Set Daily Goal */
    public static void tapDoneAtNewGoal() {
    	Gui.touchAVIew("UIButton", "Done");
    }

    public static void tapBackAtNewGoal() {
    	Gui.touchAVIew("UIButton", "Back");
    }
    
    public static void setSpinnerGoal(int value) {
    	Gui.setSpinnerValue("PTNumberSpinner", 0, value);
    }
    
    public static int getSpinnerGoal()
    {
    	return Gui.getSpinnerValue("PTNumberSpinner", 0);
    }
    
    public static void tapOKAtNewGoalPopup()
    {
    	Gui.touchPopupButton(0);
    }

    public static boolean hasDontForgetMessage()
    {
    	return Gui.getPopupContent().equals(DefaultStrings.NewGoalInstruction);
    }
    
    
    /* Mode Debug */
    public static void tapDoneAtDebug()
    {
    	Gui.touchAVIew("UIButtonLabel", "Done");
    }
    
    public static void chooseManual() {
        Gui.touchAVIew("UITextField", 0);
        Gui.setPicker(0, "Input form");
        Gui.dismissPicker();
    }

    public static void chooseAccelerometer() {
        Gui.touchAVIew("UITextField", 0);
        Gui.setPicker(0, "Accelerometer");
        Gui.dismissPicker();
    }
    
    
    /* Visible Check */
    public static boolean isAtSettings()
    {
    	return ViewUtils.isExistedView("UILabel", "SETTINGS");
    }

    public static boolean isAtEditProfile()
    {
    	return ViewUtils.isExistedView("UILabel", "EDIT PROFILE");
    }
    
    public static boolean isAtEditGoal()
    {
    	return ViewUtils.isExistedView("UILabel", "MY GOAL IS...");
    }
    
    public static boolean isAtWearingShine()
    {
    	return ViewUtils.isExistedView("UILabel", "WEARING SHINE");
    }
    
}
