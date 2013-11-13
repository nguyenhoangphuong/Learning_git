package com.misfit.ta.backend.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.ProgressData;
import com.misfit.ta.backend.data.goal.TripleTapData;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.DisplayUnit;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.PersonalRecord;
import com.misfit.ta.backend.data.statistics.Record;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.sync.SyncData;
import com.misfit.ta.backend.data.sync.SyncFileData;
import com.misfit.ta.backend.data.sync.SyncLog;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.ActivitySessionItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.ActivitySessionItemTypeChangeRecord;
import com.misfit.ta.backend.data.timeline.timelineitemdata.LifetimeDistanceItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.MilestoneItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.MilestoneItemInfo;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.utils.TextTool;

public class DataGenerator {
	
	
	// helpers
	protected static int randInt(int includeFrom, int includeTo) {
		
		Random r = new Random();
		return r.nextInt(includeTo - includeFrom + 1) + includeFrom;
	}
	
	protected static long randLong(long includeFrom, long includeTo) {
		
		Random r = new Random();
		return (long)r.nextInt((int)includeTo - (int)includeFrom + 1) + includeFrom;
	}
	
	protected static boolean coin() {
		
		return System.nanoTime() % 2 == 0;
	}

	protected static String toLittleEndianString(Integer number) {
		
		Integer reversed = Integer.reverseBytes(number);
		return String.format("%08X", reversed);
	}
	
	protected static String toLittleEndianString(Long number) {
		
		Long reversed = Long.reverseBytes(number);
		return String.format("%016X", reversed).substring(0, 8);
	}
	
	protected static String toLittleEndianString(List<Byte> bytes, boolean reverse) {
		
		if(reverse)
			Collections.reverse(bytes);
		
		String r = "";
		for(Byte b : bytes)
			r += String.format("%02X", b);
		
		return r;
	}
	
	protected static List<Byte> hexStringToByteArray(String s) {
		
	    int len = s.length();
	    List<Byte> bytes = new ArrayList<Byte>();
	    
	    for (int i = 0; i < len; i += 2) {
	        Byte b = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	        bytes.add(b);
	    }
	    return bytes;
	}
	
	
	// generators
	public static ProfileData generateRandomProfile(long timestamp, Map<String, Object> options) {
		
		ProfileData p = new ProfileData();

		p.setLocalId("profiles-" + MVPApi.generateLocalId());
		p.setUpdatedAt((long) (System.currentTimeMillis() / 1000));

		p.setWeight(randInt(1200, 1800) / 10d);
		p.setHeight(randInt(58, 70) * 1d);
		p.setGender(randInt(0, 1));
		p.setDateOfBirth((long) randInt(685000000, 686000000));
		p.setName(TextTool.getRandomString(7, 10));

		p.setLatestVersion("0.21.1.4228");
		p.setDisplayedUnits(new DisplayUnit());

		return p;
	}
	
	public static Goal generateRandomGoal(long timestamp, Map<String, Object> options) {
		
		ProgressData progressData = new ProgressData();
		progressData.setFullBmrCalorie(randInt(1200, 1600));
		progressData.setCalorie(randInt(15000, 25000) * 0.1d);
		progressData.setDistanceMiles(randInt(10, 50) * 0.1d);
		progressData.setSteps(randInt(5000, 20000));
		progressData.setPoints(randInt(500, 3000) * 2.5);
		progressData.setSeconds(randInt(4000, 10000));
		
		List<TripleTapData> tripleTaps = new ArrayList<TripleTapData>();
		tripleTaps.add(new TripleTapData(timestamp, MVPEnums.ACTIVITY_SLEEPING));
		Goal g = new Goal();

		g.setLocalId("goal-" + MVPApi.generateLocalId());
		g.setUpdatedAt(timestamp);

		g.setValue(randInt(10, 30) * 100 * 2.5);
		g.setStartTime(MVPApi.getDayStartEpoch(timestamp));
		g.setEndTime(MVPApi.getDayEndEpoch(timestamp));
		g.setTimeZoneOffsetInSeconds(7 * 3600);
		g.setProgressData(progressData);
		g.setTripleTapTypeChanges(tripleTaps);

		return g;
	}

	public static Pedometer generateRandomPedometer(long timestamp, Map<String, Object> options) {
			
		Pedometer item = new Pedometer();
		item.setLocalId("pedometer-" + MVPApi.generateLocalId());
		item.setBatteryLevel(randInt(20, 100));
		item.setBookmarkState(randInt(0, 3));
		item.setClockState(randInt(0, 3));
		item.setFirmwareRevisionString(MVPApi.LATEST_FIRMWARE_VERSION_STRING);
		item.setLastSyncedTime(timestamp);
		item.setLinkedTime(timestamp);
		item.setSerialNumberString(TextTool.getRandomString(10, 10));
		item.setUnlinkedTime(null);
		item.setUpdatedAt(timestamp);
		
		return item;
	}

	public static Statistics generateRandomStatistics(long timestamp, Map<String, Object> options) {
		
		PersonalRecord personalRecords = new PersonalRecord();
		personalRecords.setPersonalBestRecordsInPoint(new Record());
		personalRecords.getPersonalBestRecordsInPoint().setPoint(randInt(500, 6000) * 2.5);
		personalRecords.getPersonalBestRecordsInPoint().setTimestamp(timestamp);
		
		if(coin()) {
			personalRecords.setSwimming(new Record());
			personalRecords.getSwimming().setPoint(randInt(500, 6000) * 2.5);
			personalRecords.getSwimming().setTimestamp(timestamp);
		}
		
		if(coin()) {
			personalRecords.setBasketball(new Record());
			personalRecords.getBasketball().setPoint(randInt(500, 6000) * 2.5);
			personalRecords.getBasketball().setTimestamp(timestamp);
		}
		
		if(coin()) {
			personalRecords.setCycling(new Record());
			personalRecords.getCycling().setPoint(randInt(500, 6000) * 2.5);
			personalRecords.getCycling().setTimestamp(timestamp);
		}
		
		if(coin()) {
			personalRecords.setSoccer(new Record());
			personalRecords.getSoccer().setPoint(randInt(500, 6000) * 2.5);
			personalRecords.getSoccer().setTimestamp(timestamp);
		}
		
		if(coin()) {
			personalRecords.setTennis(new Record());
			personalRecords.getTennis().setPoint(randInt(500, 6000) * 2.5);
			personalRecords.getTennis().setTimestamp(timestamp);
		}

		Statistics item = new Statistics();
		item.setLocalId("statistics-" + MVPApi.generateLocalId());
		item.setUpdatedAt(timestamp);
		item.setPersonalRecords(personalRecords);
		item.setBestStreak(randInt(6, 25));
		item.setTotalGoalHit(randInt(10, 30));
		item.setLifetimeDistance(randInt(100, 1000) * 0.1d);

		return item;
	}

	public static TimelineItem generateRandomActivitySessionTimelineItem(long timestamp, Map<String, Object> options) {
		
		int[] activityTypes = new int[] { 2, 3, 4, 5, 6 };
		
		ActivitySessionItem data = new ActivitySessionItem();
		data.setActivityType(activityTypes[randInt(0, activityTypes.length - 1)]);
		data.setCalories(randInt(200, 1000) * 0.1d);
		data.setDistance(randInt(10, 200) * 0.1d);
		data.setDuration(randInt(300, 1500));
		data.setIsBestRecord(false);
		data.setSteps(randInt(120, 250) * data.getDuration() / 60);
		data.setTypeChanges(new ActivitySessionItemTypeChangeRecord[0]);
		data.setRawPoint((int)Math.floor(MVPCalculator.calculatePoint(data.getSteps(), (int)(data.getDuration() / 60), data.getActivityType())));
		data.setPoint(data.getRawPoint());
		
		TimelineItem item = new TimelineItem();
		item.setLocalId("timelineitem-" + MVPApi.generateLocalId());
		item.setUpdatedAt(timestamp);
		item.setTimestamp(timestamp);
		item.setItemType(TimelineItemDataBase.TYPE_SESSION);
		item.setData(data);

		return item;
	}
	
	public static TimelineItem generateRandomMilestoneItem(long timestamp, int event_type, Map<String, Object> options) {
		
		MilestoneItemInfo info = new MilestoneItemInfo();
		
		switch (event_type) {
		
			case TimelineItemDataBase.EVENT_TYPE_100_GOAL:
			{
				info.setPoint(randInt(500, 2500));
				break;
			}
			
			case TimelineItemDataBase.EVENT_TYPE_150_GOAL:
			{
				info.setPoint((int)(randInt(500, 2500) * 1.5));
				break;
			}
			
			case TimelineItemDataBase.EVENT_TYPE_200_GOAL:
			{
				info.setPoint(randInt(500, 2500) * 2);
				break;
			}
			
			case TimelineItemDataBase.EVENT_TYPE_PERSONAL_BEST:
			{
				info.setPoint(randInt(2500, 5000));
				info.setExceededAmount(info.getPoint() / randInt(2, 3));
				break;
			}
			
			case TimelineItemDataBase.EVENT_TYPE_STREAK:
			{
				info.setStreakNumber(randInt(3, 30));
				break;
			}
		}
		
		MilestoneItem data = new MilestoneItem();
		data.setEventType(event_type);
		data.setInfo(info);
		
		TimelineItem item = new TimelineItem();
		item.setLocalId("timelineitem-" + MVPApi.generateLocalId());
		item.setUpdatedAt(timestamp);
		item.setTimestamp(timestamp);
		item.setItemType(TimelineItemDataBase.TYPE_MILESTONE);
		item.setData(data);

		return item;
	}	

	public static TimelineItem generateRandomLifetimeDistanceItem(long timestamp, Map<String, Object> options) {
		
		LifetimeDistanceItem data = new LifetimeDistanceItem();
		data.setMilestoneType(randInt(0, 2));
		data.setUnitSystem(randInt(0, 1));
		
		TimelineItem item = new TimelineItem();
		item.setLocalId("timelineitem-" + MVPApi.generateLocalId());
		item.setUpdatedAt(timestamp);
		item.setTimestamp(timestamp);
		item.setItemType(TimelineItemDataBase.TYPE_LIFETIME_DISTANCE);
		item.setData(data);

		return item;
	}

	public static GraphItem generateRandomGraphItem(long timestamp, Map<String, Object> options) {
		
		GraphItem item = new GraphItem();

		item.setLocalId("graphitem-" + MVPApi.generateLocalId());
		item.setUpdatedAt(timestamp);
		item.setTimestamp(timestamp);
		item.setTotalValue(0d);
		item.setAverageValue(randInt(10, 70) / 60d);

		return item;
	}

	public static SyncFileData generateRandomSyncFileData(String fileHandle, long timestamp, int totalMinute) {
		
		int fileSize = 16 + 4 + totalMinute * 2;
		totalMinute = Math.min(totalMinute, 1014);
		String rawData = "";
		
		
		// prepare rawData's header: fileHandle + fileType + length 
		// 		+ timestamp + timestampMiliseconds + timezoneOffsetInMinutes
		rawData = fileHandle + "0011" + toLittleEndianString(fileSize) 
				+ toLittleEndianString(timestamp) + "0000" + "0000";
		
		List<Byte> headerBytes = hexStringToByteArray(rawData);
		
		
		// minute by minute data
		List<Byte> bytes = new ArrayList<Byte>();
		for(int i = 0; i < totalMinute; i++) {
			
			Integer steps = randInt(0, 190);
			Integer point = (int)Math.floor(steps * randInt(25, 30) * 0.01d);
			
			bytes.add(steps.byteValue());
			bytes.add(point.byteValue());
		}
		
		rawData += toLittleEndianString(bytes, false);
		
		
		// add 4 bytes crc
		byte[] byteArr = new byte[headerBytes.size() + bytes.size()];
		int index = 0;
		
		for(Byte b : headerBytes)
			byteArr[index++] = b;
		
		for(Byte b : bytes)
			byteArr[index++] = b;	
		
		Checksum checksum = new CRC32();
		checksum.update(byteArr, 0, byteArr.length);
		long checksumValue = checksum.getValue();
		
		rawData += toLittleEndianString(checksumValue);
		
		
		// prepare sync data file
		SyncFileData file = new SyncFileData();
		file.setFileHandle("0x" + fileHandle);
		file.setFileType("0x0011");
		file.setFileSize(fileSize);
		file.setFileTimestamp(timestamp);
		file.setTimestampDifference(0);
		file.setRawData(rawData);
		
		return file;
	}
	
	public static SyncLog generateRandomSyncLog(long timestamp, int numberOfFile, int totalMinuteEachFile, Map<String, Object> options) {
		
		String[] iosVersions = new String[] {"6.1.2", "6.1.3", "6.1.4", "7.0", "7.0.1", "7.0.2", "7.0.3"};
		String[] iDevices = new String[] {"iPod5,1", "iPhone5,1", "iPhone5,2", "iPhone5,3", "iPhone5,4", "iPhone6,1", "iPhone6,2"};
		
		List<SyncFileData> fileData = new ArrayList<SyncFileData>();
		for(int i = 0; i < numberOfFile; i++) {
			
			Short fileHandleInNumber = (short)(257 + i);
			String fileHandle = String.format("%04X", fileHandleInNumber);
			long fileTimestamp = timestamp - totalMinuteEachFile * 60 * (i + 1);
			
			SyncFileData file = generateRandomSyncFileData(fileHandle, fileTimestamp, totalMinuteEachFile);
			fileData.add(file);
		}

		SyncData data = new SyncData();
		data.setSyncMode(3);
		data.setAppVersion("0.21.1.4237");
		data.setDeviceInfo(iDevices[randInt(0, iDevices.length - 1)]);
		data.setIosVersion(iosVersions[randInt(0, iosVersions.length - 1)]);
		data.setHardwareLog("");
		data.setFailureLog("");
		data.setFailureReason(0);
		data.setFileData(fileData);

		SyncLog syncLog = new SyncLog();
		syncLog.setStartTime(timestamp);
		syncLog.setEndTime(timestamp + 20);
		syncLog.setFirmwareRevisionString(MVPApi.LATEST_FIRMWARE_VERSION_STRING);
		syncLog.setSerialNumberString(TextTool.getRandomString(10, 10));
		syncLog.setLog("AUTO GENRATED SYNCLOG");
		syncLog.setIsSuccessful(1);
		syncLog.setData(data);

		return syncLog;
	}
	
	
	// create user with data
	public static void createUserWithRandomData(String email, String password, int numberOfGoal, 
			int minimumSessionTileNumber, int maximumSessionTileNumber, int syncLogNumber) {
		
		// current timestamp
		long timestamp = System.currentTimeMillis() / 1000;
		
		
		// sign up new user
		String token = MVPApi.signUp(email, password).token;
		
		
		// create profile
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		profile.setEmail(email);
		profile.setAuthToken(token);
		MVPApi.createProfile(token, profile);
		
		
		// create pedometer
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		MVPApi.createPedometer(token, pedometer);
		
		
		// create statistics
		Statistics statistics = DataGenerator.generateRandomStatistics(timestamp, null);
		MVPApi.createStatistics(token, statistics);
		
		
		// create goals
		for(int i = 0; i < numberOfGoal; i++) {
			
			long goalTimestamp = timestamp - 3600 * 24 * i;
			Goal goal = DataGenerator.generateRandomGoal(goalTimestamp, null);
			MVPApi.createGoal(token, goal);
		}
		
		
		// create graph items
		List<GraphItem> graphItems = new ArrayList<GraphItem>();
		for(int i = 0; i < numberOfGoal; i++) {
			
			long goalTimestamp = timestamp - 3600 * 24 * i;
			long goalStartTime = MVPApi.getDayStartEpoch(goalTimestamp);
			long goalEndTime = MVPApi.getDayEndEpoch(goalTimestamp);
			
			for(long t = goalStartTime; t <= goalEndTime; t += 2020) {
				
				GraphItem graphItem = DataGenerator.generateRandomGraphItem(t, null);
				graphItems.add(graphItem);
			}
		}
		
		
		// create activity session timeline items
		List<TimelineItem> timelineItems = new ArrayList<TimelineItem>();
		for(int i = 0; i < numberOfGoal; i++) {
			
			long goalTimestamp = timestamp - 3600 * 24 * i;
			long goalStartTime = MVPApi.getDayStartEpoch(goalTimestamp);
			long goalEndTime = MVPApi.getDayEndEpoch(goalTimestamp);
			long numberOfItem = randInt(minimumSessionTileNumber, maximumSessionTileNumber);
			long step = (goalEndTime - goalStartTime) / numberOfItem;
						
			for(long t = goalStartTime; t <= goalEndTime; t += step) {
				
				TimelineItem activityTile = DataGenerator.generateRandomActivitySessionTimelineItem(t, null);
				timelineItems.add(activityTile);
			}
		}
		
		
		// create milestone timeline items
		for(int i = numberOfGoal; i > 0; i--) {
			
			long goalTimestamp = timestamp - 3600 * 24 * i;
			long goalStartTime = MVPApi.getDayStartEpoch(goalTimestamp);
			long goalEndTime = MVPApi.getDayEndEpoch(goalTimestamp);
			
			// personal best
			if(coin()) {
				
				long t = randLong(goalStartTime + 3600, goalEndTime - 3600);
				TimelineItem personalBestItem = DataGenerator.generateRandomMilestoneItem(t, 
						TimelineItemDataBase.EVENT_TYPE_PERSONAL_BEST, null);
				
				timelineItems.add(personalBestItem);
			}
			
			// hit goal 100 
			if(coin()) {
				
				long t = randLong(goalStartTime + 3600, goalEndTime - 3600);
				TimelineItem goal100Item = DataGenerator.generateRandomMilestoneItem(t, 
						TimelineItemDataBase.EVENT_TYPE_100_GOAL, null);
				
				timelineItems.add(goal100Item);
				
				// streak
				if(coin()) {
					
					t = randLong(goalStartTime + 3600, goalEndTime - 3600);
					TimelineItem streakItem = DataGenerator.generateRandomMilestoneItem(t, 
							TimelineItemDataBase.EVENT_TYPE_STREAK, null);
					
					timelineItems.add(streakItem);
				}
				
				// hit goal 150
				if(coin()) {
					
					t = randLong(goalStartTime + 3600, goalEndTime - 3600);
					TimelineItem goal150Item = DataGenerator.generateRandomMilestoneItem(t, 
							TimelineItemDataBase.EVENT_TYPE_150_GOAL, null);
					
					timelineItems.add(goal150Item);
					
					// hit goal 200
					if(coin()) {
						
						t = randLong(goalStartTime + 3600, goalEndTime - 3600);
						TimelineItem goal200Item = DataGenerator.generateRandomMilestoneItem(t, 
								TimelineItemDataBase.EVENT_TYPE_200_GOAL, null);
						
						timelineItems.add(goal200Item);
					}
				}
			}
			
		}
		
		
		// create lifetime distance tiles
		long startTime = MVPApi.getDayStartEpoch(timestamp - 3600 * 24 * 20);
		long t1 = randLong(startTime, startTime + 3600 * 24 * 6);
		long t2 = randLong(t1, t1 + 3600 * 24 * 6);
		long t3 = randLong(t2, t2 + 3600 * 24 * 6);

		// 2 marathons
		if(coin()) {

			TimelineItem item1 = DataGenerator.generateRandomLifetimeDistanceItem(t1, null);
			((LifetimeDistanceItem)(item1.getData())).setMilestoneType(0);

			timelineItems.add(item1);

			// 6 marathons
			if(coin()) {

				TimelineItem item2 = DataGenerator.generateRandomLifetimeDistanceItem(t2, null);
				((LifetimeDistanceItem)(item2.getData())).setMilestoneType(1);

				timelineItems.add(item2);

				// 12 marathons
				if(coin()) {

					TimelineItem item3 = DataGenerator.generateRandomLifetimeDistanceItem(t3, null);
					((LifetimeDistanceItem)(item3.getData())).setMilestoneType(2);

					timelineItems.add(item3);
				}
			}
		}
		
		
		// send requests
		MVPApi.createGraphItems(token, graphItems);
		MVPApi.createTimelineItems(token, timelineItems);

		
		// create sync logs
		syncLogNumber = Math.max(syncLogNumber, 10);
		for(int i = numberOfGoal; i > 0; i--) {
			
			long goalTimestamp = timestamp - 3600 * 24 * i;
			long goalStartTime = MVPApi.getDayStartEpoch(goalTimestamp);
			long goalEndTime = MVPApi.getDayEndEpoch(goalTimestamp);
			
			long interval = (goalEndTime - goalStartTime) / syncLogNumber;
			int totalMinute =(int) (interval - 300) / 60;
			
			for(int j = 0; j < syncLogNumber; j++) {

				long syncLogTimestamp = goalStartTime + interval * j + 1;
				SyncLog syncLog = DataGenerator.generateRandomSyncLog(syncLogTimestamp, 1, totalMinute, null);
				syncLog.setSerialNumberString(pedometer.getSerialNumberString());
				MVPApi.pushSyncLog(token, syncLog);
			}
		}
	}

}
