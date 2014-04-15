package com.misfit.ta.backend.api.internalapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.EncodingTypes;
import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.InsecureHttpClientHelper;
import com.misfit.ta.backend.api.RequestHelper;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.data.beddit.BedditSleepSession;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.goalprogress.GoalProgress;
import com.misfit.ta.backend.data.goalprogress.GoalSettings;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.sync.SyncLog;
import com.misfit.ta.backend.data.sync.sdk.SDKSyncLog;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.base.AWSHelper;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.TextTool;

public class MVPApi extends RequestHelper {

	// logger
	protected static Logger logger = Util.setupLogger(MVPApi.class);

	
	// fields
	public static String baseAddress = Settings.getValue("MVPBackendBaseAddress");
	public static Integer port = Settings.getValue("MVPBackendPort") == null ? null : 
		Integer.parseInt(Settings.getValue("MVPBackendPort"));
	
	public static String dataCenterBaseAddress = Settings.getValue("MVPDataCenterBaseAddress");
	public static Integer dataCenterPort = Settings.getValue("MVPDataCenterPort") == null ? null : 
		Integer.parseInt(Settings.getValue("MVPDataCenterPort"));
	
	public static int CACHE_TRY_TIME = 10;
	public static String LATEST_FIRMWARE_VERSION_STRING = "0.0.65r";

	
	// generators
	public static String generateUniqueEmail() {
		return "test" + System.currentTimeMillis() + TextTool.getRandomString(6, 6).toLowerCase() + "@misfitqa.com";
	}

	public static String generateLocalId()
	{
		return System.nanoTime() + "-" + TextTool.getRandomString(10, 10);
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
	
	public static ProfileResult getProfileOfUserId(String token, String userid) {
		// prepare
		String url = baseAddress + "profiles/" + userid;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		// post and receive raw data
		ServiceResponse response = MVPApi.get(url, port, requestInf);

		// format data
		ProfileResult result = new ProfileResult(response);
		return result;
	}
	
	
	// goal apis
	public static GoalsResult searchGoal(String token, Long startTime, Long endTime, Long modifiedSince) {
		
		// prepare
		String url = baseAddress + "goals";
		String queryString = "";
		
		if(startTime != null)
			queryString += ("&startTime=" + startTime);
		
		if(endTime != null)
			queryString += ("&endTime=" + endTime);
		
		if(modifiedSince != null)
			queryString += ("&modifiedSince=" + modifiedSince);
		
		url += ("?" + queryString);

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		
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
	
	public static BaseResult getRawDataAsString(Long timestamp, Integer timezoneOffsetInMinutes,
			String fileHandle, String fileFormat, GoalRawData rawData) {
	
		// prepare
		String url = baseAddress + "internal/convert_shine_binary";
		
		JSONObject header = new JSONObject();
		try {
			header.accumulate("timestamp", timestamp);
			header.accumulate("file_handler", fileHandle);
			header.accumulate("file_format_id", fileFormat);
			header.accumulate("timezone", timezoneOffsetInMinutes);
		}
		catch (Exception e) {
		}
		
		logger.info(header.toString());
		
		JSONObject rawDataJson = rawData.toJson();
		JSONObject data = new JSONObject();
		try {
			data.accumulate("headers", header);
			data.put("points", rawDataJson.getJSONArray("points"));
			data.put("steps", rawDataJson.getJSONArray("steps"));
			data.put("variances", rawDataJson.getJSONArray("variances"));
			data.put("triple_tap_minutes", rawDataJson.getJSONArray("triple_tap_minutes"));
		}
		catch (Exception e) {
		}
		
		BaseParams requestInf = new BaseParams();
		requestInf.addParam("data", data.toString());

		// post and receive raw data
		ServiceResponse response = MVPApi.post(url, port, requestInf);

		// format data
		BaseResult result = new BaseResult(response);
		return result;
	}
	
	public static BaseResult searchGoalProgress(String token, Integer type, Long startTime, Long endTime, Long modifiedSince) {
	
		// prepare
		String url = baseAddress + "goal_progresses";
		String queryString = "";

		if(type != null)
			queryString += ("&type=" + type);
		
		if(startTime != null)
			queryString += ("&startTime=" + startTime);

		if(endTime != null)
			queryString += ("&endTime=" + endTime);

		if(modifiedSince != null)
			queryString += ("&modifiedSince=" + modifiedSince);

		url += ("?" + queryString);

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		// post and recieve raw data
		ServiceResponse response = MVPApi.get(url, port, requestInf);

		// format data
		BaseResult result = new BaseResult(response);
		return result;

	}

	public static BaseResult createGoalProgress(String token, Integer type, GoalProgress goalProgress) {

		// prepare
		String url = baseAddress + "goal_progresses" + (type == null ? "" : ("?type=" + type));
		
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("goal_progress", goalProgress.toJson().toString());

		// post and receive raw data
		ServiceResponse response = MVPApi.post(url, port, requestInf);

		// format data
		BaseResult result = new BaseResult(response);
		return result;
	}
	
	public static BaseResult updateGoalProgress(String token, GoalProgress goalProgress) {

		// prepare
		String url = baseAddress + "goal_progresses/" + goalProgress.getServerId();
		
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("goal_progress", goalProgress.toJson().toString());

		// post and receive raw data
		ServiceResponse response = MVPApi.put(url, port, requestInf);

		// format data
		BaseResult result = new BaseResult(response);
		return result;
	}
	
	public static BaseResult getGoalSettings(String token, Integer type) {
		
		// prepare
		String url = baseAddress + "goal_settings" + (type == null ? "" : ("?type=" + type));

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		// post and recieve raw data
		ServiceResponse response = MVPApi.get(url, port, requestInf);

		// format data
		BaseResult result = new BaseResult(response);
		return result;
	}
	
	public static BaseResult setGoalSettings(String token, Integer type, GoalSettings goalSettings) {
		
		// prepare
		String url = baseAddress + "goal_settings" + (type == null ? "" : ("?type=" + type));

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("goal_settings", goalSettings.toJson().toString());

		// post and recieve raw data
		ServiceResponse response = MVPApi.post(url, port, requestInf);

		// format data
		BaseResult result = new BaseResult(response);
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

	public static List<GraphItem> getGraphItems(String token, Long startTime, Long endTime, Long modifiedSince) {

		String url = baseAddress + "graph_items";
		String queryString = "";
		
		if(startTime != null)
			queryString += ("&startTime=" + startTime);
		
		if(endTime != null)
			queryString += ("&endTime=" + endTime);
		
		if(modifiedSince != null)
			queryString += ("&modifiedSince=" + modifiedSince);
		
		url += ("?" + queryString);

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
	
		ServiceResponse response = MVPApi.get(url, port, requestInf);
		List<GraphItem> items = GraphItem.getGraphItems(response);

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
		request.addParam("timeline_items", items.toString());

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

	public static BaseResult updateTimelineItem(String token, TimelineItem item) {
		
		String url = baseAddress + "timeline_items/" + item.getServerId(); 
		BaseParams request = new BaseParams();

		request.addHeader("auth_token", token);
		request.addParam("timeline_item", item.toJson().toString());

		return new BaseResult(MVPApi.put(url, port, request));
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

	public static List<TimelineItem> getTimelineItems(String token, Long startTime, Long endTime, Long modifiedSince) {

		String url = baseAddress + "timeline_items";
		String queryString = "";
		
		if(startTime != null)
			queryString += ("&startTime=" + startTime);
		
		if(endTime != null)
			queryString += ("&endTime=" + endTime);
		
		if(modifiedSince != null)
			queryString += ("&modifiedSince=" + modifiedSince);
		
		url += ("?" + queryString);
		
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = MVPApi.get(url, port, requestInf);
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
		String url = baseAddress + "device_linking_status" + (serialNumberString == null ? "" :
			"?serial_number_string=" + serialNumberString);
		
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		try {
			ServiceResponse response = MVPApi.get(url, port, request);
			String message = Pedometer.getMessage(response);
			return message;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static BaseResult getDeviceLinkingStatusRaw(String token, String serialNumberString) {
		
		String url = baseAddress + "device_linking_status" + (serialNumberString == null ? "" :
			"?serial_number_string=" + serialNumberString);
		
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		ServiceResponse response = MVPApi.get(url, port, request);
		
		return new BaseResult(response);
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
		request.addParam("pedometer", pedometer.toJson().toString());
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

	public static BaseResult generateNewSerialNumber(String token) {
		
		String url = baseAddress + "shine_serials/issue";
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		
		ServiceResponse response = MVPApi.post(url, port, requestInf);
		return new BaseResult(response);
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
	
	public static BaseResult pushSDKSyncLog(SDKSyncLog syncLog, boolean gzip) {
	
		String url = dataCenterBaseAddress + "events";
		CloseableHttpClient httpclient = InsecureHttpClientHelper.getInsecureCloseableHttpClient();

		String body = syncLog.toJson().toString();
		if(gzip) {
			try {
				body = MVPCommon.compressGzip(body);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	    EntityBuilder entityBuilder = org.apache.http.client.entity.EntityBuilder.create();
		entityBuilder.setText(body);
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		if(gzip) {
			httpPost.addHeader("Content-Encoding", "gzip");
		}
		httpPost.addHeader("access_key_id", "39347523984598654-ajwoeifja399438ga3948g494g843fff");
		httpPost.setEntity(entityBuilder.build());
		
		// for debug only
		BaseParams params = new BaseParams();
		params.headers = Arrays.asList(httpPost.getAllHeaders());
		
		try {			
			logger.info("POST: " + url);
			logger.info("Request headers: " + params.getHeadersAsJsonString());
			logger.info("Request params: " + syncLog.toJson().toString());
			
			long start = System.currentTimeMillis();
			CloseableHttpResponse response = httpclient.execute(httpPost);
			ServiceResponse sr = new ServiceResponse(response, EncodingTypes.UTF8);
			long end = System.currentTimeMillis();
			
	        HttpEntity entity = response.getEntity();
	        EntityUtils.consume(entity);

	        response.close();
	        BaseResult result = new BaseResult(sr);
	        
	        logger.error("Time taken in REST: " + (end - start));
	        logger.info("Response raw Data: " + result.rawData);
	        logger.info("Response code: " + result.statusCode + "\n\n");
	        
	        return result;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static BaseResult pushSDKSyncLog(SDKSyncLog syncLog) {
		
		return pushSDKSyncLog(syncLog, false);
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

	public static BaseResult getSummaryByMonth(String token, Long startTimestamp) {
	
		String url = baseAddress + "aggregate/monthly?";
		String startDate = MVPCommon.getDateString(startTimestamp);
		if(startTimestamp != null)
			url += ("start_date=" + startDate);

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = MVPApi.get(url, port, requestInf);

		return new BaseResult(response);
	}
	
	public static BaseResult getSummaryByWeek(String token, Long startTimestamp) {
		
		String url = baseAddress + "aggregate/weekly?";
		String startDate = MVPCommon.getDateString(startTimestamp);
		if(startTimestamp != null)
			url += ("start_date=" + startDate);

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		ServiceResponse response = MVPApi.get(url, port, requestInf);

		return new BaseResult(response);
	}
	
	
	// beddit sleeps
	public static BaseResult searchBedditSleepSessions(String token, Long startTime, Long endTime, Long modifiedSince) {
		
		// prepare
		String url = baseAddress + "beddit/sleep_sessions";
		String queryString = "";
		
		if(startTime != null)
			queryString += ("&startTime=" + startTime);
		
		if(endTime != null)
			queryString += ("&endTime=" + endTime);
		
		if(modifiedSince != null)
			queryString += ("&modifiedSince=" + modifiedSince);
		
		url += ("?" + queryString);
		
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);

		// post and recieve raw data
		ServiceResponse response = MVPApi.get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult createBedditSleepSession(String token, BedditSleepSession sleep) {

		// prepare
		String url = baseAddress + "beddit/sleep_sessions";
		
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("sleep_session", sleep.toJson().toString());

		// post and recieve raw data
		ServiceResponse response = MVPApi.post(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult createBedditSleepSessions(String token, List<BedditSleepSession> sleeps) {

		// prepare
		String url = baseAddress + "beddit/sleep_sessions/batch_insert";
		
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		
		JSONArray jsonarr = new JSONArray(sleeps);
		requestInf.addParam("sleep_sessions", jsonarr.toString());

		// post and recieve raw data
		ServiceResponse response = MVPApi.post(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult updateBedditSleepSession(String token, BedditSleepSession sleep) {

		// prepare
		String url = baseAddress + "beddit/sleep_sessions/" + sleep.getServerId();
		
		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("sleep_session", sleep.toJson().toString());

		// post and recieve raw data
		ServiceResponse response = MVPApi.put(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
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
