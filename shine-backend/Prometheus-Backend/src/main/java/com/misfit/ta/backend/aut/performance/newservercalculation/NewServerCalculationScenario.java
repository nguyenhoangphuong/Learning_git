package com.misfit.ta.backend.aut.performance.newservercalculation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.aut.correctness.servercalculation.BackendServerCalculationBase;
import com.misfit.ta.backend.aut.correctness.social.SocialTestAutomationBase;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.servercalculation.ServerCalculationCursor;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.ShortcutsTyper;

public class NewServerCalculationScenario extends BackendServerCalculationBase{
	private long delayTime = 60000;
	protected static Logger logger = Util.setupLogger(NewServerCalculationScenario.class);
	public static ResultLogger resultLogger = ResultLogger.getLogger("new_server_calculation_scenario" + System.nanoTime());
	public void runNewServerCalculationIntegrationTest() {
		String email = MVPApi.generateUniqueEmail();
		runNewServerCalculationIntgerationTest(email);
		
//		resultLogger.log("Waiting " + delayTime + " miliseconds for email " + email);
//		logger.info("Waiting " + delayTime + " miliseconds");
//		ShortcutsTyper.delayTime(delayTime);
//		long runningTime = -1;
//		BaseResult result = MVPApi.getCursors(token);
//		if (result.statusCode == 200) {
//			ServerCalculationCursor cursor = ServerCalculationCursor.fromResponse(result.response);
//			long endTime = cursor.getUpdatedAt();
//			resultLogger.log("End time for email " + email + ": " + endTime);
//			logger.info("End time for email " + email + ": " + endTime);
//			runningTime = endTime - startTime;
//		}
//		resultLogger.log("Running time of server calculation for email " + email + ": " + runningTime);
//		logger.info("Running time of server calculation for email " + email + ": " + runningTime);
//		return runningTime;
//		return email;
	}
	
	public long runNewServerCalculationIntgerationTest(String email) {
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(email, "qqqqqq").token;
		String userId = MVPApi.getUserId(token);


		// create profile / pedometer / statistics
		resultLogger.log("Create profile/pedometer/statistics for email " + email);
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);


		// create goals for 4 days
		resultLogger.log("Create goals for email " + email);
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

		Goal goal0 = MVPApi.getGoal(token, goals[0].getServerId()).goals[0];
		long startTime = goal0.getUpdatedAt();
		long time = System.currentTimeMillis() / 1000;
		logger.info("System timestamp for email " + email + ": " + time);
		logger.info("Start time for email " + email + ": " + startTime);
		resultLogger.log("Push sync data for email " + email);
		resultLogger.log("System timestamp for email " + email + ": " + time);
		resultLogger.log("Start time for email " + email + ": " + startTime);
		// push data to server
		List<String> dataStrings = new ArrayList<String>();
		dataStrings.add(MVPApi.getRawDataAsString(goals[3].getStartTime(), goals[3].getTimeZoneOffsetInSeconds() / 60, "0101", "18", data3).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(goals[2].getStartTime(), goals[2].getTimeZoneOffsetInSeconds() / 60, "0102", "18", data2).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(goals[1].getStartTime(), goals[1].getTimeZoneOffsetInSeconds() / 60, "0103", "18", data1).rawData);
		dataStrings.add(MVPApi.getRawDataAsString(goals[0].getStartTime(), goals[0].getTimeZoneOffsetInSeconds() / 60, "0104", "18", data0).rawData);
		pushSyncData(timestamp, userId, pedometer.getSerialNumberString(), dataStrings);
		return startTime;
	}
	
	
	public void runNewServerCalculationGoalCreationTest(String email) {
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(email, "qqqqqq").token;
		String userId = MVPApi.getUserId(token);


		// create profile / pedometer / statistics
		resultLogger.log("Create profile/pedometer/statistics for email " + email);
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);

		
		// create goals for 4 days
		resultLogger.log("Create goal for email " + email);

		long goalTimestamp = timestamp - 3 * 3600 * 24;
		Goal goal = Goal.getDefaultGoal(goalTimestamp);
		GoalsResult result = MVPApi.createGoal(token, goal);
		goal.setServerId(result.goals[0].getServerId());
		goal.setUpdatedAt(result.goals[0].getUpdatedAt());
		
		long[] startTimes = new long[4];
		for(int i = 0; i < startTimes.length; i++) {
			long gTimestamp = timestamp - i * 3600 * 24;
			long startTime = MVPCommon.getDayStartEpoch(gTimestamp);
			startTimes[i] = startTime;
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
				// - 6 activity session tiles
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

				TimeZone tz = TimeZone.getDefault();
				Date now = new Date();
				int offsetFromUtc = tz.getOffset(now.getTime()) / 1000;
				
				// push data to server
				List<String> dataStrings = new ArrayList<String>();
				dataStrings.add(MVPApi.getRawDataAsString(startTimes[3], offsetFromUtc / 60, "0101", "18", data3).rawData);
				dataStrings.add(MVPApi.getRawDataAsString(startTimes[2], offsetFromUtc / 60, "0104", "18", data2).rawData);
				dataStrings.add(MVPApi.getRawDataAsString(startTimes[1], offsetFromUtc / 60, "0103", "18", data1).rawData);
				dataStrings.add(MVPApi.getRawDataAsString(startTimes[0], offsetFromUtc / 60, "0104", "18", data0).rawData);
				pushSyncData(timestamp, userId, pedometer.getSerialNumberString(), dataStrings);
	}
	
}