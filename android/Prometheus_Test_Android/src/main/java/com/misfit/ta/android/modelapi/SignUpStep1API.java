package com.misfit.ta.android.modelapi;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.gui.SignIn;
import com.misfit.ta.android.gui.SignUp;
import com.misfit.ta.android.gui.Helper.Helper;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.modelAPI.ModelAPI;

public class SignUpStep1API extends ModelAPI {
	public SignUpStep1API(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	String existedEmail = "qa_automation@qa.com";
	String email = "";
	String password = "";


	/**
	 * This method implements the Edge 'e_confirmAlert'
	 * 
	 */
	public void e_confirmAlert() {
		SignUp.closeAlert();
	}

	/**
	 * This method implements the Edge 'e_init'
	 * 
	 */
	public void e_init() {
		
	}

	/**
	 * This method implements the Edge 'e_inputDuplicatedEmail'
	 * 
	 */
	public void e_inputDuplicatedEmail() {
		email = existedEmail;
		password = "qwerty1";

		SignUp.inputEmail(email);
		SignUp.inputPassword(password);
		SignUp.pressNext();
		Helper.wait(5);
	}

	/**
	 * This method implements the Edge 'e_inputInvalidEmail'
	 * 
	 */
	public void e_inputInvalidEmail() {
		email = System.currentTimeMillis() + ".com";
		password = "qwerty1";

		SignUp.inputEmail(email);
		SignUp.inputPassword(password);
		SignUp.pressNext();
		Helper.wait1();
	}

	/**
	 * This method implements the Edge 'e_inputInvalidPassword'
	 * 
	 */
	public void e_inputInvalidPassword() {
		email = System.currentTimeMillis() + "@qa.com";
		password = "qwerty";

		SignUp.inputEmail(email);
		SignUp.inputPassword(password);
		SignUp.pressNext();
		Helper.wait1();
	}
	
	/**
	 * This method implements the Edge 'e_inputValidEmailPassword'
	 * 
	 */
	public void e_inputValidEmailPassword() {
		email = System.currentTimeMillis() + "@qa.com";
		password = "qwerty1";

		SignUp.inputEmail(email);
		SignUp.inputPassword(password);
		SignUp.pressNext();
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
		Assert.assertTrue(SignUp.isAtStep1());
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep1InvalidEmail'
	 * 
	 */
	public void v_SignUpStep1InvalidEmail() {
		String msg = SignUp.getAlertMessage();
		Assert.assertTrue(msg != null, "Alert is shown");
		Assert.assertTrue(msg.equals(DefaultStrings.SignUpInvalidEmailMessage));
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep1InvalidPassword'
	 * 
	 */
	public void v_SignUpStep1InvalidPassword() {
		String msg = SignUp.getAlertMessage();
		Assert.assertTrue(msg != null, "Alert is shown");
		Assert.assertTrue(msg.equals(DefaultStrings.SignUpInvalidPasswordMessage));
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep1DuplicatedEmail'
	 * 
	 */
	public void v_SignUpStep1DuplicatedEmail() {
		String msg = SignUp.getAlertMessage();
		Assert.assertTrue(msg != null, "Alert is shown");
		Assert.assertTrue(msg.equals(DefaultStrings.SignUpDuplicatedEmailMessage));
	}

	/**
	 * This method implements the Vertex 'v_SignUpStep1DuplicatedEmail'
	 * 
	 */
	public void v_SignUpStep2() {
		Assert.assertTrue(SignUp.isAtStep2());
	}
	
}
