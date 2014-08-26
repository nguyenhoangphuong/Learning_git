package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.ios.modelapi.settings.GoalSettingsAPI;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class SetWeightGoalAPI extends ModelAPI {
	public SetWeightGoalAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private static final Logger logger = Util
			.setupLogger(GoalSettingsAPI.class);

	private int weightDigit = 140;
	private int weightFraction = 0;
	private int weightDigitTemp = 140;
	private int weightFractionTemp = 0;
	private boolean weightUnitUSTemp = true;
	private boolean weightUnitUS = true;

	/**
	 * This method implements the Edge 'e_ChangeWeightGoal'
	 * 
	 */
	public void e_ChangeWeightGoal() {
		do {
			weightDigitTemp = PrometheusHelper.randInt(90, 100);
		} while (weightDigitTemp == weightDigit);

		do {
			weightFractionTemp = PrometheusHelper.randInt(0, 9);
		} while (weightFractionTemp == weightFraction);

		weightUnitUSTemp = PrometheusHelper.coin();

		HomeSettings.setWeightGoal(weightDigitTemp + "", "."
				+ weightFractionTemp, weightUnitUSTemp);

		weightDigit = weightDigitTemp;
		weightFraction = weightFractionTemp;
		weightUnitUS = weightUnitUSTemp;
		logger.info("Set weight goal to: " + weightDigitTemp + "."
				+ weightFractionTemp + (weightUnitUSTemp ? " lbs" : " kg"));
		
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(2000);
		HomeScreen.tapOpenSettingsTray();
		ShortcutsTyper.delayTime(2000);
	}

	/**
	 * This method implements the Edge 'e_goToSettingsMenu'
	 * 
	 */
	public void e_goToSettingsMenu() {
		HomeScreen.tapOpenSettingsTray();
	}

	/**
	 * This method implements the Edge 'e_goToWeightGoal'
	 * 
	 */
	public void e_goToWeightGoal() {
		HomeScreen.tapAdjustGoal();
		HomeSettings.tapWeightGoalTab();
	}

	/**
	 * This method implements the Edge 'e_goToWeightGoalScreen'
	 * 
	 */
	public void e_goToWeightGoalScreen() {
		HomeScreen.tapSetWeightGoal();

	}

	/**
	 * This method implements the Edge 'e_goToWeightScreen'
	 * 
	 */
	public void e_goToWeightScreen() {
		HomeScreen.tapWeightTimeline();
	}

	/**
	 * This method implements the Edge 'e_init'
	 * 
	 */
	public void e_init() {
		PrometheusHelper.signUpDefaultProfile();
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		Assert.assertTrue(HomeScreen.isToday(), "Current View is HomeScreen");
	}

	/**
	 * This method implements the Vertex 'v_SettingsMenu'
	 * 
	 */
	public void v_SettingsMenu() {
		Assert.assertTrue(HomeSettings.isAtSettings(),
				"Current View is Setting ");
	}

	/**
	 * This method implements the Vertex 'v_WeightGoalSettings'
	 * 
	 */
	public void v_WeightGoalSettings() {
		Assert.assertTrue(HomeSettings.isAtEditWeightGoal(),
				"Current View is Edit Weight Goal");
	}

	/**
	 * This method implements the Vertex 'v_WeightScreen'
	 * 
	 */
	public void v_WeightScreen() {
		Assert.assertTrue(HomeScreen.isWeightTimeline(),
				"Current View is HomeScreen - Weight");
	}

	/**
	 * This method implements the Vertex 'v_WeightScreenUpdated'
	 * 
	 */
	public void v_WeightScreenUpdated() {
		// check if new goal value had been updated
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format(
				"goal weight: %d.%d", weightDigit, weightFraction)),
				"Weight goal value is correct");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel",
				weightUnitUS ? "140.0 lbs" : "63.5 kg"),
				"Weight goal unit is correct");
	}

}
