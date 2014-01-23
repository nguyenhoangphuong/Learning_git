package com.misfit.ta.android.modelapi;

import java.io.File;

import org.graphwalker.generators.PathGenerator;

import com.misfit.ta.android.Gui;
import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.gui.SignUp;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class SignUpAPI extends ModelAPI {
	public SignUpAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	private int fullScreenHeight = 0;
	private int fullScreenWidth = 0;
	/**
	 * This method implements the Edge 'e_ChooseSignUp'
	 * 
	 */
	public void e_ChooseSignUp() {
		ShortcutsTyper.delayTime(2000);
		fullScreenHeight = Gui.getScreenHeight();
		fullScreenWidth = Gui.getScreenWidth();
		System.out.println(fullScreenHeight);
		System.out.println(fullScreenWidth);
		SignUp.chooseSignUp();
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
	}

	/**
	 * This method implements the Edge 'e_InputProfile'
	 * 
	 */
	public void e_InputProfile() {
		ShortcutsTyper.delayTime(2000);
		SignUp.inputSex(true);
		ShortcutsTyper.delayTime(500);
		SignUp.inputUnits(true);
		ShortcutsTyper.delayTime(500);
		SignUp.chooseDefaultHeightValue(fullScreenHeight, fullScreenWidth);
		ShortcutsTyper.delayTime(500);
		SignUp.chooseDefaultWeightValue(fullScreenHeight, fullScreenWidth);
		ShortcutsTyper.delayTime(500);
		SignUp.chooseDefaultBirthdate(fullScreenHeight, fullScreenWidth);
		ShortcutsTyper.delayOne();
		SignUp.pressNext();
	}

	/**
	 * This method implements the Edge 'e_Link'
	 * 
	 */
	public void e_Link() {
		SignUp.linkShine();
		
	}

	/**
	 * This method implements the Edge 'e_PressBack'
	 * 
	 */
	public void e_PressBack() {
		SignUp.pressBack();
	}

	/**
	 * This method implements the Edge 'e_PressBackToSignOut'
	 * 
	 */
	public void e_PressBackToSignOut() {
//		fullScreenHeight = Gui.getScreenHeight();
//		fullScreenWidth = Gui.getScreenWidth();
//		System.out.println(fullScreenHeight);
//		System.out.println(fullScreenWidth);              
		ShortcutsTyper.delayTime(100);
		SignUp.pressBack();
		ShortcutsTyper.delayTime(100);
		SignUp.tapToSignOutAtProfilePage(fullScreenHeight, fullScreenWidth);
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_SubmitValidEmailPassword'
	 * 
	 */
	public void e_SubmitValidEmailPassword() {
		String email = SignUp.generateUniqueEmail();
		String password = "test12";
		SignUp.fillSignUpForm(email, password);
		SignUp.pressNext();
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
	}

	/**
	 * This method implements the Vertex 'v_InitialView'
	 * 
	 */
	public void v_InitialView() {
		
	}

	/**
	 * This method implements the Vertex 'v_SignUpAccount'
	 * 
	 */
	public void v_SignUpAccount() {
	}

	/**
	 * This method implements the Vertex 'v_SignUpLinking'
	 * 
	 */
	public void v_SignUpLinking() {
	}

	/**
	 * This method implements the Vertex 'v_SignUpProfile'
	 * 
	 */
	public void v_SignUpProfile() {
	}
}
