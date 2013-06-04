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

public class GoalSettingsAPI extends ModelAPI {
	private static final Logger logger = Util.setupLogger(GoalSettingsAPI.class);

	public GoalSettingsAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int goal = 1000;
	private int tempGoal = 1000;
	
	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		// TODO: THIS MODEL REQUIRES:
		// Goal: 1000
		
		// sign up account with require information
		PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1", true, 16, 9, 1991, true, "5'", "8\\\"", "120", ".0", 1);
		ShortcutsTyper.delayTime(5000);
		//PrometheusHelper.setInputModeToManual();
		//ShortcutsTyper.delayTime(1000);
		PrometheusHelper.inputRandomRecord();
		ShortcutsTyper.delayTime(1000);
		PrometheusHelper.handleTutorial();
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_ToGoalSettings'
	 * 
	 */
	public void e_ToGoalSettings() {
		HomeScreen.tapOpenSyncTray();
		ShortcutsTyper.delayTime(500);
		HomeScreen.tapAdjustGoal();
		ShortcutsTyper.delayTime(500);
	}
	
	/**
	 * This method implements the Edge 'e_ChangeGoal'
	 * 
	 */
	public void e_ChangeGoal() {
		tempGoal = PrometheusHelper.randInt(10, 25) * 100;
		HomeSettings.setSpinnerGoal(tempGoal);
		ShortcutsTyper.delayTime(1000);
		
		logger.info("Set goal to: " + tempGoal);
	}

	/**
	 * This method implements the Edge 'e_CancelEdit'
	 * 
	 */
	public void e_CancelEdit() {
		HomeSettings.tapBack();
		ShortcutsTyper.delayTime(1000);
		HomeSettings.tapBack();
		ShortcutsTyper.delayTime(1000);
		
		logger.info("Cancel new goal");
	}

	/**
	 * This method implements the Edge 'e_DoneEdit'
	 * 
	 */
	public void e_DoneEdit() {
		ShortcutsTyper.delayTime(1000);
		HomeSettings.tapDoneAtNewGoal();
		ShortcutsTyper.delayTime(1000);
		
		goal = tempGoal;
		logger.info("Confirm new goal");
	}

	/**
	 * This method implements the Edge 'e_ConfirmNewGoal'
	 * 
	 */
	public void e_ConfirmNewGoal() {
		HomeSettings.tapOKAtNewGoalPopup();
		ShortcutsTyper.delayTime(1000);
		HomeSettings.tapBack();
		ShortcutsTyper.delayTime(1000);
	}
	
	
	
	
	/**
	 * This method implements the Vertex 'v_GoalSettings'
	 * 
	 */
	public void v_GoalSettings() {
		// check if current view is goal settings
		Assert.assertTrue(HomeSettings.isAtEditGoal(), "Current view is GoalSettings");
		
		// check if default value is correct
		String actual = Gui.getProperty("PTRichTextLabel", 0, "text");
		String expect = this.goal + "";
		Assert.assertTrue(actual.indexOf(expect) >= 0, "Default goal value is correct");
	}
	
	/**
	 * This method implements the Vertex 'v_GoalUpdated'
	 * 
	 */
	public void v_GoalUpdated() {
		// check new spinner value
		int newGoal = HomeSettings.getSpinnerGoal();
		Assert.assertTrue(newGoal == tempGoal, "Spinner's value is correct");
		
		// and how to hit your goal contains only dummy texts
	}

	/**
	 * This method implements the Vertex 'v_NewGoalConfirmation'
	 * 
	 */
	public void v_NewGoalConfirmation() {
		// check alert content
		Assert.assertTrue(HomeSettings.hasDontForgetMessage(), "Alert message is correct");
	}
	
	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		// check if current screen is home screen
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is HomeScreen");
	}

	/**
	 * This method implements the Vertex 'v_HomeScreenUpdated'
	 * 
	 */
	public void v_HomeScreenUpdated() {
		// check if new goal value had been updated
		String actual = Gui.getProperty("PTRichTextLabel", 0, "text");
		String expect = this.goal + "";
		logger.info("Actual goal is: " + actual + " - Expect goal is: " + expect);

		Assert.assertTrue(actual.indexOf(expect) >= 0, "Default goal value is correct");
		
	}

}
