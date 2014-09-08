package com.misfit.ta.ios.modelapi.signup;

import java.io.File;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignUp;
import com.misfit.ta.ios.AutomationTest;

public class SignUpGoalAPI extends ModelAPI {

	private String[] level = { "KINDA ACTIVE", "PRETTY ACTIVE", "VERY ACTIVE" };
	private int currentLevel = 1;

	public SignUpGoalAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	
	public void e_Init() {
		LaunchScreen.launch();
	}

	public void e_ChooseSignUp() {
		SignUp.tapSignUp();
	}
	
	public void e_SubmitValidEmailPassword() {
		SignUp.enterEmailPassword(MVPApi.generateUniqueEmail(), "qwerty1");
		PrometheusHelper.waitForView("UILabel", DefaultStrings.SignUpProfileTitle);
	}

	public void e_SubmitProfile() {

		SignUp.enterGender(true);
		SignUp.enterBirthDay();
		SignUp.enterWeight();
		SignUp.enterHeight();
		SignUp.tapShine();

		SignUp.tapSave();
	}
	
	public void e_ShineSelected() {
    	SignUp.tapSelectDevice(SignUp.SELECT_SHINE_FLASH, false);
    }
	
	public void e_OpenTutorial() {
		SignUp.tapOpenTutorial();
	}
	
	public void e_CloseTutorial() {
		SignUp.tapCloseTutorial();
	}

	public void e_SetGoalToLevel1() {
		SignUp.setGoal(0, this.currentLevel);
		this.currentLevel = 0;
	}

	public void e_SetGoalToLevel2() {
		SignUp.setGoal(1, this.currentLevel);
		this.currentLevel = 1;
	}

	public void e_SetGoalToLevel3() {
		SignUp.setGoal(2, this.currentLevel);
		this.currentLevel = 2;
	}





	public void v_InitialView() {
		Assert.assertTrue(LaunchScreen.isAtInitialScreen(),
				"Current view is InitialView");
	}

	public void v_SignUpAccount() {
		Assert.assertTrue(SignUp.isSignUpAccountView(),
				"Current view is SignUp - Account");
	}

	public void v_SignUpGoal() {
		Assert.assertTrue(SignUp.isSignUpGoalView(),
				"Current view is SignUp - Goal");
	}

	public void v_SignUpGoalLevel1() {
		assertGoal(0);
	}

	public void v_SignUpGoalLevel2() {
		assertGoal(1);
	}

	public void v_SignUpGoalLevel3() {
		assertGoal(2);
	}

	public void v_SignUpGoalTutorial() {
		Assert.assertTrue(SignUp.isSignUpTutorialView(),
				"Current view is SignUp - Tutorial");
	}

	public void v_SignUpProfile() {
		Assert.assertTrue(SignUp.isSignUpProfileView(),
				"Current view is SignUp - Profile");
	}
	
	public void v_SelectDevice() {
    	Assert.assertTrue(SignUp.isSelectDeviceView(), "This is not select device view");
    }
	
	private void assertGoal(int goalLevel) {
		
		// check goal level in string
		Assert.assertTrue(
				ViewUtils.isExistedView("UILabel", this.level[goalLevel]),
				"Level string for level " + goalLevel + " is not correct");

        // check suggest time with correct calculation
    	String[] suggestTimeStrings = SignUp.getHowToHitCurrentGoal();

        Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel", "_WALK_\\n " + suggestTimeStrings[0]), "Suggest time for walking is correct");
        Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel", "_RUN_\\n " + suggestTimeStrings[1]), "Suggest time for running is correct");
        Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel", "_SWIM_\\n " + suggestTimeStrings[2]), "Suggest time for swimming is correct");
	}

}
