package com.misfit.ta.ios.modelapi.signup;

import java.io.File;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignUp;
import com.misfit.ta.ios.AutomationTest;

public class SignUpGoalAPI extends ModelAPI {
	private static final Logger logger = Util.setupLogger(SignUpAPI.class);

	private int heightInInches = 0;
	private int weightInLbs = 0;
	private int[] goalPoints = { 600, 1000, 1600 };
	String[] runningEquals = { "60", "100", "160" };
	String[] walkingEquals = { "6,000", "10,000", "16,000" };
	
	public SignUpGoalAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

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
	 * This method implements the Edge 'e_CloseTutorial'
	 * 
	 */
	public void e_CloseTutorial() {
		SignUp.tapCloseTutorial();
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Edge 'e_SubmitProfile'
	 * 
	 */
	public void e_SubmitProfile() {
		this.weightInLbs = PrometheusHelper.randInt(100, 150);
		this.heightInInches = PrometheusHelper.randInt(50, 70);
		
		String w1 = this.weightInLbs + "";
		String w2 = ".0";
		String h1 = this.heightInInches / 12 + "'";
		String h2 = this.heightInInches % 12 + "\\\"";
		
		SignUp.enterWeight(w1, w2, true);
		SignUp.enterHeight(h1, h2, true);
		
		SignUp.tapNext();
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Edge 'e_OpenTutorial'
	 * 
	 */
	public void e_OpenTutorial() {
		SignUp.tapOpenTutorial();
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Edge 'e_SetGoalToLevel1'
	 * 
	 */
	public void e_SetGoalToLevel1() {
		SignUp.setGoal(0);
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Edge 'e_SetGoalToLevel2'
	 * 
	 */
	public void e_SetGoalToLevel2() {
		SignUp.setGoal(1);
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Edge 'e_SetGoalToLevel3'
	 * 
	 */
	public void e_SetGoalToLevel3() {
		SignUp.setGoal(2);
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Edge 'e_SubmitValidEmailPassword'
	 * 
	 */
	public void e_SubmitValidEmailPassword() {
		// trial mod is enough
		SignUp.tapNext();
	}

	
	
	
	/**
	 * This method implements the Vertex 'v_InitialView'
	 * 
	 */
	public void v_InitialView() {
		Assert.assertTrue(LaunchScreen.isAtInitialScreen(), "Current view is InitialView");
	}

	/**
	 * This method implements the Vertex 'v_SignUpAccount'
	 * 
	 */
	public void v_SignUpAccount() {
		Assert.assertTrue(SignUp.isSignUpAccountView(), "Current view is SignUp - Account");
	}

	/**
	 * This method implements the Vertex 'v_SignUpGoal'
	 * 
	 */
	public void v_SignUpGoal() {
		Assert.assertTrue(SignUp.isSignUpGoalView(), "Current view is SignUp - Goal");
	}

	/**
	 * This method implements the Vertex 'v_SignUpGoalLevel1'
	 * 
	 */
	private void assertGoal(int goalLevel)
	{
		Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel", String.format(" _Running_ %s city blocks, or", this.runningEquals[goalLevel])), "Running equivalent for level " + goalLevel + " is correct");
		Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel", String.format(" _Walking_ %s steps, or", this.walkingEquals[goalLevel])), "Running equivalent for level " + goalLevel + " is correct");
		
		//int calories = (int)(PrometheusHelper.calculateCalories(this.goalPoints[goalLevel], weightInLbs) * 2.5);
		//Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel", String.format(" _Burning_ for %d calories.", calories)), "Burning equivalent for level " + goalLevel + " is correct");
	}
	
	public void v_SignUpGoalLevel1() {
		Assert.assertTrue(SignUp.isSignUpGoalView(), "Current view is SignUp - Goal level 0");
		assertGoal(0);
	}

	/**
	 * This method implements the Vertex 'v_SignUpGoalLevel2'
	 * 
	 */
	public void v_SignUpGoalLevel2() {
		Assert.assertTrue(SignUp.isSignUpGoalView(), "Current view is SignUp - Goal level 1");
		assertGoal(1);
	}

	/**
	 * This method implements the Vertex 'v_SignUpGoalLevel3'
	 * 
	 */
	public void v_SignUpGoalLevel3() {
		Assert.assertTrue(SignUp.isSignUpGoalView(), "Current view is SignUp - Goal level 2");
		assertGoal(2);
	}

	/**
	 * This method implements the Vertex 'v_SignUpGoalTutorial'
	 * 
	 */
	public void v_SignUpGoalTutorial() {
		Assert.assertTrue(SignUp.isSignUpTutorialView(), "Current view is SignUp - Tutorial");
	}

	/**
	 * This method implements the Vertex 'v_SignUpProfile'
	 * 
	 */
	public void v_SignUpProfile() {
		Assert.assertTrue(SignUp.isSignUpProfileView(), "Current view is SignUp - Profile");
	}

}
