package com.misfit.ta.ios.modelapi.signup;

import java.io.File;
import java.util.Random;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignUp;
import com.misfit.ta.gui.Timeline;

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
    

    public void e_Init() {
    	LaunchScreen.launch();
    }
 
    public void e_ChooseSignUp() {
        SignUp.tapSignUp();
    }
    
    public void e_SubmitValidEmailPassword() {
        SignUp.enterEmailPassword(MVPApi.generateUniqueEmail(), "test12");
        PrometheusHelper.waitForView("UILabel", DefaultStrings.SignUpProfileTitle);
    }
    
    public void e_SignOutAtProfileView() {
    	SignUp.tapSignOut();
    }
    
    public void e_inputProfile() {
    	
    	// input sex
    	this.isMale = PrometheusHelper.coin();
		SignUp.enterGender(isMale);
		
		logger.info("Change male to: " + (isMale ? "Male" : "Female"));
    	
		// input birthday
		this.year = PrometheusHelper.randInt(1970, 2000) + "";
		this.month = PrometheusHelper.getMonthString(PrometheusHelper.randInt(1, 13), true);
		this.day = PrometheusHelper.randInt(1, 29) + "";
		HomeSettings.updateBirthDay(year, month, day);
		
		logger.info("Change birthday to: " + PrometheusHelper.formatBirthday(year, month, day));
		
		// input height
		this.h1 = this.isUSUnit ? PrometheusHelper.randInt(4, 8) + "'": "1";
		this.h2 = this.isUSUnit ? PrometheusHelper.randInt(0, 12) + "\\\"": "." + String.format("%2d", PrometheusHelper.randInt(30, 70));
		SignUp.enterHeight(h1, h2, isUSUnit);
		
		logger.info("Change height to: " + h1 + h2 + (isUSUnit? "" : " m"));
		
    	// input weight
		this.w1 = this.isUSUnit ? PrometheusHelper.randInt(80, 260) + "": PrometheusHelper.randInt(40, 120) + "";
		this.w2 = "." + PrometheusHelper.randInt(0, 10);
		SignUp.enterWeight(w1, w2, isUSUnit);
		
		logger.info("Change weight to: " + w1 + w2 + (isUSUnit? " lbs" : " kg"));
    }
    
    public void e_SetGoal() {
        goal = new Random().nextInt(3);
        SignUp.setGoal(goal, SignUp.getCurrentGoal());
    }
    
    public void e_OpenTutorial() {
    	SignUp.tapOpenTutorial();
    }
    
    public void e_CloseTutorial() {
    	SignUp.tapCloseTutorial();
    }
    
    public void e_toLinkShine() {
    	
    	SignUp.tapNext();
    	PrometheusHelper.waitForView("UILabel", DefaultStrings.SignUpLinkShineTitle);
    }

    public void e_Next() {
        SignUp.tapNext();
    }
    
    public void e_Back() {
        SignUp.tapPrevious();
    }
    
    public void e_SignOut() {
    	PrometheusHelper.signOut();
    }
    
    public void e_Sync() {
        SignUp.sync();
		PrometheusHelper.waitForView("PTRichTextLabel", DefaultStrings.TutorialFirstPageLabel);
		
		PrometheusHelper.handleTutorial();
    }
 
   
    
    

    public void v_InitialView() {
    	Assert.assertTrue(LaunchScreen.isAtInitialScreen(), "Current view is InitialScreen");
    }
    
    public void v_HomeScreen() {
    	Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen");
    	
    	// check tutorial tiles
    	Timeline.dragUpTimelineAndHandleTutorial();
    	Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.TileTapMeLabel), "Tap Me! tile is displayed");
    	Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.TileActivitiesLabel), "Activities tile is displayed");
    	Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.TileSleepLabel), "Sleep tile is displayed");
    	Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.TileMilestonesLabel), "Milestones tile is displayed");
    }

    public void v_SignUpAccount() {
        Assert.assertTrue(SignUp.isSignUpAccountView(), "This is not sign up account view.");
    }

    public void v_SignUpProfile() {
        Assert.assertTrue(SignUp.isSignUpProfileView(), "This is not sign up profile view.");
    }

    public void v_SignUpGoalUpdated() {

        Assert.assertTrue(SignUp.getCurrentGoal() == goal, "Goal is not updated");       
    }
    
    public void v_SignUpGoalTutorial() {
    	Assert.assertTrue(SignUp.isSignUpTutorialView(), "This is not sign up goal tutorial view.");
    }

    public void v_SignUpGoal() {
        Assert.assertTrue(SignUp.isSignUpGoalView(), "This is not sign up goal view.");
    }
    
    public void v_SignUpProfileUpdated() {
    	// assert sex
    	boolean male = Gui.getProperty("UIButton", "Male", "isSelected").equals("1") ? true : false;
    	Assert.assertTrue(isMale == male, "Gender should be " + (isMale ? "Male" : "Female"));
    	
    	// assert birthday
    	String birthday = PrometheusHelper.formatBirthday(year, month, day);
    	Assert.assertTrue(ViewUtils.isExistedView("UILabel", birthday), "Birthday should be " + birthday);
    	
    	// assert height
    	String height = h1 + h2 + (isUSUnit ? "" : " m");
    	Assert.assertTrue(ViewUtils.isExistedView("UILabel", height), "Height should be " + height);
    	
    	// assert weight
    	String weight = w1 + w2 + (isUSUnit ? " lbs" : " kg");
    	Assert.assertTrue(ViewUtils.isExistedView("UILabel", weight), "Weight should be " + weight);
    }
    
    public void v_SignUpPairing() {
        Assert.assertTrue(SignUp.isSignUpPairingView(), "This is not sign up pairing view.");
    }

}
