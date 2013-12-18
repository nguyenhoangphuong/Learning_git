package com.misfit.ta.backend.aut.correctness.servercalculation;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.ActivitySessionItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.MilestoneItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.MilestoneItemInfo;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.ShortcutsTyper;

public class BackendServerCalculationTest extends BackendAutomation {


	protected int delayTime = 10000;
	
	// test cases
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "servercalculation" })
	public void CalculationWithPredefineStory() throws FileNotFoundException {

		// sign up new account
		boolean testPassed = true;
		String email = MVPApi.generateUniqueEmail();
//		String email = "sc012@a.a";
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(email, "qqqqqq").token;
		
		
		// create profile / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();
		
		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);
		
		
		// create goals for 3 days
		Goal[] goals = new Goal[3];
		for(int i = 0; i < 3; i++) {
			
			long goalTimestamp = timestamp - i * 3600 * 24;
			Goal goal = Goal.getDefaultGoal(goalTimestamp);
			GoalsResult result = MVPApi.createGoal(token, goal);
			
			goal.setServerId(result.goals[0].getServerId());
			goal.setUpdatedAt(result.goals[0].getUpdatedAt());
			
			goals[i] = goal;
		}
		
		
		// story on 1st day (yesterday of yesterday):
		// - session: 60 minutes - 6000 steps - 600 points at 7:00
		// - session: 50 minutes - 5000 steps - 500 points at 10:00
		// - session: 30 minutes - 3000 steps - 300 points at 13:00
		// - session: 40 minutes - 4000 steps - 400 points at 17:00
		// expect:
		// - 4 acitivity session tiles
		// - 100% tile
		// - 150% tile
		pushEmptyData(token, goals[2].getServerId(), 0, 7 * 60);
		
		pushRawDataForASession(token, goals[2].getServerId(), 7 * 60, 60, 100, 10);
		pushEmptyData(token, goals[2].getServerId(), 7 * 60 + 60, 10 * 60);
		
		pushRawDataForASession(token, goals[2].getServerId(), 10 * 60, 50, 100, 10);
		pushEmptyData(token, goals[2].getServerId(), 10 * 60 + 50, 13 * 60);
		
		pushRawDataForASession(token, goals[2].getServerId(), 13 * 60, 30, 100, 10);
		pushEmptyData(token, goals[2].getServerId(), 13 * 60 + 30, 17 * 60);
		
		pushRawDataForASession(token, goals[2].getServerId(), 17 * 60, 40, 100, 10);
		pushEmptyData(token, goals[2].getServerId(), 17 * 60 + 40, 24 * 60);

		
		// story on 2nd day (yesterday):
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
		pushEmptyData(token, goals[1].getServerId(), 0, 7 * 60);
		
		pushRawDataForASession(token, goals[1].getServerId(), 7 * 60, 60, 100, 10);
		pushEmptyData(token, goals[1].getServerId(), 7 * 60 + 60, 10 * 60);
		
		pushRawDataForASession(token, goals[1].getServerId(), 10 * 60, 50, 100, 10);
		pushEmptyData(token, goals[1].getServerId(), 10 * 60 + 50, 13 * 60);
		
		pushRawDataForASession(token, goals[1].getServerId(), 13 * 60, 30, 100, 10);
		pushEmptyData(token, goals[1].getServerId(), 13 * 60 + 30, 17 * 60);
		
		pushRawDataForASession(token, goals[1].getServerId(), 17 * 60, 40, 100, 10);
		pushEmptyData(token, goals[1].getServerId(), 17 * 60 + 40, 20 * 60);
		
		pushRawDataForASession(token, goals[1].getServerId(), 20 * 60, 40, 100, 10);
		pushEmptyData(token, goals[1].getServerId(), 20 * 60 + 40, 24 * 60);

		// story on 3rd day:
		// - session: 60 minutes - 6000 steps - 600 points at 7:00
		// - session: 50 minutes - 5000 steps - 500 points at 10:00
		// - session: 30 minutes - 3000 steps - 300 points at 13:00
		// - session: 40 minutes - 4000 steps - 400 points at 17:00
		// - session: 60 minutes - 6000 steps - 600 points at 21:00
		// expect:
		// - 5 acitivity session tiles
		// - 100% tile
		// - 150% tile
		// - 200% tile
		// - streak tile
		// - personal best tile
		// - statistics updated
		pushEmptyData(token, goals[0].getServerId(), 0, 7 * 60);
		
		pushRawDataForASession(token, goals[0].getServerId(), 7 * 60, 60, 100, 10);
		pushEmptyData(token, goals[0].getServerId(), 7 * 60 + 60, 10 * 60);
		
		pushRawDataForASession(token, goals[0].getServerId(), 10 * 60, 50, 100, 10);
		pushEmptyData(token, goals[0].getServerId(), 10 * 60 + 50, 13 * 60);
		
		pushRawDataForASession(token, goals[0].getServerId(), 13 * 60, 30, 100, 10);
		pushEmptyData(token, goals[0].getServerId(), 13 * 60 + 30, 17 * 60);
		
		pushRawDataForASession(token, goals[0].getServerId(), 17 * 60, 40, 100, 10);
		pushEmptyData(token, goals[0].getServerId(), 17 * 60 + 40, 21 * 60);
		
		pushRawDataForASession(token, goals[0].getServerId(), 21 * 60, 60, 100, 10);
		pushEmptyData(token, goals[0].getServerId(), 21 * 60 + 60, 24 * 60);
		
		ShortcutsTyper.delayTime(delayTime);
		
		
		// get data from server
		List<GraphItem> graphitems2 = MVPApi.getGraphItems(token, goals[2].getStartTime(), goals[2].getEndTime(), 0);
		List<GraphItem> graphitems1 = MVPApi.getGraphItems(token, goals[1].getStartTime(), goals[1].getEndTime(), 0);
		List<GraphItem> graphitems0 = MVPApi.getGraphItems(token, goals[0].getStartTime(), goals[0].getEndTime(), 0);
		
		List<TimelineItem> timelineitems2 = MVPApi.getTimelineItems(token, goals[2].getStartTime(), goals[2].getEndTime(), 0);
		List<TimelineItem> timelineitems1 = MVPApi.getTimelineItems(token, goals[1].getStartTime(), goals[1].getEndTime(), 0);
		List<TimelineItem> timelineitems0 = MVPApi.getTimelineItems(token, goals[0].getStartTime(), goals[0].getEndTime(), 0);
		
		Goal goal2 = MVPApi.getGoal(token, goals[2].getServerId()).goals[0];
		Goal goal1 = MVPApi.getGoal(token, goals[1].getServerId()).goals[0];
		Goal goal0 = MVPApi.getGoal(token, goals[0].getServerId()).goals[0];
		
		statistics = MVPApi.getStatistics(token);
		
		
		// ===== VERIFY GRAPH ITEMS
		testPassed &= Verify.verifyEquals(graphitems2.size(), 1440 / 33, "Number of graph items");
		testPassed &= Verify.verifyEquals(graphitems1.size(), 1440 / 33, "Number of graph items");
		testPassed &= Verify.verifyEquals(graphitems0.size(), 1440 / 33, "Number of graph items");
		
		
		// ===== VERIFY TIMELINE ITEMS
		// number of session tiles are correct
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems2), 4, "Goals[2] has 4 session tiles");
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems1), 5, "Goals[1] has 5 session tiles");
		testPassed &= Verify.verifyEquals(getNumberOfSessionTiles(timelineitems0), 5, "Goals[0] has 5 session tiles");
		
		
		// session tiles are correct
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 7 * 60, 60, 600), "Goal[2] has session tile at 7:00 - 600 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 10 * 60, 50, 500), "Goal[2] has session tile at 10:00 - 500 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 13 * 60, 30, 300), "Goal[2] has session tile at 13:00 - 300 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 17 * 60, 40, 400), "Goal[2] has session tile at 17:00 - 400 pts");
		
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 7 * 60, 60, 600), "Goal[1] has session tile at 7:00 - 600 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 10 * 60, 50, 500), "Goal[1] has session tile at 10:00 - 500 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 13 * 60, 30, 300), "Goal[1] has session tile at 13:00 - 300 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 17 * 60, 40, 400), "Goal[1] has session tile at 17:00 - 400 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 20 * 60, 40, 400), "Goal[1] has session tile at 20:00 - 400 pts");
		
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 7 * 60, 60, 600), "Goal[0] has session tile at 7:00 - 600 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 10 * 60, 50, 500), "Goal[0] has session tile at 10:00 - 500 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 13 * 60, 30, 300), "Goal[0] has session tile at 13:00 - 300 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 17 * 60, 40, 400), "Goal[0] has session tile at 17:00 - 400 pts");
		testPassed &= Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 21 * 60, 60, 600), "Goal[0] has session tile at 21:00 - 600 pts");
		
		
		// daily goal milestone tiles are correct
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems2, goals[2], 10 * 60 + 40, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[2] has 100% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems2, goals[2], 17 * 60 + 10, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[2] has 150% tile");
		
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems1, goals[1], 10 * 60 + 40, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[1] has 100% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems1, goals[1], 17 * 60 + 10, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[1] has 150% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems1, goals[1], 20 * 60 + 20, TimelineItemDataBase.EVENT_TYPE_200_GOAL, 2000), "Goals[1] has 200% tile");
		
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems0, goals[0], 10 * 60 + 40, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[0] has 100% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems0, goals[0], 17 * 60 + 10, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[0] has 150% tile");
		testPassed &= Verify.verifyTrue(hasDailyGoalMilestone(timelineitems0, goals[0], 21 * 60 + 20, TimelineItemDataBase.EVENT_TYPE_200_GOAL, 2000), "Goals[0] has 200% tile");
		
		
		// personal best and streak tiles are correct
		testPassed &= Verify.verifyTrue(hasPersonalBestMilestone(timelineitems1, goals[1], 20 * 60 + 40, 2200, 400), "Goals[1] reaches 2200pts extended Goal[2] by 400pts");
		testPassed &= Verify.verifyTrue(hasPersonalBestMilestone(timelineitems0, goals[0], 21 * 60 + 60, 2400, 400), "Goals[0] reaches 2400pts extended Goal[1] by 200pts");
		testPassed &= Verify.verifyTrue(hasStreakMilestone(timelineitems0, goals[0], 10 * 60 + 40, 3), "Goals[0] has 3-day streak tile");
	
		
		// ===== VERIFY GOAL PROGRESS
		double miles2 = MVPCalculator.calculateMiles(6000, 60, profile.getHeight()) + 
						MVPCalculator.calculateMiles(5000, 50, profile.getHeight()) +
						MVPCalculator.calculateMiles(4000, 40, profile.getHeight()) +
						MVPCalculator.calculateMiles(3000, 30, profile.getHeight());
		double miles1 = miles2 + MVPCalculator.calculateMiles(4000, 40, profile.getHeight());
		double miles0 = miles2 + MVPCalculator.calculateMiles(6000, 60, profile.getHeight());
		
		
		testPassed &= Verify.verifyEquals(goal2.getProgressData().getPoints(), 1800 * 2.5, "Goals[2] progress point");
		testPassed &= Verify.verifyEquals(goal1.getProgressData().getPoints(), 2200 * 2.5, "Goals[1] progress point");
		testPassed &= Verify.verifyEquals(goal0.getProgressData().getPoints(), 2400 * 2.5, "Goals[0] progress point");
		
		testPassed &= Verify.verifyEquals(goal2.getProgressData().getSteps(), 18000, "Goals[2] progress steps");
		testPassed &= Verify.verifyEquals(goal1.getProgressData().getSteps(), 22000, "Goals[1] progress steps");
		testPassed &= Verify.verifyEquals(goal0.getProgressData().getSteps(), 24000, "Goals[0] progress steps");
		
		testPassed &= Verify.verifyNearlyEquals(goal2.getProgressData().getDistanceMiles(), miles2, 0.1, "Goals[2] progress distance in miles");
		testPassed &= Verify.verifyNearlyEquals(goal1.getProgressData().getDistanceMiles(), miles1, 0.1, "Goals[1] progress distance in miles");
		testPassed &= Verify.verifyNearlyEquals(goal0.getProgressData().getDistanceMiles(), miles0, 0.1, "Goals[0] progress distance in miles");
		
		
		// ===== VERIFY STATISTICS
		long personalBestTimestamp = goals[0].getStartTime() + (21 * 60 + 60) * 60;
		testPassed &= Verify.verifyNearlyEquals(statistics.getLifetimeDistance(), miles0 + miles1 + miles2, 0.1, "Lifetime distance");
		testPassed &= Verify.verifyEquals(statistics.getBestStreak(), 3, "Best streak number");
		testPassed &= Verify.verifyEquals(statistics.getTotalGoalHit(), 3, "Total goal hit number");
		testPassed &= Verify.verifyEquals(statistics.getPersonalRecords().getPersonalBestRecordsInPoint().getPoint(), 2400 * 2.5, "Personal best points");
		testPassed &= Verify.verifyEquals(statistics.getPersonalRecords().getPersonalBestRecordsInPoint().getTimestamp(), personalBestTimestamp, "Personal best points");
	
		Assert.assertTrue(testPassed, "All asserts are passed");
	
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "servercalculation" })
	public void CalculationForMarathonTile() {
		
		// sign up new account
		boolean testPassed = true;
//		String email = MVPApi.generateUniqueEmail();
		String email = "sc013@a.a";
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(email, "qqqqqq").token;


		// create profile / pedometer / statistics
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();
		profile.getDisplayedUnits().setHeightUnit(1);

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);


		// create goal for today
		Goal goal = Goal.getDefaultGoal();
		GoalsResult result = MVPApi.createGoal(token, goal);

		goal.setServerId(result.goals[0].getServerId());
		goal.setUpdatedAt(result.goals[0].getUpdatedAt());


		// story:
		// - Reach 49.8 miles (80 km) in SI unit
		// - Reach 50 miles in US unit
		// - Reach 150 miles in US unit
		// - Reach 156 miles (250 km) in SI unit
		// - Reach 300 miles in US unit
		// - Reach 311 miles (500 km) in SI unit
		// expect:
		// - All 6 marathons tile are created
		// - Tiles are displayed in correct order
		
		// SI: 58200 steps - 300 mins (49.91 miles)
		pushRawDataForASession(token, goal.getServerId(), 1 * 60, 60, 33820 / 60, 10);

		// US: 3000 steps - 60 mins (1 mile)
		profile.getDisplayedUnits().setHeightUnit(0);
		MVPApi.updateProfile(token, profile);
		pushRawDataForASession(token, goal.getServerId(), 2 * 60, 60, 3000 / 60, 10);

		// US: 60000 steps - 60 mins (100.3 miles)
		pushRawDataForASession(token, goal.getServerId(), 3 * 60, 60, 60000 / 60, 10);
		
		// SI: 9900 steps - 60 mins (6.63 miles)
		profile.getDisplayedUnits().setHeightUnit(1);
		MVPApi.updateProfile(token, profile);
		pushRawDataForASession(token, goal.getServerId(), 4 * 60, 60, 9900 / 60, 10);
		
		// US: 76800 steps - 60 mins (150.11 miles)
		profile.getDisplayedUnits().setHeightUnit(0);
		MVPApi.updateProfile(token, profile);
		pushRawDataForASession(token, goal.getServerId(), 5 * 60, 60, 76800 / 60, 10);
		
		// SI: 9900 steps - 60 mins (6.63 miles)
		profile.getDisplayedUnits().setHeightUnit(1);
		MVPApi.updateProfile(token, profile);
		pushRawDataForASession(token, goal.getServerId(), 4 * 60, 60, 9900 / 60, 10);

		ShortcutsTyper.delayTime(delayTime);
	}
	
	
	// helpers
	private void pushEmptyData(String token, String goalId, int includeStartOffsetMinute, int excludeEndOffsetMinute) {
		
		GoalRawData rawData = generateEmptyDataForTimeRange(includeStartOffsetMinute, excludeEndOffsetMinute);		
		MVPApi.pushRawData(token, goalId, rawData, includeStartOffsetMinute);
	}
	
	private void pushRawDataForASession(String token, String goalId, int offsetMinute, int duration, int stepPerMinute, int pointPerMinute) {
		
		GoalRawData rawData = generateRawDataForASession(stepPerMinute, pointPerMinute, duration);		
		MVPApi.pushRawData(token, goalId, rawData, offsetMinute);
	}
	
	private GoalRawData generateRawDataForASession(int stepPerMinute, int pointPerMinute, int duration) {
		
		int[] steps = new int[duration];
		int[] points = new int[duration];
		int[] variances = new int[duration];

		Arrays.fill(steps, stepPerMinute);
		Arrays.fill(points, (int)(pointPerMinute * 2.5));
		Arrays.fill(variances, 10000);
		
		GoalRawData rawdata = new GoalRawData();
		rawdata.setPoints(points);
		rawdata.setSteps(steps);
		rawdata.setVariances(variances);
		
		return rawdata;
	}
	
	private GoalRawData generateEmptyDataForTimeRange(int includeStartOffsetMinute, int excludeEndOffsetMinute) {
		
		int duration = excludeEndOffsetMinute - includeStartOffsetMinute;
		int[] steps = new int[duration];
		int[] points = new int[duration];
		int[] variances = new int[duration];

		Arrays.fill(steps, 0);
		Arrays.fill(points, 0);
		Arrays.fill(variances, 10000);
		
		GoalRawData rawdata = new GoalRawData();
		rawdata.setPoints(points);
		rawdata.setSteps(steps);
		rawdata.setVariances(variances);
		
		return rawdata;
	}
 
	
	private int getNumberOfSessionTiles(List<TimelineItem> items) {
		
		int count = 0;
		for(TimelineItem item : items) {
					
			if(item.getItemType() == TimelineItemDataBase.TYPE_SESSION)
				count++;
		}
		
		return count;
	}
	
	private boolean hasSessionTileWithData(List<TimelineItem> items, Goal goal, int offsetMinute, int minutes, int points) {
		
		long goalStartTime = goal.getStartTime();
		Calendar cal = Calendar.getInstance();
		
		for(TimelineItem item : items) {
			
			cal.setTimeInMillis(goalStartTime * 1000 + offsetMinute * 60 * 1000);
			long timestamp = cal.getTimeInMillis() / 1000;
			
			if(item.getTimestamp().equals(timestamp) && item.getItemType() == TimelineItemDataBase.TYPE_SESSION) {
				
				ActivitySessionItem session = (ActivitySessionItem) item.getData();
				double diff = (session.getPoint() - points * 2.5) + (session.getDuration() - minutes * 60);
				return diff == 0;
			}
		}
		
		return false;
	}
	
	private boolean hasDailyGoalMilestone(List<TimelineItem> items, Goal goal, int offsetMinute, int eventType, int points) {
		
		long goalStartTime = goal.getStartTime();
		Calendar cal = Calendar.getInstance();
		
		for(TimelineItem item : items) {
			
			cal.setTimeInMillis(goalStartTime * 1000 + offsetMinute * 60 * 1000);
			long timestamp = cal.getTimeInMillis() / 1000;
			
			if(item.getTimestamp().equals(timestamp) && item.getItemType() == TimelineItemDataBase.TYPE_MILESTONE) {
				
				MilestoneItem milestone = (MilestoneItem) item.getData();
				MilestoneItemInfo milestoneInfo = milestone.getInfo();
				double diff = milestoneInfo.getPoint() - points * 2.5;
				return milestone.getEventType().equals(eventType) && diff == 0;
			}
		}

		return false;
	}

	private boolean hasStreakMilestone(List<TimelineItem> items, Goal goal, int offsetMinute, int streakNumber) {
		
		long goalStartTime = goal.getStartTime();
		Calendar cal = Calendar.getInstance();
		
		for(TimelineItem item : items) {
			
			cal.setTimeInMillis(goalStartTime * 1000 + offsetMinute * 60 * 1000);
			long timestamp = cal.getTimeInMillis() / 1000;
			
			if(item.getTimestamp().equals(timestamp) && item.getItemType() == TimelineItemDataBase.TYPE_MILESTONE) {
				
				MilestoneItem milestone = (MilestoneItem) item.getData();
				MilestoneItemInfo milestoneInfo = milestone.getInfo();
				return milestone.getEventType().equals(TimelineItemDataBase.EVENT_TYPE_STREAK) && milestoneInfo.getStreakNumber().equals(streakNumber);
			}
		}

		return false;
	}
	
	private boolean hasPersonalBestMilestone(List<TimelineItem> items, Goal goal, int offsetMinute, int points, int extendedAmount) {
		
		long goalStartTime = goal.getStartTime();
		Calendar cal = Calendar.getInstance();
		
		for(TimelineItem item : items) {
			
			cal.setTimeInMillis(goalStartTime * 1000 + offsetMinute * 60 * 1000);
			long timestamp = cal.getTimeInMillis() / 1000;
			
			if(item.getTimestamp().equals(timestamp) && item.getItemType() == TimelineItemDataBase.TYPE_MILESTONE) {
				
				MilestoneItem milestone = (MilestoneItem) item.getData();
				MilestoneItemInfo milestoneInfo = milestone.getInfo();
				double diff = (milestoneInfo.getPoint() - points * 2.5) + (milestoneInfo.getExceededAmount() - extendedAmount * 2.5); 
				return milestone.getEventType().equals(TimelineItemDataBase.EVENT_TYPE_PERSONAL_BEST) && diff == 0;
			}
		}

		return false;
	}
	
	
}
