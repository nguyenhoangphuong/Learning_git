package com.misfit.ta.backend.aut.correctness.servercalculation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.sync.sdk.SDKSyncEvent;
import com.misfit.ta.backend.data.sync.sdk.SDKSyncLog;
import com.misfit.ta.backend.data.sync.sdk.requestevent.SDKSyncRequestFinishedEvent;
import com.misfit.ta.backend.data.sync.sdk.requestevent.SDKSyncRequestStartedEvent;
import com.misfit.ta.backend.data.sync.sdk.requestevent.SDKSyncResponseFinishedEvent;
import com.misfit.ta.backend.data.sync.sdk.requestevent.SDKSyncResponseStartedEvent;
import com.misfit.ta.backend.data.sync.sdk.requestevent.value.SDKSyncResponseFinishedValue;
import com.misfit.ta.backend.data.sync.sdk.requestevent.value.SDKSyncResponseStartedValue;
import com.misfit.ta.base.AWSHelper;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.TextTool;

public class ServerCalculationTestHelpers {

	protected static Logger logger = Util.setupLogger(ServerCalculationTestHelpers.class);
	protected static final int SDKSyncLogHeaderSize = 16;
	public static final String TestMetaDataFile = "test.json";


	// get data from AWS to create test case
	public static void createTest(String saveFolder, String email, int startDate, int startMonth, int startYear, int endDate, int endMonth, int endYear) {
		
		long startTime = MVPCommon.getDayStartEpoch(startDate, startMonth, startYear);
		long endTime = MVPCommon.getDayEndEpoch(endDate, endMonth, endYear);
		long lastSyncTime = getSyncFilesFromAWS(true, email, startTime, endTime, saveFolder);
		
		try {
			JSONObject json = new JSONObject();
			json.put("start_time", startTime);
			json.put("end_time", endTime);
			json.put("start_date", MVPCommon.getDateString(startTime));
			json.put("end_date", MVPCommon.getDateString(endTime));
			json.put("last_sync_time", lastSyncTime);
			json.put("last_sync_date",  MVPCommon.getDateString(lastSyncTime));
			json.put("email", email);
			
			FileUtils.write(new File(saveFolder + "/" + TestMetaDataFile), json.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static long getSyncFilesFromAWS(boolean fromStaging, String email, long fromTime, long toTime, String saveFolder) {

		String bucket = "shine-binary-data";
		String environment = fromStaging ? "staging" : "production";
		long lastSyncTime = fromTime;

		for(long i = fromTime; i <= toTime; i = i + 3600 * 24) {

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(i * 1000);

			String prefix = String.format("%s/%d/%02d/%02d/%s/", environment,
					cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH) + 1,
					cal.get(Calendar.DATE),
					email);

			List<String> objects = AWSHelper.listFiles(bucket, prefix);

			for(String obj : objects) {

				if(obj.contains("debug_log") || obj.contains("hardware_log") || obj.contains("metadata"))
					continue;

				logger.info("Downloading " + obj);
				String[] parts = obj.split("/");
				String fileName = parts[parts.length - 1];
				String timestampString = parts[parts.length - 2];

				AWSHelper.downloadFile(bucket, obj, saveFolder + "/" + timestampString + "/" + fileName);
				lastSyncTime = Long.parseLong(timestampString);
			}
		}
		
		return lastSyncTime;
	}

	
	// create sdk sync log from test folder
	public static SDKSyncLog createSDKSyncLogFromFilesInFolder(long timestamp, String email, String serialNumber, String folderName) {

		long currentTimestamp = timestamp; 
		File folder = new File(folderName);
		List<String> dataStrings = new ArrayList<String>();

		for(File file : folder.listFiles()) {

			try {
				dataStrings.add(FileUtils.readFileToString(file));
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		return createSDKSyncLogFromDataStrings(currentTimestamp, email, serialNumber, dataStrings);
	}

	public static SDKSyncLog createSDKSyncLogFromDataStrings(long timestamp, String email, String serialNumber, List<String> dataStrings) {

		long currentTimestamp = timestamp; 
		List<SDKSyncEvent> events = new ArrayList<SDKSyncEvent>();

		for(String rawdata : dataStrings) {

			SDKSyncRequestStartedEvent requestStarted = new SDKSyncRequestStartedEvent();
			requestStarted.setTimestamp(currentTimestamp++);

			SDKSyncRequestFinishedEvent requestFinished = new SDKSyncRequestFinishedEvent();
			requestFinished.setResult(0);
			requestFinished.setTimestamp(currentTimestamp++);

			SDKSyncResponseStartedValue value1 = new SDKSyncResponseStartedValue();
			value1.setData(TextTool.getRandomString(5, 10));

			SDKSyncResponseStartedEvent responseStarted = new SDKSyncResponseStartedEvent();
			responseStarted.setResult(0);
			responseStarted.setTimestamp(currentTimestamp++);
			responseStarted.setValue(value1);

			SDKSyncResponseFinishedValue value2 = new SDKSyncResponseFinishedValue();
			
			value2.setHandle("100");
			value2.setFileHandle(rawdata.substring(0, 4));
			value2.setFileFormat(rawdata.substring(6, 8) + rawdata.substring(4, 6));
			value2.setLength(MVPCommon.litteEndianStringToInteger(rawdata.substring(8, 16)));
			value2.setTimestamp(MVPCommon.litteEndianStringToLong(rawdata.substring(16, 24)));
			value2.setMiliseconds(MVPCommon.litteEndianStringToInteger(rawdata.substring(24, 28)));
			value2.setTimezoneOffsetInMinutes(MVPCommon.litteEndianStringToInteger(rawdata.substring(28, 32)));
			value2.setStatus(0);
			value2.setActivityData(rawdata.substring(SDKSyncLogHeaderSize * 2, rawdata.length() - 8));

			SDKSyncResponseFinishedEvent responseFinished = new SDKSyncResponseFinishedEvent();
			responseFinished.setResult(0);
			responseFinished.setTimestamp(currentTimestamp++);
			responseFinished.setValue(value2);

			SDKSyncEvent syncEvent = new SDKSyncEvent();
			syncEvent.setEvent(SDKSyncEvent.EVENT_GET_FILE_ACTIVITY);
			syncEvent.setRequestStarted(requestStarted);
			syncEvent.setRequestFinished(requestFinished);
			syncEvent.setResponseStarted(responseStarted);
			syncEvent.setResponseFinished(responseFinished);

			events.add(syncEvent);
		}
		
		// add close event
		SDKSyncRequestStartedEvent requestStarted = new SDKSyncRequestStartedEvent();
		requestStarted.setTimestamp(currentTimestamp++);

		SDKSyncRequestFinishedEvent requestFinished = new SDKSyncRequestFinishedEvent();
		requestFinished.setResult(0);
		requestFinished.setTimestamp(currentTimestamp++);
		
		SDKSyncEvent syncEvent = new SDKSyncEvent();
		syncEvent.setEvent(SDKSyncEvent.EVENT_CLOSE);
		syncEvent.setRequestStarted(requestStarted);
		syncEvent.setRequestFinished(requestFinished);
		
		events.add(syncEvent);
		
		// the sync log
		SDKSyncLog syncLog = new SDKSyncLog();
		syncLog.setPlatform("iOS");
		syncLog.setSystemVersion("7.1");
		syncLog.setDeviceModel("iPhone6,1");
		syncLog.setSdkVersion("0.0.0");
		syncLog.setUserId(email);
		syncLog.setSerialNumber(serialNumber);
		syncLog.setFirmwareVersion(MVPApi.LATEST_FIRMWARE_VERSION_STRING);
		syncLog.setStartAt(timestamp);
		syncLog.setEndAt(currentTimestamp);
		syncLog.setEvents(events);

		return syncLog;
	}
	
	public static GoalRawData generateSessionRawData(int totalSteps, int totalPoints, int duration, List<int[]> listDataTaginTagout){
		int stepPerMinute = totalSteps / duration;
		int pointPerMinute = totalPoints / duration;

		int[] steps = new int[duration];
		int[] points = new int[duration];
		int[] variances = new int[duration];
		
		Arrays.fill(steps, stepPerMinute);
		Arrays.fill(points, (int)(pointPerMinute * 2.5));
		Arrays.fill(variances, 10000);

		// if steps/point is not devidable
		if(totalSteps % duration != 0)
			steps[0] = totalSteps % duration;

		if(totalPoints % duration != 0)
			points[0] = totalPoints % duration;

		GoalRawData rawdata = new GoalRawData();
		rawdata.setPoints(points);
		rawdata.setSteps(steps);
		if(listDataTaginTagout != null){
			rawdata.setTagInOutMinutes(listDataTaginTagout);
		}
		rawdata.setVariances(variances);

		return rawdata;

	}

	// generate data helper
	public static GoalRawData generateSessionRawData(int totalSteps, int totalPoints, int duration)  {
		return generateSessionRawData(totalSteps, totalPoints, duration, null);
	}

	public static GoalRawData generateEmptyRawData(int duration) {

		int[] steps = new int[duration];
		int[] points = new int[duration];
		int[] variances = new int[duration];
		List<int[]> tag_in_out_minutes = new ArrayList<int[]>();
		
		Arrays.fill(steps, 0);
		Arrays.fill(points, 0);
		Arrays.fill(variances, 10000);
		tag_in_out_minutes.add(new int[]{0,0});
		
		GoalRawData rawdata = new GoalRawData();
		rawdata.setPoints(points);
		rawdata.setSteps(steps);
		rawdata.setVariances(variances);
		rawdata.setTagInOutMinutes(tag_in_out_minutes);
		return rawdata;
	}

	public static GoalRawData generateEmptyRawData(int includeStartOffsetMinute, int excludeEndOffsetMinute) {

		int duration = excludeEndOffsetMinute - includeStartOffsetMinute;
		return generateEmptyRawData(duration);
	}

	public static GoalRawData generateGapData(int stepPerMinute, int pointPerMinute, int activeInterval, int idleInterval, int duration) {

		int gapCount = duration / (idleInterval + activeInterval);

		int[] steps = new int[duration];
		int[] points = new int[duration];
		int[] variances = new int[duration];

		Arrays.fill(steps, 0);
		Arrays.fill(points, 0);
		Arrays.fill(variances, 10000);

		int index = 0;
		for(int i = 0; i < gapCount; i++) {

			for(int j = 0; j < activeInterval; j++) {
				steps[index] = stepPerMinute;
				points[index] = (int)(pointPerMinute * 2.5);
				index++;
			}

			for(int k = 0; k < idleInterval; k++) {
				steps[index] = 0;
				points[index] = 0;
				index++;
			}

		}

		GoalRawData rawdata = new GoalRawData();
		rawdata.setPoints(points);
		rawdata.setSteps(steps);
		rawdata.setVariances(variances);

		return rawdata;
	}
	
	/* Utilities */
	public int randInt(int includeFrom, int excludeTo) {
		Random r = new Random();
		return r.nextInt(excludeTo - includeFrom) + includeFrom;
	}

}
