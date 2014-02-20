package com.misfit.ta.backend.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.commons.io.FileUtils;

import com.misfit.ta.backend.api.internalapi.MVPApi;
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
import com.misfit.ta.backend.data.timeline.timelineitemdata.FoodTrackingItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.LifetimeDistanceItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.MilestoneItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.MilestoneItemInfo;
import com.misfit.ta.backend.data.timeline.timelineitemdata.SleepSessionItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimezoneChangeItem;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.TextTool;

public class DataGenerator {
	
		
	// generators
	public static ProfileData generateRandomProfile(long timestamp, Map<String, Object> options) {
		
		ProfileData p = new ProfileData();

		p.setLocalId("profiles-" + MVPApi.generateLocalId());
		p.setUpdatedAt((long) (System.currentTimeMillis() / 1000));

		p.setWeight(MVPCommon.randInt(1200, 1800) / 10d);
		p.setHeight(MVPCommon.randInt(58, 70) * 1d);
		p.setGender(MVPCommon.randInt(0, 1));
		p.setDateOfBirth((long) MVPCommon.randInt(685000000, 686000000));
		p.setName(TextTool.getRandomString(7, 10));
		p.setGoalLevel(1);

		p.setLatestVersion("0.21.1.4228");
		p.setDisplayedUnits(new DisplayUnit());

		return p;
	}
	
	public static Goal generateRandomGoal(long timestamp, Map<String, Object> options) {
		
		ProgressData progressData = new ProgressData();
		progressData.setFullBmrCalorie(MVPCommon.randInt(1200, 1600));
		progressData.setCalorie(MVPCommon.randInt(15000, 25000) * 0.1d);
		progressData.setDistanceMiles(MVPCommon.randInt(10, 50) * 0.1d);
		progressData.setSteps(MVPCommon.randInt(5000, 20000));
		progressData.setPoints(MVPCommon.randInt(500, 3000) * 2.5);
		progressData.setSeconds(MVPCommon.randInt(4000, 10000));
		
		List<TripleTapData> tripleTaps = new ArrayList<TripleTapData>();
		tripleTaps.add(new TripleTapData(timestamp, MVPEnums.ACTIVITY_SLEEPING));
		Goal g = new Goal();

		g.setLocalId("goal-" + MVPApi.generateLocalId());
		g.setUpdatedAt(timestamp);

		g.setValue(MVPCommon.randInt(10, 30) * 100 * 2.5);
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
		item.setBatteryLevel(MVPCommon.randInt(20, 100));
		item.setBookmarkState(MVPCommon.randInt(0, 3));
		item.setClockState(MVPCommon.randInt(0, 3));
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
		personalRecords.getPersonalBestRecordsInPoint().setPoint(MVPCommon.randInt(500, 6000) * 2.5);
		personalRecords.getPersonalBestRecordsInPoint().setTimestamp(timestamp);
		
		if(MVPCommon.coin()) {
			personalRecords.setSwimming(new Record());
			personalRecords.getSwimming().setPoint(MVPCommon.randInt(500, 6000) * 2.5);
			personalRecords.getSwimming().setTimestamp(timestamp);
		}
		
		if(MVPCommon.coin()) {
			personalRecords.setBasketball(new Record());
			personalRecords.getBasketball().setPoint(MVPCommon.randInt(500, 6000) * 2.5);
			personalRecords.getBasketball().setTimestamp(timestamp);
		}
		
		if(MVPCommon.coin()) {
			personalRecords.setCycling(new Record());
			personalRecords.getCycling().setPoint(MVPCommon.randInt(500, 6000) * 2.5);
			personalRecords.getCycling().setTimestamp(timestamp);
		}
		
		if(MVPCommon.coin()) {
			personalRecords.setSoccer(new Record());
			personalRecords.getSoccer().setPoint(MVPCommon.randInt(500, 6000) * 2.5);
			personalRecords.getSoccer().setTimestamp(timestamp);
		}
		
		if(MVPCommon.coin()) {
			personalRecords.setTennis(new Record());
			personalRecords.getTennis().setPoint(MVPCommon.randInt(500, 6000) * 2.5);
			personalRecords.getTennis().setTimestamp(timestamp);
		}

		Statistics item = new Statistics();
		item.setLocalId("statistics-" + MVPApi.generateLocalId());
		item.setUpdatedAt(timestamp);
		item.setPersonalRecords(personalRecords);
		item.setBestStreak(MVPCommon.randInt(6, 25));
		item.setTotalGoalHit(MVPCommon.randInt(10, 30));
		item.setLifetimeDistance(MVPCommon.randInt(100, 1000) * 0.1d);

		return item;
	}

	public static TimelineItem generateRandomActivitySessionTimelineItem(long timestamp, Map<String, Object> options) {
		
		int[] activityTypes = new int[] { 2, 3, 4, 5, 6, 7 };
		
		ActivitySessionItem data = new ActivitySessionItem();
		data.setActivityType(activityTypes[MVPCommon.randInt(0, activityTypes.length - 1)]);
		data.setCalories(MVPCommon.randInt(200, 1000) * 0.1d);
		data.setDistance(MVPCommon.randInt(10, 200) * 0.1d);
		data.setDuration(MVPCommon.randInt(300, 1500));
		data.setIsBestRecord(false);
		data.setSteps(MVPCommon.randInt(120, 250) * data.getDuration() / 60);
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
				info.setPoint(MVPCommon.randInt(500, 2500));
				break;
			}
			
			case TimelineItemDataBase.EVENT_TYPE_150_GOAL:
			{
				info.setPoint((int)(MVPCommon.randInt(500, 2500) * 1.5));
				break;
			}
			
			case TimelineItemDataBase.EVENT_TYPE_200_GOAL:
			{
				info.setPoint(MVPCommon.randInt(500, 2500) * 2);
				break;
			}
			
			case TimelineItemDataBase.EVENT_TYPE_PERSONAL_BEST:
			{
				info.setPoint(MVPCommon.randInt(2500, 5000));
				info.setExceededAmount(info.getPoint() / MVPCommon.randInt(2, 3));
				break;
			}
			
			case TimelineItemDataBase.EVENT_TYPE_STREAK:
			{
				info.setStreakNumber(MVPCommon.randInt(3, 30));
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
		data.setMilestoneType(MVPCommon.randInt(0, 2));
		data.setUnitSystem(MVPCommon.randInt(0, 1));
		
		TimelineItem item = new TimelineItem();
		item.setLocalId("timelineitem-" + MVPApi.generateLocalId());
		item.setUpdatedAt(timestamp);
		item.setTimestamp(timestamp);
		item.setItemType(TimelineItemDataBase.TYPE_LIFETIME_DISTANCE);
		item.setData(data);

		return item;
	}
	
	public static TimelineItem generateRandomFoodTimelineItem(long timestamp, Map<String, Object> options) {

		FoodTrackingItem data = new FoodTrackingItem();
		
		String filePath = "images/1mb/" + MVPCommon.randInt(1, 10) + ".jpg";
		String base64 = MVPCommon.readFileAsBase64String(filePath);

		TimelineItem item = new TimelineItem();
		item.setLocalId("timelineitem-" + MVPApi.generateLocalId());
		item.setUpdatedAt(timestamp);
		item.setTimestamp(timestamp);
		item.setItemType(TimelineItemDataBase.TYPE_FOOD);
		item.setAttachedImage(base64);
		item.setData(data);

		return item;
	}
	
	public static TimelineItem generateRandomSleepTimelineItem(long timestamp, Map<String, Object> options) {

		long realStartTime = timestamp + MVPCommon.randInt(600, 1200);
		long realEndTime = timestamp + MVPCommon.randInt(3600 * 5, 3600 * 10);
		int realSleepTimeInMinutes = (int)((realEndTime - realStartTime) / 60);
		int	realDeepSleepTimeInMinutes = realSleepTimeInMinutes * MVPCommon.randInt(20, 40) / 100;
		
		List<Integer[]> sleepStateChanges = new ArrayList<Integer[]>();
		sleepStateChanges.add(new Integer[] {0, 2});
		int currentMinuteOffset = 0;
		int currentState = 2;
		while(currentMinuteOffset < realSleepTimeInMinutes - 15) {
			
			int state;
			do {
				state = MVPCommon.randInt(1, 3);
			}
			while (state == currentState);
			
			currentMinuteOffset += MVPCommon.randLong(10, 120);
			currentState = state;
			
			sleepStateChanges.add(new Integer[] {currentMinuteOffset, state});
		}
				
		SleepSessionItem data = new SleepSessionItem();
		data.setBookmarkTime(timestamp);
		data.setIsAutoDetected(MVPCommon.coin());
		data.setIsFirstSleepOfDay(true);
		data.setNormalizedSleepQuality(MVPCommon.randInt(60, 100));
		data.setRealDeepSleepTimeInMinutes(realDeepSleepTimeInMinutes);
		data.setRealSleepTimeInMinutes(realSleepTimeInMinutes);
		data.setRealStartTime(realStartTime);
		data.setRealEndTime(realEndTime);
		data.setSleepStateChanges(sleepStateChanges);

		TimelineItem item = new TimelineItem();
		item.setLocalId("timelineitem-" + MVPApi.generateLocalId());
		item.setUpdatedAt(timestamp);
		item.setTimestamp(timestamp);
		item.setItemType(TimelineItemDataBase.TYPE_SLEEP);
		item.setData(data);

		return item;
	}
	
	public static TimelineItem generateRandomTimezoneTimelineItem(long timestamp, Map<String, Object> options) {

		TimezoneChangeItem data = new TimezoneChangeItem();
		data.setBeforeTimeZoneOffset((MVPCommon.randInt(0, 24) - 12) * 3600);
		data.setAfterTimeZoneOffset((MVPCommon.randInt(0, 24) - 12) * 3600);

		TimelineItem item = new TimelineItem();
		item.setLocalId("timelineitem-" + MVPApi.generateLocalId());
		item.setUpdatedAt(timestamp);
		item.setTimestamp(timestamp);
		item.setItemType(TimelineItemDataBase.TYPE_TIMEZONE);
		item.setData(data);

		return item;
	}
	
	
	
	public static GraphItem generateRandomGraphItem(long timestamp, Map<String, Object> options) {
		
		GraphItem item = new GraphItem();

		item.setLocalId("graphitem-" + MVPApi.generateLocalId());
		item.setUpdatedAt(timestamp);
		item.setTimestamp(timestamp);
		item.setTotalValue(0d);
		item.setAverageValue(MVPCommon.randInt(10, 70) / 60d);

		return item;
	}

	public static SyncFileData generateRandomSyncFileData(String fileHandle, long timestamp, int totalMinute) {
		
		int fileSize = 16 + 4 + totalMinute * 2;
		totalMinute = Math.min(totalMinute, 1014);
		String rawData = "";
		
		
		// prepare rawData's header: fileHandle + fileType + length 
		// 		+ timestamp + timestampMiliseconds + timezoneOffsetInMinutes
		rawData = fileHandle + "0011" + MVPCommon.toLittleEndianString(fileSize) 
				+ MVPCommon.toLittleEndianString(timestamp) + "0000" + "0000";
		
		List<Byte> headerBytes = MVPCommon.hexStringToByteArray(rawData);
		
		
		// minute by minute data
		List<Byte> bytes = new ArrayList<Byte>();
		for(int i = 0; i < totalMinute; i++) {
			
			Integer steps = MVPCommon.randInt(0, 190);
			Integer point = (int)Math.floor(steps * MVPCommon.randInt(25, 30) * 0.01d);
			
			bytes.add(steps.byteValue());
			bytes.add(point.byteValue());
		}
		
		rawData += MVPCommon.toLittleEndianString(bytes, false);
		
		
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
		
		rawData += MVPCommon.toLittleEndianString(checksumValue);
		
		
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
		data.setDeviceInfo(iDevices[MVPCommon.randInt(0, iDevices.length - 1)]);
		data.setIosVersion(iosVersions[MVPCommon.randInt(0, iosVersions.length - 1)]);
		data.setHardwareLog("");
		data.setFailureLog("");
		data.setFailureReason(0);
		data.setFileData(fileData);

		SyncLog syncLog = new SyncLog();
		syncLog.setStartTime(timestamp - 20);
		syncLog.setEndTime(timestamp);
		syncLog.setFirmwareRevisionString(MVPApi.LATEST_FIRMWARE_VERSION_STRING);
		syncLog.setSerialNumberString(TextTool.getRandomString(10, 10));
		syncLog.setLog("AUTO GENRATED SYNCLOG");
		syncLog.setIsSuccessful(1);
		syncLog.setData(data);

		return syncLog;
	}
	
	public static SyncLog createSyncLogFromFilesInFolder(long timestamp, String serialNumber, String folderName) throws IOException {
		
		// get latest files
		Files.delete(folderName);
		File folder = Files.getFile(folderName);
		
		
		// read sync files
		List<SyncFileData> fileData = new ArrayList<SyncFileData>();
		for(File file : folder.listFiles()) {

			String fileHandle = file.getName();
			String rawData = FileUtils.readFileToString(file);
			int fileSize = rawData.length() / 2;
			long fileTimestamp = SyncFileData.getFileTimestampFromRawData(rawData);
			
			SyncFileData syncFileData = new SyncFileData();
			syncFileData.setFileHandle(fileHandle);
			syncFileData.setFileSize(fileSize);
			syncFileData.setFileTimestamp(fileTimestamp);
			syncFileData.setRawData(rawData);
			syncFileData.setTimestampDifference(0);
			
			fileData.add(syncFileData);
		}
		
		SyncData data = new SyncData();
		data.setSyncMode(3);
		data.setAppVersion("0.23.4.5678");
		data.setDeviceInfo("iPhone6,2");
		data.setIosVersion("7.1");
		data.setHardwareLog("");
		data.setFailureLog("");
		data.setFailureReason(0);
		data.setFileData(fileData);

		SyncLog syncLog = new SyncLog();
		syncLog.setStartTime(timestamp - 20);
		syncLog.setEndTime(timestamp);
		syncLog.setFirmwareRevisionString(MVPApi.LATEST_FIRMWARE_VERSION_STRING);
		syncLog.setSerialNumberString(serialNumber);
		syncLog.setLog("AUTO GENRATED SYNCLOG");
		syncLog.setIsSuccessful(1);
		syncLog.setData(data);

		return syncLog;
	}
	
}
