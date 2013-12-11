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

public class BackendServerCalculationTest extends BackendAutomation {


	// test cases
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "servercalculation" })
	public void CalculationWithPredefineStory() throws FileNotFoundException {

		// sign up new account
		String email = MVPApi.generateUniqueEmail();
//		String email = "sc004@a.a";
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
		Verify.verifyEquals(graphitems2.size(), 1440 / 33, "Number of graph items");
		Verify.verifyEquals(graphitems1.size(), 1440 / 33, "Number of graph items");
		Verify.verifyEquals(graphitems0.size(), 1440 / 33, "Number of graph items");
		
		
		// ===== VERIFY TIMELINE ITEMS
		// number of session tiles are correct
		Verify.verifyEquals(getNumberOfSessionTiles(timelineitems2), 4, "Goals[2] has 4 session tiles");
		Verify.verifyEquals(getNumberOfSessionTiles(timelineitems1), 5, "Goals[1] has 5 session tiles");
		Verify.verifyEquals(getNumberOfSessionTiles(timelineitems0), 5, "Goals[0] has 5 session tiles");
		
		
		// session tiles are correct
		Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 7 * 60, 60, 600), "Goal[2] has session tile at 7:00 - 600 pts");
		Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 10 * 60, 50, 500), "Goal[2] has session tile at 10:00 - 500 pts");
		Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 13 * 60, 30, 300), "Goal[2] has session tile at 13:00 - 300 pts");
		Verify.verifyTrue(hasSessionTileWithData(timelineitems2, goals[2], 17 * 60, 40, 400), "Goal[2] has session tile at 17:00 - 400 pts");
		
		Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 7 * 60, 60, 600), "Goal[1] has session tile at 7:00 - 600 pts");
		Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 10 * 60, 50, 500), "Goal[1] has session tile at 10:00 - 500 pts");
		Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 13 * 60, 30, 300), "Goal[1] has session tile at 13:00 - 300 pts");
		Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 17 * 60, 40, 400), "Goal[1] has session tile at 17:00 - 400 pts");
		Verify.verifyTrue(hasSessionTileWithData(timelineitems1, goals[1], 20 * 60, 40, 400), "Goal[1] has session tile at 20:00 - 400 pts");
		
		Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 7 * 60, 60, 600), "Goal[0] has session tile at 7:00 - 600 pts");
		Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 10 * 60, 50, 500), "Goal[0] has session tile at 10:00 - 500 pts");
		Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 13 * 60, 30, 300), "Goal[0] has session tile at 13:00 - 300 pts");
		Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 17 * 60, 40, 400), "Goal[0] has session tile at 17:00 - 400 pts");
		Verify.verifyTrue(hasSessionTileWithData(timelineitems0, goals[0], 21 * 60, 60, 600), "Goal[0] has session tile at 21:00 - 600 pts");
		
		
		// daily goal milestone tiles are correct
		Verify.verifyTrue(hasDailyGoalMilestone(timelineitems2, goals[2], 10 * 60 + 40, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[2] has 100% tile");
		Verify.verifyTrue(hasDailyGoalMilestone(timelineitems2, goals[2], 17 * 60 + 10, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[2] has 150% tile");
		
		Verify.verifyTrue(hasDailyGoalMilestone(timelineitems1, goals[1], 10 * 60 + 40, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[1] has 100% tile");
		Verify.verifyTrue(hasDailyGoalMilestone(timelineitems1, goals[1], 17 * 60 + 10, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[1] has 150% tile");
		Verify.verifyTrue(hasDailyGoalMilestone(timelineitems1, goals[1], 20 * 60 + 20, TimelineItemDataBase.EVENT_TYPE_200_GOAL, 2000), "Goals[1] has 200% tile");
		
		Verify.verifyTrue(hasDailyGoalMilestone(timelineitems0, goals[0], 10 * 60 + 40, TimelineItemDataBase.EVENT_TYPE_100_GOAL, 1000), "Goals[0] has 100% tile");
		Verify.verifyTrue(hasDailyGoalMilestone(timelineitems0, goals[0], 17 * 60 + 10, TimelineItemDataBase.EVENT_TYPE_150_GOAL, 1500), "Goals[0] has 150% tile");
		Verify.verifyTrue(hasDailyGoalMilestone(timelineitems0, goals[0], 21 * 60 + 20, TimelineItemDataBase.EVENT_TYPE_200_GOAL, 2000), "Goals[0] has 200% tile");
		
		
		// personal best and streak tiles are correct
		Verify.verifyTrue(hasPersonalBestMilestone(timelineitems1, goals[1], 20 * 60 + 40, 2200, 400), "Goals[1] - 2200pts extended Goal[2] by 400pts");
		Verify.verifyTrue(hasPersonalBestMilestone(timelineitems0, goals[0], 21 * 60 + 60, 2400, 400), "Goals[0] - 2400pts extended Goal[1] by 200pts");
		Verify.verifyTrue(hasStreakMilestone(timelineitems0, goals[0], 10 * 60 + 40, 3), "Goals[0] has 3-day streak tile");
	
		
		// ===== VERIFY GOAL PROGRESS
		double miles2 = MVPCalculator.calculateMiles(18000, 180, profile.getHeight());
		double miles1 = MVPCalculator.calculateMiles(22000, 220, profile.getHeight());
		double miles0 = MVPCalculator.calculateMiles(24000, 240, profile.getHeight());
		
		
		Verify.verifyEquals(goal2.getProgressData().getPoints(), 1800, "Goals[2] progress point");
		Verify.verifyEquals(goal1.getProgressData().getPoints(), 2200, "Goals[1] progress point");
		Verify.verifyEquals(goal0.getProgressData().getPoints(), 2400, "Goals[0] progress point");
		
		Verify.verifyEquals(goal2.getProgressData().getSteps(), 18000, "Goals[2] progress steps");
		Verify.verifyEquals(goal1.getProgressData().getSteps(), 22000, "Goals[1] progress steps");
		Verify.verifyEquals(goal0.getProgressData().getSteps(), 24000, "Goals[0] progress steps");
		
		Verify.verifyEquals(goal2.getProgressData().getSeconds(), 180 * 60, "Goals[2] progress duration in seconds");
		Verify.verifyEquals(goal1.getProgressData().getSeconds(), 220 * 60, "Goals[1] progress duration in seconds");
		Verify.verifyEquals(goal0.getProgressData().getSeconds(), 240 * 60, "Goals[0] progress duration in seconds");
		
		Verify.verifyEquals(goal2.getProgressData().getDistanceMiles(), miles2, "Goals[2] progress distance in miles");
		Verify.verifyEquals(goal1.getProgressData().getDistanceMiles(), miles1, "Goals[1] progress distance in miles");
		Verify.verifyEquals(goal0.getProgressData().getDistanceMiles(), miles0, "Goals[0] progress distance in miles");
		
		
		// ===== VERIFY STATISTICS
		long personalBestTimestamp = goals[0].getStartTime() + (21 * 60 + 60) * 60;
		Verify.verifyEquals(statistics.getLifetimeDistance(), miles0 + miles1 + miles2, "Lifetime distance");
		Verify.verifyEquals(statistics.getBestStreak(), 3, "Best streak number");
		Verify.verifyEquals(statistics.getTotalGoalHit(), 3, "Total goal hit number");
		Verify.verifyEquals(statistics.getPersonalRecords().getPersonalBestRecordsInPoint().getPoint(), 2400, "Personal best points");
		Verify.verifyEquals(statistics.getPersonalRecords().getPersonalBestRecordsInPoint().getTimestamp(), personalBestTimestamp, "Personal best points");
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
				return session.getPoint().equals(points) && session.getDuration().equals(minutes * 60);
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
				return milestone.getEventType().equals(eventType) && milestoneInfo.getPoint().equals(points);
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
				return milestone.getEventType().equals(TimelineItemDataBase.EVENT_TYPE_PERSONAL_BEST) && 
						milestoneInfo.getExceededAmount().equals(extendedAmount) && 
						milestoneInfo.getPoint().equals(points);
			}
		}

		return false;
	}
	
	
}
