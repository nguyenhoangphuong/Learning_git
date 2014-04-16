package com.misfit.ta.backend.aut.correctness.servercalculation;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.sync.sdk.SDKSyncLog;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.ShortcutsTyper;

public class BackendNewServerCalculationIntegration extends BackendServerCalculationBase {

	// fields
	protected int delayTime = 60000;


	// test cases
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "NewServercalculation", "NewServercalculationSmoke" })
	public void NewServerCalculation_Integration() throws FileNotFoundException {

		// sign up new account
		boolean testPassed = true;
		String email = MVPApi.generateUniqueEmail();
		//	String email = "sc059@a.a";
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(email, "qqqqqq").token;
		String userId = MVPApi.getUserId(token);


		// create profile / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);


		// create goals for 4 days
		Goal[] goals = new Goal[4];
		for(int i = 0; i < goals.length; i++) {

			long goalTimestamp = timestamp - i * 3600 * 24;
			Goal goal = Goal.getDefaultGoal(goalTimestamp);

			// if today goal, increase length to 25h
			if(i == 0)
				goal.setEndTime(goal.getEndTime() + 3600);

			GoalsResult result = MVPApi.createGoal(token, goal);

			goal.setServerId(result.goals[0].getServerId());
			goal.setUpdatedAt(result.goals[0].getUpdatedAt());

			goals[i] = goal;
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
		// - session: 50 minutes - 5000 steps - 600 points at 8:00
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
		dataStrings.add(MVPApi.getRawDataAsString(goals[3].getStartTime(), goals[3].getTimeZoneOffsetInSeconds() / 60, "0101", "18", data3).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(goals[2].getStartTime(), goals[2].getTimeZoneOffsetInSeconds() / 60, "0102", "18", data2).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(goals[1].getStartTime(), goals[1].getTimeZoneOffsetInSeconds() / 60, "0103", "18", data1).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(goals[0].getStartTime(), goals[0].getTimeZoneOffsetInSeconds() / 60, "0104", "18", data0).rawData);
		pushSyncData(timestamp, userId, pedometer.getSerialNumberString(), dataStrings);

		logger.info("Waiting " + delayTime + " miliseconds");
		ShortcutsTyper.delayTime(delayTime);


		// get data from server
		List<GraphItem> graphitems3 = MVPApi.getGraphItems(token, goals[3].getStartTime(), goals[3].getEndTime(), 0l);
		List<GraphItem> graphitems2 = MVPApi.getGraphItems(token, goals[2].getStartTime(), goals[2].getEndTime(), 0l);
		List<GraphItem> graphitems1 = MVPApi.getGraphItems(token, goals[1].getStartTime(), goals[1].getEndTime(), 0l);
		List<GraphItem> graphitems0 = MVPApi.getGraphItems(token, goals[0].getStartTime(), goals[0].getEndTime(), 0l);

		List<TimelineItem> timelineitems3 = MVPApi.getTimelineItems(token, goals[3].getStartTime(), goals[3].getEndTime(), 0l);
		List<TimelineItem> timelineitems2 = MVPApi.getTimelineItems(token, goals[2].getStartTime(), goals[2].getEndTime(), 0l);
		List<TimelineItem> timelineitems1 = MVPApi.getTimelineItems(token, goals[1].getStartTime(), goals[1].getEndTime(), 0l);
		List<TimelineItem> timelineitems0 = MVPApi.getTimelineItems(token, goals[0].getStartTime(), goals[0].getEndTime(), 0l);

		Goal goal3 = MVPApi.getGoal(token, goals[3].getServerId()).goals[0];
		Goal goal2 = MVPApi.getGoal(token, goals[2].getServerId()).goals[0];
		Goal goal1 = MVPApi.getGoal(token, goals[1].getServerId()).goals[0];
		Goal goal0 = MVPApi.getGoal(token, goals[0].getServerId()).goals[0];

		statistics = MVPApi.getStatistics(token);


		// ===== VERIFY GRAPH ITEMS
		testPassed &= Verify.verifyEquals(graphitems3.size(), 44, "Number of graph items");
		testPassed &= Verify.verifyEquals(graphitems2.size(), 44, "Number of graph items");
		testPassed &= Verify.verifyEquals(graphitems1.size(), 44, "Number of graph items");
		testPassed &= Verify.verifyEquals(graphitems0.size(), 26, "Number of graph items");


		// ===== VERIFY TIMELINE ITEMS
		// number of session tiles are correct
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems3), 4, "Goals[3] has 4 session tiles");
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems2), 5, "Goals[2] has 5 session tiles");
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems1), 6, "Goals[1] has 6 session tiles");
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems0), 6, "Goals[0] has 6 session tiles");


		// session tiles are correct
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems3, goals[3], 7 * 60, 60, 600), "Goal[3] has session tile at 7:00 - 600 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems3, goals[3], 10 * 60, 50, 500), "Goal[3] has session tile at 10:00 - 500 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems3, goals[3], 13 * 60, 30, 300), "Goal[3] has session tile at 13:00 - 300 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems3, goals[3], 17 * 60, 40, 400), "Goal[3] has session tile at 17:00 - 400 pts");

		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 7 * 60, 60, 600), "Goal[2] has session tile at 7:00 - 600 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 10 * 60, 50, 500), "Goal[2] has session tile at 10:00 - 500 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 13 * 60, 30, 300), "Goal[2] has session tile at 13:00 - 300 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 17 * 60, 40, 400), "Goal[2] has session tile at 17:00 - 400 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 20 * 60, 40, 400), "Goal[2] has session tile at 20:00 - 400 pts");

		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 7 * 60, 60, 600), "Goal[1] has session tile at 7:00 - 600 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 10 * 60, 50, 500), "Goal[1] has session tile at 10:00 - 500 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 13 * 60, 30, 300), "Goal[1] has session tile at 13:00 - 300 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 17 * 60, 40, 400), "Goal[1] has session tile at 17:00 - 400 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 21 * 60, 60, 600), "Goal[1] has session tile at 21:00 - 600 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 23 * 60, 40, 400), "Goal[1] has session tile at 23:00 - 400 pts");

		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 8 * 60, 50, 500), "Goal[0] has session tile at 8:00 - 500 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 9 * 60, 50, 500), "Goal[0] has session tile at 9:00 - 500 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 10 * 60, 50, 500), "Goal[0] has session tile at 10:00 - 500 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 11 * 60, 50, 500), "Goal[0] has session tile at 11:00 - 500 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 12 * 60, 50, 500), "Goal[0] has session tile at 12:00 - 500 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 13 * 60, 50, 500), "Goal[0] has session tile at 13:00 - 500 pts");


		// daily goal milestone tiles are correct
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems3, goals[3], 10 * 60 + 40, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[3] has 100% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems3, goals[3], 17 * 60 + 10, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[3] has 150% tile");

		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems2, goals[2], 10 * 60 + 40, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[2] has 100% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems2, goals[2], 17 * 60 + 10, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[2] has 150% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems2, goals[2], 20 * 60 + 20, TimelineItemDataBase.EVENT_TYPE_200_GOAL, 2000), "Goals[2] has 200% tile");

		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems1, goals[1], 10 * 60 + 40, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[1] has 100% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems1, goals[1], 17 * 60 + 10, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[1] has 150% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems1, goals[1], 21 * 60 + 20, TimelineItemDataBase.EVENT_TYPE_200_GOAL, 2000), "Goals[1] has 200% tile");

		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems0, goals[0], 9 * 60 + 50, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[0] has 100% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems0, goals[0], 10 * 60 + 50, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[0] has 150% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems0, goals[0], 11 * 60 + 50, TimelineItemDataBase.EVENT_TYPE_200_GOAL, 2000), "Goals[0] has 200% tile");


		// personal best and streak tiles are correct
		testPassed &= Verify.verifyTrue(hasPersonalBestMilestone(timelineitems2, goals[2], 24 * 60 - 1, 2200, 400), "Goals[2] reaches 2200pts extended Goal[3] by 400pts");
		testPassed &= Verify.verifyTrue(hasPersonalBestMilestone(timelineitems1, goals[1], 24 * 60 - 1, 2800, 600), "Goals[1] reaches 2800pts extended Goal[2] by 600pts");
		testPassed &= Verify.verifyTrue(hasPersonalBestMilestone(timelineitems0, goals[0], 13 * 60 + 50, 3000, 200), "Goals[0] reaches 3000pts extended Goal[1] by 200pts");

		testPassed &= Verify.verifyTrue(hasStreakMilestone(timelineitems1, goals[1], 10 * 60 + 40, 3), "Goals[1] has 3-day streak tile");
		testPassed &= Verify.verifyTrue(hasStreakMilestone(timelineitems0, goals[0], 9 * 60 + 50, 4), "Goals[0] has 4-day streak tile");


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


		testPassed &= Verify.verifyEquals(goal3.getProgressData().getPoints(), 1800 * 2.5, "Goals[3] progress point");
		testPassed &= Verify.verifyEquals(goal2.getProgressData().getPoints(), 2200 * 2.5, "Goals[2] progress point");
		testPassed &= Verify.verifyEquals(goal1.getProgressData().getPoints(), 2800 * 2.5, "Goals[1] progress point");
		testPassed &= Verify.verifyEquals(goal0.getProgressData().getPoints(), 3000 * 2.5, "Goals[0] progress point");

		testPassed &= Verify.verifyEquals(goal3.getProgressData().getSteps(), 18000, "Goals[3] progress steps");
		testPassed &= Verify.verifyEquals(goal2.getProgressData().getSteps(), 22000, "Goals[2] progress steps");
		testPassed &= Verify.verifyEquals(goal1.getProgressData().getSteps(), 28000, "Goals[1] progress steps");
		testPassed &= Verify.verifyEquals(goal0.getProgressData().getSteps(), 30000, "Goals[1] progress steps");

		testPassed &= Verify.verifyNearlyEquals(goal3.getProgressData().getDistanceMiles(), miles3, distanceDelta, "Goals[3] progress distance in miles");
		testPassed &= Verify.verifyNearlyEquals(goal2.getProgressData().getDistanceMiles(), miles2, distanceDelta, "Goals[2] progress distance in miles");
		testPassed &= Verify.verifyNearlyEquals(goal1.getProgressData().getDistanceMiles(), miles1, distanceDelta, "Goals[1] progress distance in miles");
		testPassed &= Verify.verifyNearlyEquals(goal0.getProgressData().getDistanceMiles(), miles0, distanceDelta, "Goals[0] progress distance in miles");

		testPassed &= Verify.verifyNearlyEquals(goal3.getProgressData().getFullBmrCalorie(), fullBMR, fullBmrDelta, "Goals[3] full bmr");
		testPassed &= Verify.verifyNearlyEquals(goal2.getProgressData().getFullBmrCalorie(), fullBMR, fullBmrDelta, "Goals[2] full bmr");
		testPassed &= Verify.verifyNearlyEquals(goal1.getProgressData().getFullBmrCalorie(), fullBMR, fullBmrDelta, "Goals[1] full bmr");
		testPassed &= Verify.verifyNearlyEquals(goal0.getProgressData().getFullBmrCalorie(), fullBMR, fullBmrDelta, "Goals[0] full bmr");

		testPassed &= Verify.verifyNearlyEquals(goal3.getProgressData().getCalorie(), calorie3, totalCalorieDelta, "Goals[3] total calorie");
		testPassed &= Verify.verifyNearlyEquals(goal2.getProgressData().getCalorie(), calorie2, totalCalorieDelta, "Goals[2] total calorie");
		testPassed &= Verify.verifyNearlyEquals(goal1.getProgressData().getCalorie(), calorie1, totalCalorieDelta, "Goals[1] total calorie");
		testPassed &= Verify.verifyNearlyEquals(goal0.getProgressData().getCalorie(), calorie0, totalCalorieDelta, "Goals[0] total calorie");

		// ===== VERIFY STATISTICS
		long personalBestTimestamp = goals[0].getStartTime() + (13 * 60 + 50) * 60;
		testPassed &= Verify.verifyNearlyEquals(statistics.getLifetimeDistance(), miles0 + miles1 + miles2 + miles3, distanceDelta, "Lifetime distance");
		testPassed &= Verify.verifyEquals(statistics.getBestStreak(), 4, "Best streak number");
		testPassed &= Verify.verifyEquals(statistics.getTotalGoalHit(), 4, "Total goal hit number");
		testPassed &= Verify.verifyEquals(statistics.getPersonalRecords().getPersonalBestRecordsInPoint().getPoint(), 3000 * 2.5, "Personal best points");
		testPassed &= Verify.verifyEquals(statistics.getPersonalRecords().getPersonalBestRecordsInPoint().getTimestamp(), personalBestTimestamp, "Personal best timestamp");

		Assert.assertTrue(testPassed, "All asserts are passed");

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "NewServercalculation" })
	public void NewServerCalculation_MarathonTile() {

		// sign up new account
		boolean testPassed = true;
		String email = MVPApi.generateUniqueEmail();
//		String email = "sc038@a.a";
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(email, "qqqqqq").token;


		// create profile (height = 64") / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();
		profile.getDisplayedUnits().setDistanceUnit(1);
		profile.setHeight(64d);

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);


		// create goal for today
		Goal[] goals = new Goal[6];
		for(int i = 0; i < 6; i++) {

			long goalTimestamp = timestamp - i * 3600 * 24;
			Goal goal = Goal.getDefaultGoal(goalTimestamp);
			GoalsResult result = MVPApi.createGoal(token, goal);

			goal.setServerId(result.goals[0].getServerId());
			goal.setUpdatedAt(result.goals[0].getUpdatedAt());

			goals[i] = goal;
		}

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
		// 63720 steps - 360 minute (49.21 miles)
		GoalRawData data5 = new GoalRawData();
		data5.appendGoalRawData(generateSessionRawData(63720, 3600, 360));
		data5.appendGoalRawData(generateEmptyRawData(0 * 60 + 360, 24 * 60));

		// 2nd day:
		// SI: 1900 steps - 20 mins (0.69 miles - 49.9 total)
		// US: 2560 steps - 20 mins (1.1 miles - 51 total)
		GoalRawData data4a = new GoalRawData();
		data4a.appendGoalRawData(generateSessionRawData(1900, 200, 20));
		data4a.appendGoalRawData(generateEmptyRawData(0 * 60 + 20, 1 * 60));

		GoalRawData data4b = new GoalRawData();
		data4b.appendGoalRawData(generateSessionRawData(2560, 200, 20));
		data4b.appendGoalRawData(generateEmptyRawData(1 * 60 + 20, 24 * 60));

		// 3rd day:
		// 127440 steps - 720 minutes (98.41 miles - 149.41 total)
		GoalRawData data3 = new GoalRawData();
		data3.appendGoalRawData(generateSessionRawData(127440, 7200, 720));
		data3.appendGoalRawData(generateEmptyRawData(0 * 60 + 720, 24 * 60));

		// 4th day
		// US: 4260 steps - 30 mins (2.01 miles - 151.42 total)
		// SI: 12780 steps - 90 mins (6.03 miles - 157.45 total)
		// 60840 steps - 360 mins (42.8 miles - 200.25 total)
		GoalRawData data2a = new GoalRawData();
		data2a.appendGoalRawData(generateSessionRawData(4260, 300, 30));
		data2a.appendGoalRawData(generateEmptyRawData(0 * 60 + 30, 1 * 60));

		GoalRawData data2b = new GoalRawData();
		data2b.appendGoalRawData(generateSessionRawData(12780, 900, 90));
		data2b.appendGoalRawData(generateEmptyRawData(1 * 60 + 90, 6 * 60));

		data2b.appendGoalRawData(generateSessionRawData(60840, 3600, 360));
		data2b.appendGoalRawData(generateEmptyRawData(6 * 60 + 360, 24 * 60));

		// 5th day
		// 126000 steps - 720 mins (95.14 miles - 295.39 total)
		GoalRawData data1 = new GoalRawData();
		data1.appendGoalRawData(generateSessionRawData(126000, 7200, 720));
		data1.appendGoalRawData(generateEmptyRawData(0 * 60 + 720, 24 * 60));

		// 6th day
		// US: 21300 steps - 150 mins (10.05 miles - 305.44 total)
		// SI: 21300 steps - 150 mins (10.05 miles - 315.49 total)
		GoalRawData data0a = new GoalRawData();
		data0a.appendGoalRawData(generateSessionRawData(21300, 1500, 150));
		data0a.appendGoalRawData(generateEmptyRawData(0 * 60 + 150, 6 * 60));

		GoalRawData data0b = new GoalRawData();
		data0b.appendGoalRawData(generateSessionRawData(21300, 1500, 150));
		data0b.appendGoalRawData(generateEmptyRawData(6 * 60 + 150, 24 * 60));


		// push to server
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(goals[5].getStartTime(), goals[5].getTimeZoneOffsetInSeconds() / 60, "0101", "18", data5).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(goals[4].getStartTime(), goals[4].getTimeZoneOffsetInSeconds() / 60, "0102", "18", data4a).rawData);		
		pushSyncData(timestamp, email, pedometer.getSerialNumberString(), dataStrings);
		ShortcutsTyper.delayTime(delayTime);
		
		changeDistanceUnit(token, 0);
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(goals[4].getStartTime() + 3600, goals[4].getTimeZoneOffsetInSeconds() / 60, "0101", "18", data4b).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(goals[3].getStartTime(), goals[3].getTimeZoneOffsetInSeconds() / 60, "0102", "18", data3).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(goals[2].getStartTime(), goals[2].getTimeZoneOffsetInSeconds() / 60, "0103", "18", data2a).rawData);
		pushSyncData(timestamp, email, pedometer.getSerialNumberString(), dataStrings);
		ShortcutsTyper.delayTime(delayTime);
		
		changeDistanceUnit(token, 1);
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(goals[2].getStartTime() + 3600, goals[2].getTimeZoneOffsetInSeconds() / 60, "0101", "18", data2b).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(goals[1].getStartTime(), goals[1].getTimeZoneOffsetInSeconds() / 60, "0102", "18", data1).rawData);
		pushSyncData(timestamp, email, pedometer.getSerialNumberString(), dataStrings);
		ShortcutsTyper.delayTime(delayTime);
		
		changeDistanceUnit(token, 0);
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(goals[0].getStartTime(), goals[0].getTimeZoneOffsetInSeconds() / 60, "0101", "18", data0a).rawData);
		pushSyncData(timestamp, email, pedometer.getSerialNumberString(), dataStrings);
		ShortcutsTyper.delayTime(delayTime);
		
		changeDistanceUnit(token, 1);
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(goals[0].getStartTime() + 6 * 3600, goals[0].getTimeZoneOffsetInSeconds() / 60, "0101", "18", data0b).rawData);
		pushSyncData(timestamp, email, pedometer.getSerialNumberString(), dataStrings);
		ShortcutsTyper.delayTime(delayTime);


		// get server data
		List<TimelineItem> timelineitems4 = MVPApi.getTimelineItems(token, goals[4].getStartTime(), goals[4].getEndTime(), 0l);
		List<TimelineItem> timelineitems2 = MVPApi.getTimelineItems(token, goals[2].getStartTime(), goals[2].getEndTime(), 0l);
		List<TimelineItem> timelineitems0 = MVPApi.getTimelineItems(token, goals[0].getStartTime(), goals[0].getEndTime(), 0l);
		statistics = MVPApi.getStatistics(token);


		// === VERIFY DISTANCE TILES
		int numberOfDistanceTile = getNumberOfTile(timelineitems4, TimelineItemDataBase.TYPE_LIFETIME_DISTANCE);
		numberOfDistanceTile += getNumberOfTile(timelineitems2, TimelineItemDataBase.TYPE_LIFETIME_DISTANCE);
		numberOfDistanceTile += getNumberOfTile(timelineitems0, TimelineItemDataBase.TYPE_LIFETIME_DISTANCE);
		
		testPassed &= Verify.verifyEquals(numberOfDistanceTile, 3, "Number of lifetime distance tiles");
		testPassed &= Verify.verifyTrue(hasLifeTimeDistanceTile(timelineitems4, goals[4], 0 * 60 + 15, 2, 1), "2 marathons tile in SI unit");
		testPassed &= Verify.verifyTrue(hasLifeTimeDistanceTile(timelineitems2, goals[2], 0 * 60 + 9, 6, 0), "6 marathons tile in US unit");
		testPassed &= Verify.verifyTrue(hasLifeTimeDistanceTile(timelineitems0, goals[0], 0 * 60 + 69, 12, 0), "12 marathons tile in US unit");


		// === VERIFY STATISTICS
		testPassed &= Verify.verifyNearlyEquals(statistics.getLifetimeDistance(), 315.49d, 0.01, "Lifetime distance");

		Assert.assertTrue(testPassed, "All asserts are passed");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "NewServercalculation" })
	public void NewServerCalculation_Progress() {

		// sign up new account
		boolean testPassed = true;
		String email = MVPApi.generateUniqueEmail();
//		String email = "sc034@a.a";
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(email, "qqqqqq").token;


		// create profile / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);


		// create goal for today
		Goal goal = Goal.getDefaultGoal();
		GoalsResult result = MVPApi.createGoal(token, goal);

		goal.setServerId(result.goals[0].getServerId());
		goal.setUpdatedAt(result.goals[0].getUpdatedAt());


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
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(goal.getStartTime(), goal.getTimeZoneOffsetInSeconds() / 60, "0101", "18", data).rawData);
		pushSyncData(timestamp, email, pedometer.getSerialNumberString(), dataStrings);
		
		ShortcutsTyper.delayTime(delayTime);
		

		// get server data
		List<TimelineItem> timelineitems = MVPApi.getTimelineItems(token, goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(token, goal.getServerId()).goals[0];


		// VERIFY TIMELINE ITEMS
		// number of session tiles are correct
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems), 4, "Goal has 4 session tiles");

		// session tiles are correct
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 7 * 60, 60, 600), "Goal has session tile at 7:00 - 600 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 10 * 60, 50, 500), "Goal has session tile at 10:00 - 500 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 13 * 60, 30, 300), "Goal has session tile at 13:00 - 300 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 17 * 60, 40, 400), "Goal has session tile at 17:00 - 400 pts");

		// daily goal milestone tiles are correct
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems, goal, 10 * 60 + 32, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goal has 100% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems, goal, 15 * 60 + 2, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goal has 150% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems, goal, 17 * 60 + 32, TimelineItemDataBase.EVENT_TYPE_200_GOAL, 2000), "Goal has 200% tile");


		// ===== VERIFY GOAL PROGRESS
		double miles = MVPCalculator.calculateMiles(data.getSteps(), profile.getHeight());
		testPassed &= Verify.verifyEquals(goal.getProgressData().getPoints(), 2080 * 2.5, "Goal progress point");
		testPassed &= Verify.verifyEquals(goal.getProgressData().getSteps(), 20800, "Goal progress steps");
		testPassed &= Verify.verifyNearlyEquals(goal.getProgressData().getDistanceMiles(), miles, 0.001, "Goal progress distance in miles");
		
		
		Assert.assertTrue(testPassed, "All asserts are passed");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "NewServercalculation" })
	public void NewServerCalculation_OverlapData() {
		
		// sign up new account
		boolean testPassed = true;
		String email = MVPApi.generateUniqueEmail();
//		String email = "sc041@a.a";
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(email, "qqqqqq").token;


		// create profile / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);


		// create goal for today
		Goal goal = Goal.getDefaultGoal();
		GoalsResult result = MVPApi.createGoal(token, goal);

		goal.setServerId(result.goals[0].getServerId());
		goal.setUpdatedAt(result.goals[0].getUpdatedAt());
		
		
		// story:
		// 1st shine:
		// - 4000 steps - 40 mins (400pts) at 0:00am
		// - 2000 steps - 20 mins (200pts) at 2:00am
		// - 4000 steps - 40 mins (400pts) at 3:00am
		// 2nd shine:
		// - 2000 steps - 20 mins (200pts) at 0:10am (overlap 0:10am - 0:30am)
		// - 2000 steps - 20 mins (200pts) at 1:50am (overlap 2:00am - 2:10am)
		// - 6000 steps - 60 mins (600pts) at 2:50am (overlap 3:00am - 3:40am)
		// expect:
		// - 6 session tile (3 from each shine)
		// - 2000 pogress pts
		GoalRawData data1 = new GoalRawData();
		data1.appendGoalRawData(generateSessionRawData(4000, 400, 40));
		data1.appendGoalRawData(generateEmptyRawData(0 * 60 + 40, 2 * 60));

		data1.appendGoalRawData(generateSessionRawData(2000, 200, 20));
		data1.appendGoalRawData(generateEmptyRawData(2 * 60 + 20, 3 * 60));

		data1.appendGoalRawData(generateSessionRawData(4000, 400, 40));
		data1.appendGoalRawData(generateEmptyRawData(3 * 60 + 40, 24 * 60));
		
		GoalRawData data2 = new GoalRawData();
		data2.appendGoalRawData(generateEmptyRawData(0, 10));
		data2.appendGoalRawData(generateSessionRawData(2000, 200, 20));
		data2.appendGoalRawData(generateEmptyRawData(0 * 60 + 10 + 20, 1 * 60 + 50));

		data2.appendGoalRawData(generateSessionRawData(2000, 200, 20));
		data2.appendGoalRawData(generateEmptyRawData(1 * 60 + 50 + 20, 2 * 60 + 50));

		data2.appendGoalRawData(generateSessionRawData(6000, 600, 60));
		
		
		// push to server
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(goal.getStartTime(), goal.getTimeZoneOffsetInSeconds() / 60, "0101", "18", data1).rawData);
		pushSyncData(timestamp, email, pedometer.getSerialNumberString(), dataStrings);
		
		dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(goal.getStartTime(), goal.getTimeZoneOffsetInSeconds() / 60, "0101", "18", data2).rawData);
		pushSyncData(timestamp, email, pedometer.getSerialNumberString(), dataStrings);
		
		ShortcutsTyper.delayTime(delayTime);


		// get server data
		List<TimelineItem> timelineitems = MVPApi.getTimelineItems(token, goal.getStartTime(), goal.getEndTime(), 0l);
		goal = MVPApi.getGoal(token, goal.getServerId()).goals[0];
		
		
		// VERIFY TIMELINE ITEMS
		// number of session tiles are correct
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems), 6, "Goal has 6 session tiles");

		// session tiles are correct
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 0 * 60, 40, 400), "Goal has session tile at 0:00 - 400 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 2 * 60, 20, 200), "Goal has session tile at 2:00 - 200 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 3 * 60, 40, 400), "Goal has session tile at 3:00 - 400 pts");
		
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 0 * 60 + 10, 20, 200), "Goal has session tile at 0:10 - 200 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 1 * 60 + 50, 20, 200), "Goal has session tile at 1:50 - 200 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems, goal, 2 * 60 + 50, 60, 600), "Goal has session tile at 2:50 - 600 pts");


		// ===== VERIFY GOAL PROGRESS
		double miles = MVPCalculator.calculateMiles(data1.getSteps(), profile.getHeight());
		miles += MVPCalculator.calculateMiles(data2.getSteps(), profile.getHeight());
		
		testPassed &= Verify.verifyEquals(goal.getProgressData().getPoints(), 2000 * 2.5, "Goal progress point");
		testPassed &= Verify.verifyEquals(goal.getProgressData().getSteps(), 20000, "Goal progress steps");
		testPassed &= Verify.verifyNearlyEquals(goal.getProgressData().getDistanceMiles(), miles, 0.001, "Goal progress distance in miles");
		
		Assert.assertTrue(testPassed, "All asserts are passed");
	}

	
	// data helpers
	private void pushSyncData(long timestamp, String email, String serialNumber, List<String> dataStrings) {

		SDKSyncLog syncLog = ServerCalculationTestHelpers.createSDKSyncLogFromDataStrings(timestamp, email, serialNumber, dataStrings);
		MVPApi.pushSDKSyncLog(syncLog);
	}
	
}
