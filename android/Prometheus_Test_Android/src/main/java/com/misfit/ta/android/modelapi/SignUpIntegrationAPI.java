package com.misfit.ta.android.modelapi;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.gui.HomeScreen;
import com.misfit.ta.android.gui.Settings;
import com.misfit.ta.android.gui.SignIn;
import com.misfit.ta.android.gui.SignUp;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.modelAPI.ModelAPI;

public class SignUpIntegrationAPI extends ModelAPI {
	public SignUpIntegrationAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	String email = System.nanoTime() + "@qa.automation";
	String password = "qwerty1";
	String sex = "Male";
	int[] birthday = {16, 9, 1991};
	float height = 170;
	float weight = 69.0f;
	boolean isUS = false;
	int goalLevel = 0;
	
	/**
	 * This method implements the Edge 'e_init'
	 * 
	 */
	public void e_init() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_setGoal'
	 * 
	 */
	public void e_setGoal() {
		SignUp.setGoal(goalLevel);
		SignUp.pressNext();
		Helper.wait1();
  }

	/**
	 * This method implements the Edge 'e_setupDevice'
	 * 
	 */
	public void e_setupDevice() {
		SignUp.startSyncing();
		Helper.wait(7000);
		SignUp.stopSyncing();
	}

	/**
	 * This method implements the Edge 'e_submit'
	 * 
	 */
	public void e_submit() {
		SignUp.inputEmail(email);
		SignUp.inputPassword(password);
		SignUp.pressNext();
		Helper.wait(3000);
	}

	/**
	 * This method implements the Edge 'e_submitProfile'
	 * 
	 */
	public void e_submitProfile() {
		SignUp.inputSex(true);
		SignUp.inputBirthday(birthday[0], birthday[1], birthday[2]);
		SignUp.inputWeight(weight, isUS);
		SignUp.inputHeight(height, isUS);
		Helper.wait1();
	}

	/**
	 * This method implements the Edge 'e_tapBack'
	 * 
	 */
	public void e_tapBack() {
		SignUp.pressBack();
		Helper.wait1();
	}

	/**
	 * This method implements the Edge 'e_tapIDontHaveAnAccount'
	 * 
	 */
	public void e_tapIDontHaveAnAccount() {
		SignUp.chooseSignUp();
		Helper.wait1();
	}

	/**
	 * This method implements the Edge 'e_toProfile'
	 * 
	 */
	public void e_toProfile() {
		HomeScreen.tapSettingsMenu();
		Helper.wait(200);
		Settings.tapSettings();
		Helper.wait1();
	}

	/**
	 * This method implements the Edge 'e_toProgress'
	 * 
	 */
	public void e_toProgress() {
		SignUp.pressNext();
		Helper.wait1();
	}

	/**
	 * This method implements the Vertex 'v_InitialView'
	 * 
	 */
	public void v_InitialView() {
		Assert.assertTrue(SignIn.isInitViewVisible(), "At InitialView");
	}

	/**
	 * This method implements the Vertex 'v_Profile'
	 * 
	 */
	public void v_Profile() {
		Assert.assertTrue(Settings.hasBirthDateField(), "At Settings > Profile");
	}

	/**
	 * This method implements the Vertex 'v_Progress'
	 * 
	 */
	public void v_Progress() {
		Assert.assertTrue(HomeScreen.isHomeScreenVisible(), "At HomeScreen");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep1'
	 * 
	 */
	public void v_SignUpStep1() {
		Assert.assertTrue(SignUp.isAtStep1(), "At Step 1");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep2'
	 * 
	 */
	public void v_SignUpStep2() {
		Assert.assertTrue(SignUp.isAtStep2(), "At Step 2");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep3'
	 * 
	 */
	public void v_SignUpStep3() {
		Assert.assertTrue(SignUp.isAtStep3(), "At Step 3");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep4Init'
	 * 
	 */
	public void v_SignUpStep4Init() {
		Assert.assertTrue(SignUp.isAtStep4(), "At Step 4");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep4Success'
	 * 
	 */
	public void v_SignUpStep4Success() {
		Assert.assertTrue(SignUp.hasDectectedPassMessage(), "At Step 4 - Device Set up Successfully");
	}

}
