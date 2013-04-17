package com.misfit.ta.ios.modelapi.signup;

import java.io.File;
import java.security.Timestamp;
import java.util.Date;
import java.util.Random;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.SignUp;

public class SignUpAPI extends ModelAPI {
	public SignUpAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private static String year;
	private static String month;
	private static String day;
	private static String hFraction;
	private static String hDigit;
	private static String wDigit;
	private static String wFraction;
	private static boolean isMale;
	private static boolean isUSMetric;
	private static String[] months = { "January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November",
			"December" };

	/**
	 * This method implements the Edge 'e_Back'
	 * 
	 */
	public void e_Back() {
		SignUp.tapPrevious();
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_ChooseSignUp'
	 * 
	 */
	public void e_ChooseSignUp() {
		SignUp.tapSignUp();
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_Next'
	 * 
	 */
	public void e_Next() {
		SignUp.tapNext();
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_SetGoal'
	 * 
	 */
	public void e_SetGoal() {
		SignUp.setGoal(2);
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_SignOut'
	 * 
	 */
	public void e_SignOut() {
		HomeScreen.tapSettings();
		Gui.swipeUp(1000);
		HomeSettings.tapSignOut();
		ShortcutsTyper.delayTime(3000);
	}

	/**
	 * This method implements the Edge 'e_SubmitValidEmailPassword'
	 * 
	 */
	public void e_SubmitValidEmailPassword() {
		SignUp.enterEmailPassword(generateEmail(), "test12");
		ShortcutsTyper.delayTime(8000);
	}

	/**
	 * This method implements the Edge 'e_Sync'
	 * 
	 */
	public void e_Sync() {
		SignUp.sync();
		ShortcutsTyper.delayTime(30000);
	}

	/**
	 * This method implements the Edge 'e_inputBirthDay'
	 * 
	 */
	public void e_inputBirthDay() {
		generateBirthDay();
		SignUp.enterBirthDay(year, month, day);
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_inputHeight'
	 * 
	 */
	public void e_inputHeight() {
		generateHeight(true);
		SignUp.enterHeight(hDigit, hFraction, true);
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_inputSex'
	 * 
	 */
	public void e_inputSex() {
		isMale = new Random().nextBoolean();
		SignUp.enterGender(isMale);
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_inputWeight'
	 * 
	 */
	public void e_inputWeight() {
		generateWeight(true);
		SignUp.enterWeight(wDigit, wFraction, true);
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_InitialView'
	 * 
	 */
	public void v_InitialView() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep1'
	 * 
	 */
	public void v_SignUpStep1() {
		Assert.assertTrue(SignUp.isSignUpStep1View(),
				"This is not sign up step1 view.");
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep2'
	 * 
	 */
	public void v_SignUpStep2() {
		Assert.assertTrue(SignUp.isSignUpStep2View(),
				"This is not sign up step2 view.");
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep2Updated'
	 * 
	 */
	public void v_SignUpStep2Updated() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep3'
	 * 
	 */
	public void v_SignUpStep3() {
		Assert.assertTrue(SignUp.isSignUpStep3View(),
				"This is not sign up step3 view.");
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep4'
	 * 
	 */
	public void v_SignUpStep4() {
		Assert.assertTrue(SignUp.isSignUpStep4View(),
				"This is not sign up step4 view.");
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep4Updated'
	 * 
	 */
	public void v_SignUpStep4Updated() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep5'
	 * 
	 */
	public void v_SignUpStep5() {
		Assert.assertTrue(SignUp.isSignUpStep5View(),
				"This is not sign up step5 view.");
		ShortcutsTyper.delayTime(500);
	}

	private String generateEmail() {
		Date today = new Date();
		return "qatest" + String.valueOf(today.getTime()) + "@test.com";
	}

	private void generateBirthDay() {
		Random generator = new Random();
		year = String.valueOf(1930 + generator.nextInt(71));
		month = months[generator.nextInt(12)];
		day = String.valueOf(generator.nextInt(28) + 1);
	}
	
	private void generateHeight(boolean USMetric) {
		Random generator = new Random();
		isUSMetric = USMetric;
		if (isUSMetric) {
			hDigit = String.valueOf(3 + generator.nextInt(6)) + "'";
			hFraction = String.valueOf(generator.nextInt(12)) + "\\\"";
		} else {
			hDigit = String.valueOf(generator.nextInt(2) + 1);
			int fraction = generator.nextInt(51);
			if (fraction < 10) {
				hFraction = ".0" + String.valueOf(fraction);
			} else {
				hFraction = "." + String.valueOf(fraction);
			}
		}
	}
	
	private void generateWeight(boolean USMetric) {
		Random generator = new Random();
		isUSMetric = USMetric;
		if (isUSMetric) {
			wDigit = String.valueOf(77 + generator.nextInt(295));
		} else {
			wDigit = String.valueOf(35 + generator.nextInt(115));
		}
		wFraction = "." + String.valueOf(generator.nextInt(10));
	}
}
