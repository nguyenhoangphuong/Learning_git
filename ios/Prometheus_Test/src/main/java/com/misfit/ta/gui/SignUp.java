package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.utils.ShortcutsTyper;

public class SignUp {

    /* NAVIGATION */
    public static void tapSignUp() {
        Gui.touchAVIew("UIButtonLabel", " SIGN UP");
    }

    public static void tapNext() {
    	Gui.touchAVIew("UIButtonLabel", "Next");
    }

    public static void tapPrevious() {
    	Gui.touchAVIew("UIButtonLabel", "Back");
    }
    
    public static void tapSignOut() {
    	Gui.touchAVIew("UIButtonLabel", "Sign out");
    	Gui.touchPopupButton("Sign out");
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
        
        if(text.equals("600"))
        	return 0;
        if(text.equals("1000"))
        	return 1;
        if(text.equals("1600"))
        	return 2;
        
        return -1;
    }
    
    public static void setGoal(int level) {
    	// Current goal is 1 as default 
    	setGoal(level, 1);
    }
    
    public static void setGoal(int level, int currentLevel) {
    	if (level < currentLevel) {
    		for (int i = 0; i < currentLevel - level; i++) {
    			Gui.swipeLeft(500);
    		}
    	} else {
    		for (int i = 0; i < level - currentLevel; i++) {
    			Gui.swipeRight(500);
    		}
    	}
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

    
    /* VISIBLE CHECKING */
    public static boolean isSignUpAccountView() {
        return ViewUtils.isExistedView("UILabel", "Sign up");
    }

    public static boolean isSignUpProfileView() {
        return ViewUtils.isExistedView("UILabel", "Profile");
    }

    public static boolean isSignUpTutorialView() {
        return  ViewUtils.isExistedView("PTRichTextLabel", "\\_Walking\\_") &&
        		ViewUtils.isExistedView("PTRichTextLabel", "\\_Swimming\\_") &&
        		ViewUtils.isExistedView("PTRichTextLabel", "\\_Biking\\_") &&
        		ViewUtils.isExistedView("PTRichTextLabel", "\\_Tennis\\_") &&
        		ViewUtils.isExistedView("PTRichTextLabel", "Even\\n\\_dancing!\\_");
    }

    public static boolean isSignUpGoalView() {
        return ViewUtils.isExistedView("UILabel", "Set your goal");
    }

    public static boolean isSignUpPairingView() {
        return ViewUtils.isExistedView("UILabel", "Link Shine");
    }

    public static boolean isSignUpPairingCompleteView() {
    	return ViewUtils.isExistedView("UILabel", "LINKING COMPLETE");
    }
    
    
    /* ALERT */
    public static boolean hasSignUpInvalidEmailMessage() {
        return PrometheusHelper.hasSignUpInvalidEmailMessage();
    }

    public static boolean hasInvalidPasswordMessage() {
        return PrometheusHelper.hasInvalidPasswordMessage();
    }

    public static boolean hasExistedEmailMessage() {
        return PrometheusHelper.hasExistedEmailMessage();
    }

    public static void tapOK() {
        // popup error message
        Gui.touchPopupButton(0);
    }

}
