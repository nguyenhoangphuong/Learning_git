package com.misfit.ta.backend.api.internalapi;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.RequestHelper;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

public class MVPMetawatchApi extends RequestHelper{
	 // logger
    protected static Logger logger = Util.setupLogger(MVPMetawatchApi.class);
    
    // fields
    public static String baseAddress = Settings.getValue("MVPOpenAPIMetawatchRegister");
    public static Integer port = Settings.getValue("MVPBackendPort") == null ? null : Integer.parseInt(Settings
            .getValue("MVPBackendPort"));
    
    
    public static BaseResult registerMetawatch(String token, String userID, String userAgent){
    	String url = baseAddress + "register/metawatch";
    	BaseParams requestInfo = new BaseParams();
    	requestInfo.addHeader("User-Agent", userAgent);
    	requestInfo.addHeader("auth_token", token);
    	requestInfo.addHeader("user_id", userID);
    	
    	ServiceResponse response = MVPApi.get(url, port, requestInfo);
    	return new BaseResult(response);
    }
    
    public static BaseResult signUpMetawatch(String email, String password){
    	String url = baseAddress + "signup/metawatch";
    	BaseParams requestInfo = new BaseParams();
    	requestInfo.addHeader("email", email);
    	requestInfo.addHeader("password", password);
    	
    	ServiceResponse response = MVPApi.post(url, port, requestInfo);
    	return new BaseResult(response);
    }
}
