package com.misfit.ta.backend.aut.correctness.servercalculation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.pedometer.Pedometer;
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
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class ServerCalculationTestHelpers {

	protected static Logger logger = Util.setupLogger(ServerCalculationTestHelpers.class);
	protected static String TestMetaDataFile = "test.json";
	protected static int SDKSyncLogHeaderSize = 16;
	protected static int DelayTime = 10000;
	
	
	// get data from AWS to create test case
	public static void createTest(String saveFolder, String email, int startDate, int startMonth, int startYear, int endDate, int endMonth, int endYear) {
		
		long startTime = MVPApi.getDayStartEpoch(startDate, startMonth, startYear);
		long endTime = MVPApi.getDayEndEpoch(endDate, endMonth, endYear);
		
		getSyncFilesFromAWS(true, email, startTime, endTime, saveFolder);
		try {
			JSONObject json = new JSONObject();
			json.put("start_time", startTime);
			json.put("end_time", endTime);
			json.put("start_date", String.format("%02d/%02d/%d", startDate, startMonth, startYear));
			json.put("end_date", String.format("%02d/%02d/%d", endDate, endMonth, endYear));
			json.put("email", email);
			
			FileUtils.write(new File(saveFolder + "/" + TestMetaDataFile), json.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getSyncFilesFromAWS(boolean fromStaging, String email, long fromTime, long toTime, String saveFolder) {

		String bucket = "shine-binary-data";
		String environment = fromStaging ? "staging" : "production";

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
			}
		}
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
			syncEvent.setEvent("fileGetActivity");
			syncEvent.setRequestStarted(requestStarted);
			syncEvent.setRequestFinished(requestFinished);
			syncEvent.setResponseStarted(responseStarted);
			syncEvent.setResponseFinished(responseFinished);

			events.add(syncEvent);
		}

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
	
	
	// run tests
	public static void runTest(String testFolderPath, String email, String password) {
		
		try {
						
			// create user and create goals in the test time range
			String jsonString = FileUtils.readFileToString(new File(testFolderPath + "/" + TestMetaDataFile));
			JSONObject json = new JSONObject(jsonString);
			
			long startTime = json.getLong("start_time");
			long endTime = json.getLong("end_time");
			Pedometer pedometer = DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null);
			
			String token = MVPApi.signUp(email, password).token;
			if(token == null)
				token = MVPApi.signIn(email, password).token;
			
			MVPApi.createProfile(token, DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null));
			MVPApi.createPedometer(token, pedometer);
			MVPApi.createGoal(token, Goal.getDefaultGoal());
			
			for(long i = startTime - 3600 * 24; i <= endTime; i = i + 3600 * 24) {
				
				MVPApi.createGoal(token, Goal.getDefaultGoal(i));
			}
			
			
			// push raw data to server
			File testFolder = new File(testFolderPath);
			long startTimestamp = MVPApi.getDayStartEpoch();
			
			for(File syncFolder : testFolder.listFiles()) {
				
				if(syncFolder.isFile())
					continue;
				
				SDKSyncLog syncLog = createSDKSyncLogFromFilesInFolder(startTimestamp, email, pedometer.getSerialNumberString(), syncFolder.getAbsolutePath());
				startTimestamp += 600;
				
				MVPApi.pushSDKSyncLog(syncLog);
				ShortcutsTyper.delayTime(DelayTime);
 			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
