package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;

public class SignUp {
    public static void tapSignUp() {
        Gui.touchAVIew("UIButton", "SIGN UP");
    }

    /* STEP 1: ENTER EMAIL AND PASSWORD */
    public static void enterEmailPassword(String email, String password) {
        PrometheusHelper.enterEmailPassword(email, password);
    }

    /* STEP 2: PROFILE */
    public static void enterGender(boolean isMale) {
        PrometheusHelper.enterGender(isMale);
    }

    public static void enterBirthDay(String year, String month, String day) {
        PrometheusHelper.enterBirthDay(year, month, day);
    }

    public static void enterHeight(String digit, String fraction, boolean isUSUnit) {
        PrometheusHelper.enterHeight(digit, fraction, isUSUnit);
    }

    public static void enterWeight(String digit, String fraction, boolean isUSUnit) {
        PrometheusHelper.enterWeight(digit, fraction, isUSUnit);
    }

    /* STEP 3: WHAT ARE POINTS? */
    /* STEP 4: SET YOUR GOAL */

    public static void tapNext() {
        Gui.tapNext();
    }

    public static void tapPrevious() {
        Gui.tapPrevious();
    }

    public static int getCurrentGoal() {
        int level = 0;
        String text = Gui.getText("UILabel", 0);
        if ("ACTIVE".equals(text)) {
            level = 0;
        } else if ("VERY ACTIVE".equals(text)) {
            level = 1;
        } else {
            level = 2;
        }
        return level;
    }

    public static void setGoal(int level) {
        Gui.setGoalSlider(level);
    }

    public static void sync() {
        PrometheusHelper.sync();
    }

    public static void tapAllowUseCurrentLocation() {

    }

    public static boolean isSignUpAccountView() {
        return ViewUtils.isExistedView("UILabel", "SIGN UP");
    }

    public static boolean isSignUpProfileView() {
        return ViewUtils.isExistedView("UILabel", "PROFILE");
    }

    public static boolean isSignUpTutorialView() {
        return ViewUtils.isExistedView("UILabel", "WHAT ARE POINTS?");
    }

    public static boolean isSignUpGoalView() {
        return ViewUtils.isExistedView("UILabel", "MY GOAL IS...");
    }

    public static boolean isSignUpPairingView() {
        return ViewUtils.isExistedView("UILabel", "SYNC SHINE");
    }

    public static boolean hasInvalidEmailMessage() {
        return PrometheusHelper.hasInvalidEmailMessage();
    }

    public static boolean hasInvalidPasswordMessage() {
        return PrometheusHelper.hasInvalidPasswordMessage();
    }

    public static boolean hasExistedEmailMessage() {
        return Gui.getPopupTitle().equals("Error")
                && Gui.getPopupContent().equals(DefaultStrings.SignUpDuplicatedEmailMessage);
    }

    public static void tapOK() {
        // popup error message
        Gui.touchPopupButton(0);
    }
}
