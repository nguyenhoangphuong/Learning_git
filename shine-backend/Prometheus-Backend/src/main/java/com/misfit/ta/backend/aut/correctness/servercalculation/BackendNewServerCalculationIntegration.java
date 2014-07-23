package com.misfit.ta.backend.aut.correctness.servercalculation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.internalapi.UserInfo;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.aut.performance.newservercalculation.NewServerCalculationScenario;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.sync.sdk.SDKSyncEvent;
import com.misfit.ta.backend.data.sync.sdk.SDKSyncLog;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.SleepSessionItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class BackendNewServerCalculationIntegration extends BackendServerCalculationBase {

	// fields
	protected int delayTime = 60000;
	protected int DURATION_DELTA = 10;
	protected long TIMESTAMP_DELTA = 600; 
	protected long QUALITY_DELTA = 3;
	protected int GRAPH_ITEMS_DELTA = 2;


	// test cases
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "NewServercalculation", "NewServercalculationSmoke", "Integration" })
	public void NewServerCalculation_Integration() throws FileNotFoundException {

		// sign up new account
		boolean testPassed = true;
		UserInfo userInfo = MVPApi.signUp();
		long timestamp = System.currentTimeMillis() / 1000;
		Long startDay = MVPCommon.getDayStartEpoch(timestamp);
		Long endDay = MVPCommon.getDayEndEpoch(timestamp);


		// create profile / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(userInfo.getToken(), profile);
		MVPApi.createPedometer(userInfo.getToken(), pedometer);
		MVPApi.createStatistics(userInfo.getToken(), statistics);

		Integer timezoneOffset = 25200;
		setDefaultTrackingChanges(startDay - 3 * 3600 * 24, userInfo);
		// create goals for 4 days
		int daysNumber = 4;
		Long[] startDayTimestamps = new Long[daysNumber];
		Long[] endDayTimestamps = new Long[daysNumber];
		for (int i = 0; i < daysNumber; i++) {
			startDayTimestamps[i] = startDay - i * 3600 * 24;
			endDayTimestamps[i]= endDay - i * 3600 * 24;
		}

		// story on 4th day (3 days ago):
		// - session: 60 minutes - 6000 steps - 600 points at 7:00
		// - session: 50 minutes - 5000 steps - 500 points at 10:00
		// - session: 30 minutes - 3000 steps - 300 points at 13:00
		// - session: 40 minutes - 4000 steps - 400 points at 17:00
		// expect:
		// - 4 acitivity session tiles
		// - 100% tile
		// - 150% tile
		GoalRawData data3 = new GoalRawData();
		data3.appendGoalRawData(generateEmptyRawData(0, 7 * 60));

		data3.appendGoalRawData(generateSessionRawData(6000, 600, 60));
		data3.appendGoalRawData(generateEmptyRawData(7 * 60 + 60, 10 * 60));

		data3.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data3.appendGoalRawData(generateEmptyRawData(10 * 60 + 50, 13 * 60));

		data3.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		data3.appendGoalRawData(generateEmptyRawData(13 * 60 + 30, 17 * 60));

		data3.appendGoalRawData(generateSessionRawData(4000, 400, 40));
		data3.appendGoalRawData(generateEmptyRawData(17 * 60 + 40, 24 * 60));

		// story on 3th day (2 days ago):
		// - session: 60 minutes - 6000 steps - 600 points at 7:00
		// - session: 50 minutes - 5000 steps - 500 points at 10:00
		// - session: 30 minutes - 3000 steps - 300 points at 13:00
		// - session: 40 minutes - 4000 steps - 400 points at 17:00
		// - session: 40 minutes - 4000 steps - 400 points at 20:00
		// expect:
		// - 5 acitivity session tiles
		// - 100% tile
		// - 150% tile
		// - 200% tile
		// - personal best tile
		// - statistics updated
		GoalRawData data2 = new GoalRawData();
		data2.appendGoalRawData(generateEmptyRawData(0, 7 * 60));

		data2.appendGoalRawData(generateSessionRawData(6000, 600, 60));
		data2.appendGoalRawData(generateEmptyRawData(7 * 60 + 60, 10 * 60));

		data2.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data2.appendGoalRawData(generateEmptyRawData(10 * 60 + 50, 13 * 60));

		data2.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		data2.appendGoalRawData(generateEmptyRawData(13 * 60 + 30, 17 * 60));

		data2.appendGoalRawData(generateSessionRawData(4000, 400, 40));
		data2.appendGoalRawData(generateEmptyRawData(17 * 60 + 40, 20 * 60));

		data2.appendGoalRawData(generateSessionRawData(4000, 400, 40));
		data2.appendGoalRawData(generateEmptyRawData(20 * 60 + 40, 24 * 60));

		// story on 2nd day (yesterday):
		// - session: 60 minutes - 6000 steps - 600 points at 7:00
		// - session: 50 minutes - 5000 steps - 500 points at 10:00
		// - session: 30 minutes - 3000 steps - 300 points at 13:00
		// - session: 40 minutes - 4000 steps - 400 points at 17:00
		// - session: 60 minutes - 6000 steps - 600 points at 21:00
		// - session: 40 minutes - 4000 steps - 400 points at 23:00
		// expect:
		// - 6 acitivity session tiles
		// - 100% tile
		// - 150% tile
		// - 200% tile
		// - streak tile
		// - personal best tile
		// - statistics updated
		GoalRawData data1 = new GoalRawData();
		data1.appendGoalRawData(generateEmptyRawData(0, 7 * 60));

		data1.appendGoalRawData(generateSessionRawData(6000, 600, 60));
		data1.appendGoalRawData(generateEmptyRawData(7 * 60 + 60, 10 * 60));

		data1.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data1.appendGoalRawData(generateEmptyRawData(10 * 60 + 50, 13 * 60));

		data1.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		data1.appendGoalRawData(generateEmptyRawData(13 * 60 + 30, 17 * 60));

		data1.appendGoalRawData(generateSessionRawData(4000, 400, 40));
		data1.appendGoalRawData(generateEmptyRawData(17 * 60 + 40, 21 * 60));

		data1.appendGoalRawData(generateSessionRawData(6000, 600, 60));
		data1.appendGoalRawData(generateEmptyRawData(21 * 60 + 60, 23 * 60));

		data1.appendGoalRawData(generateSessionRawData(4000, 400, 40));
		data1.appendGoalRawData(generateEmptyRawData(23 * 60 + 40, 24 * 60));


		// story on today:
		// - session: 50 minutes - 5000 steps - 500 points at 8:00
		// - session: 50 minutes - 5000 steps - 500 points at 9:00
		// - session: 50 minutes - 5000 steps - 500 points at 10:00
		// - session: 50 minutes - 5000 steps - 500 points at 11:00
		// - session: 50 minutes - 5000 steps - 500 points at 12:00
		// - session: 50 minutes - 5000 steps - 500 points at 13:00
		// expect:
		// - 26 graph items
		// - 6 acitivity session tiles
		// - 100% tile
		// - 150% tile
		// - 200% tile
		// - streak tile
		// - personal best
		// - statistics updated
		GoalRawData data0 = new GoalRawData();
		data0.appendGoalRawData(generateEmptyRawData(0, 8 * 60));

		data0.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data0.appendGoalRawData(generateEmptyRawData(8 * 60 + 50, 9 * 60));

		data0.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data0.appendGoalRawData(generateEmptyRawData(9 * 60 + 50, 10 * 60));

		data0.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data0.appendGoalRawData(generateEmptyRawData(10 * 60 + 50, 11 * 60));

		data0.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data0.appendGoalRawData(generateEmptyRawData(11 * 60 + 50, 12 * 60));

		data0.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data0.appendGoalRawData(generateEmptyRawData(12 * 60 + 50, 13 * 60));

		data0.appendGoalRawData(generateSessionRawData(5000, 500, 50));


		// push data to server
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[3], timezoneOffset / 60, "0101", "18", data3).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[2], timezoneOffset / 60, "0102", "18", data2).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[1], timezoneOffset / 60, "0103", "18", data1).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[0], timezoneOffset / 60, "0104", "18", data0).rawData);
		pushSyncData(timestamp, userInfo.getUserId(), pedometer.getSerialNumberString(), dataStrings);

		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);

		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);
		Goal[] goals = goalResult.goals;
		
		// get data from server
		List<GraphItem> graphitems3 = MVPApi.getGraphItems(userInfo.getToken(), startDayTimestamps[3], endDayTimestamps[3], 0l);
		List<GraphItem> graphitems2 = MVPApi.getGraphItems(userInfo.getToken(), startDayTimestamps[2], endDayTimestamps[2], 0l);
		List<GraphItem> graphitems1 = MVPApi.getGraphItems(userInfo.getToken(), startDayTimestamps[1], endDayTimestamps[1], 0l);
		List<GraphItem> graphitems0 = MVPApi.getGraphItems(userInfo.getToken(), startDayTimestamps[0], endDayTimestamps[0], 0l);

		List<TimelineItem> timelineitems3 = MVPApi.getTimelineItems(userInfo.getToken(), startDayTimestamps[3], endDayTimestamps[3], 0l);
		List<TimelineItem> timelineitems2 = MVPApi.getTimelineItems(userInfo.getToken(), startDayTimestamps[2], endDayTimestamps[2], 0l);
		List<TimelineItem> timelineitems1 = MVPApi.getTimelineItems(userInfo.getToken(), startDayTimestamps[1], endDayTimestamps[1], 0l);
		List<TimelineItem> timelineitems0 = MVPApi.getTimelineItems(userInfo.getToken(), startDayTimestamps[0], endDayTimestamps[0], 0l);

		

		statistics = MVPApi.getStatistics(userInfo.getToken());


		// ===== VERIFY GRAPH ITEMS
		testPassed &= Verify.verifyNearlyEquals(graphitems3.size(), 44, GRAPH_ITEMS_DELTA, "Number of graph items") == null;
		testPassed &= Verify.verifyNearlyEquals(graphitems2.size(), 44, GRAPH_ITEMS_DELTA, "Number of graph items") == null;
		testPassed &= Verify.verifyNearlyEquals(graphitems1.size(), 44, GRAPH_ITEMS_DELTA, "Number of graph items") == null;
		testPassed &= Verify.verifyNearlyEquals(graphitems0.size(), 26, GRAPH_ITEMS_DELTA, "Number of graph items") == null;


		// ===== VERIFY TIMELINE ITEMS
		// number of session tiles are correct
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems3), 4, "Goals[3] has 4 session tiles") == null;
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems2), 5, "Goals[2] has 5 session tiles") == null;
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems1), 6, "Goals[1] has 6 session tiles") == null;
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems0), 6, "Goals[0] has 6 session tiles") == null;


		// session tiles are correct
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems3, goals[3], 7 * 60, 60, 600), "Goal[3] has session tile at 7:00 - 600 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems3, goals[3], 10 * 60, 50, 500), "Goal[3] has session tile at 10:00 - 500 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems3, goals[3], 13 * 60, 30, 300), "Goal[3] has session tile at 13:00 - 300 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems3, goals[3], 17 * 60, 40, 400), "Goal[3] has session tile at 17:00 - 400 pts") == null;

		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 7 * 60, 60, 600), "Goal[2] has session tile at 7:00 - 600 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 10 * 60, 50, 500), "Goal[2] has session tile at 10:00 - 500 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 13 * 60, 30, 300), "Goal[2] has session tile at 13:00 - 300 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 17 * 60, 40, 400), "Goal[2] has session tile at 17:00 - 400 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 20 * 60, 40, 400), "Goal[2] has session tile at 20:00 - 400 pts") == null;

		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 7 * 60, 60, 600), "Goal[1] has session tile at 7:00 - 600 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 10 * 60, 50, 500), "Goal[1] has session tile at 10:00 - 500 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 13 * 60, 30, 300), "Goal[1] has session tile at 13:00 - 300 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 17 * 60, 40, 400), "Goal[1] has session tile at 17:00 - 400 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 21 * 60, 60, 600), "Goal[1] has session tile at 21:00 - 600 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 23 * 60, 40, 400), "Goal[1] has session tile at 23:00 - 400 pts") == null;

		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 8 * 60, 50, 500), "Goal[0] has session tile at 8:00 - 500 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 9 * 60, 50, 500), "Goal[0] has session tile at 9:00 - 500 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 10 * 60, 50, 500), "Goal[0] has session tile at 10:00 - 500 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 11 * 60, 50, 500), "Goal[0] has session tile at 11:00 - 500 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 12 * 60, 50, 500), "Goal[0] has session tile at 12:00 - 500 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 13 * 60, 50, 500), "Goal[0] has session tile at 13:00 - 500 pts") == null;


		// daily goal milestone tiles are correct
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems3, goals[3], 10 * 60 + 40, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[3] has 100% tile") == null;
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems3, goals[3], 17 * 60 + 10, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[3] has 150% tile") == null;

		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems2, goals[2], 10 * 60 + 40, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[2] has 100% tile") == null;
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems2, goals[2], 17 * 60 + 10, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[2] has 150% tile") == null;
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems2, goals[2], 20 * 60 + 20, TimelineItemDataBase.EVENT_TYPE_200_GOAL, 2000), "Goals[2] has 200% tile") == null;

		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems1, goals[1], 10 * 60 + 40, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[1] has 100% tile") == null;
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems1, goals[1], 17 * 60 + 10, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[1] has 150% tile") == null;
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems1, goals[1], 21 * 60 + 20, TimelineItemDataBase.EVENT_TYPE_200_GOAL, 2000), "Goals[1] has 200% tile") == null;

		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems0, goals[0], 9 * 60 + 50, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[0] has 100% tile") == null;
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems0, goals[0], 10 * 60 + 50, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[0] has 150% tile") == null;
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems0, goals[0], 11 * 60 + 50, TimelineItemDataBase.EVENT_TYPE_200_GOAL, 2000), "Goals[0] has 200% tile") == null;


		// personal best and streak tiles are correct
		testPassed &= Verify.verifyTrue(hasPersonalBestMilestone(timelineitems2, goals[2], 24 * 60 - 1, 2200, 400), "Goals[2] reaches 2200pts extended Goal[3] by 400pts") == null;
		testPassed &= Verify.verifyTrue(hasPersonalBestMilestone(timelineitems1, goals[1], 24 * 60 - 1, 2800, 600), "Goals[1] reaches 2800pts extended Goal[2] by 600pts") == null;
		testPassed &= Verify.verifyTrue(hasPersonalBestMilestone(timelineitems0, goals[0], 13 * 60 + 50, 3000, 200), "Goals[0] reaches 3000pts extended Goal[1] by 200pts") == null;

		testPassed &= Verify.verifyTrue(hasStreakMilestone(timelineitems1, goals[1], 10 * 60 + 40, 3), "Goals[1] has 3-day streak tile") == null;
		testPassed &= Verify.verifyTrue(hasStreakMilestone(timelineitems0, goals[0], 9 * 60 + 50, 4), "Goals[0] has 4-day streak tile") == null;


		// ===== VERIFY GOAL PROGRESS
		double miles3 = MVPCalculator.calculateMiles(6000, 60, profile.getHeight()) + 
				MVPCalculator.calculateMiles(5000, 50, profile.getHeight()) +
				MVPCalculator.calculateMiles(4000, 40, profile.getHeight()) +
				MVPCalculator.calculateMiles(3000, 30, profile.getHeight());
		double miles2 = miles3 + MVPCalculator.calculateMiles(4000, 40, profile.getHeight());
		double miles1 = miles3 + MVPCalculator.calculateMiles(6000, 60, profile.getHeight()) 
				+ MVPCalculator.calculateMiles(4000, 40, profile.getHeight());
		double miles0 = MVPCalculator.calculateMiles(5000, 50, profile.getHeight()) * 6;

		double weight = profile.getWeight();
		double height = profile.getHeight();
		double fullBMR = MVPCalculator.calculateFullBMR((float)weight, (float)height, 
				profile.calculateAge() + 1, profile.getGender().equals(0));

		double calorie3 = MVPCalculator.calculateCalories(1800, (float)weight, (float)fullBMR, 24 * 60);
		double calorie2 = MVPCalculator.calculateCalories(2200, (float)weight, (float)fullBMR, 24 * 60);
		double calorie1 = MVPCalculator.calculateCalories(2800, (float)weight, (float)fullBMR, 24 * 60);
		double calorie0 = MVPCalculator.calculateCalories(3000, (float)weight, (float)fullBMR, 25 * 60);


		testPassed &= Verify.verifyEquals(goals[3].getProgressData().getPoints(), 1800 * 2.5, "Goals[3] progress point") == null;
		testPassed &= Verify.verifyEquals(goals[2].getProgressData().getPoints(), 2200 * 2.5, "Goals[2] progress point") == null;
		testPassed &= Verify.verifyEquals(goals[1].getProgressData().getPoints(), 2800 * 2.5, "Goals[1] progress point") == null;
		testPassed &= Verify.verifyEquals(goals[0].getProgressData().getPoints(), 3000 * 2.5, "Goals[0] progress point") == null;

		testPassed &= Verify.verifyEquals(goals[3].getProgressData().getSteps(), 18000, "Goals[3] progress steps") == null;
		testPassed &= Verify.verifyEquals(goals[2].getProgressData().getSteps(), 22000, "Goals[2] progress steps") == null;
		testPassed &= Verify.verifyEquals(goals[1].getProgressData().getSteps(), 28000, "Goals[1] progress steps") == null;
		testPassed &= Verify.verifyEquals(goals[0].getProgressData().getSteps(), 30000, "Goals[1] progress steps") == null;

		testPassed &= Verify.verifyNearlyEquals(goals[3].getProgressData().getDistanceMiles(), miles3, distanceDelta, "Goals[3] progress distance in miles") == null;
		testPassed &= Verify.verifyNearlyEquals(goals[2].getProgressData().getDistanceMiles(), miles2, distanceDelta, "Goals[2] progress distance in miles") == null;
		testPassed &= Verify.verifyNearlyEquals(goals[1].getProgressData().getDistanceMiles(), miles1, distanceDelta, "Goals[1] progress distance in miles") == null;
		testPassed &= Verify.verifyNearlyEquals(goals[0].getProgressData().getDistanceMiles(), miles0, distanceDelta, "Goals[0] progress distance in miles") == null;

		testPassed &= Verify.verifyNearlyEquals(goals[3].getProgressData().getFullBmrCalorie(), fullBMR, fullBmrDelta, "Goals[3] full bmr") == null;
		testPassed &= Verify.verifyNearlyEquals(goals[2].getProgressData().getFullBmrCalorie(), fullBMR, fullBmrDelta, "Goals[2] full bmr") == null;
		testPassed &= Verify.verifyNearlyEquals(goals[1].getProgressData().getFullBmrCalorie(), fullBMR, fullBmrDelta, "Goals[1] full bmr") == null;
		testPassed &= Verify.verifyNearlyEquals(goals[0].getProgressData().getFullBmrCalorie(), fullBMR, fullBmrDelta, "Goals[0] full bmr") == null;

		testPassed &= Verify.verifyNearlyEquals(goals[3].getProgressData().getCalorie(), calorie3, totalCalorieDelta, "Goals[3] total calorie") == null;
		testPassed &= Verify.verifyNearlyEquals(goals[2].getProgressData().getCalorie(), calorie2, totalCalorieDelta, "Goals[2] total calorie") == null;
		testPassed &= Verify.verifyNearlyEquals(goals[1].getProgressData().getCalorie(), calorie1, totalCalorieDelta, "Goals[1] total calorie") == null;
		testPassed &= Verify.verifyNearlyEquals(goals[0].getProgressData().getCalorie(), calorie0, totalCalorieDelta, "Goals[0] total calorie") == null;

		// ===== VERIFY STATISTICS
		long personalBestTimestamp = goals[0].getStartTime() + (13 * 60 + 50) * 60;
		testPassed &= Verify.verifyNearlyEquals(statistics.getLifetimeDistance(), miles0 + miles1 + miles2 + miles3, distanceDelta, "Lifetime distance") == null;
		testPassed &= Verify.verifyEquals(statistics.getBestStreak(), 4, "Best streak number") == null;
		testPassed &= Verify.verifyEquals(statistics.getTotalGoalHit(), 4, "Total goal hit number") == null;
		testPassed &= Verify.verifyEquals(statistics.getPersonalRecords().getPersonalBestRecordsInPoint().getPoint(), 3000 * 2.5, "Personal best points") == null;
		testPassed &= Verify.verifyEquals(statistics.getPersonalRecords().getPersonalBestRecordsInPoint().getTimestamp(), personalBestTimestamp, "Personal best timestamp") == null;

		Assert.assertTrue(testPassed, "All asserts are passed");

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "NewServercalculation" })
	public void NewServerCalculation_MarathonTile() {

		// sign up new account
		boolean testPassed = true;
		UserInfo userInfo = MVPApi.signUp();
		long timestamp = System.currentTimeMillis() / 1000;
		Long startDay = MVPCommon.getDayStartEpoch(timestamp);
		Long endDay = MVPCommon.getDayEndEpoch(timestamp);


		// create profile (height = 64") / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();
		profile.getDisplayedUnits().setDistanceUnit(1);
		profile.setHeight(64d);

		MVPApi.createProfile(userInfo.getToken(), profile);
		MVPApi.createPedometer(userInfo.getToken(), pedometer);
		MVPApi.createStatistics(userInfo.getToken(), statistics);

		setDefaultTrackingChanges(startDay - 6 * 24 * 3600, userInfo);


		// story:
		// 1st day:
		// - Reach 49 miles
		// 2nd day:
		// - Reach 49.9 miles in SI unit (hit 2 marathon SI)
		// - Reach 52 miles in US unit (hit 2 marathon US)
		// 3rd day:
		// - Reach 149 miles
		// 4th day:
		// - Reach 150 miles in US unit (hit 6 marathon US)
		// - Reach 156 miles in SI unit (hit 6 marathon SI)
		// - Reach 200 miles
		// 5th day:
		// - Reach 295 miles
		// 6th day:
		// - Reach 300 miles in US unit (hit 12 marathon US)
		// - Reach 311 miles in SI unit (hit 12 marathon SI)
		// expect:
		// - Only 3 marathons tile are created: 2 mrt SI, 6 mrt US, 12 mrt US
		// - Tiles are displayed in correct order

		// 1st day:
		// 63360 steps - 360 minute (48.38 miles)
		GoalRawData data5 = new GoalRawData();
		data5.appendGoalRawData(generateSessionRawData(63360, 3600, 360));
		data5.appendGoalRawData(generateEmptyRawData(0 * 60 + 360, 24 * 60));

		// 2nd day:
		// SI: 2960 steps - 20 mins (1.55 miles - 49.93 total)
		// US: 2560 steps - 20 mins (1.1 miles - 51.03 total)
		GoalRawData data4a = new GoalRawData();
		data4a.appendGoalRawData(generateSessionRawData(2960, 200, 20));
		data4a.appendGoalRawData(generateEmptyRawData(0 * 60 + 20, 1 * 60));

		GoalRawData data4b = new GoalRawData();
		data4b.appendGoalRawData(generateSessionRawData(2560, 200, 20));
		data4b.appendGoalRawData(generateEmptyRawData(1 * 60 + 20, 24 * 60));

		// 3rd day:
		// 126720 steps - 720 minutes (96.77 miles - 147.80 total)
		GoalRawData data3 = new GoalRawData();
		data3.appendGoalRawData(generateSessionRawData(126720, 7200, 720));
		data3.appendGoalRawData(generateEmptyRawData(0 * 60 + 720, 24 * 60));

		// 4th day
		// US: 5100 steps - 30 mins (3.63 miles - 151.43 total)
		// SI: 12780 steps - 90 mins (6.03 miles - 157.46 total)
		// 61200 steps - 360 mins (43.58 miles - 201.04 total)
		GoalRawData data2a = new GoalRawData();
		data2a.appendGoalRawData(generateSessionRawData(5100, 300, 30));
		data2a.appendGoalRawData(generateEmptyRawData(0 * 60 + 30, 1 * 60));

		GoalRawData data2b = new GoalRawData();
		data2b.appendGoalRawData(generateSessionRawData(12780, 900, 90));
		data2b.appendGoalRawData(generateEmptyRawData(1 * 60 + 90, 6 * 60));

		data2b.appendGoalRawData(generateSessionRawData(61200, 3600, 360));
		data2b.appendGoalRawData(generateEmptyRawData(6 * 60 + 360, 24 * 60));

		// 5th day
		// 126720 steps - 720 mins (96.77 miles - 297.81 total)
		GoalRawData data1 = new GoalRawData();
		data1.appendGoalRawData(generateSessionRawData(126720, 7200, 720));
		data1.appendGoalRawData(generateEmptyRawData(0 * 60 + 720, 24 * 60));

		// 6th day
		// US: 21300 steps - 150 mins (10.05 miles - 307.86 total)
		// SI: 21300 steps - 150 mins (10.05 miles - 317.91 total)
		GoalRawData data0a = new GoalRawData();
		data0a.appendGoalRawData(generateSessionRawData(21300, 1500, 150));
		data0a.appendGoalRawData(generateEmptyRawData(0 * 60 + 150, 6 * 60));

		GoalRawData data0b = new GoalRawData();
		data0b.appendGoalRawData(generateSessionRawData(21300, 1500, 150));
		data0b.appendGoalRawData(generateEmptyRawData(6 * 60 + 150, 24 * 60));


		// create timestamp for 6 days
		int daysNumber = 6;
		Long[] startDayTimestamps = new Long[daysNumber];
		Long[] endDayTimestamps = new Long[daysNumber];
		for (int i = 0; i < daysNumber; i++) {
			startDayTimestamps[i] = startDay - i * 3600 * 24;
			endDayTimestamps[i] = endDay - i * 3600 * 24;
		}
		int timezoneOffset = 25200;
		// push to server
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[5], timezoneOffset / 60, "0101", "18", data5).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[4], timezoneOffset / 60, "0102", "18", data4a).rawData);		
		pushSyncData(timestamp + delayTime * 1, userInfo.getUserId(), pedometer.getSerialNumberString(), dataStrings);
		ShortcutsTyper.delayTime(delayTime);
		
		changeDistanceUnit(userInfo.getToken(), 0);
		
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[4] + 3600, timezoneOffset / 60, "0101", "18", data4b).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[3],timezoneOffset / 60, "0102", "18", data3).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[2], timezoneOffset / 60, "0103", "18", data2a).rawData);
		pushSyncData(timestamp + delayTime * 2, userInfo.getUserId(), pedometer.getSerialNumberString(), dataStrings);
		ShortcutsTyper.delayTime(delayTime);
		
		changeDistanceUnit(userInfo.getToken(), 1);
		
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[2] + 3600, timezoneOffset / 60, "0101", "18", data2b).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[1], timezoneOffset / 60, "0102", "18", data1).rawData);
		pushSyncData(timestamp + delayTime * 3, userInfo.getUserId(), pedometer.getSerialNumberString(), dataStrings);
		ShortcutsTyper.delayTime(delayTime);
		
		changeDistanceUnit(userInfo.getToken(), 0);
		
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[0], timezoneOffset / 60, "0101", "18", data0a).rawData);
		pushSyncData(timestamp + delayTime * 4, userInfo.getUserId(), pedometer.getSerialNumberString(), dataStrings);
		ShortcutsTyper.delayTime(delayTime);
		
		changeDistanceUnit(userInfo.getToken(), 1);
		
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDayTimestamps[0] + 6 * 3600, timezoneOffset / 60, "0101", "18", data0b).rawData);
		pushSyncData(timestamp + delayTime * 5, userInfo.getUserId(), pedometer.getSerialNumberString(), dataStrings);
	
		ShortcutsTyper.delayTime(delayTime);
		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);
		Goal[] goals = goalResult.goals;

		// get server data
		List<TimelineItem> timelineitems4 = MVPApi.getTimelineItems(userInfo.getToken(), goals[4].getStartTime(), goals[4].getEndTime(), 0l);
		List<TimelineItem> timelineitems2 = MVPApi.getTimelineItems(userInfo.getToken(), goals[2].getStartTime(), goals[2].getEndTime(), 0l);
		List<TimelineItem> timelineitems0 = MVPApi.getTimelineItems(userInfo.getToken(), goals[0].getStartTime(), goals[0].getEndTime(), 0l);
		statistics = MVPApi.getStatistics(userInfo.getToken());


		// === VERIFY DISTANCE TILES
		int numberOfDistanceTile = getNumberOfTile(timelineitems4, TimelineItemDataBase.TYPE_LIFETIME_DISTANCE);
		numberOfDistanceTile += getNumberOfTile(timelineitems2, TimelineItemDataBase.TYPE_LIFETIME_DISTANCE);
		numberOfDistanceTile += getNumberOfTile(timelineitems0, TimelineItemDataBase.TYPE_LIFETIME_DISTANCE);
		
		testPassed &= Verify.verifyEquals(numberOfDistanceTile, 3, "Number of lifetime distance tiles") == null;
		testPassed &= Verify.verifyTrue(hasLifeTimeDistanceTile(timelineitems4, goals[4], 0 * 60 + 18, 2, 1), "2 marathons tile in SI unit") == null;
		testPassed &= Verify.verifyTrue(hasLifeTimeDistanceTile(timelineitems2, goals[2], 0 * 60 + 19, 6, 0), "6 marathons tile in US unit") == null;
		testPassed &= Verify.verifyTrue(hasLifeTimeDistanceTile(timelineitems0, goals[0], 0 * 60 + 33, 12, 0), "12 marathons tile in US unit") == null;


		// === VERIFY STATISTICS
		testPassed &= Verify.verifyNearlyEquals(statistics.getLifetimeDistance(), 317.91d	, 0.01, "Lifetime distance") == null;

		Assert.assertTrue(testPassed, "All asserts are passed");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "NewServercalculation" })
	public void NewServerCalculation_Progress() {

		// sign up new account
		boolean testPassed = true;
		UserInfo userInfo = MVPApi.signUp();
		long timestamp = System.currentTimeMillis() / 1000;
		Long startDay = MVPCommon.getDayStartEpoch(timestamp);
		Long endDay = MVPCommon.getDayEndEpoch(timestamp);


		// create profile / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(userInfo.getToken(), profile);
		MVPApi.createPedometer(userInfo.getToken(), pedometer);
		MVPApi.createStatistics(userInfo.getToken(), statistics);


		setDefaultTrackingChanges(startDay, userInfo);


		// story
		// - session: 60 minutes - 6000 steps - 600 points at 7:00
		// - inactive: 4 minutes - 400 steps - 40 points at 9:00
		// - inactive: 4 minutes - 400 steps - 40 points at 9:10
		// - session: 50 minutes - 5000 steps - 500 points at 10:00
		// - session: 30 minutes - 3000 steps - 300 points at 13:00
		// - gap: 2 active + 4 idle / 60 minutes - 2000 steps - 200 points from 15:00 to 16:00
		// - session: 40 minutes - 4000 steps - 400 points at 17:00
		// expect:
		// - 4 acitivity session tiles
		// - 100% tile
		// - 150% tile
		// - 200% tile
		// - progress is correct (points, steps, distance) 
		GoalRawData data = new GoalRawData();
		data.appendGoalRawData(generateEmptyRawData(0, 7 * 60));

		data.appendGoalRawData(generateSessionRawData(6000, 600, 60));
		data.appendGoalRawData(generateEmptyRawData(7 * 60 + 60, 9 * 60));

		data.appendGoalRawData(generateSessionRawData(400, 40, 4));
		data.appendGoalRawData(generateEmptyRawData(9 * 60 + 4, 9 * 60 + 10));
		data.appendGoalRawData(generateSessionRawData(400, 40, 4));
		data.appendGoalRawData(generateEmptyRawData(9 * 60 + 10 + 4, 10 * 60));

		data.appendGoalRawData(generateSessionRawData(5000, 500, 50));
		data.appendGoalRawData(generateEmptyRawData(10 * 60 + 50, 13 * 60));

		data.appendGoalRawData(generateSessionRawData(3000, 300, 30));
		data.appendGoalRawData(generateEmptyRawData(13 * 60 + 30, 15 * 60));

		data.appendGoalRawData(generateGapData(100, 10, 2, 4, 60));
		data.appendGoalRawData(generateEmptyRawData(15 * 60 + 60, 17 * 60));

		data.appendGoalRawData(generateSessionRawData(4000, 400, 40));
		data.appendGoalRawData(generateEmptyRawData(17 * 60 + 40, 24 * 60));

		
		// push to server
		int timezoneOffset = 25200;
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay, timezoneOffset / 60, "0101", "18", data).rawData);
		pushSyncData(timestamp, userInfo.getUserId(), pedometer.getSerialNumberString(), dataStrings);
		
		ShortcutsTyper.delayTime(delayTime);
		
		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);
		Goal goal = goalResult.goals[0];
		// get server data
		List<TimelineItem> timelineitems = MVPApi.getTimelineItems(userInfo.getToken(), goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(userInfo.getToken(), goal.getServerId()).goals[0];


		// VERIFY TIMELINE ITEMS
		// number of session tiles are correct
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems), 4, "Goal has 4 session tiles") == null;

		// session tiles are correct
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 7 * 60, 60, 600), "Goal has session tile at 7:00 - 600 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 10 * 60, 50, 500), "Goal has session tile at 10:00 - 500 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 13 * 60, 30, 300), "Goal has session tile at 13:00 - 300 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 17 * 60, 40, 400), "Goal has session tile at 17:00 - 400 pts") == null;

		// daily goal milestone tiles are correct
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems, goal, 10 * 60 + 32, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goal has 100% tile") == null;
		/*
		 * This is a corner case.
		 * Server calculation doesn't have the minute data so that it uses the average value to accumulate the daily goal milestone
		 * from start day to 13:30: 1480 pts
		 * 13:30 - 15:00: empty data
		 * 15:00 - 16:00: 100 pts
		 * 150 min and 100 pts ==> at 13:51 we have total 1500 pts ==> hit milestone
		 */
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems, goal, 13 * 60 + 51, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goal has 150% tile") == null;
		
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems, goal, 17 * 60 + 32, TimelineItemDataBase.EVENT_TYPE_200_GOAL, 2000), "Goal has 200% tile") == null;


		// ===== VERIFY GOAL PROGRESS
		double miles = MVPCalculator.calculateMiles(data.getSteps(), profile.getHeight());
		testPassed &= Verify.verifyEquals(goal.getProgressData().getPoints(), 2080 * 2.5, "Goal progress point") == null;
		testPassed &= Verify.verifyEquals(goal.getProgressData().getSteps(), 20800, "Goal progress steps") == null;
		testPassed &= Verify.verifyNearlyEquals(goal.getProgressData().getDistanceMiles(), miles, 0.001, "Goal progress distance in miles") == null;
		
		
		Assert.assertTrue(testPassed, "All asserts are passed");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "NewServercalculation", "PushDuplicatedData"})
	public void NewServerCalculation_PushDuplicatedData() {
		
		// sign up new account
		boolean testPassed = true;
		UserInfo userInfo = MVPApi.signUp();
		long timestamp = System.currentTimeMillis() / 1000;
		Long startDay = MVPCommon.getDayStartEpoch(timestamp);
		Long endDay = MVPCommon.getDayEndEpoch(timestamp);

		// create profile / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(userInfo.getToken(), profile);
		MVPApi.createPedometer(userInfo.getToken(), pedometer);
		MVPApi.createStatistics(userInfo.getToken(), statistics);

		Integer timezoneOffset = 25200;
		setDefaultTrackingChanges(startDay, userInfo);

		
		// story:
		// 1st shine:
		// - 4000 steps - 40 mins (400pts) at 0:00am
		// - 2000 steps - 20 mins (200pts) at 2:00am
		// - 4000 steps - 40 mins (400pts) at 3:00am
		
		// expect:
		// - 2 session tile 
		// - 1000 pogress pts
		GoalRawData data1 = new GoalRawData();
		data1.appendGoalRawData(generateSessionRawData(4000, 400, 40));
		data1.appendGoalRawData(generateEmptyRawData(0 * 60 + 40, 2 * 60));

		data1.appendGoalRawData(generateSessionRawData(2000, 200, 20));
		data1.appendGoalRawData(generateEmptyRawData(2 * 60 + 20, 3 * 60));

		data1.appendGoalRawData(generateSessionRawData(4000, 400, 40));
		data1.appendGoalRawData(generateEmptyRawData(3 * 60 + 40, 24 * 60));
		
		
		// push to server
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay, timezoneOffset / 60, "0101", "18", data1).rawData);
		pushSyncData(timestamp + delayTime * 1, userInfo.getUserId(), pedometer.getSerialNumberString(), dataStrings);
		
		
		// link to new shine 
		String serialNumber = TextTool.getRandomString(10, 10);
		BackendHelper.link(userInfo.getToken(), serialNumber);
		
		ShortcutsTyper.delayTime(delayTime);
		
		
		// push data to server
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(startDay, timezoneOffset / 60, "0101", "18", data1).rawData);
		pushSyncData(timestamp + delayTime * 2, userInfo.getUserId(), serialNumber, dataStrings);
		
		ShortcutsTyper.delayTime(delayTime);


		// get server data
		GoalsResult goalResult = MVPApi.searchGoal(userInfo.getToken(), 0l,
				(long) Integer.MAX_VALUE, 0l);
		Goal[] goals = goalResult.goals;
		Goal goal = goals[0];
		List<TimelineItem> timelineitems = MVPApi.getTimelineItems(userInfo.getToken(), startDay, endDay, 0l);
		
		
		// VERIFY TIMELINE ITEMS
		// number of session tiles are correct
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems), 3, "Goal has 3 session tiles") == null;

		// session tiles are correct
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 0 * 60, 40, 400), "Goal has session tile at 0:00 - 400 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 2 * 60, 20, 200), "Goal has session tile at 2:00 - 200 pts") == null;
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 3 * 60, 40, 400), "Goal has session tile at 3:00 - 400 pts") == null;
		

		// ===== VERIFY GOAL PROGRESS
		double miles = MVPCalculator.calculateMiles(data1.getSteps(), profile.getHeight());
		
		testPassed &= Verify.verifyEquals((int) Math.floor(goal.getProgressData().getPoints()), 2500, "Goal progress point") == null;
		testPassed &= Verify.verifyEquals(goal.getProgressData().getSteps(), 10000, "Goal progress steps") == null;
		testPassed &= Verify.verifyNearlyEquals(goal.getProgressData().getDistanceMiles(), miles, 0.001, "Goal progress distance in miles") == null;
		
		Assert.assertTrue(testPassed, "All asserts are passed");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "NewServerCalculationSleep", "NewServercalculation", "NSCSleep" })
	public void NewServerCalculation_RealSleepData() throws IOException, JSONException {

		try {
			Files.delete("rawdata");
			Files.getFile("rawdata");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String[] testPaths = new String[] {
			"rawdata/test0", 
			"rawdata/test1", 
//			"rawdata/test2", 
//			"rawdata/test3", 
//			"rawdata/test4"
		};
		
		for(String testFolderPath : testPaths) {
			
			String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
//			String token = MVPApi.signUp("sc077@a.a", "qqqqqq").token;
			String userId = MVPApi.getUserId(token);
			
			// parse test metadata file
			long currentTimestamp = System.currentTimeMillis() / 1000;
			String jsonString = FileUtils.readFileToString(new File(testFolderPath + "/" + 
					ServerCalculationTestHelpers.TestMetaDataFile));
			logger.info("Data: " + jsonString);
			JSONObject json = new JSONObject(jsonString);

			// create user and create goals in the test time range
			long startTime = json.getLong("start_time");
			long lastSyncTime = json.getLong("last_sync_time");
			int numberOfDays = (int) ((lastSyncTime - startTime) / (3600 * 24) + 2);
			logger.info("Number of days: " + numberOfDays);			
			
			Pedometer pedometer = DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null);
			MVPApi.createProfile(token, DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null));
			MVPApi.createPedometer(token, pedometer);
			MVPApi.createGoal(token, Goal.getDefaultGoal());
			
			// create goal in old day too
			for(int i = numberOfDays; i >= 0; i--)
				MVPApi.createGoal(token, Goal.getDefaultGoal(currentTimestamp - i * 3600 * 24));

			// push raw data to server
			File testFolder = new File(testFolderPath);
			long startTimestamp = MVPCommon.getDayStartEpoch();
			int numberOfDaysToAdd = (int) ((currentTimestamp - lastSyncTime) / (3600 * 24));

			for(File syncFolder : testFolder.listFiles()) {

				if(syncFolder.isFile())
					continue;

				SDKSyncLog syncLog = ServerCalculationTestHelpers.createSDKSyncLogFromFilesInFolder(startTimestamp, userId, pedometer.getSerialNumberString(), syncFolder.getAbsolutePath());
				
				for(SDKSyncEvent event : syncLog.getEvents()) {
					if(event.getEvent().equals(SDKSyncEvent.EVENT_GET_FILE_ACTIVITY)) {
						
						long timestamp = event.getResponseFinished().getValue().getTimestamp();
						
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(timestamp * 1000);
						cal.add(Calendar.DAY_OF_MONTH, numberOfDaysToAdd);
						
						event.getResponseFinished().getValue().setTimestamp(cal.getTimeInMillis() / 1000);
					}
				}
				startTimestamp += 600;

				MVPApi.pushSDKSyncLog(syncLog);
				ShortcutsTyper.delayTime(5000);
			}
			
			logger.info("Delay " + delayTime + " miliseconds");
			ShortcutsTyper.delayTime(delayTime);
			
			// get timeline items
			List<TimelineItem> actualItems = MVPApi.getTimelineItems(token, 
					currentTimestamp - 3600 * 24 * numberOfDays, 
					currentTimestamp + 3600 * 24, null, TimelineItemDataBase.TYPE_SLEEP);
			List<TimelineItem> expectedItems = TimelineItem.getTimelineItems(json.getJSONArray("sleeps"));
			
			logger.info("Actual sleeps: ");
			for(TimelineItem item : actualItems)
				if(item.getItemType() == 5)
					logger.info(item.toJson().toString());
			
			logger.info("");
			logger.info("Expected sleeps: ");
			for(TimelineItem item : expectedItems)
				if(item.getItemType() == 5)
					logger.info(item.toJson().toString());
			logger.info("");
			
			checkSleepTimelineItems(actualItems, expectedItems, numberOfDaysToAdd);
		}
	}
	

	private void checkSleepTimelineItems(List<TimelineItem> scItems, List<TimelineItem> expectedItems, int dayDifference) {
		
		logger.info("Test parameters: ");
		logger.info(String.format("TIMESTAMP_DELTA: %d, DURATION_DELTA: %d, QUALITY_DELTA: %d", 
				TIMESTAMP_DELTA, DURATION_DELTA, QUALITY_DELTA));
		logger.info("");
		
		Assert.assertEquals(scItems.size(), expectedItems.size(), "Number of sleep tile");
		int numberOfFailedItems = 0;
		
		for(int i = 0; i < expectedItems.size(); i++) {
		
			TimelineItem expect = expectedItems.get(i);
			TimelineItem actual = scItems.get(i);
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(expect.getTimestamp() * 1000);
			cal.add(Calendar.DAY_OF_MONTH, dayDifference);
			
			long timestampDifference = cal.getTimeInMillis() / 1000 - expect.getTimestamp();
			logger.info("Day difference " + dayDifference + ", Timestamp different: " + timestampDifference);
			
			// timestamp
			boolean pass = true;
			if(!(Math.abs(expect.getTimestamp() + timestampDifference - actual.getTimestamp()) <= TIMESTAMP_DELTA)) {
			
				logger.info("Sleep[" + i + "] - Expect timestamp: " + (expect.getTimestamp() + timestampDifference) + 
						", Actualy timestamp: " + actual.getTimestamp());
				pass = false;
			}
			
			// sleep data
			SleepSessionItem actualSleep = (SleepSessionItem)actual.getData();
			SleepSessionItem expectSleep = (SleepSessionItem)expect.getData();

			if (Math.abs(expectSleep.getRealStartTime() + timestampDifference - actualSleep.getRealStartTime()) > TIMESTAMP_DELTA ||
				Math.abs(expectSleep.getRealEndTime() + timestampDifference - actualSleep.getRealEndTime()) > TIMESTAMP_DELTA ||
				Math.abs(actualSleep.getRealSleepTimeInMinutes() - expectSleep.getRealSleepTimeInMinutes()) > DURATION_DELTA ||
				Math.abs(actualSleep.getRealDeepSleepTimeInMinutes() - expectSleep.getRealDeepSleepTimeInMinutes()) > DURATION_DELTA ||
				Math.abs(actualSleep.getNormalizedSleepQuality() - expectSleep.getNormalizedSleepQuality()) > QUALITY_DELTA ||
				actualSleep.getIsAutoDetected() == null || !actualSleep.getIsAutoDetected().equals(expectSleep.getIsAutoDetected()) ||
				actualSleep.getIsFirstSleepOfDay() == null || !actualSleep.getIsFirstSleepOfDay().equals(expectSleep.getIsFirstSleepOfDay()) ) {
				
				logger.info("Sleep[" + i + "]: ");
				logger.info("Real start time: " + actualSleep.getRealStartTime() + " - " + (expectSleep.getRealStartTime() + timestampDifference));
				logger.info("Real end time: " + actualSleep.getRealEndTime() + " - " + (expectSleep.getRealEndTime() + timestampDifference));
				logger.info("Real sleep time: " + actualSleep.getRealSleepTimeInMinutes() + " - " + expectSleep.getRealSleepTimeInMinutes());
				logger.info("Real deep sleep time: " + actualSleep.getRealDeepSleepTimeInMinutes() + " - " + expectSleep.getRealDeepSleepTimeInMinutes());
				logger.info("Sleep quality: " + actualSleep.getNormalizedSleepQuality() + " - " + expectSleep.getNormalizedSleepQuality());
				logger.info("Is auto detected: " + actualSleep.getIsAutoDetected() + " - " + expectSleep.getIsAutoDetected());
				logger.info("Is 1st sleep: " + actualSleep.getIsFirstSleepOfDay() + " - " + expectSleep.getIsFirstSleepOfDay());
				
				pass = false;
			}
			
			// sleep states
			List<Integer[]> actualStateChanges = actualSleep.getSleepStateChanges();
			List<Integer[]> expectStateChanges = expectSleep.getSleepStateChanges();
			
			if(actualStateChanges.size() != expectStateChanges.size()) {
				
				logger.info(String.format("Sleep[%d]: Number of sleep states changes: %d - %d", 
						actualStateChanges.size(), expectStateChanges.size()));
				
				pass = false;
			}
			else {
				int numberOfSleepStatesFailed = 0;
				for(int j = 0; j < actualStateChanges.size(); j++) {
					
					if (Math.abs(actualStateChanges.get(j)[0] - expectStateChanges.get(j)[0]) > 5 ||
						!actualStateChanges.get(j)[1].equals(expectStateChanges.get(j)[1])) {
					
						logger.info("Sleep[" + i + "]: ");
						logger.info(String.format("SleepState[%d]: %d, %d - %d, %d", j,
								actualStateChanges.get(j)[0], actualStateChanges.get(j)[1],
								expectStateChanges.get(j)[0], expectStateChanges.get(j)[1]));
						
						numberOfSleepStatesFailed++;
					}
				}
				
				if(numberOfSleepStatesFailed > 0)
					pass = false;
			}
			
			if(pass == false)
				numberOfFailedItems++;
		}
		
		Assert.assertEquals(numberOfFailedItems, 0, "Number of failed sleeps");
	}
	
}
