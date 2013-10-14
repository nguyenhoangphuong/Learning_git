package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.gui.*;
import com.misfit.ta.ios.AutomationTest;

public class GoalSettingsAPI extends ModelAPI {
	private static final Logger logger = Util
			.setupLogger(GoalSettingsAPI.class);

	public GoalSettingsAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int goal = 1000;
	private int tempGoal = 1000;

	public void e_Init() {

		PrometheusHelper.signUp();
		
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(new String[] {"1", "00", "am"}, 5, 500);
		HomeScreen.tapSave();
	}

	public void e_ToGoalSettings() {
		
		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapAdjustGoal();
	}

	public void e_ChangeGoal() {
		
		do {
			tempGoal = PrometheusHelper.randInt(10, 25) * 100;
		}
		while (tempGoal == goal);
		
		int currentGoal = HomeSettings.getSpinnerGoal();
		HomeSettings.setSpinnerGoal(tempGoal, currentGoal);

		logger.info("Set goal to: " + tempGoal);
	}

	public void e_CancelEdit() {
		
		HomeSettings.tapCancel();
		logger.info("Cancel new goal");
	}

	public void e_DoneEdit() {
		
		HomeSettings.tapDoneAtNewGoal();
		PrometheusHelper.waitForAlert();
		goal = tempGoal;
		logger.info("Confirm new goal");
	}

	public void e_ConfirmNewGoal() {
		
		HomeSettings.tapOKAtNewGoalPopup();
	}

	public void v_GoalSettings() {
		
		// check if current view is goal settings
		Assert.assertTrue(HomeSettings.isAtEditGoal(),
				"Current view is GoalSettings");

		// check if default value is correct
		String actual = Gui.getProperty("PTRichTextLabel", 0, "text");
		String expect = this.goal + "";
		Assert.assertTrue(actual.indexOf(expect) >= 0,
				"Default goal value is correct");
	}

	public void v_GoalUpdated() {
		
		// check new spinner value
		int newGoal = HomeSettings.getSpinnerGoal();
		Assert.assertEquals(newGoal, tempGoal, "Spinner's value is correct");

		// check how to hit your goal
		int walkMins = MVPCalculator.calculateNearestTimeRemainInMinute(
				tempGoal, MVPEnums.ACTIVITY_WALKING);
		int runMins = MVPCalculator.calculateNearestTimeRemainInMinute(
				tempGoal, MVPEnums.ACTIVITY_RUNNING);
		int swimMins = MVPCalculator.calculateNearestTimeRemainInMinute(
				tempGoal, MVPEnums.ACTIVITY_SWIMMING);

		// check suggest time with correct calculation
		Assert.assertTrue(ViewUtils.isExistedView(
				"PTRichTextLabel",
				"_WALK_\\n "
						+ MVPCalculator
								.convertNearestTimeInMinuteToString(walkMins)),
				"Suggest time for walking is correct");
		Assert.assertTrue(ViewUtils.isExistedView(
				"PTRichTextLabel",
				"_RUN_\\n "
						+ MVPCalculator
								.convertNearestTimeInMinuteToString(runMins)),
				"Suggest time for running is correct");
		Assert.assertTrue(ViewUtils.isExistedView(
				"PTRichTextLabel",
				"_SWIM_\\n "
						+ MVPCalculator
								.convertNearestTimeInMinuteToString(swimMins)),
				"Suggest time for swimming is correct");

	}

	public void v_NewGoalConfirmation() {
		
		// check alert content
		Assert.assertTrue(HomeSettings.hasDontForgetMessage(),
				"Alert message is correct");
	}

	public void v_HomeScreen() {
		
		// check if current screen is home screen
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is HomeScreen");
	}

	public void v_HomeScreenUpdated() {
		
		// check if new goal value had been updated
		String actual = Gui.getProperty("UILabel", 4, "text");
		String expect = this.goal + "";
		logger.info("Actual goal is: " + actual + " - Expect goal is: "
				+ expect);
		Assert.assertTrue(
				actual.indexOf(expect) >= 0
						|| ViewUtils.isExistedView("UILabel",
								String.format("of %s points", expect)),
				"Default goal value is correct");
	}

}
