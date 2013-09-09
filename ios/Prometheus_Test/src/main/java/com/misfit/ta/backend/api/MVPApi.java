package com.misfit.ta.backend.api;

import static com.google.resting.component.EncodingTypes.UTF8;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.Resting;
import com.google.resting.component.content.IContentData;
import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.google.resting.method.post.PostHelper;
import com.google.resting.method.put.PutHelper;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.aws.AWSHelper;
import com.misfit.ta.backend.data.account.*;
import com.misfit.ta.backend.data.goal.*;
import com.misfit.ta.backend.data.graph.*;
import com.misfit.ta.backend.data.profile.*;
import com.misfit.ta.backend.data.pedometer.*;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.timeline.*;
import com.misfit.ta.backend.data.*;
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.TextTool;

public class MVPApi {

	// logger
	private static Logger logger = Util.setupLogger(MVPApi.class);

	// fields
	public static String baseAddress = Settings.getValue("MVPBackendBaseAddress");
	public static int port = Integer.parseInt(Settings.getValue("MVPBackendPort"));
	
	public static String HTTP_POST = "POST";
	public static String HTTP_GET = "GET";
	public static String HTTP_PUT = "PUT";
	
	public static int CACHE_TRY_TIME = 10;
	public static String LATEST_FIRMWARE_VERSION_STRING = "0.0.39r";

	// request helpers
	static private ServiceResponse request(String type, String url, int port, BaseParams requestInf) {
		
		// log address
		logger.info(type.toUpperCase() + ": " + url + " - port: " + port);
		logger.info("Request headers: " + requestInf.getHeadersAsJsonString());
		logger.info("Request params: " + requestInf.getParamsAsJsonString());

		// send to TRS
		TRS.instance().addStep(type.toUpperCase() + ": " + url + " - port: " + port, null);
		TRS.instance().addCode("Request headers: " + requestInf.getHeadersAsJsonString(), null);
		TRS.instance().addCode("Request params: " + requestInf.getParamsAsJsonString(), null);

		// wrapper send request
		ServiceResponse response = null;
		ResultLogger.registerRequest();
		if (type.equalsIgnoreCase(MVPApi.HTTP_POST))
			response = PostHelper.post(url, port, UTF8, requestInf.params, requestInf.headers);
		else if (type.equalsIgnoreCase(MVPApi.HTTP_GET))
			response = Resting.get(url, port, requestInf.params, UTF8, requestInf.headers);
		else if (type.equalsIgnoreCase(MVPApi.HTTP_PUT))
			response = PutHelper.put(url, UTF8, port, requestInf.params, requestInf.headers);

		// log result
		IContentData rawData = response.getContentData();
		logger.info("Response raw Data: " + rawData);
		ResultLogger.registerResponse();
		int error = response.getStatusCode();
		ResultLogger.addErrorCode(error);
		logger.info("Response code: " + error + "\n\n");

		// send to TRS
		TRS.instance().addCode("Response raw Data: " + rawData, null);
		TRS.instance().addCode("Response code: " + error + "\n\n", null);

		return response;
	}

	static private ServiceResponse post(String url, int port, BaseParams requestInf) {
		return request("post", url, port, requestInf);
	}

	static private ServiceResponse get(String url, int port, BaseParams requestInf) {
		return request("get", url, port, requestInf);
	}

	static private ServiceResponse put(String url, int port, BaseParams requestInf) {
		return request("put", url, port, requestInf);
	}

	// generators
	public static String generateUniqueEmail() {
		return "test" + System.currentTimeMillis() + TextTool.getRandomString(6, 6) + "@qa.com";
	}

	public static String generateLocalId()
	{
		return System.nanoTime() + "-" + TextTool.getRandomString(10);
	}
	
	public static JSONArray[] generateTimelineItemsAndGraphItems() {
		int numberOfItemsPerDay = 1;
		int numberOfDays = 1;
		numberOfItemsPerDay = Settings.getInt("NUMBER_OF_ITEMS_PER_DAY");
		numberOfDays = Settings.getInt("NUMBER_OF_DAYS");
		return generateTimelineItemsAndGraphItems(numberOfDays, numberOfItemsPerDay);
	}

	public static JSONArray[] generateTimelineItemsAndGraphItems(int numberOfDays, int numberOfItemsPerDay) {

		JSONArray timelineItems = new JSONArray();
		JSONArray graphItems = new JSONArray();
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		long now = System.currentTimeMillis() / 1000;
		// data for a number of days
		long period = 86400 / numberOfItemsPerDay;
		for (int k = 0; k < numberOfDays; k++) {
			long tmp = now - (k * period * 24);
			// one weather per day
			TimelineItem timeline = generateWeatherTimelineItem(tmp);
			timelineItems.put(timeline.toJson());

			for (int j = 0; j < numberOfItemsPerDay; j++) {
				// one timeline item per hour
				tmp -= 3600;

				// df.format(new Date(tmp));

				TimelineItem tmpItem = generateActivitySessionItem(tmp);
				timelineItems.put(tmpItem.toJson());

				// one graph item per hour
				// TODO: one graph item per 2020s
				GraphItem graphItem = new GraphItem(tmp, 1, tmp);
				graphItems.put(graphItem.toJson());
			}
		}

		JSONArray[] array = new JSONArray[2];
		array[0] = timelineItems;
		array[1] = graphItems;
		return array;
	}

	public static TimelineItem generateActivitySessionItem(long tmp) {
		ActivitySessionItem session = new ActivitySessionItem(tmp, 2222, 22, tmp, 22, 2, 22, 22);
		TimelineItem tmpItem = new TimelineItem(TimelineItemBase.TYPE_SESSION, tmp, tmp, session, TextTool.getRandomString(19, 20), null, null);
		return tmpItem;
	}

	public static TimelineItem generateWeatherTimelineItem(long tmp) {
		WeatherItem weather = new WeatherItem(tmp, 100, 200, "Stockholm");
		TimelineItem timeline = new TimelineItem(TimelineItemBase.TYPE_WEATHER, tmp, tmp, weather, TextTool.getRandomString(19, 20), null, null);
		return timeline;
	}

	public static TimelineItem generateNotableEventItem(long tmp, int value) {
		NotableEventItem notableEventItem = new NotableEventItem(tmp, null, null, value, 1);
		TimelineItem timeline = new TimelineItem(TimelineItemBase.TYPE_NOTABLE, tmp, tmp, notableEventItem, TextTool.getRandomString(19, 20), null, null);
		return timeline;
	}

	public static String generateSyncLog() {

		return generateSyncLog(TextTool.getRandomString(10), System.currentTimeMillis() / 1000);
	}
	
	public static String generateSyncLog(String serialNumber, Long timestamp) {
		String log = "{\"startTime\": " + timestamp 
				+ ", \"endTime\": " + (timestamp + 10000) 
				+ ", \"timestamp\": " + timestamp
				+ ", \"serialNumberString\": \"" + serialNumber
				+ "\", \"isSuccessful\": 1, \"data\": {\"fileData\": [ {\"rawData\": \"0101010101\", \"timestampDifference\": 1 } ], \"hardwareLog\": \"misfit\" }, \"log\": \"misfit\"}";
	
		return log;
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
	static private BaseParams createProfileParams(String token, String name, Double weight, Double height, Integer gender, Long dateOfBirth, Integer goalLevel, String latestVersion, String wearingPosition, PersonalRecord personalRecords, DisplayUnit displayedUnits, String localId, Long updatedAt) {

		// build json object string
		ProfileData profile = new ProfileData(null, updatedAt, localId, weight, height, gender, dateOfBirth, name, goalLevel, latestVersion, wearingPosition, personalRecords, displayedUnits);

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("profile", profile.toJson().toString());

		return requestInf;
	}

	public static ProfileResult createProfile(String token, ProfileData data) {

		// prepare
		String url = baseAddress + "profile";
		BaseParams requestInf = createProfileParams(token, data.getName(), data.getWeight(), data.getHeight(), data.getGender(), data.getDateOfBirth(), data.getGoalLevel(), data.getLatestVersion(), data.getWearingPosition(), data.getPersonalRecords(), data.getDisplayedUnits(), data.getLocalId(), data.getUpdatedAt());

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

	public static ProfileResult updateProfile(String token, ProfileData data, String serverId) {
		logger.info("Id: " + serverId + ", Updated at: " + data.getUpdatedAt());

		// prepare
		String url = baseAddress + "profile";
		BaseParams requestInf = createProfileParams(token, data.getName(), data.getWeight(), data.getHeight(), data.getGender(), data.getDateOfBirth(), data.getGoalLevel(), data.getLatestVersion(), data.getWearingPosition(), data.getPersonalRecords(), data.getDisplayedUnits(), data.getLocalId(), data.getUpdatedAt());

		requestInf.addParam("id", serverId);

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

	// goal apis
	static private BaseParams createGoalParams(String token, Double goalValue, Long startTime, Long endTime, Integer timeZoneOffsetInSeconds, ProgressData progressData, List<TrippleTapData> trippleTapTypeChanges, String localId, Long updatedAt) {

		// build json object string
		Goal goal = new Goal(goalValue, startTime, endTime, timeZoneOffsetInSeconds, progressData, trippleTapTypeChanges, localId, updatedAt);

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("auth_token", token);
		requestInf.addParam("goal", goal.toJson().toString());

		return requestInf;
	}

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

	public static GoalsResult createGoal(String token, double goalValue, long startTime, long endTime, int timeZoneOffsetInSeconds, ProgressData progressData, List<TrippleTapData> trippleTapTypeChanges, String localId, long updatedAt) {

		// prepare
		String url = baseAddress + "goals";

		BaseParams requestInf = createGoalParams(token, goalValue, startTime, endTime, timeZoneOffsetInSeconds, progressData, trippleTapTypeChanges, localId, updatedAt);

		// post and receive raw data
		ServiceResponse response = MVPApi.post(url, port, requestInf);

		// format data
		GoalsResult result = new GoalsResult(response);
		return result;
	}

	public static GoalsResult createGoal(String token, Goal goal) {
		return createGoal(token, goal.getValue(), goal.getStartTime(), goal.getEndTime(), goal.getTimeZoneOffsetInSeconds(), goal.getProgressData(), goal.getTripleTapTypeChanges(), goal.getLocalId(), goal.getUpdatedAt());
	}

	public static GoalsResult updateGoal(String token, long updatedAt, String serverId, double goalValue, long startTime, long endTime, int timeZoneOffsetInSeconds, ProgressData progressData, List<TrippleTapData> trippleTapTypeChanges, String localId) {

		// prepare
		String url = baseAddress + "goals/" + serverId;

		BaseParams requestInf = createGoalParams(token, goalValue, startTime, endTime, timeZoneOffsetInSeconds, progressData, trippleTapTypeChanges, localId, updatedAt);

		// post and recieve raw data
		ServiceResponse response = MVPApi.put(url, port, requestInf);

		if (response.getStatusCode() != 200 && response.getStatusCode() != 210) {
			return null;
		}
		GoalsResult result = new GoalsResult(response);
		return result;
	}

	public static GoalsResult updateGoal(String token, Goal goal) {

		// prepare
		String url = baseAddress + "goals/" + goal.getServerId();

		BaseParams requestInf = createGoalParams(token, goal.getValue(), goal.getStartTime(), goal.getEndTime(), goal.getTimeZoneOffsetInSeconds(), goal.getProgressData(), goal.getTripleTapTypeChanges(), goal.getLocalId(), goal.getUpdatedAt());

		// post and recieve raw data
		ServiceResponse response = MVPApi.put(url, port, requestInf);

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
