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

	boolean isMale = true;
	boolean isUSUnit = true;
	String h1 = "", h2 = "";
	String w1 = "", w2 = "";
	String year = "", month = "", day = "";
    private static int goal = 0;
    
//    private static String[] goalPoints = {"1500", "2500", "4000" };
//    private static String[] goalEqualRunnings = {"60", "100", "160"};
//    private static String[] goalEqualCyclings = {"21", "35", "57"};
//    private static String[] goalEqualWalkings = {"6,000", "10,000", "16,000"};
//    private static String[] goalEqualBurnings = {"0.2", "0.3", "0.5"};

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
        ShortcutsTyper.delayTime(1000);
    }
    
    /**
     * This method implements the Edge 'e_Back'
     * 
     */
    public void e_Back() {
        SignUp.tapPrevious();
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_Next'
     * 
     */
    public void e_Next() {
        SignUp.tapNext();
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_SetGoal'
     * 
     */
    public void e_SetGoal() {
        goal = new Random().nextInt(3);
        SignUp.setGoal(goal);
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_SignOut'
     * 
     */
    public void e_SignOut() {
        HomeScreen.tapSettings();
        ShortcutsTyper.delayTime(1000);
        Gui.swipeUp(1000);
        HomeSettings.tapSignOut();
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_SubmitValidEmailPassword'
     * 
     */
    public void e_SubmitValidEmailPassword() {
        SignUp.enterEmailPassword(MVPApi.generateUniqueEmail(), "test12");
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_Sync'
     * 
     */
    public void e_Sync() {
        SignUp.sync();
        ShortcutsTyper.delayTime(10000);
        SignUp.tapFinishSetup();
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_inputBirthDay'
     * 
     */
    public void e_inputBirthDay() {
    	this.year = PrometheusHelper.randInt(1970, 2000) + "";
		this.month = PrometheusHelper.getMonthString(PrometheusHelper.randInt(1, 13), true);
		this.day = PrometheusHelper.randInt(1, 29) + "";
		HomeSettings.updateBirthDay(year, month, day);
		
		logger.info("Change birthday to: " + PrometheusHelper.formatBirthday(year, month, day));
		ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_inputHeight'
     * 
     */
    public void e_inputHeight() {
		this.isUSUnit = PrometheusHelper.coin();
		this.h1 = this.isUSUnit ? PrometheusHelper.randInt(4, 8) + "'": PrometheusHelper.randInt(1, 3) + "";
		this.h2 = this.isUSUnit ? PrometheusHelper.randInt(0, 12) + "\\\"": "." + String.format("%2d", PrometheusHelper.randInt(50, 100));
		SignUp.enterHeight(h1, h2, isUSUnit);
		
		logger.info("Change height to: " + h1 + h2 + (isUSUnit? "" : " m"));
		ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_inputSex'
     * 
     */
    public void e_inputSex() {
		this.isMale = PrometheusHelper.coin();
		SignUp.enterGender(isMale);
		
		logger.info("Change male to: " + (isMale ? "Male" : "Female"));
		ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_inputWeight'
     * 
     */
    public void e_inputWeight() {
		this.isUSUnit = PrometheusHelper.coin();
		this.w1 = this.isUSUnit ? PrometheusHelper.randInt(80, 260) + "": PrometheusHelper.randInt(40, 120) + "";
		this.w2 = "." + PrometheusHelper.randInt(0, 10);
		SignUp.enterWeight(w1, w2, isUSUnit);
		
		logger.info("Change weight to: " + w1 + w2 + (isUSUnit? " lbs" : " kg"));
		ShortcutsTyper.delayTime(1000);
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
     * This method implements the Vertex 'v_SignUpStep4'
     * 
     */
    public void v_SignUpGoal() {
        Assert.assertTrue(SignUp.isSignUpGoalView(), "This is not sign up goal view.");
    }

    /**
     * This method implements the Vertex 'v_SignUpStep4UpdatedBirthday'
     * 
     */
    public void v_SignUpProfileUpdatedBirthday() {      
		String birthday = PrometheusHelper.formatBirthday(year, month, day);
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", birthday), "Birthday should be " + birthday);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep4UpdatedSex'
     * 
     */
    public void v_SignUpProfileUpdatedSex() {
        boolean male = Gui.getProperty("UIButton", "Male", "isSelected").equals("1") ? true : false;
        Assert.assertTrue(isMale == male, "Gender should be " + (isMale ? "Male" : "Female"));
    }

    /**
     * This method implements the Vertex 'v_SignUpStep4UpdatedHeight'
     * 
     */
    public void v_SignUpProfileUpdatedHeight() {
		String height = h1 + h2 + (isUSUnit ? "" : " m");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", height), "Height should be " + height);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep4UpdatedWeight'
     * 
     */
    public void v_SignUpProfileUpdatedWeight() {
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
