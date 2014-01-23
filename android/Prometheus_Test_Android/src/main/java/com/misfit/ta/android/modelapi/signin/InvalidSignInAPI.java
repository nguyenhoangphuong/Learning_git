package com.misfit.ta.android.modelapi.signin;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.gui.SignIn;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class InvalidSignInAPI extends ModelAPI {
	public InvalidSignInAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int fullScreenHeight = 0;
	private int fullScreenWidth = 0;
	private int popupHeight = 0;
	private int popupWidth = 0;
	boolean hasIncorrectSignInPopup = true;

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
	public void e_ClearFields() {
		SignIn.pressBack();
		ShortcutsTyper.delayTime(1000);
		SignIn.chooseSignIn();
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
		SignIn.fillSignIn("thy@misfitwearables.com", "teste");
		SignIn.pressNext();
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
	}

	/**
	 * This method implements the Edge 'e_PressBack'
	 * 
	 */
	public void e_PressBack() {
		Gui.pressBack();
		ShortcutsTyper.delayTime(500);
		SignIn.pressBack();
	}

	/**
	 * This method implements the Vertex 'v_IncorrectSignIn'
	 * 
	 */
	public void v_IncorrectSignIn() {
		ShortcutsTyper.delayTime(500);
		Assert.assertTrue(SignIn.hasIncorrectEmailPasswordMessage(),
				"Invalid Email Or Password");
	}

	/**
	 * This method implements the Vertex 'v_InitialView'
	 * 
	 */
	public void v_InitialView() {
		Assert.assertTrue(SignIn.isInitViewVisible(),
				"Init view is not visible");
		fullScreenHeight = Gui.getScreenHeight();
		fullScreenWidth = Gui.getScreenWidth();
	}

	/**
	 * This method implements the Vertex 'v_InvalidEmail'
	 * 
	 */
	public void v_InvalidEmail() {
		// Assert.assertTrue(SignIn.hasInvalidEmailMessage(), "Invalid Email");
	}

	public void e_DismissPopup() {
		popupHeight = Gui.getHeight();
		popupWidth = Gui.getWidth();
		ViewNode okButton = ViewUtils.findView("TextView", "mText", DefaultStrings.OKText, 0);
		Gui.touchViewOnPopup(fullScreenHeight, fullScreenWidth, popupHeight,
				popupWidth, okButton);
		// Magic line which makes ViewServer reload views after we dismiss popup
		ShortcutsTyper.delayTime(50);
	}

	/**
	 * This method implements the Vertex 'v_InvalidPassword'
	 * 
	 */
	public void v_InvalidPassword() {
		// Assert.assertTrue(SignIn.hasInvalidPasswordMessage(),
		// "Invalid Password");
	}

	/**
	 * This method implements the Vertex 'v_SignInVisible'
	 * 
	 */
	public void v_SignInVisible() {
		Assert.assertTrue(SignIn.isSignInVisible(), "Sign in is visible");
	}

	private boolean hasIncorrectSignInPopup() {
		ViewNode node = ViewUtils.findView("TextView", "mText",
				DefaultStrings.IncorrectSignInMessage, 0);
		return node != null;
	}
}
