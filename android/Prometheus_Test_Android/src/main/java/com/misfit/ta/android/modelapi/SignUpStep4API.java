package com.misfit.ta.android.modelapi;

import java.io.File;
import java.util.HashMap;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.gui.*;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.modelAPI.ModelAPI;

public class SignUpStep4API extends ModelAPI {
	public SignUpStep4API(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	/**
	 * This method implements the Edge 'e_endTouch'
	 * 
	 */
	public void e_endTouch() {
		Helper.wait(2000);
	}

	/**
	 * This method implements the Edge 'e_init'
	 * 
	 */
	public void e_init() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_longHold'
	 * 
	 */
	public void e_longHold() {
		Helper.wait(7000);
		SignUp.stopSyncing();
	}

	/**
	 * This method implements the Edge 'e_shortHold'
	 * 
	 */
	public void e_shortHold() {
		Helper.wait(2000);
		SignUp.stopSyncing();
	}

	/**
	 * This method implements the Edge 'e_startTouch'
	 * 
	 */
	public void e_startTouch() {
		SignUp.startSyncing();
	}

	/**
	 * This method implements the Edge 'e_submit'
	 * 
	 */
	public void e_submit() {
		SignUp.inputEmail(System.nanoTime() + "@qa.automation");
		SignUp.inputPassword("qwerty1");
		SignUp.pressNext();
		Helper.wait(3000);
	}

	/**
	 * This method implements the Edge 'e_tapIDontHaveAnAccount'
	 * 
	 */
	public void e_tapIDontHaveAnAccount() {
		SignUp.chooseSignUp();
	}

	/**
	 * This method implements the Edge 'e_tapNext'
	 * 
	 */
	public void e_tapNext() {
		SignUp.pressNext();
	}

	/**
	 * This method implements the Edge 'e_wait'
	 * 
	 */
	public void e_wait() {
		Helper.wait(6000);
	}

	/**
	 * This method implements the Vertex 'v_InitialView'
	 * 
	 */
	public void v_InitialView() {
		Assert.assertTrue(SignIn.isInitViewVisible(), "At InitialView");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep1'
	 * 
	 */
	public void v_SignUpStep1() {
		Assert.assertEquals(SignUp.isAtStep1(), "At Step 1");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep2'
	 * 
	 */
	public void v_SignUpStep2() {
		Assert.assertEquals(SignUp.isAtStep2(), "At Step 2");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep3'
	 * 
	 */
	public void v_SignUpStep3() {
		Assert.assertEquals(SignUp.isAtStep3(), "At Step 3");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep4AfterWait'
	 * 
	 */
	public void v_SignUpStep4AfterWait() {
		Assert.assertTrue(SignUp.hasGetYoursNowButton(), "Button GetYoursNow existed");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep4DetectedFail'
	 * 
	 */
	public void v_SignUpStep4DetectedFail() {
		Assert.assertTrue(SignUp.hasDetectedFailMessage(), "Detected failed message shown");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep4DetectedPass'
	 * 
	 */
	public void v_SignUpStep4DetectedPass() {
		Assert.assertTrue(SignUp.hasDectectedPassMessage(), "Dectected passed message shown");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep4Init'
	 * 
	 */
	public void v_SignUpStep4Init() {
		Assert.assertEquals(SignUp.isAtStep4(), "At Step 4");
		Assert.assertTrue(SignUp.hasGetYoursNowButton() == false, "Button GetYoursNow not existed");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep4Syncing'
	 * 
	 */
	public void v_SignUpStep4Syncing() {
		Assert.assertTrue(SignUp.hasMagicHappening(), "Magic happening message shown");
	}

}
