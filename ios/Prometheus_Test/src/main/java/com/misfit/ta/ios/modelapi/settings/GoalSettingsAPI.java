package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
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

	private int activityGoal = 1000;
	private int activityGoalTemporary = 1000;
	
	private int sleepGoalHours = 8;
	private int sleepGoalMinutes = 0;
	private int sleepGoalHoursTemp = 8;
	private int sleepGoalMinutesTemp = 0;
	
	private int weightDigit = 140;
	private int weightFraction = 0;
	private int weightDigitTemp = 140;
	private int weightFractionTemp = 0;
	private boolean weightUnitUS = true;
	private boolean weightUnitUSTemp = true;
	

	public void e_Init() {

		// sign up with goal = 1000
		PrometheusHelper.signUpDefaultProfile();

		// input a dummy record
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(new String[] { "1", "00", "am" }, 5, 500);
		ShortcutsTyper.delayOne();
		HomeScreen.tap180MinNap();
		ShortcutsTyper.delayOne();
		HomeScreen.tapSave();
	}
	
	public void e_ToActivityTimeline() {
	
		HomeScreen.tapActivityTimeline();
	}
	
	public void e_ToSleepTimeline() {
		
		HomeScreen.tapSleepTimeline();
	}
	
	public void e_ToWeightTimeline() {
		
		HomeScreen.tapWeightTimeline();
	}

	public void e_SetWeightGoal() {
		
		HomeScreen.tapSetWeightGoal();
	}
	
	public void e_ToActivityGoalSettings() {

		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapAdjustGoal();
	}
	
	public void e_ToSleepGoalSettings() {

		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapAdjustGoal();
		HomeSettings.tapSleepGoalTab();
	}
	
	public void e_ToWeightGoalSettings() {

		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapAdjustGoal();
		HomeSettings.tapWeightGoalTab();
	}

	public void e_ChangeActivityGoal() {

		do {
			activityGoalTemporary = PrometheusHelper.randInt(10, 25) * 100;
		} while (activityGoalTemporary == activityGoal);

		HomeSettings.setActivityGoal(activityGoalTemporary);

		logger.info("Set activity goal to: " + activityGoalTemporary);
	}
	
	public void e_ChangeSleepGoal() {

		do {
			sleepGoalHoursTemp = PrometheusHelper.randInt(1, 12);
		} while (sleepGoalHoursTemp == sleepGoalHours);
		
		do {
			sleepGoalMinutesTemp = PrometheusHelper.randInt(0, 59);
		} while (sleepGoalMinutesTemp == sleepGoalMinutes);

		HomeSettings.setSleepGoal(sleepGoalHoursTemp, sleepGoalMinutesTemp);

		logger.info("Set sleep goal to: " + sleepGoalHoursTemp + " hrs " + sleepGoalMinutesTemp + " mins");
	}
	
	public void e_ChangeWeightGoal() {

		do {
			weightDigitTemp = PrometheusHelper.randInt(90, 100);
		} while (weightDigitTemp == weightDigit);
		
		do {
			weightFractionTemp = PrometheusHelper.randInt(0, 9);
		} while (weightFractionTemp == weightFraction);
		
		weightUnitUSTemp = PrometheusHelper.coin();

		HomeSettings.setWeightGoal(weightDigitTemp + "", "." + weightFractionTemp, weightUnitUSTemp);

		logger.info("Set weight goal to: " + weightDigitTemp + "." + weightFractionTemp + (weightUnitUSTemp ? " lbs" : " kg"));
	}

	public void e_CancelEdit() {

		HomeSettings.tapCancel();
		logger.info("Cancel new goal");
	}

	public void e_DoneEdit() {

		HomeSettings.tapDoneAtNewGoal();
		
		activityGoal = activityGoalTemporary;
		sleepGoalHours = sleepGoalHoursTemp;
		sleepGoalMinutes = sleepGoalMinutesTemp;
		weightDigit = weightDigitTemp;
		weightFraction = weightFractionTemp;
		weightUnitUS = weightUnitUSTemp;
		
		logger.info("Confirm new goal");
	}

	public void e_ConfirmNewGoal() {

		HomeSettings.tapOKAtNewGoalPopup();
	}

	
	
	public void v_HomeScreenNoCheck() {

	}
	
	public void v_ActivityTimeline() {

		Assert.assertTrue(HomeScreen.isActivityTimeline(), "Current view is HomeScreen - Activity");
	}

	public void v_SleepTimeline() {

		Assert.assertTrue(HomeScreen.isSleepTimeline(), "Current view is HomeScreen - Sleep");
	}

	public void v_WeightTimelineInitial() {

		Assert.assertTrue(HomeScreen.isWeightTimelineInitial(), "Current view is HomeScreen - Weight");
	}
	
	public void v_WeightTimeline() {

		Assert.assertTrue(HomeScreen.isWeightTimeline(), "Current view is HomeScreen - Weight");
	}
	
	public void v_ActivityGoalSettings() {

		// check if current view is goal settings
		Assert.assertTrue(HomeSettings.isAtEditActivityGoal(), "Current view is GoalSettings - Activity");

		// check if default value is correct
		String actual = Gui.getProperty("PTRichTextLabel", 0, "text");
		String expect = this.activityGoal + "";
		Assert.assertTrue(actual.indexOf(expect) >= 0, "Current activity goal value is correct");
	}
	
	public void v_SleepGoalSettings() {

		// check if current view is goal settings
		Assert.assertTrue(HomeSettings.isAtEditSleepGoal(), "Current view is GoalSettings - Sleep");

		// check if default value is correct
		String actual = Gui.getProperty("PTRichTextLabel", 0, "text");
		String expectHours = this.sleepGoalHours + (this.sleepGoalHours == 1 ? " hour" : " hours");
		String expectMins = this.sleepGoalMinutes + (this.sleepGoalMinutes == 1 ? " min" : " mins");
		
		Assert.assertTrue(actual.indexOf(expectHours) >= 0, "Current sleep goal value (hours) is correct");
		if(this.sleepGoalMinutes != 0)
			Assert.assertTrue(actual.indexOf(expectMins) >= 0, "Current sleep goal value (mins) is correct");
	}
	
	public void v_WeightGoalSettings() {

		// check if current view is goal settings
		Assert.assertTrue(HomeSettings.isAtEditWeightGoal(), "Current view is GoalSettings - Weight");
	}

	public void v_ActivityGoalUpdated() {

		// check how to hit your goal
		int walkMins = MVPCalculator.calculateNearestTimeRemainInMinute(
				activityGoalTemporary, MVPEnums.ACTIVITY_WALKING);
		int runMins = MVPCalculator.calculateNearestTimeRemainInMinute(
				activityGoalTemporary, MVPEnums.ACTIVITY_RUNNING);
		int swimMins = MVPCalculator.calculateNearestTimeRemainInMinute(
				activityGoalTemporary, MVPEnums.ACTIVITY_SWIMMING);

		// check suggest time with correct calculation
		Assert.assertTrue(ViewUtils.isExistedView(
				"PTRichTextLabel",
				"_WALK_\\n "
						+ PrometheusHelper
								.convertNearestTimeInMinuteToString(walkMins)),
				"Suggest time for walking is correct");
		Assert.assertTrue(ViewUtils.isExistedView(
				"PTRichTextLabel",
				"_RUN_\\n "
						+ PrometheusHelper
								.convertNearestTimeInMinuteToString(runMins)),
				"Suggest time for running is correct");
		Assert.assertTrue(ViewUtils.isExistedView(
				"PTRichTextLabel",
				"_SWIM_\\n "
						+ PrometheusHelper
								.convertNearestTimeInMinuteToString(swimMins)),
				"Suggest time for swimming is correct");

	}
	
	public void v_SleepGoalUpdated() {

		// currently nothing to check
		// preserve for future
	}

	public void v_WeightGoalUpdated() {

		// currently nothing to check
		// preserve for future
	}

	public void v_NewGoalConfirmation() {

		PrometheusHelper.waitForAlert();
		
		// check alert content
		Assert.assertTrue(HomeSettings.hasNewGoalInstructionMessage(),
				"Alert message is correct");
	}

	public void v_ActivityTimelineUpdated() {

		// check if new goal value had been updated
		String actual = HomeScreen.getCurrentActivityGoalString();
		String expect = this.activityGoal + "";
		logger.info("Actual goal is: " + actual + " - Expect goal is: "+ expect);
		Assert.assertTrue(actual.indexOf(expect) >= 0 || 
				ViewUtils.isExistedView("UILabel", String.format("of %s points", expect)),
				"Activity goal value is correct");
	}
	
	public void v_SleepTimelineUpdated() {

		// check if new goal value had been updated
		String actual = HomeScreen.getCurrentSleepGoalString();
		String expect = "of " + this.sleepGoalHours + " hour " +
				(this.sleepGoalMinutes == 0 ? "" : this.sleepGoalMinutes + " min ") + "goal";
		logger.info("Actual goal is: " + actual + " - Expect goal is: "+ expect);
		Assert.assertTrue(actual.indexOf(expect) >= 0 || 
				ViewUtils.isExistedView("UILabel",String.format("of %s points", expect)),
				"Sleep goal value is correct");
	}
	
	public void v_WeightTimelineUpdated() {

		// check if new goal value had been updated
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("goal weight: %d.%d", weightDigit, weightFraction)),
				"Weight goal value is correct");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", weightUnitUS ? "140.0 lbs" : "63.5 kg"),
				"Weight goal unit is correct");
	}

}
