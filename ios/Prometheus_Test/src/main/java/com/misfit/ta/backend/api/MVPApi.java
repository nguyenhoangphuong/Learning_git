package com.misfit.ta.backend.api;

import static com.google.resting.component.EncodingTypes.UTF8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.Resting;
import com.google.resting.component.content.IContentData;
import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONException;
import com.google.resting.method.post.PostHelper;
import com.google.resting.method.put.PutHelper;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.data.*;

public class MVPApi 
{
	
	// logger
	private static Logger logger = Util.setupLogger(MVPApi.class);
	
	
	// fields
    static public String baseAddress = Settings.getValue("MVPBackendBaseAddress");
    static public int port = Integer.parseInt(Settings.getValue("MVPBackendPort"));

    
    // helpers
    static private ServiceResponse request(String type, String url, int port, BaseParams requestInf)
    {
    	// log address
    	logger.info(type.toUpperCase() + ": " + url + " - port: " + port);
    	
    	// wrapper send request
    	ServiceResponse response = null;
    	
    	if(type == "post")
    		response = PostHelper.post(url, port, UTF8, requestInf.params, requestInf.headers);
    	else if(type == "get")
    		response = Resting.get(url, port, requestInf.params, UTF8, requestInf.headers);
    	else if(type == "put")
    		response = PutHelper.put(url, UTF8, port, requestInf.params, requestInf.headers);
    	
    	// log result
    	IContentData rawData = response.getContentData();
    	logger.info("Raw Data: " + rawData + "\n");
    	
    	return response;	
    }
    
    static private ServiceResponse post(String url, int port, BaseParams requestInf)
    {
    	return request("post", url, port, requestInf);
    }
    
    static private ServiceResponse get(String url, int port, BaseParams requestInf)
    {
    	return request("get", url, port, requestInf);
    }
    
    static private ServiceResponse put(String url, int port, BaseParams requestInf)
    {
    	return request("put", url, port, requestInf);
    }
    
    static public String generateUniqueEmail()
    {
    	return "auto_generated_" + System.currentTimeMillis() + "@qa.com";
    }
    
		
    // account apis
    static private AccountResult sign(String email, String password, String udid, String shortUrl)
	{
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
    	
    	// format data
    	AccountResult result = new AccountResult(response);
    	return result;
	}
    
	static public AccountResult signIn(String email, String password, String udid)
	{
		return sign(email, password, udid, "login");
	}
	
	static public AccountResult signUp(String email, String password, String udid)
	{
		return sign(email, password, udid, "signup");
	}
	
	static public BaseResult signOut(String token)
	{
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
			Integer unit, Integer gender, Long dateOfBirth, Integer goalLevel, String trackingDeviceId, 
			String localId, String latestVersion, Long updatedAt)
	{
		// build json object string

    	JSONBuilder json = new JSONBuilder();
    	if(name != null) 			json.addValue("name", name);
    	if(weight != null) 			json.addValue("weight", weight);
    	if(height != null) 			json.addValue("height", height);
    	if(unit != null)			json.addValue("unit", unit);
    	if(gender != null) 			json.addValue("gender", gender);
    	if(dateOfBirth != null) 	json.addValue("dateOfBirth", dateOfBirth);
    	if(goalLevel != null) 		json.addValue("goalLevel", goalLevel);
    	if(trackingDeviceId != null)json.addValue("trackingDeviceId", trackingDeviceId);
    	if(localId != null) 		json.addValue("localId", localId);
    	if(latestVersion != null) 	json.addValue("latestVersion", latestVersion);
    	if(updatedAt != null) 		json.addValue("updatedAt", updatedAt);
    	
    	BaseParams requestInf = new BaseParams();
    	requestInf.addHeader("auth_token", token);
    	requestInf.addParam("profile", json.toJSONString());
    	
		return requestInf;
	}

	static public ProfileResult createProfile(String token, ProfileResult.ProfileData data)
	{
    	// prepare
		String url = baseAddress + "profile";
		
		BaseParams requestInf = createProfileParams(token, data.name, data.weight, data.height, data.unit,
				data.gender, data.dateOfBirth, data.goalLevel, data.trackingDeviceId, data.localId, 
				data.latestVersion, null);
    	
    	// post and receive raw data
    	ServiceResponse response = MVPApi.post(url, port, requestInf);
    	
    	// format data
    	ProfileResult result = new ProfileResult(response);
    	return result;
	}

	static public ProfileResult getProfile(String token)
	{
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
	
	static public ProfileResult updateProfile(String token, ProfileResult.ProfileData data, String id)
	{
		logger.info("Id: " + id + ", Updated at: " + data.updatedAt);
    	// prepare
		String url = baseAddress + "profile";
		
		BaseParams requestInf = createProfileParams(token, data.name, data.weight, data.height, data.unit, 
				data.gender, data.dateOfBirth, data.goalLevel, 
				data.trackingDeviceId, data.localId, data.latestVersion, data.updatedAt);
    	
		requestInf.addParam("id", id);
    	
    	// post and recieve raw data
    	ServiceResponse response = MVPApi.put(url, port, requestInf);
    	
    	// format data
    	ProfileResult result = new ProfileResult(response);
    	return result;
	}
	
	
	// goal apis
	static private BaseParams createGoalParams(String token, Double goalValue, Long startTime, Long endTime, 
			Integer absoluteLevel,Integer userRelativeLevel, Integer timeZoneOffsetInSeconds, 
			String[] progressValuesInMinutesNSData, String localId, Long updatedAt)
	{
		// build json object string
    	JSONBuilder json = new JSONBuilder();
    	if(goalValue != null) 					json.addValue("goalValue", goalValue);
    	if(startTime != null) 					json.addValue("startTime", startTime);
    	if(endTime != null) 					json.addValue("endTime", endTime);
    	if(absoluteLevel != null)				json.addValue("absoluteLevel", absoluteLevel);
    	if(userRelativeLevel != null) 			json.addValue("userRelativeLevel", userRelativeLevel);
    	if(timeZoneOffsetInSeconds != null) 	json.addValue("timeZoneOffsetInSeconds", timeZoneOffsetInSeconds);
    	if(progressValuesInMinutesNSData != null)	json.addValue("progressValuesInMinutesNSData", Arrays.toString(progressValuesInMinutesNSData));
    	if(localId != null) 					json.addValue("localId", localId);
    	if(updatedAt != null) 					json.addValue("updatedAt", updatedAt);
    	
    	BaseParams requestInf = new BaseParams();
    	requestInf.addHeader("auth_token", token);
    	requestInf.addParam("goal", json.toJSONString());
    	
		return requestInf;
	}

	static public GoalsResult searchGoal(String token, Long startTime, Long endTime, Long modifiedSince)
	{
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
	
	static public GoalsResult getGoal(String token, String serverId)
	{
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
	
	static public GoalsResult createGoal(String token, Double goalValue, Long startTime, Long endTime, 
			Integer absoluteLevel,Integer userRelativeLevel, Integer timeZoneOffsetInSeconds, 
			String[] progressValuesInMinutesNSData, String localId)
	{
    	// prepare
		String url = baseAddress + "goals";
		
		BaseParams requestInf = createGoalParams(token, goalValue, startTime, endTime, absoluteLevel, 
				userRelativeLevel, timeZoneOffsetInSeconds,
				progressValuesInMinutesNSData, localId, null);
    	
    	// post and receive raw data
    	ServiceResponse response = MVPApi.post(url, port, requestInf);
    	
    	// format data
    	GoalsResult result = new GoalsResult(response);
    	return result;
	}
	
	static public GoalsResult updateGoal(String token, Long updatedAt, Double goalValue, Long startTime, Long endTime, 
			Integer absoluteLevel, Integer userRelativeLevel, Integer timeZoneOffsetInSeconds, 
			String[] progressValuesInMinutesNSData, String localId)
	{
    	// prepare
		String url = baseAddress + "profile";
		
		BaseParams requestInf = createGoalParams(token, goalValue, startTime, endTime, absoluteLevel, 
				userRelativeLevel, timeZoneOffsetInSeconds,
				progressValuesInMinutesNSData, localId, updatedAt);
		
    	// post and recieve raw data
    	ServiceResponse response = MVPApi.put(url, port, requestInf);
    	
    	// format data
    	GoalsResult result = new GoalsResult(response);
    	return result;
	}
	
	static public GoalsResult updateGoal(String token, GoalsResult.Goal goal)
	{
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
	static public ActivityResult getActivity(String token, Object id)
	{
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
	
	static public List<ActivityResult> searchActivity(
			String token,
			Object startTime,
			Object endTime,
			Object modifiedSince) throws JSONException
	{
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
    	ArrayList<ActivityResult> activities =
    			ActivityResult.getActivityResults(response);
    	
    	return activities;
	}
	
	static public List<ActivityResult> createActivities(
			String token,
			List<ActivityResult> activities,
			Object serverId,
			Object clientId,
			Object updatedAt) throws JSONException
	{
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
	
	
	static public void removeUser(String id)
	{
		BaseParams requestInf = new BaseParams();
		requestInf.addParam("authenticity_token", "ki5608apM0mqEpFwvNW6i8Czu6tktVT0+UlWWVhu0Mg");
		MVPApi.post("https://staging-api.misfitwearables.com/shine/v6/admin/delete_user?id=" + id, 443, requestInf);
	}
	
	public static void main(String[] args)
	{	
		MVPApi.removeUser("5142d9a6caedd02bee000062");
	}
}

