package com.misfit.ta.backend.api;

import static com.google.resting.component.EncodingTypes.UTF8;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.Resting;
import com.google.resting.component.content.IContentData;
import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.method.post.PostHelper;
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
    	logger.info(type.toUpperCase() + ": " + url + " - " + port);
    	
    	// wrapper send request
    	ServiceResponse response = null;
    	
    	if(type == "post")
    		response = PostHelper.post(url, port, UTF8, requestInf.params, requestInf.headers);
    	else if(type == "get")
    		response = Resting.get(url, port, requestInf.params, UTF8, requestInf.headers);	
    	
    	// log result
    	IContentData rawData = response.getContentData();
    	logger.info("Raw Data: " + rawData);
    	
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
    
		
    // account apis
    static private AccountResult sign(String email, String password, String udid, String shortUrl)
	{
    	// prepare
		String url = baseAddress + shortUrl;
		
    	BaseParams requestInf = new BaseParams();
    	requestInf.addParam("email", email);
    	requestInf.addParam("password", password);
    	requestInf.addParam("udid", udid);
    	
    	// post and recieve raw data
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
    	// prepare
		String url = baseAddress + "logout";
		
    	BaseParams requestInf = new BaseParams();
    	requestInf.addHeader("auth_token", token);
    	
    	// post and recieve raw data
    	ServiceResponse response = MVPApi.post(url, port, requestInf);
    	
    	// format data
    	BaseResult result = new BaseResult(response);
    	return result;
	}
	
	
	// profile apis
	static private BaseParams createProfileParams(String token, String name, Double weight, Double height, 
			Integer unit, Integer gender, Long dateOfBirth, Integer goalLevel, String trackingDevice, 
			String localId, String latestVersion)
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
    	if(trackingDevice != null) 	json.addValue("trackingDevice", trackingDevice);
    	if(localId != null) 		json.addValue("localId", localId);
    	if(latestVersion != null) 	json.addValue("latestVersion", latestVersion);
    	
    	BaseParams requestInf = new BaseParams();
    	requestInf.addHeader("auth_token", token);
    	requestInf.addParam("profile", json.toJSONString());
    	
		return requestInf;
	}
	
	static public ProfileResult createProfile(String token, String name, Double weight, Double height, Integer unit, 
			Integer gender, Long dateOfBirth, Integer goalLevel, String trackingDevice)
	{
    	// prepare
		String url = baseAddress + "profile";
		
		BaseParams requestInf = createProfileParams(token, name, weight, height, unit, gender, dateOfBirth,
				goalLevel, trackingDevice, null, null);
    	
    	// post and recieve raw data
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
    	
    	// post and recieve raw data
    	ServiceResponse response = MVPApi.get(url, port, requestInf);
    	
    	// format data
    	ProfileResult result = new ProfileResult(response);
    	return result;
	}
	
	static public ProfileResult updateProfile(String token, String name, Double weight, Double height, Integer unit, 
			Integer gender, Long dateOfBirth, Integer goalLevel, String trackingDevice)
	{
    	// prepare
		String url = baseAddress + "profile";
		
    	JSONBuilder json = new JSONBuilder();
    	json.addValue("name", name);
    	json.addValue("weight", weight);
    	json.addValue("height", height);
    	json.addValue("unit", unit);
    	json.addValue("gender", gender);
    	json.addValue("dateOfBirth", dateOfBirth);
    	json.addValue("goalLevel", goalLevel);
    	json.addValue("trackingDevice", trackingDevice);
    	logger.info(json.toJSONString());
    	
    	BaseParams requestInf = new BaseParams();
    	requestInf.addHeader("auth_token", token);
    	requestInf.addParam("profile", json.toJSONString());
    	
    	
    	// post and recieve raw data
    	ServiceResponse response = MVPApi.post(url, port, requestInf);
    	
    	// format data
    	ProfileResult result = new ProfileResult(response);
    	return result;
	}
	
	static public ProfileResult updateProfile(String token, ProfileResult.ProfileData data)
	{
		
		
		return null;
	}
	
	
	// test
	static public void test()
	{

	}
	
	public static void main(String[] args)
	{	
		// default fields
		String name = "Tears";
		Double weight = 68.2;
		Double height = 1.71;
		Integer unit = 1;
		Integer gender = 0;
		Long dateOfBirth = (long) 684954000;
		Integer goalLevel = 1;
		String trackingDevice = "f230d0c4e69f08cb31e8535f5b512ed7c140289b";
		
		//AccountResult r = MVPApi.signUp("qa1.3@test.com", "password1", "f230d0c4e69f08cb31e8535f5b512ed7c140289b");
		//AccountResult r = MVPApi.signIn("hn@yahoo.com", "qwerty1", "f230d0c4e69f08cb31e8535f5b512ed7c140289b");
		AccountResult r = MVPApi.signIn("qa1.1@test.com", "password1", "f230d0c4e69f08cb31e8535f5b512ed7c140289b");
		r.printKeyPairsValue();
		//MVPApi.getProfile(r.token).printKeyPairsValue();
		
		MVPApi.createProfile(r.token, name, weight, height, unit, gender, dateOfBirth, goalLevel, trackingDevice).printKeyPairsValue();
		
	}
	
}
