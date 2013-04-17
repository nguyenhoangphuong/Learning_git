package com.misfit.ta.ios.modelapi.signup;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.SignUp;
public class SignUpAPI extends ModelAPI {
	public SignUpAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	/**
	 * This method implements the Edge 'e_Back'
	 * 
	 */
	public void e_Back() {
		SignUp.tapPrevious();
		ShortcutsTyper.delayTime(500);
	}
	
	
	/**
	 * This method implements the Edge 'e_BackToStep4'
	 * 
	 */
	public void e_BackToStep4() {
		Gui.touchAVIew("UIButton", 2);
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_ChooseSignUp'
	 * 
	 */
	public void e_ChooseSignUp() {
		SignUp.tapSignUp();
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
	 * This method implements the Edge 'e_NextToStep3'
	 * 
	 */
	public void e_NextToStep3() {
		Gui.touchAVIew("UIButton", 3);
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_SetGoal'
	 * 
	 */
	public void e_SetGoal() {
		SignUp.setGoal(2);
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_SignOut'
	 * 
	 */
	public void e_SignOut() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_SubmitValidEmailPassword'
	 * 
	 */
	public void e_SubmitValidEmailPassword() {
		SignUp.enterEmailPassword("test165@thy.com", "test12");
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_Sync'
	 * 
	 */
	public void e_Sync() {
		SignUp.sync();
	}

	/**
	 * This method implements the Edge 'e_inputBirthDay'
	 * 
	 */
	public void e_inputBirthDay() {
		SignUp.enterBirthDay("1990", "May", "20");
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_inputHeight'
	 * 
	 */
	public void e_inputHeight() {
		SignUp.enterHeight("6'", "3\\\"", true);
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_inputSex'
	 * 
	 */
	public void e_inputSex() {
		SignUp.enterGender(false);
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_inputWeight'
	 * 
	 */
	public void e_inputWeight() {
		
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
		Assert.assertTrue(SignUp.isSignUpStep1View(), "This is not sign up step1 view.");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep2'
	 * 
	 */
	public void v_SignUpStep2() {
		Assert.assertTrue(SignUp.isSignUpStep2View(), "This is not sign up step2 view.");
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
		Assert.assertTrue(SignUp.isSignUpStep3View(), "This is not sign up step3 view.");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep4'
	 * 
	 */
	public void v_SignUpStep4() {
		Assert.assertTrue(SignUp.isSignUpStep4View(), "This is not sign up step4 view.");
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
		Assert.assertTrue(SignUp.isSignUpStep5View(), "This is not sign up step5 view.");
	}
}
