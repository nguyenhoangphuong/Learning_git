package com.misfit.ta.android.modelapi;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.gui.SignIn;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class SignInAPI extends ModelAPI {
	public SignInAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
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
	   * This method implements the Edge 'e_ClearFields1'
	   * 
	   */
	  public void e_ClearFields1() {
		  SignIn.pressBack();
		  ShortcutsTyper.delayTime(1000);
		  SignIn.chooseSignIn();
	  }




	  /**
	   * This method implements the Edge 'e_ClearFields2'
	   * 
	   */
	  public void e_ClearFields2() {
		  SignIn.pressBack();
		  ShortcutsTyper.delayTime(1000);
		  SignIn.chooseSignIn();
	  }




	  /**
	   * This method implements the Edge 'e_ClearFields3'
	   * 
	   */
	  public void e_ClearFields3() {
		  SignIn.pressBack();
		  ShortcutsTyper.delayTime(1000);
		  SignIn.chooseSignIn(); 
	  }


	/**
	 * This method implements the Edge 'e_FillCorrectEmailPassword'
	 * 
	 */
	public void e_FillCorrectEmailPassword() {
		SignIn.fillSignIn("test27@thy.com", "test12");
	}

	/**
	 * This method implements the Edge 'e_FillIncorrectEmailOrPassword'
	 * 
	 */
	public void e_FillIncorrectEmailOrPassword() {
		SignIn.fillSignIn("test27@thythy.com", "test12");
		SignIn.pressNext();
	}

	/**
	 * This method implements the Edge 'e_FillInvalidEmail'
	 * 
	 */
	public void e_FillInvalidEmail() {
		SignIn.fillSignIn("test27.com", "");
		SignIn.pressNext();
	}

	/**
	 * This method implements the Edge 'e_FillInvalidPassword'
	 * 
	 */
	public void e_FillInvalidPassword() {
		SignIn.fillSignIn("test27@thy.com", "teste");
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
	 * This method implements the Edge 'e_PressBack'
	 * 
	 */
	public void e_PressBack() {
		SignIn.pressBack();
        ShortcutsTyper.delayTime(500);
        SignIn.pressBack();
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_IncorrectSignIn'
	 * 
	 */
	public void v_IncorrectSignIn() {
		Assert.assertTrue(SignIn.hasIncorrectEmailPasswordMessage(), "Invalid Email Or Password");
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
	 * This method implements the Vertex 'v_InvalidEmail'
	 * 
	 */
	public void v_InvalidEmail() {
		Assert.assertTrue(SignIn.hasInvalidEmailMessage(), "Invalid Email");
	}

	/**
	 * This method implements the Vertex 'v_InvalidPassword'
	 * 
	 */
	public void v_InvalidPassword() {
		Assert.assertTrue(SignIn.hasInvalidPasswordMessage(), "Invalid Password");
	}

	/**
	 * This method implements the Vertex 'v_SignInVisible'
	 * 
	 */
	public void v_SignInVisible() {
		Assert.assertTrue(SignIn.isSignInVisible(), "Sign in is not visible");
	}

	/**
	 * This method implements the Vertex 'v_SuccessfulSignIn'
	 * 
	 */
	public void v_SuccessfulSignIn() {
		SignIn.pressNext();
	}

}
