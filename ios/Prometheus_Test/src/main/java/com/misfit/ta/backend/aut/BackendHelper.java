package com.misfit.ta.backend.aut;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.statistics.Statistics;

public class BackendHelper {

	public static void unlink(String token) {
		
		MVPApi.unlinkDevice(token);
	}
	
	public static void unlink(String email, String password) {

		String token = MVPApi.signIn(email, password).token;
		unlink(token);
	}

	public static void link(String token, String serialNumber) {
		
		Long now = System.currentTimeMillis() / 1000;
		
		// create first
		String localId = "pedometer-" + System.nanoTime();
		Pedometer pedo = MVPApi.createPedometer(token, serialNumber, MVPApi.LATEST_FIRMWARE_VERSION_STRING, 
				now, null, now, localId, null, now);
		
		if(pedo.getLocalId() != null && pedo.getLocalId().equals(localId))
			return;
		
		// if server return another pedo, use update instead
		pedo = MVPApi.getPedometer(token);
		pedo.setFirmwareRevisionString(MVPApi.LATEST_FIRMWARE_VERSION_STRING);
		pedo.setSerialNumberString(serialNumber);
		pedo.setLocalId("pedometer-" + MVPApi.generateLocalId());
		pedo.setLastSyncedTime(now);
		pedo.setLinkedTime(now);
		pedo.setUpdatedAt(now);
		MVPApi.updatePedometer(token, pedo);
	}
	
	public static void link(String email, String password, String serialNumber) {

		
		String token = MVPApi.signIn(email, password).token;
		link(token, serialNumber);
	}

	
	public static void setLifetimeDistance(String token, double miles) {
		
		// create first
		Statistics statistics = DefaultValues.RandomStatistic();
		statistics.setLifetimeDistance(miles);
		statistics.setUpdatedAt(System.currentTimeMillis() / 1000);
		BaseResult result = MVPApi.createStatistics(token, statistics);

		// if existed, update instead
		if(result.isExisted()) {

			statistics = Statistics.fromResponse(result.response);
			statistics.setLifetimeDistance(miles);
			statistics.setUpdatedAt(System.currentTimeMillis() / 1000);
			MVPApi.updateStatistics(token, statistics);
		}
	}
	
	public static void setLifetimeDistance(String email, String password, double miles) {
		
		setLifetimeDistance(MVPApi.signIn(email, password).token, miles);
	}
	
	public static void setPersonalBest(String token, int points) {
		
		// create first
		Statistics statistics = DefaultValues.RandomStatistic();
		statistics.getPersonalRecords().getPersonalBestRecordsInPoint().setPoint((double)points * 2.5);
		statistics.getPersonalRecords().getPersonalBestRecordsInPoint().setTimestamp(System.currentTimeMillis() / 1000);
		statistics.setUpdatedAt(System.currentTimeMillis() / 1000);
		BaseResult result = MVPApi.createStatistics(token, statistics);

		// if existed, update instead
		if(result.isExisted()) {

			statistics = Statistics.fromResponse(result.response);
			statistics.getPersonalRecords().getPersonalBestRecordsInPoint().setPoint((double)points * 2.5);
			statistics.getPersonalRecords().getPersonalBestRecordsInPoint().setTimestamp(System.currentTimeMillis() / 1000);
			statistics.setUpdatedAt(System.currentTimeMillis() / 1000);
			MVPApi.updateStatistics(token, statistics);
		}
	}
	
	public static void setPersonalBest(String email, String password, int points) {

		String token = MVPApi.signIn(email, password).token;
		setPersonalBest(token, points);
	}
	
	
	public static void createGoalInPast(String token, int diffFromToday) {
		
		long timestamp = System.currentTimeMillis() / 1000 - 3600 * 24 * diffFromToday;
		Goal goal = Goal.getDefaultGoal(timestamp);

		MVPApi.createGoal(token, goal);
	}
	
	public static void completeGoalInPast(String token, int diffFromToday) {
		
		long timestamp = System.currentTimeMillis() / 1000 - 3600 * 24 * diffFromToday;
		Goal goal = Goal.getDefaultGoal(timestamp);
		goal.setValue(2500d);
		goal.getProgressData().setPoints(2500d);
		goal.getProgressData().setDistanceMiles(2d);
		goal.getProgressData().setSteps(4000);
		goal.getProgressData().setSeconds(3600);
		MVPApi.createGoal(token, goal);
	}
	
	public static void completeGoalInPast(String email, String password, int diffFromToday) {

		String token = MVPApi.signIn(email, password).token;
		completeGoalInPast(token, diffFromToday);
	}
	
	public static void clearLatestGoal(String token) {
		
		Goal[] goals = MVPApi.searchGoal(token, 0, Integer.MAX_VALUE, 0).goals;
		if(goals == null || goals.length == 0)
			return;
		
		Goal blank = goals[0];
		blank.getProgressData().setDistanceMiles(0d);
		blank.getProgressData().setPoints(0d);
		blank.getProgressData().setFullBmrCalorie(0);
		blank.getProgressData().setSteps(0);
		blank.getProgressData().setSeconds(0);
		
		MVPApi.updateGoal(token, blank);
	}
	
	public static void clearLatestGoal(String email, String password) {
		
		String token = MVPApi.signIn(email, password).token;
		clearLatestGoal(token);
	}
	
}
