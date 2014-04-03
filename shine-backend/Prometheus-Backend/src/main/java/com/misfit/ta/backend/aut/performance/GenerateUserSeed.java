package com.misfit.ta.backend.aut.performance;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.sync.SyncFileData;
import com.misfit.ta.backend.data.sync.SyncLog;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.LifetimeDistanceItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.base.SeedThread;
import com.misfit.ta.common.MVPCommon;

public class GenerateUserSeed extends SeedThread {

	protected static Logger logger = Util.setupLogger(MVPApi.class);
	
	// static fields
	public static ResultLogger resultLogger = ResultLogger.getLogger("user_generate_seed_" + System.nanoTime());
	public static int numberOfUserFullyCreated = 0;
	public static int numberOfUserCreated = 0;
	
	public static long globalStartTime = 0;
	public static long globalEndTime = 0;
	
	
	// fields
	protected int numberOfGoal;
	protected int minimumActivityTile;
	protected int maximumActivityTile;
	protected int graphItemInterval;
	protected int numberOfSyncLog;
	protected boolean includeSyncBinary;
	
	
	// constructors	
	public GenerateUserSeed(int numberOfGoal, int minimumActivityTile, int maximumActivityTile,
			int graphItemInterval, int numberOfSyncLog, boolean includeSyncBinary) {
		
		this.numberOfGoal = numberOfGoal;
		this.minimumActivityTile = minimumActivityTile;
		this.maximumActivityTile = maximumActivityTile;
		this.numberOfSyncLog = numberOfSyncLog;
		this.includeSyncBinary = includeSyncBinary;
		this.graphItemInterval = graphItemInterval;
	}
	
	
	// methods	
	public static void createUserWithRandomData(String email, String password, int numberOfGoal, 
			int minimumSessionTileNumber, int maximumSessionTileNumber, int graphItemInterval, int syncLogNumber,
			boolean includeSyncBinary) {
		
		boolean hasError = false;
		if(globalStartTime <= 0)
			globalStartTime = System.currentTimeMillis() / 1000;
		
		
		// current timestamp
		long timestamp = System.currentTimeMillis() / 1000;
		
		
		// sign up new user
		BaseResult result = MVPApi.signUp(email, password);
		String token = ((AccountResult)result).token;
		hasError = (result.statusCode >= 300);
		numberOfUserCreated++;
		
		// create profile
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		profile.setEmail(email);
		profile.setAuthToken(token);
		
		result = MVPApi.createProfile(token, profile);
		hasError = (result.statusCode >= 300);
		
		
		// create pedometer
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		result = MVPApi.createPedometer(token, pedometer);
		hasError = (result.statusCode >= 300);
		
		
		// create statistics
		Statistics statistics = DataGenerator.generateRandomStatistics(timestamp, null);
		result = MVPApi.createStatistics(token, statistics);
		hasError = (result.statusCode >= 300);
		
		
		// create goals
		for(int i = 0; i < numberOfGoal; i++) {
			
			long goalTimestamp = timestamp - 3600 * 24 * i;
			Goal goal = DataGenerator.generateRandomGoal(goalTimestamp, null);
			result = MVPApi.createGoal(token, goal);
			
			hasError = (result.statusCode >= 300);
		}
		
		
		// create graph items
		List<GraphItem> graphItems = new ArrayList<GraphItem>();
		for(int i = 0; i < numberOfGoal; i++) {
			
			long goalTimestamp = timestamp - 3600 * 24 * i;
			long goalStartTime = MVPCommon.getDayStartEpoch(goalTimestamp);
			long goalEndTime = MVPCommon.getDayEndEpoch(goalTimestamp);
			
			for(long t = goalStartTime; t <= goalEndTime; t += graphItemInterval) {
				
				GraphItem graphItem = DataGenerator.generateRandomGraphItem(t, null);
				graphItems.add(graphItem);
			}
		}
		
		
		// create activity session timeline items
		List<TimelineItem> timelineItems = new ArrayList<TimelineItem>();
		for(int i = 0; i < numberOfGoal; i++) {
			
			long goalTimestamp = timestamp - 3600 * 24 * i;
			long goalStartTime = MVPCommon.getDayStartEpoch(goalTimestamp);
			long goalEndTime = MVPCommon.getDayEndEpoch(goalTimestamp);
			long numberOfItem = MVPCommon.randInt(minimumSessionTileNumber, maximumSessionTileNumber);
			if(numberOfItem <= 0)
				continue;
				
			long step = (goalEndTime - goalStartTime) / numberOfItem;
						
			for(long t = goalStartTime; t <= goalEndTime; t += step) {
				
				TimelineItem activityTile = DataGenerator.generateRandomActivitySessionTimelineItem(t, null);
				timelineItems.add(activityTile);
			}
		}
		
		
		// create milestone timeline items
		for(int i = numberOfGoal; i > 0; i--) {
			
			long goalTimestamp = timestamp - 3600 * 24 * i;
			long goalStartTime = MVPCommon.getDayStartEpoch(goalTimestamp);
			long goalEndTime = MVPCommon.getDayEndEpoch(goalTimestamp);
			
			// personal best
			if(MVPCommon.coin()) {
				
				long t = MVPCommon.randLong(goalStartTime + 3600, goalEndTime - 3600);
				TimelineItem personalBestItem = DataGenerator.generateRandomMilestoneItem(t, 
						TimelineItemDataBase.EVENT_TYPE_PERSONAL_BEST, null);
				
				timelineItems.add(personalBestItem);
			}
			
			// hit goal 100 
			if(MVPCommon.coin()) {
				
				long t = MVPCommon.randLong(goalStartTime + 3600, goalEndTime - 3600);
				TimelineItem goal100Item = DataGenerator.generateRandomMilestoneItem(t, 
						TimelineItemDataBase.EVENT_TYPE_100_GOAL, null);
				
				timelineItems.add(goal100Item);
				
				// streak
				if(MVPCommon.coin()) {
					
					t = MVPCommon.randLong(goalStartTime + 3600, goalEndTime - 3600);
					TimelineItem streakItem = DataGenerator.generateRandomMilestoneItem(t, 
							TimelineItemDataBase.EVENT_TYPE_STREAK, null);
					
					timelineItems.add(streakItem);
				}
				
				// hit goal 150
				if(MVPCommon.coin()) {
					
					t = MVPCommon.randLong(goalStartTime + 3600, goalEndTime - 3600);
					TimelineItem goal150Item = DataGenerator.generateRandomMilestoneItem(t, 
							TimelineItemDataBase.EVENT_TYPE_150_GOAL, null);
					
					timelineItems.add(goal150Item);
					
					// hit goal 200
					if(MVPCommon.coin()) {
						
						t = MVPCommon.randLong(goalStartTime + 3600, goalEndTime - 3600);
						TimelineItem goal200Item = DataGenerator.generateRandomMilestoneItem(t, 
								TimelineItemDataBase.EVENT_TYPE_200_GOAL, null);
						
						timelineItems.add(goal200Item);
					}
				}
			}
			
		}
		
		
		// create lifetime distance tiles
		long startTime = MVPCommon.getDayStartEpoch(timestamp - 3600 * 24 * 20);
		long t1 = MVPCommon.randLong(startTime, startTime + 3600 * 24 * 6);
		long t2 = MVPCommon.randLong(t1, t1 + 3600 * 24 * 6);
		long t3 = MVPCommon.randLong(t2, t2 + 3600 * 24 * 6);

		// 2 marathons
		if(MVPCommon.coin()) {

			TimelineItem item1 = DataGenerator.generateRandomLifetimeDistanceItem(t1, null);
			((LifetimeDistanceItem)(item1.getData())).setMilestoneType(0);

			timelineItems.add(item1);

			// 6 marathons
			if(MVPCommon.coin()) {

				TimelineItem item2 = DataGenerator.generateRandomLifetimeDistanceItem(t2, null);
				((LifetimeDistanceItem)(item2.getData())).setMilestoneType(1);

				timelineItems.add(item2);

				// 12 marathons
				if(MVPCommon.coin()) {

					TimelineItem item3 = DataGenerator.generateRandomLifetimeDistanceItem(t3, null);
					((LifetimeDistanceItem)(item3.getData())).setMilestoneType(2);

					timelineItems.add(item3);
				}
			}
		}
		
		
		// send requests
		ServiceResponse response = MVPApi.createGraphItems(token, graphItems);
		result = new BaseResult(response);
		hasError = (result.statusCode >= 300);
		
		response = MVPApi.createTimelineItems(token, timelineItems);
		result = new BaseResult(response);
		hasError = (result.statusCode >= 300);

		
		// create sync logs
		syncLogNumber = Math.min(syncLogNumber, 10);
		for(int i = numberOfGoal; i > 0; i--) {
			
			long goalTimestamp = timestamp - 3600 * 24 * i;
			long goalStartTime = MVPCommon.getDayStartEpoch(goalTimestamp);
			long goalEndTime = MVPCommon.getDayEndEpoch(goalTimestamp);
			
			long interval = (goalEndTime - goalStartTime) / syncLogNumber;
			int totalMinute =(int) (interval - 300) / 60;
			
			for(int j = 0; j < syncLogNumber; j++) {

				long syncLogTimestamp = goalStartTime + interval * j + 1;
				SyncLog syncLog = DataGenerator.generateRandomSyncLog(syncLogTimestamp, 1, totalMinute, null);
				
				// if don't include binary
				syncLog.setLog("");
				if(!includeSyncBinary) {
					for(SyncFileData file : syncLog.getData().getFileData()) {
						file.setRawData("");
					}
				}
				
				syncLog.setSerialNumberString(pedometer.getSerialNumberString());
				result = MVPApi.pushSyncLog(token, syncLog);
				
				hasError = (result.statusCode >= 300);
			}
		}
		
		if(hasError == false)
			numberOfUserFullyCreated++;
		
		globalEndTime = System.currentTimeMillis() / 1000;
		
		resultLogger.log(email + "\t" + 
				hasError + "\t" + 
				numberOfUserCreated + "\t" + 
				numberOfUserFullyCreated + "\t" +
				(globalEndTime - globalStartTime));
	}

	public static void printSummary() {
		
		logger.info("-----------------------------------------------");
		logger.info("Total user created: " + numberOfUserCreated);
		logger.info("Total user created without error: " + numberOfUserFullyCreated);
		logger.info("-----------------------------------------------");
	}
	
	
	// implements intefaces
	public void run() {
		
		long start = System.currentTimeMillis() / 1000;
		createUserWithRandomData(MVPApi.generateUniqueEmail(), "qwerty1", 
				numberOfGoal, minimumActivityTile, maximumActivityTile, graphItemInterval,
				numberOfSyncLog, includeSyncBinary);
		long end = System.currentTimeMillis() / 1000;
		
		System.out.println("Running time of this seed: " + (end - start));
	}
	
	public SeedThread duplicate() {
		
		return new GenerateUserSeed(numberOfGoal, minimumActivityTile, maximumActivityTile, 
				graphItemInterval, numberOfSyncLog, includeSyncBinary);
	}

}
