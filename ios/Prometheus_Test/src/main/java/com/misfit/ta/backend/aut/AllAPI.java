package com.misfit.ta.backend.aut;

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
import com.misfit.ta.backend.data.AuthToken;
import com.misfit.ta.backend.data.SignInData;
import com.misfit.ta.backend.data.SyncData;
import com.misfit.ta.backend.rest.MVPRest;
import com.misfit.ta.backend.rest.SignInRest;
import com.misfit.ta.backend.rest.SignUpRest;
import com.misfit.ta.backend.rest.SyncRest;

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
    	MVPRest rest = null;
    	String email = null;
    	
    	// set supported version to 4 and server to 4
    	setSupportedVersions("3");
    	setCurrentServer(4);
    	email = getUniqueEmail();
    	
    	rest = signUp(email, "misfit1");
    	logger.info(rest.statusCode());
    	
    	rest = logIn(email, "misfit1");
    	logger.info(rest.statusCode());
	}

    public static void main(String[] args) 
    {
    	test();
    }
}