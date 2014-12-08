package com.misfit.ta.backend.aut.correctness.servercalculation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.google.resting.json.JSONException;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.metawatch.UserInfo;
import com.misfit.ta.backend.aut.performance.newservercalculation.NewServerCalculationScenario;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.TimestampObject;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.goal.TripleTapData;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsAutoSleepStateChange;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsGoalValueChange;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsTimezoneOffsetChange;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsTracking;
import com.misfit.ta.backend.data.goalprogress.GoalSettingsTripleTapTypeChange;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.pedometer.Pedometer;
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
		UserInfo userInfo = MVPApi.signUp();
		System.out.println("Email : " + userInfo.getEmail());
		long timestamp = System.currentTimeMillis() / 1000;
		Long startDay = MVPCommon.getDayStartEpoch(timestamp);
		Long endDay = MVPCommon.getDayEndEpoch(timestamp);
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

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
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		GoalRawData data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0 * 60, 14 * 60));
		List<String> dataStrings = new ArrayList<String>();

		dataStrings.add(MVPApi.getRawDataAsString(startDay,
				timezoneOffset / 60, "0104", "18", data).rawData);
		pushSyncData(startDay + 14 * 60, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);

		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);

		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
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

		Assert.assertTrue(testPassed);
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend",
			"NewServerCalculationGoalCreation", "NewServercalculation",
			"GoalCreation", "TripleTapTypeChanging1" })
	public void NewServerCalculation_GoalCreation_SimpleCase_ChangeTripleTapTypeBeforeSync()
			throws IOException, JSONException {
		logger.info("Test if the goal is created corresponding to goal settings correctly (start time of file < timestamp of triple tap changing)");
		UserInfo userInfo = MVPApi.signUp();
		long timestamp = System.currentTimeMillis() / 1000;
		Long startDay = MVPCommon.getDayStartEpoch(timestamp);
		Long endDay = MVPCommon.getDayEndEpoch(timestamp);
		// create profile / pedometer / statistics
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

		Integer timezoneOffset = 25200;
		Double goalValue = 600.152;
		Integer tripleTapType = 2;
		Integer autoSleepState = 1;

		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();
		GoalSettingsTimezoneOffsetChange timezone = new GoalSettingsTimezoneOffsetChange(
				startDay, timezoneOffset);
		GoalSettingsGoalValueChange goalValueChange = new GoalSettingsGoalValueChange(
				startDay, goalValue);
		// change triple tap at 10:00am
		GoalSettingsTripleTapTypeChange tripleTapTypeChange = new GoalSettingsTripleTapTypeChange(
				startDay + 10 * 3600, tripleTapType);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				startDay, autoSleepState);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(timezone);
		changes.add(goalValueChange);
		changes.add(tripleTapTypeChange);
		changes.add(autoSleepStateChange);

		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		GoalRawData data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0 * 60, 13 * 60 + 30));
		data.appendGoalRawData(generateSessionRawData(3000, 300, 30));

		List<String> dataStrings = new ArrayList<String>();

		dataStrings.add(MVPApi.getRawDataAsString(startDay,
				timezoneOffset / 60, "0104", "18", data).rawData);
		pushSyncData(startDay + 14 * 60, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);

		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);

		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
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
				.getActivityType(), 100,
				"Default triple tap type is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getTripleTapTypeChanges().get(0)
				.getTimestamp(), startDay,
				"Default triple tap timestamp is not correct") == null;

		testPassed &= Verify.verifyEquals(goal.getTripleTapTypeChanges().get(1)
				.getActivityType(), tripleTapType,
				"Triple tap type is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getTripleTapTypeChanges().get(1)
				.getTimestamp(), startDay + 10 * 3600,
				"Triple tap timestamp is not correct") == null;

		Assert.assertTrue(testPassed);
	}


	@Test(groups = { "ios", "Prometheus", "MVPBackend",
			"NewServerCalculationGoalCreation", "NewServercalculation",
			"GoalCreation", "TravelForward", "Timezone" })
	public void NewServerCalculation_GoalCreation_TimezoneChanging_TravelForward()
			throws IOException, JSONException {
		UserInfo userInfo = MVPApi.signUp();
		int[] timezoneOffsetInSeconds = { 25200, 32400 };
		int diff = timezoneOffsetInSeconds[0] - timezoneOffsetInSeconds[1];
		long startDay = MVPCommon
				.getDayStartEpoch(System.currentTimeMillis() / 1000);
		long endDay = MVPCommon
				.getDayEndEpoch(System.currentTimeMillis() / 1000);

		// create profile / pedometer / statistics
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

		System.out.println("****** Startday: " + startDay);
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();

		GoalSettingsTimezoneOffsetChange fromTimezone = new GoalSettingsTimezoneOffsetChange(
				startDay, timezoneOffsetInSeconds[0]);
		GoalSettingsGoalValueChange goalValue = new GoalSettingsGoalValueChange(
				startDay, 700.8);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				startDay, 0);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(fromTimezone);
		changes.add(goalValue);
		changes.add(autoSleepStateChange);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		GoalRawData data = new GoalRawData();
		// - session: 60 minutes - 6000 steps - 600 points at 10:00am UTC+7
		data.appendGoalRawData(generateEmptyRawData(0, 10 * 60));
		data.appendGoalRawData(generateSessionRawData(6000, 600, 60));
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay,
				timezoneOffsetInSeconds[0] / 60, "0104", "18", data).rawData);
		pushSyncData(startDay + 11 * 3600, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);

		goalSettingsTracking = new GoalSettingsTracking();
		changes = new ArrayList<TimestampObject>();
		// change timezone at 11:00am UTC+7
		GoalSettingsTimezoneOffsetChange toTimezone = new GoalSettingsTimezoneOffsetChange(
				startDay + 3600 * 11, timezoneOffsetInSeconds[1]);
		changes.add(toTimezone);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);
		boolean testPassed = true;
		// verify end time of goal with new timezone, verify timeline items
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);

		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);
		List<TimelineItem> actualItems = MVPApi.getTimelineItems(
				userInfo.getToken(), startDay, endDay + diff, null,
				TimelineItemDataBase.TYPE_TIMEZONE);
		Goal goal = goalResult.goals[0];
		testPassed &= Verify.verifyEquals(goal.getEndTime(), endDay + diff,
				"End time of goal is not correct when user changes timezone") == null;
		testPassed &= Verify.verifyEquals(goal.getStartTime(), startDay,
				"Start time of goal is not correct") == null;

		testPassed &= Verify.verifyTrue(actualItems.size() == 1,
				"Timezone timeline item should be found") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getTimestamp(),
				startDay + 3600 * 11,
				"Timestamp of timeline item is not correct") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getItemType(),
				TimelineItemDataBase.TYPE_TIMEZONE,
				"Type of timeline item is not correct") == null;
		testPassed &= Verify
				.verifyTrue(
						isCorrectTimezoneTimelineItem(actualItems.get(0),
								timezoneOffsetInSeconds[0],
								timezoneOffsetInSeconds[1]),
						"Timezone timeline item should be created properly") == null;
		Assert.assertTrue(testPassed);

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend",
			"NewServerCalculationGoalCreation", "NewServercalculation",
			"GoalCreation", "TravelBackward", "Timezone" })
	public void NewServerCalculation_GoalCreation_TimezoneChanging_TravelBackward()
			throws IOException, JSONException {
		UserInfo userInfo = MVPApi.signUp();

		int[] timezoneOffsetInSeconds = { 32400, 25200 };
		int diff = timezoneOffsetInSeconds[0] - timezoneOffsetInSeconds[1];
		long startDay = MVPCommon
				.getDayStartEpoch(System.currentTimeMillis() / 1000) - diff;
		long endDay = MVPCommon
				.getDayEndEpoch(System.currentTimeMillis() / 1000) - diff;
		// create profile / pedometer / statistics
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

		System.out.println("****** Startday: " + startDay);
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();

		GoalSettingsTimezoneOffsetChange fromTimezone = new GoalSettingsTimezoneOffsetChange(
				startDay, timezoneOffsetInSeconds[0]);
		GoalSettingsGoalValueChange goalValue = new GoalSettingsGoalValueChange(
				startDay, 878.8);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				startDay, 0);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(fromTimezone);
		changes.add(goalValue);
		changes.add(autoSleepStateChange);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		GoalRawData data = new GoalRawData();
		// - session: 60 minutes - 6000 steps - 600 points at 10:00am UTC+9
		data.appendGoalRawData(generateEmptyRawData(0, 10 * 60));
		data.appendGoalRawData(generateSessionRawData(6000, 600, 60));
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay,
				timezoneOffsetInSeconds[0] / 60, "0104", "18", data).rawData);
		pushSyncData(startDay + 11 * 3600, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);

		goalSettingsTracking = new GoalSettingsTracking();
		changes = new ArrayList<TimestampObject>();
		// change timezone at 11:15am UTC+9
		GoalSettingsTimezoneOffsetChange toTimezone = new GoalSettingsTimezoneOffsetChange(
				startDay + 3600 * 11 + 60 * 15, timezoneOffsetInSeconds[1]);
		changes.add(toTimezone);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		boolean testPassed = true;
		// verify end time of goal with new timezone, verify timeline items
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);

		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);

		Goal goal = goalResult.goals[0];
		List<TimelineItem> actualItems = MVPApi.getTimelineItems(
				userInfo.getToken(), startDay, endDay + diff, null,
				TimelineItemDataBase.TYPE_TIMEZONE);
		testPassed &= Verify.verifyEquals(goal.getEndTime(), endDay + diff,
				"End time of goal is not correct when user changes timezone") == null;
		testPassed &= Verify.verifyEquals(goal.getStartTime(), startDay,
				"Start time of goal is not correct") == null;

		testPassed &= Verify.verifyTrue(actualItems.size() == 1,
				"Timezone timeline item should be found") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getTimestamp(),
				startDay + 3600 * 11 + 60 * 15,
				"Timestamp of timeline item is not correct") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getItemType(),
				TimelineItemDataBase.TYPE_TIMEZONE,
				"Type of timeline item is not correct") == null;
		testPassed &= Verify
				.verifyTrue(
						isCorrectTimezoneTimelineItem(actualItems.get(0),
								timezoneOffsetInSeconds[0],
								timezoneOffsetInSeconds[1]),
						"Timezone timeline item should be created properly") == null;
		Assert.assertTrue(testPassed);

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend",
			"NewServerCalculationGoalCreation", "NewServercalculation",
			"GoalCreation", "TravelBackwardDifferentDays", "Timezone" })
	public void NewServerCalculation_GoalCreation_TimezoneChanging_TravelBackward_DifferentDays()
			throws IOException, JSONException {
		UserInfo userInfo = MVPApi.signUp();

		int[] timezoneOffsetInSeconds = { 32400, 25200 };
		int diff = timezoneOffsetInSeconds[0] - timezoneOffsetInSeconds[1];
		long startDay = MVPCommon
				.getDayStartEpoch(System.currentTimeMillis() / 1000) - diff;
		long endDay = MVPCommon
				.getDayEndEpoch(System.currentTimeMillis() / 1000) - diff;
		// create profile / pedometer / statistics
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

		System.out.println("****** Startday: " + startDay);
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();

		GoalSettingsTimezoneOffsetChange fromTimezone = new GoalSettingsTimezoneOffsetChange(
				startDay, timezoneOffsetInSeconds[0]);
		GoalSettingsGoalValueChange goalValue = new GoalSettingsGoalValueChange(
				startDay, 878.8);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				startDay, 0);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(fromTimezone);
		changes.add(goalValue);
		changes.add(autoSleepStateChange);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		GoalRawData data = new GoalRawData();
		// - session: 60 minutes - 6000 steps - 600 points at 00:30am UTC+9
		data.appendGoalRawData(generateEmptyRawData(0, 30));
		data.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay,
				timezoneOffsetInSeconds[0] / 60, "0104", "18", data).rawData);
		pushSyncData(startDay + 3600, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);

		goalSettingsTracking = new GoalSettingsTracking();
		changes = new ArrayList<TimestampObject>();
		// change timezone at 01:00am UTC+9
		GoalSettingsTimezoneOffsetChange toTimezone = new GoalSettingsTimezoneOffsetChange(
				startDay + 3600, timezoneOffsetInSeconds[1]);
		changes.add(toTimezone);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		boolean testPassed = true;
		// verify end time of goal with new timezone, verify timeline items
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);

		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);

		Goal goal = goalResult.goals[0];
		List<TimelineItem> actualItems = MVPApi.getTimelineItems(
				userInfo.getToken(), startDay, endDay + diff, null,
				TimelineItemDataBase.TYPE_TIMEZONE);
		testPassed &= Verify.verifyEquals(goal.getEndTime(), endDay + diff,
				"End time of goal is not correct when user changes timezone") == null;
		testPassed &= Verify.verifyEquals(goal.getStartTime(), startDay,
				"Start time of goal is not correct") == null;

		testPassed &= Verify.verifyTrue(actualItems.size() == 1,
				"Timezone timeline item should be found") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getTimestamp(),
				startDay + 3600, "Timestamp of timeline item is not correct") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getItemType(),
				TimelineItemDataBase.TYPE_TIMEZONE,
				"Type of timeline item is not correct") == null;
		testPassed &= Verify
				.verifyTrue(
						isCorrectTimezoneTimelineItem(actualItems.get(0),
								timezoneOffsetInSeconds[0],
								timezoneOffsetInSeconds[1]),
						"Timezone timeline item should be created properly") == null;
		Assert.assertTrue(testPassed);

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend",
			"NewServerCalculationGoalCreation", "NewServercalculation",
			"GoalCreation", "TravelForwardDifferentDays_OneGoal", "Timezone" })
	public void NewServerCalculation_GoalCreation_TimezoneChanging_TravelForward_DifferentDays_OneGoal()
			throws IOException, JSONException {
		UserInfo userInfo = MVPApi.signUp();

		int[] timezoneOffsetInSeconds = { 25200, 34200 };
		int diff = timezoneOffsetInSeconds[0] - timezoneOffsetInSeconds[1];
		long startDay = MVPCommon
				.getDayStartEpoch(System.currentTimeMillis() / 1000);
		long endDay = MVPCommon
				.getDayEndEpoch(System.currentTimeMillis() / 1000);
		// create profile / pedometer / statistics
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

		System.out.println("****** Startday: " + startDay);
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();

		GoalSettingsTimezoneOffsetChange fromTimezone = new GoalSettingsTimezoneOffsetChange(
				startDay, timezoneOffsetInSeconds[0]);
		GoalSettingsGoalValueChange goalValue = new GoalSettingsGoalValueChange(
				startDay, 878.8);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				startDay, 0);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(fromTimezone);
		changes.add(goalValue);
		changes.add(autoSleepStateChange);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		GoalRawData data = new GoalRawData();
		// - session: 30 minutes - 3000 steps - 300 points at 22:30pm UTC+7
		data.appendGoalRawData(generateEmptyRawData(0, 22 * 60));
		data.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay,
				timezoneOffsetInSeconds[0] / 60, "0104", "18", data).rawData);
		pushSyncData(startDay + 22 * 3600 + 30 * 60, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);

		goalSettingsTracking = new GoalSettingsTracking();
		changes = new ArrayList<TimestampObject>();
		// change timezone at 23:18pm UTC+7
		Long changeTimezoneTimestamp = startDay + 23 * 3600 + 60 * 18;
		GoalSettingsTimezoneOffsetChange toTimezone = new GoalSettingsTimezoneOffsetChange(
				changeTimezoneTimestamp, timezoneOffsetInSeconds[1]);
		changes.add(toTimezone);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		boolean testPassed = true;
		// verify end time of goal with new timezone, verify timeline items
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);

		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);
		Long endDayOfNewGoal = endDay + 60 * 24 - diff * 3600;
		List<TimelineItem> actualItems = MVPApi.getTimelineItems(
				userInfo.getToken(), startDay, endDayOfNewGoal, null,
				TimelineItemDataBase.TYPE_TIMEZONE);

		testPassed &= Verify
				.verifyEquals(goalResult.goals[0].getEndTime(),
						changeTimezoneTimestamp,
						"End time of old goal is not correct when user changes timezone") == null;
		testPassed &= Verify.verifyEquals(goalResult.goals[0].getStartTime(),
				startDay, "Start time of old goal is not correct") == null;

		testPassed &= Verify.verifyTrue(actualItems.size() == 1,
				"Timezone timeline item should be found") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getTimestamp(),
				startDay + 3600 * 23 + 60 * 18,
				"Timestamp of timeline item is not correct") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getItemType(),
				TimelineItemDataBase.TYPE_TIMEZONE,
				"Type of timeline item is not correct") == null;
		testPassed &= Verify
				.verifyTrue(
						isCorrectTimezoneTimelineItem(actualItems.get(0),
								timezoneOffsetInSeconds[0],
								timezoneOffsetInSeconds[1]),
						"Timezone timeline item should be created properly") == null;
		Assert.assertTrue(testPassed);

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend",
			"NewServerCalculationGoalCreation", "NewServercalculation",
			"GoalCreation", "TravelForwardDifferentDays_TwoGoals", "Timezone" })
	public void NewServerCalculation_GoalCreation_TimezoneChanging_TravelForward_DifferentDays_TwoGoals()
			throws IOException, JSONException {
		UserInfo userInfo = MVPApi.signUp();

		int[] timezoneOffsetInSeconds = { 25200, 34200 };
		int diff = timezoneOffsetInSeconds[0] - timezoneOffsetInSeconds[1];
		long startDay = MVPCommon
				.getDayStartEpoch(System.currentTimeMillis() / 1000);
		long endDay = MVPCommon
				.getDayEndEpoch(System.currentTimeMillis() / 1000);
		// create profile / pedometer / statistics
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

		System.out.println("****** Startday: " + startDay);
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();

		GoalSettingsTimezoneOffsetChange fromTimezone = new GoalSettingsTimezoneOffsetChange(
				startDay, timezoneOffsetInSeconds[0]);
		GoalSettingsGoalValueChange goalValue = new GoalSettingsGoalValueChange(
				startDay, 878.8);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				startDay, 0);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(fromTimezone);
		changes.add(goalValue);
		changes.add(autoSleepStateChange);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		GoalRawData data = new GoalRawData();
		// - session: 30 minutes - 3000 steps - 300 points at 22:30pm UTC+7
		data.appendGoalRawData(generateEmptyRawData(0, 22 * 60));
		data.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay,
				timezoneOffsetInSeconds[0] / 60, "0104", "18", data).rawData);
		pushSyncData(startDay + 22 * 3600 + 30 * 60, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);

		goalSettingsTracking = new GoalSettingsTracking();
		changes = new ArrayList<TimestampObject>();
		// change timezone at 23:18pm UTC+7
		Long changeTimezoneTimestamp = startDay + 23 * 3600 + 60 * 18;
		GoalSettingsTimezoneOffsetChange toTimezone = new GoalSettingsTimezoneOffsetChange(
				changeTimezoneTimestamp, timezoneOffsetInSeconds[1]);
		changes.add(toTimezone);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		ShortcutsTyper.delayTime(10000);
		data = new GoalRawData();
		// - session: 30 minutes - 3000 steps - 300 points at 23:48pm UTC+7
		// from 22:30 to 23:18
		data.appendGoalRawData(generateEmptyRawData(0, 48));
		// from 23:18 to 23:48
		data.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay + 22 * 3600 + 30,
				timezoneOffsetInSeconds[0] / 60, "0104", "18", data).rawData);
		// push sync data the second time to create new goal after changing
		// timezone
		pushSyncData(startDay + 23 * 3600 + 50 * 60, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);

		boolean testPassed = true;
		// verify end time of goal with new timezone, verify timeline items
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);

		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);
		Long endDayOfNewGoal = endDay + 3600 * 24 + diff;
		List<TimelineItem> actualItems = MVPApi.getTimelineItems(
				userInfo.getToken(), startDay, endDayOfNewGoal, null,
				TimelineItemDataBase.TYPE_TIMEZONE);

		testPassed &= Verify.verifyEquals(goalResult.goals.length, 2,
				"We assume that 2 goals are returned") == null;

		testPassed &= Verify
				.verifyEquals(goalResult.goals[1].getEndTime(),
						changeTimezoneTimestamp,
						"End time of old goal is not correct when user changes timezone") == null;
		testPassed &= Verify.verifyEquals(goalResult.goals[1].getStartTime(),
				startDay, "Start time of old goal is not correct") == null;

		testPassed &= Verify
				.verifyEquals(goalResult.goals[0].getEndTime(),
						endDayOfNewGoal,
						"End time of new goal is not correct when user changes timezone") == null;
		testPassed &= Verify.verifyEquals(goalResult.goals[0].getStartTime(),
				changeTimezoneTimestamp + 1,
				"Start time of new goal is not correct") == null;

		testPassed &= Verify.verifyTrue(actualItems.size() == 1,
				"Timezone timeline item should be found") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getTimestamp(),
				startDay + 3600 * 23 + 60 * 18,
				"Timestamp of timeline item is not correct") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getItemType(),
				TimelineItemDataBase.TYPE_TIMEZONE,
				"Type of timeline item is not correct") == null;
		testPassed &= Verify
				.verifyTrue(
						isCorrectTimezoneTimelineItem(actualItems.get(0),
								timezoneOffsetInSeconds[0],
								timezoneOffsetInSeconds[1]),
						"Timezone timeline item should be created properly") == null;
		Assert.assertTrue(testPassed);

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend",
			"NewServerCalculationGoalCreation", "NewServercalculation",
			"GoalCreation", "ChangeTripleTapSeveralTimesInADay" })
	public void NewServerCalculation_GoalCreation_ChangeTripleTapSeveralTimesInADay() {
		UserInfo userInfo = MVPApi.signUp();
		long timestamp = System.currentTimeMillis() / 1000;
		Long startDay = MVPCommon.getDayStartEpoch(timestamp);
		Long endDay = MVPCommon.getDayEndEpoch(timestamp);
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

		Integer[] tripleTapTypes = { 2, 3, 5, 6 };
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();

		Integer timezoneOffset = 25200;
		Double goalValue = 600.152;
		Integer autoSleepState = 1;
		GoalSettingsTimezoneOffsetChange timezone = new GoalSettingsTimezoneOffsetChange(
				startDay, timezoneOffset);
		GoalSettingsGoalValueChange goalValueChange = new GoalSettingsGoalValueChange(
				startDay, goalValue);
		GoalSettingsTripleTapTypeChange tripleTapTypeChange = new GoalSettingsTripleTapTypeChange(
				startDay, tripleTapTypes[0]);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				startDay, autoSleepState);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(timezone);
		changes.add(goalValueChange);
		changes.add(tripleTapTypeChange);
		changes.add(autoSleepStateChange);

		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		GoalRawData data = new GoalRawData();
		// - session: 60 minutes - 6000 steps - 600 points at 05:00
		data.appendGoalRawData(generateEmptyRawData(0, 5 * 60));
		data.appendGoalRawData(generateSessionRawData(6000, 600, 60));
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay, 25200 / 60, "0104",
				"18", data).rawData);
		System.out.println("Push Sync Data!");
		pushSyncData(startDay + 6 * 3600, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);
		Long[] timestampList = { startDay, startDay + 8 * 3600,
				startDay + 12 * 3600, startDay + 15 * 3600 };
		for (int i = 1; i < tripleTapTypes.length; i++) {
			changeActivityType(userInfo, startDay,
					(int) ((timestampList[i] - startDay) / 3600),
					tripleTapTypes[i]);
			ShortcutsTyper.delayTime(2000);
		}
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);
		boolean testPassed = true;
		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);

		Goal goal = goalResult.goals[0];
		List<TripleTapData> tripleTapDataList = goal.getTripleTapTypeChanges();
		for (int i = 0; i < tripleTapDataList.size(); i++) {
			logger.info("Checking triple tap type no " + i);
			testPassed &= Verify.verifyEquals(tripleTapTypes[i],
					tripleTapDataList.get(i).getActivityType(),
					"Triple tap type is not correct") == null;
			testPassed &= Verify.verifyEquals(timestampList[i],
					tripleTapDataList.get(i).getTimestamp(),
					"Triple tap timestamp is not correct") == null;
		}

		Assert.assertTrue(testPassed);
	}

	private void changeActivityType(UserInfo userInfo, Long startDay, int hour,
			int tripleTapType) {
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();
		;
		GoalSettingsTripleTapTypeChange tripleTapTypeChange = new GoalSettingsTripleTapTypeChange(
				startDay + hour * 3600, tripleTapType);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		;
		changes.add(tripleTapTypeChange);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend",
			"NewServerCalculationGoalCreation", "NewServercalculation",
			"GoalCreation", "DataLoss" })
	public void NewServerCalculation_GoalCreation_DataLoss() {
		UserInfo userInfo = MVPApi.signUp();
		long timestamp = System.currentTimeMillis() / 1000;
		Long startDay = MVPCommon.getDayStartEpoch(timestamp);
		Long endDay = MVPCommon.getDayEndEpoch(timestamp);
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();

		GoalSettingsTimezoneOffsetChange timezone = new GoalSettingsTimezoneOffsetChange(
				startDay, 25200);
		GoalSettingsGoalValueChange goalValue = new GoalSettingsGoalValueChange(
				startDay, 800.8);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				startDay, 1);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(timezone);
		changes.add(goalValue);
		changes.add(autoSleepStateChange);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		GoalRawData data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0, 30));
		data.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		// push sync data in the middle of the day: timestamp - 3600
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(timestamp - 3600, 25200,
				"0104", "18", data).rawData);
		pushSyncData(timestamp, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);

		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);

		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);

		Goal goal = goalResult.goals[0];
		boolean testPassed = true;
		// verify if goal is created with correct settings
		testPassed &= Verify.verifyEquals(goal.getStartTime(), startDay,
				"Start time of goal is not correct") == null;
		testPassed &= Verify.verifyEquals(goal.getEndTime(), endDay,
				"End time of goal is not correct") == null;

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend",
			"NewServerCalculationGoalCreation", "NewServercalculation",
			"GoalCreation", "DataLossInSeveralDays" })
	public void NewServerCalculation_GoalCreation_DataLossInSeveralDays() {
		UserInfo userInfo = MVPApi.signUp();
		long timestamp = System.currentTimeMillis() / 1000;
		Long startDay = MVPCommon.getDayStartEpoch(timestamp);
		Long endDay = MVPCommon.getDayEndEpoch(timestamp);
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

		Long threeDaysAgoStartDay = startDay - 3600 * 3 * 24;
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();

		GoalSettingsTimezoneOffsetChange timezone = new GoalSettingsTimezoneOffsetChange(
				threeDaysAgoStartDay, 25200);
		GoalSettingsGoalValueChange goalValue = new GoalSettingsGoalValueChange(
				threeDaysAgoStartDay, 800.8);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				threeDaysAgoStartDay, 1);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(timezone);
		changes.add(goalValue);
		changes.add(autoSleepStateChange);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		GoalRawData data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0, 30));
		data.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		// push sync data in the middle of the day: timestamp - 3600
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(threeDaysAgoStartDay, 25200,
				"0104", "18", data).rawData);
		pushSyncData(timestamp - 3600 * 24 * 3, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);

		ShortcutsTyper.delayTime(10000);
		data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0, 30));
		data.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay, 25200, "0104",
				"18", data).rawData);
		pushSyncData(timestamp, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);

		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);

		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);
		for (int i = 0; i < goalResult.goals.length; i++) {
			logger.info(goalResult.goals[i].getStartTime() + " "
					+ goalResult.goals[i].getEndTime());
		}

		boolean testPassed = true;
		testPassed &= Verify.verifyTrue(goalResult.goals.length > 1,
				"More goals should be created") == null;
		// verify if goal is created with correct settings
		for (int i = 0; i < goalResult.goals.length; i++) {
			Goal goal = goalResult.goals[i];
			testPassed &= Verify.verifyEquals(goal.getStartTime(), startDay - i
					* 24 * 3600, "Start time of goal in day " + i
					+ " is not correct") == null;
			testPassed &= Verify.verifyEquals(goal.getEndTime(), endDay - i
					* 24 * 3600, "End time of goal in day " + i
					+ " is not correct") == null;
		}

		Assert.assertTrue(testPassed);
	}

	 @Test(groups = { "ios", "Prometheus", "MVPBackend",
	 "NewServerCalculationGoalCreation", "NewServercalculation",
	 "GoalCreation", "SyncDataOfSeveralDays" })
	public void NewServerCalculation_GoalCreation_SyncDataOfSeveralDays() {
		UserInfo userInfo = MVPApi.signUp();
		long timestamp = System.currentTimeMillis() / 1000;
		Long startDay = MVPCommon.getDayStartEpoch(timestamp);
		Long endDay = MVPCommon.getDayEndEpoch(timestamp);
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

		Long threeDaysAgoStartDay = startDay - 3600 * 3 * 24;
		GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();

		GoalSettingsTimezoneOffsetChange timezone = new GoalSettingsTimezoneOffsetChange(
				threeDaysAgoStartDay, 25200);
		GoalSettingsGoalValueChange goalValue = new GoalSettingsGoalValueChange(
				threeDaysAgoStartDay, 800.8);
		GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
				threeDaysAgoStartDay, 1);
		List<TimestampObject> changes = new ArrayList<TimestampObject>();
		changes.add(timezone);
		changes.add(goalValue);
		changes.add(autoSleepStateChange);
		goalSettingsTracking.setChanges(changes);
		MVPApi.createTrackingGoalSettings(userInfo.getToken(),
				goalSettingsTracking);

		List<String> dataStrings = new ArrayList<String>();
		// 3 days ago
		GoalRawData data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0, 13 * 60));
		data.appendGoalRawData(generateSessionRawData(6000, 600, 60));
		dataStrings.add(MVPApi.getRawDataAsString(threeDaysAgoStartDay, 25200,
				"0104", "18", data).rawData);

		// 2 days ago
		data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0, 12 * 60));
		data.appendGoalRawData(generateSessionRawData(8000, 800, 80));
		dataStrings
				.add(MVPApi.getRawDataAsString(threeDaysAgoStartDay + 24 * 3600,
						25200, "0104", "18", data).rawData);

		// yesterday
		data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0, 10 * 60));
		data.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		dataStrings
				.add(MVPApi.getRawDataAsString(
						threeDaysAgoStartDay + 2 * 24 * 3600, 25200, "0104",
						"18", data).rawData);

		// today
		data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0, 2 * 60));
		dataStrings.add(MVPApi.getRawDataAsString(startDay, 25200, "0104",
				"18", data).rawData);

		pushSyncData(timestamp, userInfo.getUserId(),
				pedometer.getSerialNumberString(), dataStrings);
		
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);
		
		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);
		boolean testPassed = true;
		for (int i = 0; i < goalResult.goals.length; i++) {
			Goal goal = goalResult.goals[i];
			logger.info("****** Verifty goal in day " + i);
			testPassed &= Verify.verifyEquals(goal.getStartTime(), startDay - i
					* 24 * 3600, "Start time of goal in day " + i
					+ " is not correct") == null;
			testPassed &= Verify.verifyEquals(goal.getEndTime(), endDay - i
					* 24 * 3600, "End time of goal in day " + i
					+ " is not correct") == null;
			testPassed &= Verify.verifyEquals(
					(int) Math.floor(goal.getGoalValue()),
					(int) Math.floor(goalValue.getgoalValue()), "Goal value is not correct") == null;
			testPassed &= Verify.verifyEquals(goal.getAutosleepState(),
					1, "Auto sleep state is not correct") == null;
			testPassed &= Verify.verifyEquals(goal.getTimeZoneOffsetInSeconds(),
					25200, "Timezone offset is not correct") == null;
		}
		Assert.assertTrue(testPassed);
	}
	 
	 @Test(groups = { "ios", "Prometheus", "MVPBackend",
			 "NewServerCalculationGoalCreation", "NewServercalculation",
			 "GoalCreation", "PushTrackChangesSyncDataOfSeveralDays" })
			public void NewServerCalculation_GoalCreation_PushTrackChangesSyncDataOfSeveralDays() {
				UserInfo userInfo = MVPApi.signUp();
				long timestamp = System.currentTimeMillis() / 1000;
				Long startDay = MVPCommon.getDayStartEpoch(timestamp);
				Long endDay = MVPCommon.getDayEndEpoch(timestamp);
				Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

				Long threeDaysAgoStartDay = startDay - 3600 * 3 * 24;
				GoalSettingsTracking goalSettingsTracking = new GoalSettingsTracking();
				int[] timezoneOffsetInSeconds = { 25200, 34200, 28800 };
				GoalSettingsTimezoneOffsetChange timezone = new GoalSettingsTimezoneOffsetChange(
						threeDaysAgoStartDay, timezoneOffsetInSeconds[0]);
				GoalSettingsGoalValueChange goalValue = new GoalSettingsGoalValueChange(
						threeDaysAgoStartDay, 800.8);
				GoalSettingsAutoSleepStateChange autoSleepStateChange = new GoalSettingsAutoSleepStateChange(
						threeDaysAgoStartDay, 1);
				List<TimestampObject> changes = new ArrayList<TimestampObject>();
				changes.add(timezone);
				changes.add(goalValue);
				changes.add(autoSleepStateChange);
				goalSettingsTracking.setChanges(changes);
				MVPApi.createTrackingGoalSettings(userInfo.getToken(),
						goalSettingsTracking);

				// after this step, user doesn't sync 
				List<String> dataStrings = new ArrayList<String>();
				// 3 days ago
				GoalRawData data = new GoalRawData();
				data.appendGoalRawData(generateEmptyRawData(0, 13 * 60));
				data.appendGoalRawData(generateSessionRawData(6000, 600, 60));
				dataStrings.add(MVPApi.getRawDataAsString(threeDaysAgoStartDay, 25200,
						"0104", "18", data).rawData);
				// change timezone at 20:00pm 3 days ago
				GoalSettingsTracking goalSettingsTracking1 = new GoalSettingsTracking();
				GoalSettingsTimezoneOffsetChange timezone1 = new GoalSettingsTimezoneOffsetChange(
						threeDaysAgoStartDay + 20 * 3600, timezoneOffsetInSeconds[1]);
				List<TimestampObject> changes1 = new ArrayList<TimestampObject>();
				changes1.add(timezone1);
				goalSettingsTracking1.setChanges(changes1);
				
				// 2 days ago
				data = new GoalRawData();
				data.appendGoalRawData(generateEmptyRawData(0, 12 * 60));
				data.appendGoalRawData(generateSessionRawData(8000, 800, 80));
				dataStrings
						.add(MVPApi.getRawDataAsString(threeDaysAgoStartDay + 24 * 3600,
								25200, "0104", "18", data).rawData);

				// yesterday
				data = new GoalRawData();
				data.appendGoalRawData(generateEmptyRawData(0, 10 * 60));
				data.appendGoalRawData(generateSessionRawData(5000, 500, 50));
				dataStrings
						.add(MVPApi.getRawDataAsString(
								threeDaysAgoStartDay + 2 * 24 * 3600, 25200, "0104",
								"18", data).rawData);

				// today
				data = new GoalRawData();
				data.appendGoalRawData(generateEmptyRawData(0, 2 * 60));
				dataStrings.add(MVPApi.getRawDataAsString(startDay, 25200, "0104",
						"18", data).rawData);

				GoalSettingsTracking goalSettingsTracking2 = new GoalSettingsTracking();
				GoalSettingsTimezoneOffsetChange timezone2 = new GoalSettingsTimezoneOffsetChange(
						threeDaysAgoStartDay + 2 * 24 * 3600, timezoneOffsetInSeconds[2]);
				List<TimestampObject> changes2 = new ArrayList<TimestampObject>();
				changes2.add(timezone2);
				goalSettingsTracking2.setChanges(changes2);
				
				
				MVPApi.createTrackingGoalSettings(userInfo.getToken(),
						goalSettingsTracking1);
				MVPApi.createTrackingGoalSettings(userInfo.getToken(),
						goalSettingsTracking2);
				pushSyncData(timestamp, userInfo.getUserId(),
						pedometer.getSerialNumberString(), dataStrings);
				
				logger.info("Waiting " + delayTime + " miliseconds");
				ShortcutsTyper.delayTime(delayTime);
				
				GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
						(long) Integer.MAX_VALUE, 0l);
				boolean testPassed = true;
				// Verify latest goal, it should be timezone[2]
				testPassed &= Verify.verifyEquals(goalResult.goals[0].getTimeZoneOffsetInSeconds(), timezoneOffsetInSeconds[2], "Timezone of the latest goal is incorrect") == null;
				Assert.assertTrue(testPassed);
	 }
	 
	 @Test(groups = { "ios", "Prometheus", "MVPBackend",
			 "NewServerCalculationGoalCreation", "NewServercalculation",
			 "GoalCreation", "DefaultTrackChanges" })
			public void NewServerCalculation_GoalCreation_DefaultTrackChanges() {
				UserInfo userInfo = MVPApi.signUp();
				long timestamp = System.currentTimeMillis() / 1000;
				Long startDay = MVPCommon.getDayStartEpoch(timestamp);
				Long endDay = MVPCommon.getDayEndEpoch(timestamp);
				Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);
				
				// Don't push track changes, default timezone will be UTZC+0
				GoalRawData data = new GoalRawData();
				data.appendGoalRawData(generateEmptyRawData(0 * 60, 14 * 60));
				List<String> dataStrings = new ArrayList<String>();

				dataStrings.add(MVPApi.getRawDataAsString(startDay,
						25200 / 60, "0104", "18", data).rawData);
				pushSyncData(startDay + 14 * 60, userInfo.getUserId(),
						pedometer.getSerialNumberString(), dataStrings);

				//data crosses 2 days in timezone UTC+0, hence 2 goals will be created
				
				logger.info("Waiting " + delayTime + " miliseconds");
				ShortcutsTyper.delayTime(delayTime);

				GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
						(long) Integer.MAX_VALUE, 0l);

				Goal goal = goalResult.goals[0];
				boolean testPassed = true;
				testPassed &= Verify.verifyEquals(goalResult.goals.length, 2, "Two goals are created oke") == null;
				testPassed &= Verify.verifyEquals(goalResult.goals[0].getTimeZoneOffsetInSeconds(), 0, "Timezone of the latest goal is incorrect") == null;
				testPassed &= Verify.verifyEquals(goalResult.goals[1].getTimeZoneOffsetInSeconds(), 0, "Timezone of the yesterday goal is incorrect") == null;

				Assert.assertTrue(testPassed);

	 }
	 
	 @Test(groups = { "ios", "Prometheus", "MVPBackend",
			 "NewServerCalculationGoalCreation", "NewServercalculation",
			 "GoalCreation", "PushTrackChangesSyncDataOfSeveralDays" })
	 public void createWrongRawDataForNewAccount(){
		 String email = MVPApi.generateUniqueEmail();
		 String password = "qwerty";
		 String token = MVPApi.signUp(email, password).token;
		 long timestamp = System.currentTimeMillis() / 1000 - 3600*24;
		 
		 Goal goal = Goal.getDefaultGoal(timestamp);
		 GoalsResult result = MVPApi.createGoal(token, goal);
		 Verify.verifyTrue(result.isOK(), "Can't create goal!");
		 goal.setServerId(result.goals[0].getServerId());
		 goal.setUpdatedAt(result.goals[0].getUpdatedAt());
		 
		 GoalRawData goalRawData = new GoalRawData();
		 goalRawData.appendGoalRawData(generateEmptyRawData(0, 6*60));
		 goalRawData.appendGoalRawData(generateSessionRawData(-1, -1, 50));
		 
		 BaseResult baseResult = MVPApi.pushRawData(token, goal.getServerId(), goalRawData, 0);
		 System.out.println("BaseResult : " + baseResult.isOK());
	 }
	 
	 @Test(groups = { "ios", "Prometheus", "MVPBackend",
			 "NewServerCalculationGoalCreation", "NewServercalculation",
			 "GoalCreation", "PushTrackChangesSyncDataOfSeveralDays" })
	 public void pushRawDataWithOldAccount(){
		 pushRawDataWithNewAccount(false);
	 }
	 
	 @Test(groups = { "ios", "Prometheus", "MVPBackend",
			 "NewServerCalculationGoalCreation", "NewServercalculation",
			 "GoalCreation", "DefaultTrackChanges" })
	 public void pushRawDataWithNewAccount(boolean isNew){
		 String email = MVPApi.generateUniqueEmail();
		 String password = "qwerty";
		 String token = "";
		 if(isNew){
			 token = MVPApi.signUp(email, password, true).token;
		 }else{
			 token = MVPApi.signUp(email, password).token;
		 }
		 
		 long timestamp = System.currentTimeMillis() / 1000 - 3600*24;
		 
		 Goal goal = Goal.getDefaultGoal(timestamp);
		 GoalsResult result = MVPApi.createGoal(token, goal);
		 
		 Verify.verifyTrue(result.isOK(), "Can't create goal!");
		 goal.setServerId(result.goals[0].getServerId());
		 goal.setUpdatedAt(result.goals[0].getUpdatedAt());
		 
		 //6:00 AM  - 50 minutes - 4000 steps - 140 points
		 //7:30 AM  - 60 mintutes - 4800 steps - 200 points
		 //9:45 AM -  30 mitues - 4200 steps - 300  points
		 //11:00 AM - 60 minutes - 4000 steps - 500 points
		 //14:00 PM - 120 minutes - 7200 steps - 1200 points
		 //18:30 PM - 40 mintues - 1200 steps - 200 points
		 
		 GoalRawData goalRawData = new GoalRawData();
		 goalRawData.appendGoalRawData(generateEmptyRawData(0, 6*60));
		 goalRawData.appendGoalRawData(generateSessionRawData(4000, 140, 50));
		 
		 goalRawData.appendGoalRawData(generateEmptyRawData(6*60 + 50, 7*60 + 30));
		 goalRawData.appendGoalRawData(generateSessionRawData(4800, 200, 60));
		 
		 goalRawData.appendGoalRawData(generateEmptyRawData(8*60 + 30, 9*60 + 45));
		 goalRawData.appendGoalRawData(generateSessionRawData(4200, 300, 30));
		 
		 goalRawData.appendGoalRawData(generateEmptyRawData(10*60 + 15, 11*60));
		 goalRawData.appendGoalRawData(generateSessionRawData(4000, 500, 60));
		 
		 goalRawData.appendGoalRawData(generateEmptyRawData(12*60, 14*60));
		 goalRawData.appendGoalRawData(generateSessionRawData(7200, 1200, 120));
		 
		 goalRawData.appendGoalRawData(generateEmptyRawData(16*60, 18*60 + 30));
		 goalRawData.appendGoalRawData(generateSessionRawData(1200, 200, 40));
		 
		 goalRawData.appendGoalRawData(generateEmptyRawData(24*60, 25*60));
			
		 BaseResult baseResult = MVPApi.pushRawData(token, goal.getServerId(), goalRawData, 0);
		 System.out.println("BaseResult : " + baseResult.isOK());
		 logger.info("Waiting " + delayTime + " miliseconds");
			ShortcutsTyper.delayTime(delayTime);
		 List<GraphItem> graphItemList = MVPApi.getGraphItems(token, goal.getStartTime(), goal.getEndTime(), 0l);
		 
		 System.out.println("Size : " + graphItemList.size());
	 }

}
