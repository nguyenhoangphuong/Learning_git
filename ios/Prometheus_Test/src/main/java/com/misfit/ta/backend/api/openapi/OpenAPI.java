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
	
	public static String RESOURCE_DEVICES = "Devices";
	public static String RESOURCE_PROFILE = "Profile";
	
	
	// fields
	public static String resourceServerBaseAddress = Settings.getValue("MVPOpenAPIResourceServerBaseAddress");
	public static int resourceServerPort = Integer.parseInt(Settings.getValue("MVPOpenAPIResourceServerPort"));
	
	public static String subscribeServerBaseAddress = Settings.getValue("MVPOpenAPISubscribeServerBaseAddress");
	public static int subscribeServerPort = Integer.parseInt(Settings.getValue("MVPOpenAPISubcribeServerPort"));
	
	public static String authenticateServerBaseAddress = Settings.getValue("MVPOpenAPIAuthenticateServerBaseAddress");
	public static int authenticateServerPort = Integer.parseInt(Settings.getValue("MVPOpenAPIAuthenticateServerPort"));
	
	
	// authentication and athorization
	public static BaseResult getLogIn() {
		
		String url = authenticateServerBaseAddress + "login";
		
		BaseParams requestInf = new BaseParams();

		ServiceResponse response = get(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult dialogOAuth(String responseType, String clientKey, 
			String redirectUrl, String scope, String state, String cookie) {
		
		String url = authenticateServerBaseAddress + "dialog/authorize?" +
				(responseType == null ? "" : ("response_type=" + responseType)) +
				(clientKey == null ? "" : ("&client_id=" + clientKey)) +
				(redirectUrl == null ? "" : ("&redirect_uri=" + redirectUrl)) +
				(scope == null ? "" : ("&scope=" + scope)) +
				(state == null ? "" : ("&state=" + state));
		
		BaseParams requestInf = new BaseParams();
		if(cookie != null)
			requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = get(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult loginUserSession(String email, String password, String cookie) {
		
		String url = authenticateServerBaseAddress + "users/session";
		
		BaseParams requestInf = new BaseParams();
		requestInf.addParam("email", email);
		requestInf.addParam("password", password);
		
		if(cookie != null)
			requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = post(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult dialogOAuthDecision(String transactionId, String cookie) {
		
		String url = authenticateServerBaseAddress + "dialog/authorize/decision";
		
		BaseParams requestInf = new BaseParams();
		requestInf.addParam("transaction_id", transactionId);
		if(cookie != null)
			requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = post(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult oauthToken(String code, String clientKey, String clientSecret, 
			String redirectUrl, String cookie) {
		
		String url = authenticateServerBaseAddress + "dialog/authorize/decision";
		BaseParams requestInf = new BaseParams();
		
		requestInf.addParam("grant_type", "authorization_code");
		
		if(code != null)
			requestInf.addParam("code", code);
		
		if(clientKey != null)
			requestInf.addParam("client_id", clientKey);
		
		if(clientSecret != null)
			requestInf.addParam("client_secret", clientSecret);
		
		if(redirectUrl != null)
			requestInf.addParam("redirect_uri", redirectUrl);
		
		if(cookie != null)
			requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = post(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	
	private static String getTransactionId(BaseResult result) {
		
		String rawData = result.rawData;
		String startString = "<input name=\"transaction_id\" type=\"hidden\" value=\"";
		int startPos = rawData.indexOf(startString);
		int endPos = rawData.indexOf("\"", startPos + startString.length());
		
		String id = rawData.substring(startPos + startString.length(), endPos);

		return id;
	}

	public static String getAccessToken(String username, String password, String scope, String clientKey, String returnUrl) {
		
		BaseResult result0 = getLogIn();
		loginUserSession(username, password, result0.getHeaderValue("Set-Cookie"));
		BaseResult result2 = dialogOAuth(RESPONSE_TYPE_TOKEN, clientKey, returnUrl, scope, null, result0.getHeaderValue("Set-Cookie"));
		BaseResult result3 = dialogOAuthDecision(getTransactionId(result2), result0.getHeaderValue("Set-Cookie"));
		
		String location = result3.getHeaderValue("Location");
		int startPos = location.indexOf("access_token=") + "access_token=".length();
		int endPos = location.indexOf("&", startPos);
		
		if(endPos < 0)
			return location.substring(startPos);
		
		return location.substring(startPos, endPos);
	}
	
	public static String getCode(String username, String password, String scope, String clientKey, String returnUrl) {
		
		BaseResult result0 = getLogIn();
		loginUserSession(username, password, result0.getHeaderValue("Set-Cookie"));
		BaseResult result2 = dialogOAuth(RESPONSE_TYPE_CODE, clientKey, returnUrl, scope, null, result0.getHeaderValue("Set-Cookie"));
		BaseResult result3 = dialogOAuthDecision(getTransactionId(result2), result0.getHeaderValue("Set-Cookie"));
		
		String location = result3.getHeaderValue("Location");
		int startPos = location.indexOf("code=") + "code=".length();
		int endPos = location.indexOf("&", startPos);
		
		if(endPos < 0)
			return location.substring(startPos);
		
		return location.substring(startPos, endPos);
	}
	
	
	// resource apis
	public static BaseResult getProfile(String accessToken, String userId) {
		
		String url = resourceServerBaseAddress + "user/" + userId + "/profile";

		BaseParams requestInf = new BaseParams();
		if(accessToken != null)
			requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getDevice(String accessToken, String userId) {
		
		String url = resourceServerBaseAddress + "user/" + userId + "/device";

		BaseParams requestInf = new BaseParams();
		if(accessToken != null)
			requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getGoals(String accessToken, String userId, String startDay, String endDate) {
		
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/goals?" +
				(startDay == null ? "" : ("start_date=" + startDay)) +
				(endDate == null ? "" : ("&end_date=" + endDate));

		BaseParams requestInf = new BaseParams();
		if(accessToken != null)
			requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getSummary(String accessToken, String userId, String startDay, String endDate) {
		
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/summary?" +
				(startDay == null ? "" : ("start_date=" + startDay)) +
				(endDate == null ? "" : ("&end_date=" + endDate));

		BaseParams requestInf = new BaseParams();
		if(accessToken != null)
			requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getSessions(String accessToken, String userId, String startDay, String endDate) {
		
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/sessions?" +
				(startDay == null ? "" : ("start_date=" + startDay)) +
				(endDate == null ? "" : ("&end_date=" + endDate));

		BaseParams requestInf = new BaseParams();
		if(accessToken != null)
			requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getSleeps(String accessToken, String userId, String startDay, String endDate) {
		
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/sleeps?" +
				(startDay == null ? "" : ("start_date=" + startDay)) +
				(endDate == null ? "" : ("&end_date=" + endDate));

		BaseParams requestInf = new BaseParams();
		if(accessToken != null)
			requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	
	
	// notification apis
	public static BaseResult subscribeNotification(String clientKey, String clientSecret, String endpoint, String resourceToSubscribe) {
		
		String url = subscribeServerBaseAddress + "subscriptions/" + resourceToSubscribe;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("access_key_id", clientKey);
		requestInf.addHeader("access_secret", clientSecret);
		requestInf.addHeader("access_token", clientKey+"|"+clientSecret);
		
		requestInf.addParam("endpoint", endpoint);

		ServiceResponse response = post(url, subscribeServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}

	public static BaseResult unsubscribeNotification(String clientKey, String clientSecret, String resourceToSubscribe) {
		
		String url = subscribeServerBaseAddress + "subscriptions/" + resourceToSubscribe;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("access_key_id", clientKey);
		requestInf.addHeader("access_secret", clientSecret);
		requestInf.addHeader("access_token", clientKey+"|"+clientSecret);

		ServiceResponse response = delete(url, subscribeServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static void confirmSubcription(String subcriptionUrl) {
		
		BaseParams requestInf = new BaseParams();
		get(subcriptionUrl, 443, requestInf);
	}
	
}

