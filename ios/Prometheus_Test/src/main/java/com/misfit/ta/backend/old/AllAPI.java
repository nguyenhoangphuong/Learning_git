package com.misfit.ta.backend.old;

import java.util.List;
import java.util.Vector;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.Resting;
import com.google.resting.component.RequestParams;
import com.google.resting.component.content.IContentData;
import com.google.resting.component.impl.BasicRequestParams;
import com.google.resting.component.impl.ServiceResponse;

public class AllAPI
{
	// fields
    private static Logger logger = Util.setupLogger(AllAPI.class);
    
    // methods
    public static void setSupportedVersions(String versions)
    {
    	// prepare 
    	String url = "https://staging-api.misfitwearables.com/shine/admin/update_valid_versions";
    	int port = 443;
    	RequestParams params = new BasicRequestParams();
    	params.add("versions", versions);
    	
    	// post
    	ServiceResponse response = Resting.post(url, port, params);
    	IContentData contentData = response.getContentData();
    	
    	// result
    	logger.info(contentData);
    }
	
    public static void setCurrentServer(int version)
	{
		MVPRest.baseAddress = "https://staging-api.misfitwearables.com/shine/v" + version + "/";
	}
	
    public static String getUniqueEmail()
    {
    	return "test" + System.currentTimeMillis() + "@qa.com";
    }
    
	public static MVPRest logIn(String usr, String pwd)
	{
		SignInData u = new SignInData(usr, pwd);
		SignInRest rest = new SignInRest(u);
		rest.post();
		
        return rest;
	}
	
	public static MVPRest signUp(String usr, String pwd)
	{
		SignInData u = new SignInData(usr, pwd);
		SignUpRest rest = new SignUpRest(u);
		rest.post();
		
        return rest;
	}
	
	public static MVPRest sync(SyncData data, AuthToken auth)
	{
		List<Header> headers = new Vector<Header>();
        BasicHeader header = new BasicHeader("auth_token", auth.token);
        headers.add(header);
        
		SyncRest rest = new SyncRest(data);
		rest.postWithHeader(headers);
		
		return rest;
	}
	

	
	// test methods
	public static void test()
	{
    	// prepare 
    	String url = "https://staging-api.misfitwearables.com/shine/admin/update_valid_versions";
    	int port = 443;
    	RequestParams params = new BasicRequestParams();
    	String versions = "1.0";
    	params.add("versions", versions);
    	
    	// post
    	ServiceResponse response = Resting.post(url, port, params);
    	IContentData contentData = response.getContentData();
    	
    	// result
    	logger.info(contentData);
	}
}