package com.misfit.ta.backend.api;

import static com.google.resting.component.EncodingTypes.UTF8;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import netscape.javascript.JSObject;

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
import com.misfit.ta.backend.data.AccountResult;
import com.misfit.ta.backend.data.ActivityResult;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.GoalsResult;
import com.misfit.ta.backend.data.JSONBuilder;
import com.misfit.ta.backend.data.ProfileData;
import com.misfit.ta.backend.data.ProfileResult;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.timeline.ActivitySessionItem;
import com.misfit.ta.backend.data.timeline.NotableEventItem;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.TimelineItemBase;
import com.misfit.ta.backend.data.timeline.WeatherItem;
import com.misfit.ta.utils.TextTool;
import com.thoughtworks.selenium.condition.Not;

public class MVPApi {

    // logger
    private static Logger logger = Util.setupLogger(MVPApi.class);

    // fields
    public static String baseAddress = Settings.getValue("MVPBackendBaseAddress");
    public static int port = Integer.parseInt(Settings.getValue("MVPBackendPort"));

    // helpers
    static private ServiceResponse request(String type, String url, int port, BaseParams requestInf) {
        // log address
        logger.info(type.toUpperCase() + ": " + url + " - port: " + port);

        // wrapper send request
        ServiceResponse response = null;

        if (type == "post")
            response = PostHelper.post(url, port, UTF8, requestInf.params, requestInf.headers);
        else if (type == "get")
            response = Resting.get(url, port, requestInf.params, UTF8, requestInf.headers);
        else if (type == "put")
            response = PutHelper.put(url, UTF8, port, requestInf.params, requestInf.headers);

        // log result
        IContentData rawData = response.getContentData();
        logger.info("Raw Data: " + rawData + "\n");

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

    public static String generateUniqueEmail() {
        return System.currentTimeMillis() + TextTool.getRandomString(6, 6) + "@qa.com";
    }

    // account apis
    static private AccountResult sign(String email, String password, String udid, String shortUrl) {
        // trace
        logger.info("Email: " + email + ", Password: " + password + ", Udid: " + udid);

        // prepare
        String url = baseAddress + shortUrl;

        BaseParams requestInf = new BaseParams();
        requestInf.addParam("email", email);
        requestInf.addParam("password", password);
        requestInf.addParam("udid", udid);

        // post and receive raw data
        ServiceResponse response = MVPApi.post(url, port, requestInf);
        System.out.println("LOG [MVPApi.sign]: response: " + response.getContentData());
        // format data
        AccountResult result = new AccountResult(response);
        return result;
    }

    public static AccountResult signIn(String email, String password, String udid) {
        return sign(email, password, udid, "login");
    }

    public static AccountResult signUp(String email, String password, String udid) {
        return sign(email, password, udid, "signup");
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
    static private BaseParams createProfileParams(String token, String name, Double weight, Double height,
            Integer unit, Integer gender, Long dateOfBirth, Integer goalLevel, String trackingDeviceId, String localId,
            String latestVersion, Long updatedAt) {
        // build json object string

        JSONBuilder json = new JSONBuilder();
        if (name != null)
            json.addValue("name", name);
        if (weight != null)
            json.addValue("weight", weight);
        if (height != null)
            json.addValue("height", height);
        if (unit != null)
            json.addValue("unit", unit);
        if (gender != null)
            json.addValue("gender", gender);
        if (dateOfBirth != null)
            json.addValue("dateOfBirth", dateOfBirth);
        if (goalLevel != null)
            json.addValue("goalLevel", goalLevel);
        if (trackingDeviceId != null)
            json.addValue("trackingDeviceId", trackingDeviceId);
        if (localId != null)
            json.addValue("localId", localId);
        if (latestVersion != null)
            json.addValue("latestVersion", latestVersion);
        if (updatedAt != null)
            json.addValue("updatedAt", updatedAt);

        BaseParams requestInf = new BaseParams();
        requestInf.addHeader("auth_token", token);
        requestInf.addParam("profile", json.toJSONString());

        return requestInf;
    }

    public static ProfileResult createProfile(String token, ProfileData data) {
        // prepare
        String url = baseAddress + "profile";

        BaseParams requestInf = createProfileParams(token, data.name, data.weight, data.height, data.unit, data.gender,
                data.dateOfBirth, data.goalLevel, data.trackingDeviceId, data.localId, data.latestVersion, null);

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

    public static ProfileResult updateProfile(String token, ProfileData data, String id) {
        logger.info("Id: " + id + ", Updated at: " + data.updatedAt);
        // prepare
        String url = baseAddress + "profile";

        BaseParams requestInf = createProfileParams(token, data.name, data.weight, data.height, data.unit, data.gender,
                data.dateOfBirth, data.goalLevel, data.trackingDeviceId, data.localId, data.latestVersion,
                data.updatedAt);

        requestInf.addParam("id", id);

        // post and recieve raw data
        ServiceResponse response = MVPApi.put(url, port, requestInf);

        // format data
        ProfileResult result = new ProfileResult(response);
        return result;
    }

    // goal apis
    static private BaseParams createGoalParams(String token, Double goalValue, Long startTime, Long endTime,
            Integer absoluteLevel, Integer userRelativeLevel, Integer timeZoneOffsetInSeconds,
            String[] progressValuesInMinutesNSData, String localId, Long updatedAt) {
        // build json object string
        JSONBuilder json = new JSONBuilder();
        if (goalValue != null)
            json.addValue("goalValue", goalValue);
        if (startTime != null)
            json.addValue("startTime", startTime);
        if (endTime != null)
            json.addValue("endTime", endTime);
        if (absoluteLevel != null)
            json.addValue("absoluteLevel", absoluteLevel);
        if (userRelativeLevel != null)
            json.addValue("userRelativeLevel", userRelativeLevel);
        if (timeZoneOffsetInSeconds != null)
            json.addValue("timeZoneOffsetInSeconds", timeZoneOffsetInSeconds);
        if (progressValuesInMinutesNSData != null)
            json.addValue("progressValuesInMinutesNSData", Arrays.toString(progressValuesInMinutesNSData));
        if (localId != null)
            json.addValue("localId", localId);
        if (updatedAt != null)
            json.addValue("updatedAt", updatedAt);

        BaseParams requestInf = new BaseParams();
        requestInf.addHeader("auth_token", token);
        requestInf.addParam("goal", json.toJSONString());

        return requestInf;
    }

    public static GoalsResult searchGoal(String token, Long startTime, Long endTime, Long modifiedSince) {
        // prepare
        String url = baseAddress + "goals";

        BaseParams requestInf = new BaseParams();
        requestInf.addHeader("auth_token", token);
        requestInf.addParam("startTime", startTime.toString());
        requestInf.addParam("endTime", endTime.toString());
        requestInf.addParam("updatedAt", modifiedSince.toString());

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

    public static GoalsResult createGoal(String token, Double goalValue, Long startTime, Long endTime,
            Integer absoluteLevel, Integer userRelativeLevel, Integer timeZoneOffsetInSeconds,
            String[] progressValuesInMinutesNSData, String localId) {
        // prepare
        String url = baseAddress + "goals";

        BaseParams requestInf = createGoalParams(token, goalValue, startTime, endTime, absoluteLevel,
                userRelativeLevel, timeZoneOffsetInSeconds, progressValuesInMinutesNSData, localId, null);

        // post and receive raw data
        ServiceResponse response = MVPApi.post(url, port, requestInf);

        // format data
        GoalsResult result = new GoalsResult(response);
        return result;
    }

    public static GoalsResult updateGoal(String token, Long updatedAt, Double goalValue, Long startTime, Long endTime,
            Integer absoluteLevel, Integer userRelativeLevel, Integer timeZoneOffsetInSeconds,
            String[] progressValuesInMinutesNSData, String localId) {
        // prepare
        String url = baseAddress + "profile";

        BaseParams requestInf = createGoalParams(token, goalValue, startTime, endTime, absoluteLevel,
                userRelativeLevel, timeZoneOffsetInSeconds, progressValuesInMinutesNSData, localId, updatedAt);

        // post and recieve raw data
        ServiceResponse response = MVPApi.put(url, port, requestInf);

        // format data
        GoalsResult result = new GoalsResult(response);
        return result;
    }

    public static GoalsResult updateGoal(String token, GoalsResult.Goal goal) {
        // prepare
        String url = baseAddress + "profile";

        BaseParams requestInf = createGoalParams(token, goal.goalValue, goal.startTime, goal.endTime,
                goal.absoluteLevel, goal.userRelativeLevel, goal.timeZoneOffsetInSeconds,
                goal.progressValuesInMinutesNSData, goal.localId, goal.updatedAt);

        // post and recieve raw data
        ServiceResponse response = MVPApi.put(url, port, requestInf);

        // format data
        GoalsResult result = new GoalsResult(response);
        return result;
    }

    // Activity APIs
    public static ActivityResult getActivity(String token, Object id) {
        // prepare
        String url = baseAddress + "activities/" + id.toString();
        BaseParams requestInf = new BaseParams();

        requestInf.addHeader("auth_token", token);

        // make GET request and receive raw data
        ServiceResponse response = MVPApi.get(url, port, requestInf);

        // format data
        ActivityResult result = new ActivityResult(response);

        return result;
    }

    public static List<ActivityResult> searchActivity(String token, Object startTime, Object endTime,
            Object modifiedSince) throws JSONException {
        // prepare
        String url = baseAddress + "activities";
        BaseParams requestInf = new BaseParams();

        requestInf.addHeader("auth_token", token);
        requestInf.addParam("startTime", startTime.toString());
        requestInf.addParam("endTime", endTime.toString());
        requestInf.addParam("modifiedSince", modifiedSince.toString());

        // make GET request and receive raw data
        ServiceResponse response = MVPApi.get(url, port, requestInf);

        // format data
        ArrayList<ActivityResult> activities = ActivityResult.getActivityResults(response);

        return activities;
    }

    public static List<ActivityResult> createActivities(String token, List<ActivityResult> activities, Object serverId,
            Object clientId, Object updatedAt) throws JSONException {
        // prepare
        String url = baseAddress + "activities";
        BaseParams requestInf = new BaseParams();

        requestInf.addHeader("auth_token", token);
        requestInf.addParam("serverId", serverId.toString());
        requestInf.addParam("clientId", clientId.toString());
        requestInf.addParam("updatedAt", updatedAt.toString());

        // make POST request and receive raw data
        ServiceResponse response = MVPApi.post(url, port, requestInf);

        // format data
        activities = ActivityResult.getActivityResults(response);

        return activities;
    }

    public static void removeUser(String id) {
        BaseParams requestInf = new BaseParams();
        requestInf.addParam("authenticity_token", "ki5608apM0mqEpFwvNW6i8Czu6tktVT0+UlWWVhu0Mg");
        MVPApi.post("https://staging-api.misfitwearables.com/shine/v6/admin/delete_user?id=" + id, 443, requestInf);
    }

    public static ServiceResponse createGraphItems(String token,
    		JSONArray jsonItems) {
        String url = baseAddress + "graph_items/batch_insert";
        BaseParams request = new BaseParams();
        ServiceResponse response;
        
        request.addHeader("auth_token", token);
        request.addParam("graph_items", jsonItems.toString());
        
        response = MVPApi.post(url, port, request);
        
        return response;
    }
    
    public static ServiceResponse createGraphItems(String token,
    		List<GraphItem> graphItems) throws JSONException {
        JSONArray jsonItems = new JSONArray();
        
        for (int i = 0; i < graphItems.size(); i++) {
        	jsonItems.put(graphItems.get(i).toJson());
        }
        
        return createGraphItems(token, jsonItems);
    }
    
	public static List<GraphItem> getGraphItems(String token,
			long startTime,
			long endTime,
			long modifiedSince) {
		String url = baseAddress + "graph_items";
		BaseParams request = new BaseParams();
		ServiceResponse response;
		List<GraphItem> items;

		request.addHeader("auth_token", token);
		request.addParam("startTime", String.valueOf(startTime));
		request.addParam("endTime", String.valueOf(endTime));
		request.addParam("modifiedSince", String.valueOf(modifiedSince));

		try {
			response = MVPApi.get(url, port, request);
			items = GraphItem.getGraphItems(response);
			System.out.println("LOG [MVPApi.getGraphItems]: count = "
					+ items.size());

			return items;
		} catch (JSONException e) {
			e.printStackTrace();

			return null;
		}
	}

    public static List<TimelineItem> getTimelineItems(String token, long startTime, long endTime, long modifiedSince) {
        String url = baseAddress + "timeline_items";

        BaseParams request = new BaseParams();
        request.addHeader("auth_token", token);
        ServiceResponse response;
        try {

            int count = 0;
//             request.addParam("startTime", String.valueOf(startTime));
//             request.addParam("endTime", String.valueOf(endTime));
            request.addParam("modifiedSince", String.valueOf(modifiedSince));
            response = MVPApi.get(url, port, request);
            List<TimelineItem> items = TimelineItem.getTimelineItems(response);
            count += items.size();
            System.out.println("LOG [MVPApi.getTimelineItems]: count= " + count);
            return items;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

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
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long now = System.currentTimeMillis() / 1000;
        System.out.println("LOG [MVPApi.generateTimelineItemsAndGraphItems]: now1=" + now);
        System.out.println("LOG [MVPApi.generateTimelineItemsAndGraphItems]: now= " + df.format(new Date(now * 1000)));
        // data for a number of days
        long period = 86400 / numberOfItemsPerDay;
        System.out.println("LOG [MVPApi.generateTimelineItemsAndGraphItems]: period= " + period);
        for (int k = 0; k < numberOfDays; k++) {
            long tmp = now - (k * period * 24);
            // one weather per day
            TimelineItem timeline = generateWeatherTimelineItem(tmp);
            timelineItems.put(timeline.toJson());

            for (int j = 0; j < numberOfItemsPerDay; j++) {
                // one timeline item per hour
                tmp -= 3600;

                // df.format(new Date(tmp));
                System.out.println("LOG [MVPApi.generateTimelineItemsAndGraphItems]: tmp= " + tmp);
                System.out.println("LOG [MVPApi.generateTimelineItemsAndGraphItems]: date: "
                        + df.format(new Date(tmp * 1000)));

                TimelineItem tmpItem = generateActivitySessionItem(tmp);
                timelineItems.put(tmpItem.toJson());

                // one graph item per hour
                //TODO: one graph item per 2020s 
                GraphItem graphItem = new GraphItem(tmp, 1, tmp);
                graphItems.put(graphItem.toJson());
            }
        }

        JSONArray[] array = new JSONArray[2];
        array[0] = timelineItems;
        array[1] = graphItems;
        // System.exit(0);
        return array;
    }

	public static TimelineItem generateActivitySessionItem(long tmp) {
		ActivitySessionItem session = new ActivitySessionItem(tmp, 2222, 22, tmp, 22, 2, 22, 22);
		TimelineItem tmpItem = new TimelineItem(TimelineItemBase.TYPE_SESSION, tmp, tmp, session, TextTool
		        .getRandomString(19, 20), null, null);
		return tmpItem;
	}

	public static TimelineItem generateWeatherTimelineItem(long tmp) {
		WeatherItem weather = new WeatherItem(tmp, 100, 200, "Stockholm");
		TimelineItem timeline = new TimelineItem(TimelineItemBase.TYPE_WEATHER, tmp, tmp, weather, TextTool
		        .getRandomString(19, 20), null, null);
		return timeline;
	}
	
	public static TimelineItem generateNotableEventItem(long tmp, int value) {
		NotableEventItem notableEventItem = new NotableEventItem(tmp, null, null, value, 1);
		TimelineItem timeline = new TimelineItem(TimelineItemBase.TYPE_NOTABLE, tmp, tmp, notableEventItem, TextTool
		        .getRandomString(19, 20), null, null);
		return timeline;
	}
	
	public static Pedometer showPedometer(String token) {
		String url = baseAddress + "pedometer";
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		ServiceResponse response;
		try {
			response = MVPApi.get(url, port, request);
			Pedometer pedometer = Pedometer.getPedometer(response);
			return pedometer;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getDeviceLinkingStatus(String token, String serialNumberString) {
		String url = baseAddress + "device_linking_status";
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		request.addParam("serial_number_string", serialNumberString);
		ServiceResponse response;
		try {
			response = MVPApi.get(url, port, request);
			String message = Pedometer.getMessage(response);
			return message;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String unlinkDevice(String token, String serialNumberString) {
		String url = baseAddress + "unlink_device";
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		request.addParam("serial_number_string", serialNumberString);
		ServiceResponse response;
		try {
			response = MVPApi.put(url, port, request);
			String message = Pedometer.getMessage(response);
			return message;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Pedometer editPedometer(String token,
			String serialNumberString, String firmwareRevisionString,
			Long linkedTime, Long unlinkedTime, Long lastSyncedTime,
			String localId, String serverId, long updatedAt) {
		String url = baseAddress + "pedometer";
		BaseParams request = new BaseParams();
		request.addHeader("auth_token", token);
		Pedometer pedometer = new Pedometer(serialNumberString,
				firmwareRevisionString, linkedTime, unlinkedTime,
				lastSyncedTime, localId, serverId, updatedAt);
		request.addObjectParam("pedometer", pedometer.toJson().toString());
		ServiceResponse response;
		try {
			response = MVPApi.post(url, port, request);
			Pedometer result = Pedometer.getPedometer(response);
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

    public static void main(String[] args) throws JSONException {
    	AccountResult r1 = MVPApi.signIn("tung@misfitwearables.com", "qwerty1",
    			"somelocal id");
    	AccountResult r2 = MVPApi.signIn("keepmesignedin@email.com", "qwerty1",
    			"somelocal id");
    	
    	List<GraphItem> graphItems = MVPApi.getGraphItems(r1.token,
    			1370070000,
    			1370073600,
    			631152000);
    	MVPApi.getGraphItems(r2.token,
    			1370070000,
    			1370073600,
    			631152000);
    	MVPApi.createGraphItems(r2.token, graphItems);
    	MVPApi.getGraphItems(r2.token,
    			1370070000,
    			1370073600,
    			631152000);
    }
}
