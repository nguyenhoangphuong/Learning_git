package com.misfit.ta.backend.aut.performance.backendapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.aut.correctness.servercalculation.BackendServerCalculationBase;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.Statistics;
public class BackendPushDuplicatedRawData extends BackendServerCalculationBase{
	protected int NUMBER_OF_USERS = 2;
	protected int NUMBER_OF_THREADS = 3;
	public static ResultLogger resultLogger = ResultLogger.getLogger("data_collector_" + System.nanoTime());
	Logger logger = Util.setupLogger(BackendPushDuplicatedRawDataThread.class);
	
	public BackendPushDuplicatedRawData() {
	}

	public void doPushRawData() {
		int userCount = 0;
		String email = MVPApi.generateUniqueEmail();
		String token = MVPApi.signUp(email, "qwerty1").token;
		
		long timestamp = System.currentTimeMillis() / 1000;
		String userId = MVPApi.getUserId(token);


		// create profile / pedometer / statistics
		resultLogger.log("Create profile/pedometer/statistics for email " + email);
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = Statistics.getDefaultStatistics();

		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);		
		
		resultLogger.log("Create goals for email " + email);
		Goal[] goals = new Goal[1];
		for(int i = 0; i < goals.length; i++) {

			long goalTimestamp = timestamp - i * 3600 * 24;
			Goal goal = Goal.getDefaultGoal(goalTimestamp);
			GoalsResult result = MVPApi.createGoal(token, goal);
			goal.setServerId(result.goals[0].getServerId());
			goal.setUpdatedAt(result.goals[0].getUpdatedAt());

			goals[i] = goal;
		}
		logger.info("------Push data for user " + email);
		generateAndPushRawData(email, token, goals, userId, timestamp, pedometer);
		Collection<Future<?>> futures = new LinkedList<Future<?>>();

		ExecutorService executor = Executors
				.newFixedThreadPool(NUMBER_OF_THREADS);
		while (userCount < NUMBER_OF_USERS) {
			
			email = MVPApi.generateUniqueEmail();
			token = MVPApi.signUp(email, "qwerty1").token;
			
			timestamp = System.currentTimeMillis() / 1000;
		userId = MVPApi.getUserId(token);


			// create profile / pedometer / statistics
			resultLogger.log("Create profile/pedometer/statistics for email " + email);
			profile = DataGenerator.generateRandomProfile(timestamp, null);
			pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
			statistics = Statistics.getDefaultStatistics();

			MVPApi.createProfile(token, profile);
			MVPApi.createPedometer(token, pedometer);
			MVPApi.createStatistics(token, statistics);	
			logger.info("**** Create user #" + userCount + " " + email );
			
			resultLogger.log("Create goals for email " + email);
			goals = new Goal[1];
			for(int i = 0; i < goals.length; i++) {

				long goalTimestamp = timestamp - i * 3600 * 24;
				Goal goal = Goal.getDefaultGoal(goalTimestamp);
				GoalsResult result = MVPApi.createGoal(token, goal);
				goal.setServerId(result.goals[0].getServerId());
				goal.setUpdatedAt(result.goals[0].getUpdatedAt());

				goals[i] = goal;
			}
			for (int threads = 0; threads < NUMBER_OF_THREADS; threads++) {
				BackendPushDuplicatedRawDataThread thread = new BackendPushDuplicatedRawDataThread(email, token, goals, userId, timestamp, pedometer);
				futures.add(executor.submit(thread));
				
			}
			userCount++;

		}
		executor.shutdown();

		for (Future<?> future : futures) {
			try {
				future.get();
			} catch (Exception e) {
			}
		}
	}

	public String generateAndPushRawData(String email, String token, Goal[] goals, String userId, long timestamp, Pedometer pedometer) {
		
		
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
		goal0.setLocalId("goalRawData-" + MVPApi.generateLocalId());
		long startTime = goal0.getUpdatedAt();
		long time = System.currentTimeMillis() / 1000;
		logger.info("System timestamp for email " + email + ": " + time);
		logger.info("Start time for email " + email + ": " + startTime);
		resultLogger.log("Push sync data for email " + email);
		resultLogger.log("System timestamp for email " + email + ": " + time);
		resultLogger.log("Start time for email " + email + ": " + startTime);
		// push data to server
		MVPApi.pushRawData(token, goals[0].getServerId(), data0, 0);

		return token;
	}

	public static void main(String[] args) {
		BackendPushDuplicatedRawData pushDuplicatedRawDataAction = new BackendPushDuplicatedRawData();
		pushDuplicatedRawDataAction.doPushRawData();
	}
}
