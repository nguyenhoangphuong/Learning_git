package com.misfit.ta.backend.api.metawatch;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.RequestHelper;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

public class MetawatchApi extends MVPApi{
	 // logger
    protected static Logger logger = Util.setupLogger(MetawatchApi.class);
    
    // fields
    public static String baseAddress = Settings.getValue("MVPMetawatchApiAddress");
    public static Integer port = Settings.getValue("MVPBackendPort") == null ? null : Integer.parseInt(Settings
            .getValue("MVPBackendPort"));
    
    
    public static BaseResult registerMetawatch(String token, String userID){
    	String url = baseAddress + "register/metawatch";
    	BaseParams requestInfo = new MetawatchBaseParams();
    	String userAgent = "Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
    	requestInfo.addHeader("User-Agent", userAgent);
    	
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
    
    public static void main (String[] args) {
        registerMetawatch("mytoken", "thinh");
//        String email = "test"+ System.currentTimeMillis()+ "@misfitqa.com";
//        System.out.println("LOG [MetawatchApi.main]: email: " + email);
//        signUpMetawatch(email, "misfit1");
        
    }
}
