package com.misfit.ta.ios.modelapi.signup;

import java.io.File;
import java.util.Random;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignUp;

public class SignUpAPI extends ModelAPI {
	private static final Logger logger = Util.setupLogger(SignUpAPI.class);
    public SignUpAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
        super(automation, model, efsm, generator, weight);
    }

	private boolean isMale = true;
	private boolean isUSUnit = true;
	private String h1 = "";
	private String h2 = "";
	private String w1 = "";
	private String w2 = "";
	private String year = "";
	private String month = "";
	private String day = "";
    private static int goal = 0;
    
    /**
     * This method implements the Edge 'e_Init'
     * 
     */
    public void e_Init() {
    	LaunchScreen.launch();
    }
    
    /**
     * This method implements the Edge 'e_ChooseSignUp'
     * 
     */
    public void e_ChooseSignUp() {
        SignUp.tapSignUp();
        ShortcutsTyper.delayOne();
    }
    
    /**
     * This method implements the Edge 'e_Back'
     * 
     */
    public void e_Back() {
        SignUp.tapPrevious();
        ShortcutsTyper.delayOne();
    }

    /**
     * This method implements the Edge 'e_Next'
     * 
     */
    public void e_Next() {
        SignUp.tapNext();
        ShortcutsTyper.delayTime(6000);
    }

    /**
     * This method implements the Edge 'e_SetGoal'
     * 
     */
    public void e_SetGoal() {
        goal = new Random().nextInt(3);
        SignUp.setGoal(goal, SignUp.getCurrentGoal());
        ShortcutsTyper.delayOne();
    }

    /**
     * This method implements the Edge 'e_SignOut'
     * 
     */
    public void e_SignOut() {
    	HomeScreen.tapOpenSettingsTray();
		ShortcutsTyper.delayOne();
		HomeScreen.tapSettings();
		ShortcutsTyper.delayOne();
		
        Gui.swipeUp(1000);
        HomeSettings.tapSignOut();
        ShortcutsTyper.delayOne();
        HomeSettings.chooseSignOut();
        ShortcutsTyper.delayOne();
    }
    
    public void e_SignOutAtProfileView() {
    	SignUp.tapSignOut();
    	ShortcutsTyper.delayOne();
    }

    /**
     * This method implements the Edge 'e_SubmitValidEmailPassword'
     * 
     */
    public void e_SubmitValidEmailPassword() {
        SignUp.enterEmailPassword(MVPApi.generateUniqueEmail(), "test12");
        ShortcutsTyper.delayTime(10000);
    }

    /**
     * This method implements the Edge 'e_Sync'
     * 
     */
    public void e_Sync() {
        SignUp.sync();
        ShortcutsTyper.delayOne();
        SignUp.tapFinishSetup();
        ShortcutsTyper.delayOne();
        
        // wait for sync
        ShortcutsTyper.delayTime(10000);
        PrometheusHelper.handleTutorial();
    }
    
    /**
     * This method implements the Edge 'e_OpenTutorial'
     * 
     */
    public void e_OpenTutorial() {
    	SignUp.tapOpenTutorial();
    	ShortcutsTyper.delayTime(200);
    }
    
    /**
     * This method implements the Edge 'e_OpenTutorial'
     * 
     */
    public void e_CloseTutorial() {
    	SignUp.tapCloseTutorial();
    	ShortcutsTyper.delayTime(200);
    }


    /**
     * This method implements the Edge 'e_inputProfile'
     * 
     */
    public void e_inputProfile() {
    	//input sex
    	this.isMale = PrometheusHelper.coin();
		SignUp.enterGender(isMale);
		
		logger.info("Change male to: " + (isMale ? "Male" : "Female"));
		ShortcutsTyper.delayTime(200);
    	
		//input birthday
		this.year = PrometheusHelper.randInt(1970, 2000) + "";
		this.month = PrometheusHelper.getMonthString(PrometheusHelper.randInt(1, 13), true);
		this.day = PrometheusHelper.randInt(1, 29) + "";
		HomeSettings.updateBirthDay(year, month, day);
		
		logger.info("Change birthday to: " + PrometheusHelper.formatBirthday(year, month, day));
		ShortcutsTyper.delayTime(200);
		
		//input height
		this.h1 = this.isUSUnit ? PrometheusHelper.randInt(4, 8) + "'": PrometheusHelper.randInt(1, 3) + "";
		this.h2 = this.isUSUnit ? PrometheusHelper.randInt(0, 12) + "\\\"": "." + String.format("%2d", PrometheusHelper.randInt(30, 70));
		SignUp.enterHeight(h1, h2, isUSUnit);
		
		logger.info("Change height to: " + h1 + h2 + (isUSUnit? "" : " m"));
		ShortcutsTyper.delayTime(200);
		
    	//input weight
		this.w1 = this.isUSUnit ? PrometheusHelper.randInt(80, 260) + "": PrometheusHelper.randInt(40, 120) + "";
		this.w2 = "." + PrometheusHelper.randInt(0, 10);
		SignUp.enterWeight(w1, w2, isUSUnit);
		
		logger.info("Change weight to: " + w1 + w2 + (isUSUnit? " lbs" : " kg"));
		ShortcutsTyper.delayTime(200);
    }
    
    /**
     * This method implements the Vertex 'v_InitialView'
     * 
     */
    public void v_InitialView() {
        // nothing to do here
    }
    
    /**
     * This method implements the Vertex 'v_HomeScreen'
     * 
     */
    public void v_HomeScreen() {
        // check if current view is HomeScreen
    	Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen");
    }

    /**
     * This method implements the Vertex 'v_SignUpAccount'
     * 
     */
    public void v_SignUpAccount() {
        Assert.assertTrue(SignUp.isSignUpAccountView(), "This is not sign up account view.");
    }

    /**
     * This method implements the Vertex 'v_SignUpProfile'
     * 
     */
    public void v_SignUpProfile() {
        Assert.assertTrue(SignUp.isSignUpProfileView(), "This is not sign up profile view.");
    }

    /**
     * This method implements the Vertex 'v_SignUpGoalUpdated'
     * 
     */
    public void v_SignUpGoalUpdated() {
        Assert.assertTrue(SignUp.getCurrentGoal() == goal, "Goal is not updated");
    }
    
    /**
     * This method implements the Vertex 'v_SignUpGoalTutorial'
     * 
     */
    public void v_SignUpGoalTutorial() {
    	Assert.assertTrue(SignUp.isSignUpTutorialView(), "This is not sign up goal tutorial view.");
    }



    /**
     * This method implements the Vertex 'v_SignUpStep4'
     * 
     */
    public void v_SignUpGoal() {
        Assert.assertTrue(SignUp.isSignUpGoalView(), "This is not sign up goal view.");
    }

    /**
     * This method implements the Vertex 'v_SignUpProfileUpdated'
     * 
     */
    public void v_SignUpProfileUpdated() {
    	//assert sex
    	 boolean male = Gui.getProperty("UIButton", "Male", "isSelected").equals("1") ? true : false;
         Assert.assertTrue(isMale == male, "Gender should be " + (isMale ? "Male" : "Female"));
    	//assert birthday
    	String birthday = PrometheusHelper.formatBirthday(year, month, day);
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", birthday), "Birthday should be " + birthday);
    	//assert height
    	String height = h1 + h2 + (isUSUnit ? "" : " m");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", height), "Height should be " + height);
    	//assert weight
		String weight = w1 + w2 + (isUSUnit ? " lbs" : " kg");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", weight), "Weight should be " + weight);
    }
    
    /**
     * This method implements the Vertex 'v_SignUpStep5'
     * 
     */
    public void v_SignUpPairing() {
        Assert.assertTrue(SignUp.isSignUpPairingView(), "This is not sign up pairing view.");
    }
}
