package com.misfit.ta.backend.aut.correctness.servercalculation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.google.resting.json.JSONException;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.internalapi.UserInfo;
import com.misfit.ta.backend.aut.performance.newservercalculation.NewServerCalculationScenario;
import com.misfit.ta.backend.data.DataGenerator;
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

//	 @Test(groups = { "ios", "Prometheus", "MVPBackend",
//	 "NewServerCalculationGoalCreation", "NewServercalculation",
//	 "GoalCreation" })
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
		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);
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

	private Pedometer setUpNewAccount(String token, Long startDay) {
		ProfileData profile = DataGenerator.generateRandomProfile(startDay,
				null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(startDay,
				null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);
		return pedometer;
	}

	 @Test(groups = { "ios", "Prometheus", "MVPBackend",
	 "NewServerCalculationGoalCreation", "NewServercalculation",
	 "GoalCreation", "TravelForward", "Timezone" })
	public void NewServerCalculation_GoalCreation_TimezoneChanging_TravelForward()
			throws IOException, JSONException {
		UserInfo userInfo = MVPApi.signUp();
		int[] timezoneOffsetInSeconds = { 25200, 36000 };
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

		Goal goal = goalResult.goals[0];
		testPassed &= Verify.verifyEquals(goal.getEndTime(), endDay - diff,
				"End time of goal is not correct when user changes timezone") == null;
		testPassed &= Verify.verifyEquals(goal.getStartTime(), startDay,
				"Start time of goal is not correct") == null;

		List<TimelineItem> actualItems = MVPApi.getTimelineItems(
				userInfo.getToken(), startDay, endDay - diff, null,
				TimelineItemDataBase.TYPE_TIMEZONE);
		testPassed &= Verify.verifyTrue(actualItems.size() == 1,
				"Timezone timeline item should be found") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getTimestamp(),
				startDay + 3600 * 11,
				"Timestamp of timeline item is not correct") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getItemType(),
				TimelineItemDataBase.TYPE_TIMEZONE,
				"Type of timeline item is not correct") == null;
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
		long startDay = MVPCommon.getDayStartEpoch(
				System.currentTimeMillis() / 1000, TimeZone.getTimeZone("UTC"))
				+ diff;
		long endDay = MVPCommon.getDayEndEpoch(
				System.currentTimeMillis() / 1000, TimeZone.getTimeZone("UTC"))
				+ diff;
		// create profile / pedometer / statistics
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

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
		testPassed &= Verify.verifyEquals(goal.getEndTime(), endDay - diff,
				"End time of goal is not correct when user changes timezone") == null;
		testPassed &= Verify.verifyEquals(goal.getStartTime(), startDay,
				"Start time of goal is not correct") == null;
		List<TimelineItem> actualItems = MVPApi.getTimelineItems(
				userInfo.getToken(), startDay, endDay - diff, null,
				TimelineItemDataBase.TYPE_TIMEZONE);
		testPassed &= Verify.verifyTrue(actualItems.size() == 1,
				"Timezone timeline item should be found") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getTimestamp(),
				startDay + 3600 * 11 + 60 * 15,
				"Timestamp of timeline item is not correct") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getItemType(),
				TimelineItemDataBase.TYPE_TIMEZONE,
				"Type of timeline item is not correct") == null;

	}

	 @Test(groups = { "ios", "Prometheus", "MVPBackend",
	 "NewServerCalculationGoalCreation", "NewServercalculation",
	 "GoalCreation", "TravelBackwardDifferentDays", "Timezone" })
	public void NewServerCalculation_GoalCreation_TimezoneChanging_TravelBackward_DifferentDays()

	throws IOException, JSONException {
		UserInfo userInfo = MVPApi.signUp();

		int[] timezoneOffsetInSeconds = { 32400, 25200 };
		int diff = timezoneOffsetInSeconds[0] - timezoneOffsetInSeconds[1];
		long startDay = MVPCommon.getDayStartEpoch(
				System.currentTimeMillis() / 1000, TimeZone.getTimeZone("UTC"))
				+ diff;
		long endDay = MVPCommon.getDayEndEpoch(
				System.currentTimeMillis() / 1000, TimeZone.getTimeZone("UTC"))
				+ diff;
		// create profile / pedometer / statistics
		Pedometer pedometer = setUpNewAccount(userInfo.getToken(), startDay);

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
		testPassed &= Verify.verifyEquals(goal.getEndTime(), endDay - diff,
				"End time of goal is not correct when user changes timezone") == null;
		testPassed &= Verify.verifyEquals(goal.getStartTime(), startDay,
				"Start time of goal is not correct") == null;
		List<TimelineItem> actualItems = MVPApi.getTimelineItems(
				userInfo.getToken(), startDay, endDay - diff, null,
				TimelineItemDataBase.TYPE_TIMEZONE);
		testPassed &= Verify.verifyTrue(actualItems.size() == 1,
				"Timezone timeline item should be found") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getTimestamp(),
				startDay + 3600, "Timestamp of timeline item is not correct") == null;
		testPassed &= Verify.verifyEquals(actualItems.get(0).getItemType(),
				TimelineItemDataBase.TYPE_TIMEZONE,
				"Type of timeline item is not correct") == null;

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
		// changes.add(tripleTapTypeChange);
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

}
