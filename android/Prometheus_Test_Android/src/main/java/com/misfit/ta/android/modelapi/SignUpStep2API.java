package com.misfit.ta.android.modelapi;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.gui.SignIn;
import com.misfit.ta.android.gui.SignUp;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.modelAPI.ModelAPI;

public class SignUpStep2API extends ModelAPI {
	public SignUpStep2API(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	String sex = "Male"; 				// Male / Female
	int[] birthday = { 16, 9, 1991 };	// Format: Sep 19, 1991 [1980 - 1992]
	float height = 70;					// US: [50 - 100 inche] Metric: [150 - 200 cm] 
	float weight = 100;					// US: [100 - 300 lbs] Metric: [40 - 100 kg]
	boolean isUS = true;
	
	/**
	 * This method implements the Edge 'e_init'
	 * 
	 */
	public void e_init() {
		// TODO:
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
	 * This method implements the Edge 'e_inputBirthDay'
	 * 
	 */
	public void e_inputBirthDay() 
	{
		birthday[0] = Helper.randomInt(1, 28);
		birthday[1] = Helper.randomInt(1, 12);
		birthday[2] = Helper.randomInt(1980, 1992);
		
		SignUp.inputBirthday(birthday[0], birthday[1], birthday[2]);
	}

	/**
	 * This method implements the Edge 'e_inputHeight'
	 * 
	 */
	public void e_inputHeight() 
	{
		isUS = System.currentTimeMillis() % 2 == 0;
		
		if(isUS)
		{
			height = Helper.randomInt(50, 100);
			weight = Helper.randomInt(100, 300);
		}
		else
		{
			height = Helper.randomInt(100, 200);
			weight = Helper.randomInt(40, 100);
		}
			
		SignUp.inputHeight(height, isUS);
	}

	/**
	 * This method implements the Edge 'e_inputSex'
	 * 
	 */
	public void e_inputSex() {
		SignUp.inputSex(true);
	}

	/**
	 * This method implements the Edge 'e_inputWeight'
	 * 
	 */
	public void e_inputWeight()
	{			
		SignUp.inputHeight(weight, isUS);
	}

	/**
	 * This method implements the Edge 'e_tapNext'
	 * 
	 */
	public void e_submit() {
		SignUp.inputEmail(System.nanoTime() + "@qa.com");
		SignUp.inputPassword("qwery1");
		SignUp.pressNext();
		Helper.wait(3);
	}

	/**
	 * This method implements the Vertex 'v_InitialView'
	 * 
	 */
	public void v_InitialView() {
		Assert.assertTrue(SignIn.isInitViewVisible(),
				"Current view is InitView");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep1'
	 * 
	 */
	public void v_SignUpStep1() {
		Assert.assertTrue(SignUp.isAtStep1(), "Current view is SignUpStep1");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep2'
	 * 
	 */
	public void v_SignUpStep2() {
		Assert.assertTrue(SignUp.isAtStep2(), "Current view is SignUpStep2");
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep2'
	 * 
	 */
	public void v_SignUpStep2Updated() {
		String formatedBirthday = SignUp.formatDateString(birthday[0], birthday[1], birthday[2]);
		String formatedWeight = SignUp.formatWeightString(weight, isUS);
		String formatedHeight = SignUp.formatHeightString(height, isUS);
		String actualBirthday = SignUp.getBirthday();
		String actualWeight = SignUp.getWeight();
		String actualHeight = SignUp.getHeight();
		
		Assert.assertTrue(sex == SignUp.getSex(), "Sex updated");
		Assert.assertTrue(formatedBirthday.equals(actualBirthday), "Birthday updated");
		Assert.assertTrue(formatedWeight.equals(actualWeight), "Weight updated");
		Assert.assertTrue(formatedHeight.equals(actualHeight), "Height updated");
		
	}

}
