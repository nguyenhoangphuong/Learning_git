package com.misfit.ta.backend.api.internalapi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.RequestHelper;
import com.misfit.ta.backend.aws.AWSHelper;
import com.misfit.ta.backend.data.account.*;
import com.misfit.ta.backend.data.goal.*;
import com.misfit.ta.backend.data.graph.*;
import com.misfit.ta.backend.data.profile.*;
import com.misfit.ta.backend.data.pedometer.*;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.sync.SyncLog;
import com.misfit.ta.backend.data.timeline.*;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.backend.data.*;
import com.misfit.ta.utils.TextTool;

public class MVPApi extends RequestHelper {

	// logger
	protected static Logger logger = Util.setupLogger(MVPApi.class);

	// fields
	public static String baseAddress = Settings.getValue("MVPBackendBaseAddress");
	public static int port = Integer.parseInt(Settings.getValue("MVPBackendPort"));
		
	public static int CACHE_TRY_TIME = 10;
	public static String LATEST_FIRMWARE_VERSION_STRING = "0.0.60r";

	// generators
	public static String generateUniqueEmail() {
		return "test" + System.currentTimeMillis() + TextTool.getRandomString(6, 6).toLowerCase() + "@misfitqa.com";
	}

	public static String generateLocalId()
	{
		return System.nanoTime() + "-" + TextTool.getRandomString(10, 10);
	}
	
	public static JSONArray[] generateTimelineItemsAndGraphItems() {
		int numberOfItemsPerDay = 1;
		int numberOfDays = 1;
		numberOfItemsPerDay = Settings.getInt("NUMBER_OF_ITEMS_PER_DAY");
		numberOfDays = Settings.getInt("NUMBER_OF_DAYS");
		return generateTimelineItemsAndGraphItems(numberOfDays, numberOfItemsPerDay);
	}

	public static JSONArray[] generateTimelineItemsAndGraphItems(int numberOfDays, int numberOfItemsPerDay) {

		long timestamp = System.currentTimeMillis() / 1000;
				
		// create graph items
		List<GraphItem> graphItems = new ArrayList<GraphItem>();
		for(int i = 0; i < numberOfDays; i++) {

			long goalTimestamp = timestamp - 3600 * 24 * i;
			long goalStartTime = MVPApi.getDayStartEpoch(goalTimestamp);
			long goalEndTime = MVPApi.getDayEndEpoch(goalTimestamp);
			long graphItemInterval = (goalEndTime - goalStartTime) / Math.max(numberOfItemsPerDay, 1);

			for(long t = goalStartTime; t <= goalEndTime; t += graphItemInterval) {

				GraphItem graphItem = DataGenerator.generateRandomGraphItem(t, null);
				graphItems.add(graphItem);
			}
		}


		// create activity session timeline items
		List<TimelineItem> timelineItems = new ArrayList<TimelineItem>();
		for(int i = 0; i < numberOfDays; i++) {

			long goalTimestamp = timestamp - 3600 * 24 * i;
			long goalStartTime = MVPApi.getDayStartEpoch(goalTimestamp);
			long goalEndTime = MVPApi.getDayEndEpoch(goalTimestamp);
			long numberOfItem = Math.max(1, numberOfItemsPerDay);
			long step = (goalEndTime - goalStartTime) / numberOfItem;

			for(long t = goalStartTime; t <= goalEndTime; t += step) {

				TimelineItem activityTile = DataGenerator.generateRandomActivitySessionTimelineItem(t, null);
				timelineItems.add(activityTile);
			}
		}
		
		
		// data for a number of days
		JSONArray timelineItemsJsonArr = new JSONArray();
		JSONArray graphItemsJsonArr = new JSONArray();
		
		for(TimelineItem item: timelineItems)
			timelineItemsJsonArr.put(item.toJson());
		
		for(GraphItem item: graphItems)
			graphItemsJsonArr.put(item.toJson());
		

		JSONArray[] array = new JSONArray[2];
		array[0] = timelineItemsJsonArr;
		array[1] = graphItemsJsonArr;
		return array;
	}
	
	// utilities
	public static long getDayStartEpoch() {
		return getDayStartEpoch(System.currentTimeMillis() / 1000);
	}

	public static long getDayStartEpoch(long epoch) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(epoch * 1000);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTimeInMillis() / 1000;
	}

	public static long getDayStartEpoch(int date, int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date, 0, 0, 0);

		return cal.getTimeInMillis() / 1000;
	}

	public static long getDayEndEpoch() {
		return getDayEndEpoch(System.currentTimeMillis() / 1000);
	}

	public static long getDayEndEpoch(long epoch) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(epoch * 1000);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		return cal.getTimeInMillis() / 1000;
	}

	public static long getDayEndEpoch(int date, int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date, 23, 59, 59);

		return cal.getTimeInMillis() / 1000;
	}

	// account apis
	static private AccountResult sign(String email, String password, String shortUrl) {
		// trace
		logger.info("Email: " + email + ", Password: " + password);

		// prepare
		String url = baseAddress + shortUrl;

		BaseParams requestInf = new BaseParams();
		requestInf.addParam("email", email);
		requestInf.addParam("password", password);

		// post and receive raw data
		ServiceResponse response = MVPApi.post(url, port, requestInf);

		// format data
		AccountResult result = new AccountResult(response);
		return result;
	}

	public static AccountResult signIn(String email, String password) {
		return sign(email, password, "login");
	}

	public static AccountResult signUp(String email, String password) {
		return sign(email, password, "signup");
	}

	public static BaseResult signOut(String token) {
		// trace
		logger.info("Token: " + token);

		// prepare
		String url = baseAddress + "logout";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		// post and receive raw data
		ServiceResponse response = MVPApi.post(url, port, requestInf);

		// format data
		BaseResult result = new BaseResult(response);
		return result;
	}

	// profile apis
	public static ProfileResult createProfile(String token, ProfileData data) {

		// prepare
		String url = baseAddress + "profile";
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("profile", data.toJson().toString());

		// post and receive raw data
		ServiceResponse response = MVPApi.post(url, port, requestInf);

		// format data
		ProfileResult result = new ProfileResult(response);
		return result;
	}

	public static ProfileResult getProfile(String token) {
		// prepare
		String url = baseAddress + "profile";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		// post and receive raw data
		ServiceResponse response = MVPApi.get(url, port, requestInf);

		// format data
		ProfileResult result = new ProfileResult(response);
		return result;
	}

	public static ProfileResult updateProfile(String token, ProfileData data) {

		// prepare
		String url = baseAddress + "profile";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("profile", data.toJson().toString());

		// post and recieve raw data
		ServiceResponse response = MVPApi.put(url, port, requestInf);

		// format data
		ProfileResult result = new ProfileResult(response);
		return result;
	}

	public static BaseResult userInfo(String token) {

		// prepare
		String url = baseAddress + "user_info";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		// post and receive raw data
		ServiceResponse response = MVPApi.get(url, port, requestInf);

		// format data
		BaseResult result = new BaseResult(response);
		return result;
	}

	public static String getUserId(String token) {
		
		BaseResult result = userInfo(token);
		try {
			JSONObject json = new JSONObject(result.response.getResponseString());
			return json.getJSONObject("user").getString("id");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	// goal apis
	public static GoalsResult searchGoal(String token, long startTime, long endTime, long modifiedSince) {
		// prepare
		String url = baseAddress + "goals";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("startTime", String.valueOf(startTime));
		requestInf.addParam("endTime", String.valueOf(endTime));
		requestInf.addParam("updatedAt", String.valueOf(modifiedSince));

		// post and recieve raw data
		ServiceResponse response = MVPApi.get(url, port, requestInf);

		// format data
		GoalsResult result = new GoalsResult(response);
		return result;
	}

	public static GoalsResult getGoal(String token, String serverId) {
		// prepare
		String url = baseAddress + "goals/" + serverId;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		// post and recieve raw data
		ServiceResponse response = MVPApi.get(url, port, requestInf);

		// format data
		GoalsResult result = new GoalsResult(response);
		return result;
	}

	public static GoalsResult createGoal(String token, Goal goal) {

		// prepare
		String url = baseAddress + "goals";
		
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("goal", goal.toJson().toString());

		// post and receive raw data
		ServiceResponse response = MVPApi.post(url, port, requestInf);

		// format data
		GoalsResult result = new GoalsResult(response);
		return result;
	}

	public static GoalsResult updateGoal(String token, Goal goal) {

		// prepare
		String url = baseAddress + "goals/" + goal.getServerId();
		
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("goal", goal.toJson().toString());

		// post and recieve raw data
		ServiceResponse response = MVPApi.put(url, port, requestInf);

		// format data
		GoalsResult result = new GoalsResult(response);
		return result;
	}

	public static BaseResult pushRawData(String token, String goalId, GoalRawData rawData, int offsetMinute) {
		
		// prepare
		String url = baseAddress + "goals/" + goalId + "/raw_data";
		
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addHeader("platform", "android");
		requestInf.addParam("minute_offset", offsetMinute + "");
		requestInf.addParam("data", rawData.toJson().toString());

		// post and receive raw data
		ServiceResponse response = MVPApi.post(url, port, requestInf);

		// format data
		GoalsResult result = new GoalsResult(response);
		return result;
	}
	
	// graph apis
	public static BaseResult createGraphItem(String token, GraphItem item) {

		String url = baseAddress + "graph_items";
		BaseParams request = new BaseParams();

		request.addHeader("auth_token", token);
		request.addParam("graph_item", item.toJson().toString());

		return new BaseResult(MVPApi.post(url, port, request));
	}

	public static ServiceResponse createGraphItems(String token, JSONArray jsonItems) {

		String url = baseAddress + "graph_items/batch_insert";
		BaseParams request = new BaseParams();

		request.addHeader("auth_token", token);
		request.addParam("graph_items", jsonItems.toString());

		return MVPApi.post(url, port, request);
	}

	public static ServiceResponse createGraphItems(String token, List<GraphItem> graphItems) {

		JSONArray jsonItems = new JSONArray();
		for (int i = 0; i < graphItems.size(); i++) {
			jsonItems.put(graphItems.get(i).toJson());
		}

		return createGraphItems(token, jsonItems);
	}

	public static GraphItem getGraphItem(String token, String serverId) {
		// prepare
		String url = baseAddress + "graph_items/" + serverId;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		// post and recieve raw data
		ServiceResponse response = MVPApi.get(url, port, requestInf);

		// format data
		return GraphItem.getGraphItem(response);
	}

	public static List<GraphItem> getGraphItems(String token, long startTime, long endTime, long modifiedSince) {
		String url = baseAddress + "graph_items";
		BaseParams request = new BaseParams();
		ServiceResponse response;
		List<GraphItem> items;

		request.addHeader("auth_token", token);
		request.addParam("startTime", String.valueOf(startTime));
		request.addParam("endTime", String.valueOf(endTime));
		request.addParam("modifiedSince", String.valueOf(modifiedSince));

		response = MVPApi.get(url, port, request);
		items = GraphItem.getGraphItems(response);
		logger.info("Graph items count: " + items.size());

		return items;
	}

	// timeline apis
	public static BaseResult createTimelineItem(String token, TimelineItem item) {

		String url = baseAddress + "timeline_items";
		if(item.getItemType() == TimelineItemDataBase.TYPE_FOOD)
			url += "?attached_image=true";
		BaseParams request = new BaseParams();

		request.addHeader("auth_token", token);
		request.addParam("timeline_item", item.toJson().toString());

		return new BaseResult(MVPApi.post(url, port, request));
	}

	public static ServiceResponse createTimelineItems(String token, JSONArray items) {
		
		// prepare
		String url = baseAddress + "timeline_items/batch_insert";

		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		request.addObjectParam("timeline_items", items.toString());

		// post and receive raw data
		ServiceResponse response = MVPApi.post(url, port, request);
		return response;
	}

	public static ServiceResponse createTimelineItems(String token, List<TimelineItem> items) {

		JSONArray jsonItems = new JSONArray();
		for (int i = 0; i < items.size(); i++) {
			jsonItems.put(items.get(i).toJson());
		}

		return createTimelineItems(token, jsonItems);
	}

	public static TimelineItem getTimelineItem(String token, String serverId) {

		// prepare
		String url = baseAddress + "timeline_items/" + serverId;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		// post and recieve raw data
		ServiceResponse response = MVPApi.get(url, port, requestInf);

		// format data
		return TimelineItem.getTimelineItem(response);
	}

	public static List<TimelineItem> getTimelineItems(String token, long startTime, long endTime, long modifiedSince) {

		String url = baseAddress + "timeline_items";

		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		request.addParam("startTime", String.valueOf(startTime));
		request.addParam("endTime", String.valueOf(endTime));
		request.addParam("modifiedSince", String.valueOf(modifiedSince));

		ServiceResponse response = MVPApi.get(url, port, request);
		List<TimelineItem> items = TimelineItem.getTimelineItems(response);
		logger.info("Timeline items count: " + items.size());

		return items;

	}

	// pedometer apis
	public static Pedometer showPedometer(String token) {
		String url = baseAddress + "pedometer";
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		ServiceResponse response = MVPApi.get(url, port, request);
		Pedometer pedometer = Pedometer.getPedometer(response);
		return pedometer;
	}

	public static String getDeviceLinkingStatus(String token, String serialNumberString) {
		String url = baseAddress + "device_linking_status";
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		request.addParam("serial_number_string", serialNumberString);
		try {
			ServiceResponse response = MVPApi.get(url, port, request);
			String message = Pedometer.getMessage(response);
			return message;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String unlinkDevice(String token) {
		String url = baseAddress + "unlink_device";
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		try {
			ServiceResponse response = MVPApi.put(url, port, request);
			String message = Pedometer.getMessage(response);
			return message;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Pedometer createPedometer(String token, String serialNumberString, String firmwareRevisionString, Long linkedTime, Long unlinkedTime, Long lastSyncedTime, String localId, String serverId, long updatedAt) {
		String url = baseAddress + "pedometer";
		BaseParams request = buildEditPedometerRequest(token, serialNumberString, firmwareRevisionString, linkedTime, unlinkedTime, lastSyncedTime, localId, serverId, updatedAt);
		ServiceResponse response = MVPApi.post(url, port, request);
		Pedometer result = Pedometer.getPedometer(response);
		return result;
	}

	public static BaseResult createPedometer(String token, Pedometer item) {
		
		String url = baseAddress + "pedometer";
		BaseParams request = new BaseParams();

		request.addHeader("auth_token", token);
		request.addParam("pedometer", item.toJson().toString());

		return new BaseResult(MVPApi.post(url, port, request));
	}
	
	public static Pedometer updatePedometer(String token, String serialNumberString, String firmwareRevisionString, Long linkedTime, Long unlinkedTime, Long lastSyncedTime, String localId, String serverId, long updatedAt) {
		String url = baseAddress + "pedometer";
		BaseParams request = buildEditPedometerRequest(token, serialNumberString, firmwareRevisionString, linkedTime, unlinkedTime, lastSyncedTime, localId, serverId, updatedAt);
		ServiceResponse response = MVPApi.put(url, port, request);
		Pedometer result = Pedometer.getPedometer(response);
		return result;
	}
	
	public static BaseResult updatePedometer(String token, Pedometer item) {

		// prepare
		String url = baseAddress + "pedometer";
		BaseParams request = new BaseParams();

		request.addHeader("auth_token", token);
		request.addParam("pedometer", item.toJson().toString());

		// post and recieve raw data
		ServiceResponse response = MVPApi.put(url, port, request);

		// format data
		BaseResult result = new BaseResult(response);
		return result;
	}
	
	public static Pedometer updatePedometer(String token, JSONObject data) {
		
		// prepare
		String url = baseAddress + "pedometer";
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("pedometer", data.toString());

		// post and recieve raw data
		ServiceResponse response = MVPApi.put(url, port, requestInf);

		// format data
		return Pedometer.getPedometer(response);
	}

	public static Pedometer getPedometer(String token) {

		// prepare
		String url = baseAddress + "pedometer";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		// post and receive raw data
		ServiceResponse response = MVPApi.get(url, port, requestInf);

		// format data
		return Pedometer.getPedometer(response);
	}

	private static BaseParams buildEditPedometerRequest(String token, String serialNumberString, String firmwareRevisionString, Long linkedTime, Long unlinkedTime, Long lastSyncedTime, String localId, String serverId, long updatedAt) {
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		Pedometer pedometer = new Pedometer(serialNumberString, firmwareRevisionString, linkedTime, unlinkedTime, lastSyncedTime, localId, serverId, updatedAt);
		request.addObjectParam("pedometer", pedometer.toJson().toString());
		return request;
	}

	public static String getLatestFirmwareVersionString() {

		// prepare
		String url = baseAddress + "shine_firmwares/get_latest";
		BaseParams requestInf = new BaseParams();

		// post and receive raw data
		ServiceResponse response = MVPApi.get(url, port, requestInf);

		// format data
		try {
			JSONObject json = new JSONObject(response.getResponseString());
			return json.getString("version_number");
			
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
	}

	// sync apis
	public static ServiceResponse syncLog(String token, String log) {

		String url = baseAddress + "sync_logs";
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		request.addParam("log", log);

		ServiceResponse response = MVPApi.post(url, port, request);
		logger.info("Sync log status code: " + response.getStatusCode());

		return response;
	}
	
	public static BaseResult pushSyncLog(String token, SyncLog syncLog) {
		
		return pushSyncLog(token, syncLog, null);
	}

	public static BaseResult pushSyncLog(String token, SyncLog syncLog, String platform) {
		
		String url = baseAddress + "sync_logs";
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		if(platform != null) {
			request.removeHeader("platform");
			request.addHeader("platform", platform);
		}
			
		request.addParam("log", syncLog.toJson().toString());

		ServiceResponse response = MVPApi.post(url, port, request);
		logger.info("Sync log status code: " + response.getStatusCode());

		return new BaseResult(response);
	}
	
	public static String getStagingDebugSyncLog(String email, String serialNumber, Long timestamp) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp * 1000);
		
		String s3Path = "staging/" 
				+ cal.get(Calendar.YEAR) + "/" 
				+ String.format("%02d", cal.get(Calendar.MONTH) + 1) + "/" 
				+ String.format("%02d", cal.get(Calendar.DATE)) + "/" 
				+ email + "/" 
				+ serialNumber + "/" 
				+ timestamp + "/"
				+ "debug_log.txt";
		
		logger.info(s3Path);
		return AWSHelper.downloadFileAsString("shine-binary-data", s3Path);
	}
	
	public static String getStagingDebugSyncLog(String s3Path) {
		
		return AWSHelper.downloadFileAsString("shine-binary-data", s3Path);
	}
	
	public static String[] listStagingDebugSyncLogs(String email, String serialNumber, long fromTimestamp, long toTimestamp) {
		
		Calendar fromCal = Calendar.getInstance();
		fromCal.setTimeInMillis(fromTimestamp * 1000);
		int fromYear = fromCal.get(Calendar.YEAR);
		int fromMonth = fromCal.get(Calendar.MONTH) + 1;
		int fromDate = fromCal.get(Calendar.DATE);

		Calendar toCal = Calendar.getInstance();
		toCal.setTimeInMillis(toTimestamp * 1000);
		int toYear = toCal.get(Calendar.YEAR);
		int toMonth = toCal.get(Calendar.MONTH) + 1;
		int toDate = toCal.get(Calendar.DATE);
		
		List<String> result = new ArrayList<String>();
		
		for(int y = fromYear; y <= toYear; y++) {
			for(int m = fromMonth; m <= toMonth; m++) {
				for(int d = fromDate; d <= toDate; d++) {
					
					// prefix for this day
					String prefix = "staging/" 
							+ y + "/" 
							+ String.format("%02d", m) + "/" 
							+ String.format("%02d", d) + "/" 
							+ email + "/" 
							+ serialNumber + "/";
					
					// check if timestamp is in range
					List<String> keys = AWSHelper.listFiles("shine-binary-data", prefix);
					for(String key : keys) {
											
						if(!key.contains("debug_log.txt")) {
							continue;
						}
						
						String[] parts = key.split("/");
						long timestamp = Long.valueOf(parts[6]);
						if(timestamp > toTimestamp || timestamp < fromTimestamp)
							continue;
						
						result.add(key);
					}
				}
			}
		}
		
		return result.toArray(new String[result.size()]);
	}
	
	public static String getLatestSyncLog(String email, String serialNumber, long sinceTimestamp) {
		
		logger.info("Get latest sync log since: " + sinceTimestamp);
		String[] paths = listStagingDebugSyncLogs(email, serialNumber, sinceTimestamp, System.currentTimeMillis() / 1000 + 360);
		if(paths.length == 0)
			return "";
		
		logger.info(paths[paths.length - 1]);
		String log = getStagingDebugSyncLog(paths[paths.length - 1]);
		
		return log;
	}
	
	// statistics
 	public static BaseResult createStatistics(String token, Statistics statistics) {

		// prepare
		String url = baseAddress + "statistics";
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		request.addParam("statistics", statistics.toJson().toString());

		// post and receive raw data
		ServiceResponse response = MVPApi.post(url, port, request);

		// format data
		BaseResult result = new BaseResult(response);
		return result;
	}

	public static BaseResult updateStatistics(String token, Statistics statistics) {

		// prepare
		String url = baseAddress + "statistics";
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		request.addParam("statistics", statistics.toJson().toString());

		// post and receive raw data
		ServiceResponse response = MVPApi.put(url, port, request);

		// format data
		BaseResult result = new BaseResult(response);
		return result;
	}

	public static Statistics getStatistics(String token) {
		
		BaseResult result = userInfo(token);
		return Statistics.fromResponse(result.response);
	}
	
	
	// others
	public static BaseResult customRequest(String shortUrl, String verb, BaseParams params) {
		
		String url = baseAddress + shortUrl;
		ServiceResponse response = MVPApi.request(verb, url, port, params);

		return new BaseResult(response);
	}
	
	// test
	public static void main(String[] args) throws JSONException {
	    
	    
	}

}
