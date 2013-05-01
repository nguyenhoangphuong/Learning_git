package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.*;
import com.misfit.ta.ios.AutomationTest;

public class SignOutSettingsAPI extends ModelAPI {
	private static final Logger logger = Util
			.setupLogger(SignOutSettingsAPI.class);

	public SignOutSettingsAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	String email = MVPApi.generateUniqueEmail();
	String password = "qwerty1";
	boolean isMale = true;
	int date = 16;
	int month = 9;
	int year = 1991;
	boolean isUSUnit = false;
	String h1 = "1";
	String h2 = ".71";
	String w1 = "69";
	String w2 = ".5";
	int goalLevel = 1;
	int goalValue = 2500;
	
	
	public void e_Init() {
		// This model require none
	}

	public void e_SignUp() {
		// sign up an account
		PrometheusHelper.signUp(email, password, isMale, date, month, year, isUSUnit, h1, h2, w1, w2, goalLevel);
	}
	
	public void e_ToProfileSettings() {
		// TODO:
	}
	
	public void e_BackFromSettings() {
		HomeSettings.tapBackAtSettings();
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Edge 'e_ChangeProfile'
	 * 
	 */
	public void e_ChangeProfile() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_ChangeWearingShine'
	 * 
	 */
	public void e_ChangeWearingShine() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_ConfirmProfile'
	 * 
	 */
	public void e_ConfirmProfile() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_ConfirmWearingShine'
	 * 
	 */
	public void e_ConfirmWearingShine() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_InputActivity'
	 * 
	 */
	public void e_InputActivity() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_LogIn'
	 * 
	 */
	public void e_LogIn() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_SignOut'
	 * 
	 */
	public void e_SignOut() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_ToSettings'
	 * 
	 */
	public void e_ToSettings() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_ToWearingShineSettings'
	 * 
	 */
	public void e_ToWearingShineSettings() {
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
	 * This method implements the Vertex 'v_HomeScreenAfterLogin'
	 * 
	 */
	public void v_HomeScreenAfterLogin() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_HomeScreenUpdated'
	 * 
	 */
	public void v_HomeScreenUpdated() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_InitScreen'
	 * 
	 */
	public void v_InitScreen() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_ProfileSettings'
	 * 
	 */
	public void v_ProfileSettings() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_ProfileSettingsUpdated'
	 * 
	 */
	public void v_ProfileSettingsUpdated() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_Settings'
	 * 
	 */
	public void v_Settings() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_StartScreen'
	 * 
	 */
	public void v_StartScreen() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_WearingShineSettings'
	 * 
	 */
	public void v_WearingShineSettings() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_WearingShineSettingsUpdated'
	 * 
	 */
	public void v_WearingShineSettingsUpdated() {
		// TODO:
	}

}
