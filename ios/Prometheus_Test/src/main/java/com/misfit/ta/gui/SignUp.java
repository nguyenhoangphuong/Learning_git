package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPEnums;

public class SignUp {

    /* NAVIGATION */
    public static void tapSignUp() {
        Gui.touchAVIew("UIButtonLabel", DefaultStrings.SignUpButton);
    }

    public static void tapNext() {
    	Gui.touchAVIew("UIButtonLabel", DefaultStrings.NextButton);
    }

    public static void tapPrevious() {
    	Gui.touchAVIew("UIButtonLabel", DefaultStrings.BackButton);
    }
    
    public static void tapSignOut() {
    	Gui.touchAVIew("UIButtonLabel", DefaultStrings.SignOutAtProfileViewButton);
    	Gui.touchPopupButton(DefaultStrings.SignOutAtProfileViewButton);
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
    
    public static void enterBirthDay() {
    	PrometheusHelper.enterBirthDay();
    }

    public static void enterHeight(String digit, String fraction, boolean isUSUnit) {
        PrometheusHelper.enterHeight(digit, fraction, isUSUnit);
    }
    
    public static void enterHeight() {
    	PrometheusHelper.enterHeight();
    }

    public static void enterWeight(String digit, String fraction, boolean isUSUnit) {
        PrometheusHelper.enterWeight(digit, fraction, isUSUnit);
    }
    
    public static void enterWeight() {
    	PrometheusHelper.enterWeight();
    }
    
    public static void tapBeddit() {
    	
    	Gui.touchAVIew("UILabel", DefaultStrings.BedditLabel);
    }
    
    public static void tapShine() {
    	
    	Gui.touchAVIew("UILabel", DefaultStrings.ShineLabel);
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
    
    public static String[] getHowToHitCurrentGoal() {
    	
    	String text = Gui.getText("UILabel", 0);
        int goal = Integer.parseInt(text);
        
        int walkTime = MVPCalculator.calculateNearestTimeRemainInMinute(goal, MVPEnums.ACTIVITY_WALKING);
        int runTime = MVPCalculator.calculateNearestTimeRemainInMinute(goal, MVPEnums.ACTIVITY_RUNNING);
        int swimTime = MVPCalculator.calculateNearestTimeRemainInMinute(goal, MVPEnums.ACTIVITY_SWIMMING);
        
        return new String[] { 
        		PrometheusHelper.convertNearestTimeInMinuteToString(walkTime),
        		PrometheusHelper.convertNearestTimeInMinuteToString(runTime),
        		PrometheusHelper.convertNearestTimeInMinuteToString(swimTime),
        };        
    }
    
    public static void setGoal(int level) {
    	// Current goal is 1 as default 
    	setGoal(level, getCurrentGoal());
    }
    
    public static void setGoal(int level, int currentLevel) {
    	if (level < currentLevel) {
    		for (int i = 0; i < currentLevel - level; i++) {
    			Gui.swipeLeftToSetGoal();
    		}
    	} else {
    		for (int i = 0; i < level - currentLevel; i++) {
    			Gui.swipeRightToSetGoal();
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
    
    public static void triggerSync() {
    	
    }
    
    public static void tapFinishSetup()
    {
    	Gui.touchAVIew("UIButton", DefaultStrings.FinishSetUpButton);
    }

    
    /* VISIBLE CHECKING */
    public static boolean isSignUpAccountView() {
        return ViewUtils.isExistedView("UILabel", DefaultStrings.SignUpTitle);
    }

    public static boolean isSignUpProfileView() {
        return ViewUtils.isExistedView("UILabel", DefaultStrings.SignUpProfileTitle);
    }

	public static boolean isSignUpTutorialView() {
		int i = 0;
		while (i < DefaultStrings.SignUpTutorial.length
				&& ViewUtils.isExistedView("PTRichTextLabel",
						DefaultStrings.SignUpTutorial[i])) {
			i++;
		}
		return i == DefaultStrings.SignUpTutorial.length;
	}

    public static boolean isSignUpGoalView() {
        return ViewUtils.isExistedView("UILabel", DefaultStrings.SignUpSetYourGoalTitle);
    }

    public static boolean isSignUpPairingView() {
        return ViewUtils.isExistedView("UILabel", DefaultStrings.SignUpLinkShineTitle);
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
