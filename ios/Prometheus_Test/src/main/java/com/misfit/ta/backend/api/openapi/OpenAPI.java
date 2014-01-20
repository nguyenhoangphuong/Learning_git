package com.misfit.ta.backend.api.openapi;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.RequestHelper;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

public class OpenAPI extends RequestHelper {
	
	protected static Logger logger = Util.setupLogger(OpenAPI.class);
	
	// static 
	public static String RESPONSE_TYPE_CODE = "code";
	public static String RESPONSE_TYPE_TOKEN = "token";
	
	
	// fields
	public static String baseAddress = Settings.getValue("MVPOpenAPIBaseAddress");
	public static int port = Integer.parseInt(Settings.getValue("MVPOpenAPIPort"));
	
	
	// authentication and athorization
	public static BaseResult dialogOAuth(String responseType, String clientKey, 
			String redirectUrl, String state, String cookie) {
		
		return null;
	}
	
	public static BaseResult loginUserSession(String username, String password) {
		
		return null;
	}
	
	public static BaseResult dialogOAuthDecision(String cookie) {
		
		return null;
	}
	
	public static String getAccessToken(String username, String password, String scope, String clientKey) {
		
		BaseResult result1 = loginUserSession(username, password);
		BaseResult result2 = dialogOAuth(RESPONSE_TYPE_TOKEN, clientKey, null, null, result1.getHeaderValue("Set-Cookie"));
		BaseResult result3 = dialogOAuthDecision(result2.getHeaderValue("Set-Cookie"));
		
		logger.info(result3.getHeaderValue("Location"));
		return result3.getHeaderValue("Location");
	}
	
	
	// resource apis
	public static BaseResult getProfile(String accessToken, String userId) {
		
		String url = baseAddress + userId + "/profile";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getProgress(String accessToken, String userId, long fromTimestamp, long toTimestamp) {
		
		String url = baseAddress + userId + "/progress?from_date=" + fromTimestamp + 
				"&to_date=" + toTimestamp;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getActivities(String accessToken, String userId, long fromTimestamp, long toTimestamp) {
		
		String url = baseAddress + userId + "/activities?from_date=" + fromTimestamp + 
				"&to_date=" + toTimestamp;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getRecords(String accessToken, String userId) {
		
		String url = baseAddress + userId + "/records";

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}	

	public static BaseResult getRawData(String accessToken, String userId, long fromTimestamp, long toTimestamp) {
		
		String url = baseAddress + userId + "/raw_data?from_date=" + fromTimestamp + 
				"&to_date=" + toTimestamp;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}

	
	// notification apis
	public static BaseResult subscribeNotification(String clientKey, String clientSecret, String endpoint, String resourceToSubscribe) {
		
		String url = baseAddress + "/subscription/" + resourceToSubscribe;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("access_key_id", clientKey);
		requestInf.addHeader("access_secret", clientSecret);
		requestInf.addParam("endpoint", endpoint);

		ServiceResponse response = post(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}

	public static BaseResult unsubscribeNotification(String clientKey, String clientSecret, String resourceToSubscribe) {
		
		String url = baseAddress + "/subscription/" + resourceToSubscribe;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("access_key_id", clientKey);
		requestInf.addHeader("access_secret", clientSecret);

		ServiceResponse response = delete(url, port, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static String getConfirmSubscriptionUrl(BaseResult result) {
		
		Object obj = result.getJsonResponseValue("SubscribeURL");
		if(obj == null)
			return null;
		
		return (String)obj;
	}
	
	public static BaseResult confirmSubcription(String subcriptionUrl) {
		
		BaseParams requestInf = new BaseParams();
		ServiceResponse response = get(subcriptionUrl, 443, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
}

