package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.*;
import com.misfit.ta.ios.AutomationTest;

public class GoalSettingsAPI extends ModelAPI {
	private static final Logger logger = Util
			.setupLogger(GoalSettingsAPI.class);

	public GoalSettingsAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	int goal = 25;
	int delta = 0;
	
	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		// TODO: THIS MODEL REQUIRES:
		// Goal: 2500
		
		// sign up account with require information
		PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1", true, 
				16, 9, 1991, true, "5'", "8\\\"", "120", ".0", 1);
		
		// change input mode to manual
		
		// insert a dummy record to close no data yet screne
		HomeScreen.tapOpenManualInput();
		ShortcutsTyper.delayTime(1000);
		String[] times = {"7", "00", "AM"};
		HomeScreen.enterManualActivity(times, 1, 100);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_ToGoalSettings'
	 * 
	 */
	public void e_ToGoalSettings() {
		HomeScreen.tapSettings();
		ShortcutsTyper.delayTime(1000);
		HomeSettings.tapAdjustGoal();
		ShortcutsTyper.delayTime(1000);
	}
	
	/**
	 * This method implements the Edge 'e_DecreaseGoal'
	 * 
	 */
	public void e_DecreaseGoal() {
		delta = -PrometheusHelper.randInt(1, 5);
		HomeSettings.decreaseGoalBy(-delta);
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_IncreaseGoal'
	 * 
	 */
	public void e_IncreaseGoal() {
		delta = PrometheusHelper.randInt(1, 5);
		HomeSettings.increaseGoalBy(delta);
		ShortcutsTyper.delayTime(1000);
	}
	
	/**
	 * This method implements the Edge 'e_CancelEdit'
	 * 
	 */
	public void e_CancelEdit() {
		HomeSettings.tapBackAtNewGoal();
		ShortcutsTyper.delayTime(1000);
		HomeSettings.tapBackAtSettings();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_DoneEdit'
	 * 
	 */
	public void e_DoneEdit() {
		HomeSettings.tapDoneAtNewGoal();
		ShortcutsTyper.delayTime(1000);
		HomeSettings.tapBackAtSettings();
		ShortcutsTyper.delayTime(1000);
		
		this.goal += delta;
	}

	/**
	 * This method implements the Vertex 'v_GoalSettings'
	 * 
	 */
	public void v_GoalSettings() {
		// check if current view is goal settings
		Assert.assertTrue("Current view is GoalSettings", ViewUtils.isExistedView("UILabel", "MY GOAL IS..."));
		
		// check if default value is correct
		String actual = Gui.getProperty("PTRichTextLabel", 0, "text");
		String expect = this.goal * 100 + "";
		Assert.assertTrue("Default goal value is correct", actual.indexOf(expect) >= 0);
	}
	
	/**
	 * This method implements the Vertex 'v_GoalDecreased'
	 * 
	 */
	public void v_GoalDecreased() {
		// TODO:
		// check if goal has been updated (use goal + delta for new goal value)
		// check how to hit your goal values
	}

	/**
	 * This method implements the Vertex 'v_GoalIncreased'
	 * 
	 */
	public void v_GoalIncreased() {
		// TODO:
		// check if goal has been updated (use goal + delta for new goal value)
		// check how to hit your goal values
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		// check if current screen is home screen
		Assert.assertTrue("Current screen is HomeScreen", HomeScreen.isToday());
	}

	/**
	 * This method implements the Vertex 'v_HomeScreenUpdated'
	 * 
	 */
	public void v_HomeScreenUpdated() {
		// check if new goal value had been updated
		String actual = Gui.getProperty("PTRichTextLabel", 0, "text");
		String expect = this.goal * 100 + "";
		Assert.assertTrue("Default goal value is correct", actual.indexOf(expect) >= 0);
		
	}

}
