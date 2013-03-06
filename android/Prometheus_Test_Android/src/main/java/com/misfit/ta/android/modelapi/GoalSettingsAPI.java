package com.misfit.ta.android.modelapi;

import java.io.File;

import org.testng.Assert;

import org.graphwalker.generators.PathGenerator;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.gui.HomeScreen;
import com.misfit.ta.android.gui.Settings;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class GoalSettingsAPI extends ModelAPI {
	public GoalSettingsAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private static Integer currentLevel;

	/**
	 * This method implements the Edge 'e_AdjustGoal'
	 * 
	 */
	public void e_AdjustGoal() {
		HomeScreen.tapSettingsMenu();
		Settings.tapAdjustGoal();
		ShortcutsTyper.delayTime(1000);
		currentLevel = Settings.getCurrentLevel();
	}

	/**
	 * This method implements the Edge 'e_DecreaseGoal'
	 * 
	 */
	public void e_DecreaseGoal() {
		int steps = currentLevel > 1 ? currentLevel - 1 : 0;
		Settings.setGoalDown(steps);
		currentLevel = 1;
		Settings.checkToConfirmGoal();
	}

	/**
	 * This method implements the Edge 'e_IncreaseGoal'
	 * 
	 */
	public void e_IncreaseGoal() {
		int steps = currentLevel < 6 ? 6 - currentLevel : 0;
		Settings.setGoalUp(steps);
		currentLevel = 6;
		Settings.checkToConfirmGoal();
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		// TODO:
	}

	/**
	 * This method implements the Edge 'e_Check'
	 * 
	 */
	public void e_Check() {
		ShortcutsTyper.delayTime(1000);
		HomeScreen.tapSettingsMenu();
		Settings.tapAdjustGoal();
		ShortcutsTyper.delayTime(1000);
		Assert.assertEquals(currentLevel, Settings.getCurrentLevel());
	}

	/**
	 * This method implements the Edge 'e_PressBack'
	 * 
	 */
	public void e_PressBack() {
		Settings.backToDayView();
	}

	/**
	 * This method implements the Vertex 'v_GoalSettings'
	 * 
	 */
	public void v_GoalSettings() {
		Assert.assertTrue(Settings.isGoalSettings(),
				"Goal Settings is not visible");
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		// TODO:
	}

}
