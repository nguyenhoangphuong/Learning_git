package com.misfit.ta.gui;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPEnums;

public class SignUp {
	
	public static final int SELECT_SHINE = 0;
	public static final int SELECT_BEDDIT = 2;
	public static final int SELECT_FLASH = 1;
	public static final int LINK_SHINE = 0;
	public static final int LINK_FLASH = 1;
	public static final int LINK_BEDDIT= 2;

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
    
    public static void tapSelectDevice(int deviceSelectCode) {
    	String deviceLabel = "";
    	switch (deviceSelectCode) {
    	case SELECT_SHINE:
    		deviceLabel = DefaultStrings.ShineLabel;
    		break;
    	case SELECT_FLASH :
    		deviceLabel = DefaultStrings.FlashLabel;
    		break;
    	case SELECT_BEDDIT:
    		deviceLabel = DefaultStrings.BedditLabel;
    		break;
    	}
    	
    	Gui.touchAVIew("UIButtonLabel", deviceLabel);
    }
    
    public static void tapSelectLinkDevice(int deviceSelectCode){
    	String deviceLabel = "";
    	switch (deviceSelectCode) {
		case LINK_SHINE:
			deviceLabel = DefaultStrings.ShineLabel;
			break;

		case LINK_FLASH:
			deviceLabel = DefaultStrings.FlashLabel;
			break;
		}
    	Gui.touchAVIew("UILabel", deviceLabel);
    }
    
    public static void tapSignOut() {
    	Gui.touchAVIew("UIButtonLabel", DefaultStrings.SignOutAtProfileViewButton);
    	Gui.touchPopupButton(DefaultStrings.SignOutAtProfileViewButton);
    }
    
    public static void tapSelectColorForShine(int number){
    	Gui.touchAVIew("UILabel", PrometheusHelper.ThemeColor[number]);
    }
    
    public static void tapSelectColorForFlash(int number){
    	Gui.touchAVIew("UILabel", PrometheusHelper.ThemeColorForFlash[number]);
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
    
    public static void tapFlash(){
    	Gui.touchAVIew("UILabel", DefaultStrings.FlashLabel);
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
    
    public static void connectSimulatedBeddit() {
    	
    	NuRemoteClient.sendToServer("(Gui doActionForSwipeGestureIndex: 0 viewName: @\"UIView\" viewIndex: 4 controller: @\"PTBedditSetupViewController\" action: @\"userDidSwipe:\" direction: 1 state: 3)");
    }
    
    
    /* VISIBLE CHECKING */
    public static boolean isSignUpAccountView() {
        return ViewUtils.isExistedView("UILabel", DefaultStrings.SignUpTitle);
    }

    public static boolean isSignUpProfileView() {
        return ViewUtils.isExistedView("UILabel", DefaultStrings.SignUpProfileTitle);
    }

	public static boolean isSelectDeviceView() {
		return ViewUtils.isExistedView("UILabel", DefaultStrings.SelectDeviceTitle);
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

    public static boolean isSignUpConnectBedditView() {
        return ViewUtils.isExistedView("UILabel", DefaultStrings.SignUpConnectBedditTitle);
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
    
    public static void tapSave() {
    	Gui.touchAVIew("UILabel", DefaultStrings.SaveButton);
    }
}
