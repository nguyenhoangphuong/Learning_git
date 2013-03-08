package com.misfit.ta.android.modelapi.signin;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.gui.SignIn;
import com.misfit.ta.modelAPI.ModelAPI;

public class SuccessfulSignInAPI extends ModelAPI {
	public SuccessfulSignInAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	/**
	 * This method implements the Edge 'e_ChooseSignIn'
	 * 
	 */
	public void e_ChooseSignIn() {
		SignIn.chooseSignIn();
	}

	/**
	 * This method implements the Edge 'e_FillCorrectEmailPassword'
	 * 
	 */
	public void e_FillCorrectEmailPassword() {
		SignIn.fillSignIn("test27@thy.com", "test12");
		SignIn.pressNext();
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		// TODO:
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
		Assert.assertTrue(SignIn.isInitViewVisible(),
				"Init view is not visible");
	}

	/**
	 * This method implements the Vertex 'v_SignInVisible'
	 * 
	 */
	public void v_SignInVisible() {
		Assert.assertTrue(SignIn.isSignInVisible(), "Sign in is not visible");
	}

}
