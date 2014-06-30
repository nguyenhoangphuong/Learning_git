package com.misfit.ta.backend.aut.correctness.servercalculation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.google.resting.json.JSONException;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.performance.newservercalculation.NewServerCalculationScenario;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.TimestampObject;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsAutoSleepStateChange;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsGoalValueChange;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsTimezoneOffsetChange;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsTracking;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsTripleTapTypeChange;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.ShortcutsTyper;

public class BackendNewServerCalculationActivityGoalSettingsTracking extends
		BackendServerCalculationBase {
	protected int delayTime = 60000;
	// @Test(groups = { "ios", "Prometheus", "MVPBackend",
	// "NewServerCalculationGoalCreation", "NewServercalculation",
	// "GoalCreation" })
	public void NewServerCalculation_GoalCreation() throws IOException,
			JSONException {
		NewServerCalculationScenario scenarioTest = new NewServerCalculationScenario();
		String email = MVPApi.generateUniqueEmail();
		System.out.println(email);
		scenarioTest.runNewServerCalculationGoalCreationTest(email);
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend",
			"NewServerCalculationGoalCreation", "NewServercalculation",
			"GoalCreation", "TripleTapTypeChanging" })
	public void NewServerCalculation_GoalCreation_SimpleCase()
			throws IOException, JSONException {
		logger.info("Test if the goal is created corresponding to goal settings correctly (start time of file = timestamp of triple tap changing = start day)");
		String email = MVPApi.generateUniqueEmail();
		long timestamp = System.currentTimeMillis() / 1000;
		Long startDay = MVPCommon.getDayStartEpoch(timestamp);
		Long endDay = MVPCommon.getDayEndEpoch(timestamp);
		String token = MVPApi.signUp(email, "qqqqqq").token;
		String userId = MVPApi.getUserId(token);

		// create profile / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(startDay,
				null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(startDay,
				null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);

		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();

		Integer timezoneOffset = 25200;
		Double goalValue = 600.152;
		Integer tripleTapType = 2;
		Integer autoSleepState = 1;
		GoalSettingsTimezoneOffsetChange timezone = new GoalSettingsTimezoneOffsetChange(
				startDay, timezoneOffset);
		GoalSettingsGoalValueChange goalValueChange = new GoalSettingsGoalValueChange(
				startDay, goalValue);
		GoalSettingsTripleTapTypeChange tripleTapTypeChange = new GoalSettingsTripleTapTypeChange(
				startDay, tripleTapType);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				startDay, autoSleepState);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(timezone);
		changes.add(goalValueChange);
		changes.add(tripleTapTypeChange);
		changes.add(autoSleepStateChange);

		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(token, goalSettingsTracking);
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);
		GoalRawData data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0 * 60, 14 * 60));
		List<String> dataStrings = new ArrayList<String>();
		
		
		dataStrings.add(MVPApi.getRawDataAsString(startDay,
				timezoneOffset / 60, "0104", "18", data).rawData);
		pushSyncData(startDay + 14 * 60, userId, pedometer.getSerialNumberString(),
				dataStrings);
		
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);
		
		GoalsResult goalResult = MVPApi.searchGoal(token, 0l,
				(long) Integer.MAX_VALUE, 0l);

		Goal goal = goalResult.goals[0];
		boolean testPassed = true;
		// verify if goal is created with correct settings
		testPassed &= Verify.verifyEquals(goal.getStartTime(), startDay,
				"Start time of goal is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getEndTime(), endDay,
				"End time of goal is not correct") == null;
		testPassed &= Verify.verifyEquals(
				(int) Math.floor(goal.getGoalValue()),
				(int) Math.floor(goalValue), "Goal value is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getAutosleepState(),
				autoSleepState, "Auto sleep state is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getTimeZoneOffsetInSeconds(),
				timezoneOffset, "Timezone offset is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getTripleTapTypeChanges().get(0)
				.getActivityType(), tripleTapType,
				"Triple tap type is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getTripleTapTypeChanges().get(0)
				.getTimestamp(), startDay, "Triple tap type is not correct") == null;
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend",
			"NewServerCalculationGoalCreation", "NewServercalculation",
			"GoalCreation", "TripleTapTypeChanging1" })
	public void NewServerCalculation_GoalCreation_SimpleCase_ChangeTripleTapTypeBeforeSync()
			throws IOException, JSONException {
		logger.info("Test if the goal is created corresponding to goal settings correctly (start time of file < timestamp of triple tap changing)");
		String email = MVPApi.generateUniqueEmail();
		long timestamp = System.currentTimeMillis() / 1000;
		Long startDay = MVPCommon.getDayStartEpoch(timestamp);
		Long endDay = MVPCommon.getDayEndEpoch(timestamp);
		String token = MVPApi.signUp(email, "qqqqqq").token;
		String userId = MVPApi.getUserId(token);

		// create profile / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(startDay,
				null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(startDay,
				null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);

		Integer timezoneOffset = 25200;
		Double goalValue = 600.152;
		Integer tripleTapType = 2;
		Integer autoSleepState = 1;
		
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();
		GoalSettingsTimezoneOffsetChange timezone = new GoalSettingsTimezoneOffsetChange(
				startDay, timezoneOffset);
		GoalSettingsGoalValueChange goalValueChange = new GoalSettingsGoalValueChange(
				startDay, goalValue);
		GoalSettingsTripleTapTypeChange tripleTapTypeChange = new GoalSettingsTripleTapTypeChange(
				startDay  + 10 * 3600, tripleTapType);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				startDay, autoSleepState);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(timezone);
		changes.add(goalValueChange);
		changes.add(tripleTapTypeChange);
		changes.add(autoSleepStateChange);

		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(token, goalSettingsTracking);

		GoalRawData data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0 * 60, 14 * 60));
		List<String> dataStrings = new ArrayList<String>();
		
		
		dataStrings.add(MVPApi.getRawDataAsString(startDay,
				timezoneOffset / 60, "0104", "18", data).rawData);
		pushSyncData(startDay + 14 * 60, userId, pedometer.getSerialNumberString(),
				dataStrings);
		
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);
		
		GoalsResult goalResult = MVPApi.searchGoal(token, 0l,
				(long) Integer.MAX_VALUE, 0l);

		Goal goal = goalResult.goals[0];
		boolean testPassed = true;
		// verify if goal is created with correct settings
		testPassed &= Verify.verifyEquals(goal.getStartTime(), startDay,
				"Start time of goal is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getEndTime(), endDay,
				"End time of goal is not correct") == null;
		testPassed &= Verify.verifyEquals(
				(int) Math.floor(goal.getGoalValue()),
				(int) Math.floor(goalValue), "Goal value is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getAutosleepState(),
				autoSleepState, "Auto sleep state is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getTimeZoneOffsetInSeconds(),
				timezoneOffset, "Timezone offset is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getTripleTapTypeChanges().get(0)
				.getActivityType(), tripleTapType,
				"Triple tap type is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getTripleTapTypeChanges().get(0)
				.getTimestamp(), startDay + 10 * 3600, "Triple tap type is not correct") == null;
	}

//	@Test(groups = { "ios", "Prometheus", "MVPBackend",
//			"NewServerCalculationGoalCreation", "NewServercalculation",
//			"GoalCreation" })
	public void NewServerCalculation_GoalCreation_TimezoneChanging_TravelForward()
			throws IOException, JSONException {
		String email = MVPApi.generateUniqueEmail();
		long startDay = MVPCommon
				.getDayStartEpoch(System.currentTimeMillis() / 1000);
		String token = MVPApi.signUp(email, "qqqqqq").token;
		String userId = MVPApi.getUserId(token);

		// create profile / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(startDay,
				null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(startDay,
				null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);

		int[] timezoneOffsetInSeconds = { 25200, 36000 };
		System.out.println("****** Startday: " + startDay);
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();

		GoalSettingsTimezoneOffsetChange fromTimezone = new GoalSettingsTimezoneOffsetChange(
				startDay, timezoneOffsetInSeconds[0]);
		GoalSettingsGoalValueChange goalValue = new GoalSettingsGoalValueChange(
				startDay, 878.8);
		GoalSettingsTripleTapTypeChange tripleTapTypeChange = new GoalSettingsTripleTapTypeChange(
				startDay, 5);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				startDay, 0);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(fromTimezone);
		changes.add(goalValue);
		// changes.add(tripleTapTypeChange);
		changes.add(autoSleepStateChange);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(token, goalSettingsTracking);

		GoalRawData data = new GoalRawData();
		// - session: 60 minutes - 6000 steps - 600 points at 3:00am
		data.appendGoalRawData(generateEmptyRawData(0, 3 * 60));
		data.appendGoalRawData(generateSessionRawData(6000, 600, 60));
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay,
				timezoneOffsetInSeconds[0] / 60, "0104", "18", data).rawData);
		pushSyncData(startDay + 4 * 3600, userId, pedometer.getSerialNumberString(),
				dataStrings);

		goalSettingsTracking = new GoalSettingsTracking();
		changes = new ArrayList<TimestampObject>();
		GoalSettingsTimezoneOffsetChange toTimezone = new GoalSettingsTimezoneOffsetChange(
				startDay + 3600 * 3 + 1, timezoneOffsetInSeconds[1]);
		changes.add(toTimezone);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(token, goalSettingsTracking);
		data = new GoalRawData();
		// - session: 30 minutes - 3000 steps - 300 points at 4:30am
		data.appendGoalRawData(generateEmptyRawData(0, 30));
		data.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay + 4 * 3600,
				timezoneOffsetInSeconds[0] / 60, "0104", "18", data).rawData);
		pushSyncData(startDay + 5 * 3600, userId, pedometer.getSerialNumberString(),
				dataStrings);
		
		boolean testPassed = true;
		// verify end time of goal with new timezone, verify timeline items
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);
		
		GoalsResult goalResult = MVPApi.searchGoal(token, 0l,
				(long) Integer.MAX_VALUE, 0l);

		Goal goal = goalResult.goals[0];
		testPassed &= Verify.verifyEquals(goal.getEndTime(), goal.getEndTime() - timezoneOffsetInSeconds[1],
				"End time of goal is not correct when user changes timezone") == null;
		testPassed &= Verify.verifyEquals(goal.getStartTime(), goal.getStartTime(),
				"Start time of goal is not correct") == null;
		
		List<TimelineItem> actualItems = MVPApi.getTimelineItems(token, null,
				null, null, TimelineItemDataBase.TYPE_TIMEZONE);
	}
}
