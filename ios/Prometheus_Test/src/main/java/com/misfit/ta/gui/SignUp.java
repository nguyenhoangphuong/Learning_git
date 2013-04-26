package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class SignUp {

    /* NAVIGATION */
    public static void tapSignUp() {
        Gui.touchAVIew("UIButtonLabel", " SIGN UP");
    }
    
    public static void tapNext() {
        Gui.tapNext();
    }

    public static void tapPrevious() {
        Gui.tapPrevious();
    }
    
	public static void tapCloseAllTips() 
	{
		for (int i = 0; i < 3; i++) 
		{
			Gui.touchAVIew("PTGoalCircleView", 0);
			ShortcutsTyper.delayTime(1000);
		}
		
		Gui.touchAVIew("UIButton", "DISMISS IT");
		ShortcutsTyper.delayTime(1000);
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

    
    /* STEP 3: SET YOUR GOAL */
    public static int getCurrentGoal() {
        String text = Gui.getText("UILabel", 0);
        
        if(text.equals("1500"))
        	return 0;
        if(text.equals("2500"))
        	return 1;
        if(text.equals("4000"))
        	return 2;
        
        return -1;
    }
    
    public static void setGoal(int level) {
        Gui.setGoalSlider(level);
    }
    
    public static void tapOpenTutorial()
    {
    	Gui.touchAVIew("UIButton", 0);
    }
    
    public static void tapCloseTutorial()
    {
    	Gui.touchAVIew("UIButton", 6);
    }
    

    /* STEP 4: PARING */
    public static void sync() {
        PrometheusHelper.sync();
    }
    
    public static void tapFinishSetup()
    {
    	Gui.touchAVIew("UIButton", "FINISH SETUP");
    }

    
    /* UTILITIES */
    public static void tapAllowUseCurrentLocation() {

    }

    public static boolean isSignUpAccountView() {
        return ViewUtils.isExistedView("UILabel", "SIGN UP");
    }

    public static boolean isSignUpProfileView() {
        return ViewUtils.isExistedView("UILabel", "PROFILE");
    }

    public static boolean isSignUpTutorialView() {
        return  ViewUtils.isExistedView("UILabel", "_walking_") &&
        		ViewUtils.isExistedView("UILabel", "_swimming_") &&
        		ViewUtils.isExistedView("UILabel", "and even _cooking!_");
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
