package com.misfit.ta.gui;

public class HomeSettings {
    public static void tapAdjustGoal() {
        Gui.touchAVIew("UILabel", "ADJUST GOAL");
    }

    public static void tapYourProfile() {
        Gui.touchAVIew("UILabel", "YOUR PROFILE");
    }

    public static void tapWearingShine() {
        Gui.touchAVIew("UILabel", "WEARING SHINE");
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

    /* Set Daily Goal */
    public static void tapOKNewGoal() {
        // TODO to complete
        Gui.touchAVIew("UIButton", 1);
    }

    public static void tapCancelNewGoal() {
        // TODO to complete
        Gui.touchAVIew("UIButton", 0);
    }

    public static void editNewGoal() {
        Gui.touchAVIew("PTNumberSpinner", 0);
    }

    /* Mode Debug */
    public static void chooseManual() {
        Gui.touchAVIew("UITextField", 0);
    }

    public static void chooseAccelerometer() {
        Gui.touchAVIew("UITextField", 0);
    }
}
