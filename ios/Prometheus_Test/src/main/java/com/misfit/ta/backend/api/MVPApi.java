package com.misfit.ta.backend.api;

import static com.google.resting.component.EncodingTypes.UTF8;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.Resting;
import com.google.resting.component.content.IContentData;
import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.method.post.PostHelper;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.data.AccountResult;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

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
	static public void createProfile()
	{
		
	}
	
	
	// test
	static public void test()
	{
		String token = "nqqHMupEiCv8iS7UuoRp";
		
    	// prepare
		String url = baseAddress + "profile";
		
    	BaseParams requestInf = new BaseParams();
    	requestInf.addHeader("auth_token", token);
    	
    	// post and recieve raw data
    	ServiceResponse response = MVPApi.get(url, port, requestInf);
    	
    	// format data
    	//BaseResult result = new BaseResult(response);
    	//return result;
	}
	
	public static void main(String[] args)
	{		
		//MVPApi.signIn("nga3@misfitwearables.com", "password1", "f230d0c4e69f08cb31e8535f5b512ed7c140289b");
		MVPApi.test();
	}
	
}
