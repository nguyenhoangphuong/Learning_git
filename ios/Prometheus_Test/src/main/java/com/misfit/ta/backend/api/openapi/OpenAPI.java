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
	
	public static String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
	
	public static String RESOURCE_DEVICE = "Device";
	public static String RESOURCE_PROFILE = "Profile";
	public static String RESOURCE_SUMMARY = "Summary";
	public static String RESOURCE_GOAL = "Goal";
	public static String RESOURCE_SESSION = "Session";
	public static String RESOURCE_SLEEP = "Sleep";
	
	
	// fields
	public static String resourceServerBaseAddress = Settings.getValue("MVPOpenAPIResourceServerBaseAddress");
	public static int resourceServerPort = Integer.parseInt(Settings.getValue("MVPOpenAPIResourceServerPort"));
	
	public static String subscribeServerBaseAddress = Settings.getValue("MVPOpenAPISubscribeServerBaseAddress");
	public static int subscribeServerPort = Integer.parseInt(Settings.getValue("MVPOpenAPISubcribeServerPort"));
	
	public static String authenticateServerBaseAddress = Settings.getValue("MVPOpenAPIAuthenticateServerBaseAddress");
	public static int authenticateServerPort = Integer.parseInt(Settings.getValue("MVPOpenAPIAuthenticateServerPort"));
	
	
	// authentication and athorization
	public static BaseResult logInForm() {
		
		String url = authenticateServerBaseAddress + "login";
		
		BaseParams requestInf = new BaseParams();

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
	
	public static BaseResult logoutUserSession(String cookie) {
		
		String url = authenticateServerBaseAddress + "logout";
		
		BaseParams requestInf = new BaseParams();
		
		if(cookie != null)
			requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = get(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult authorizationDialog(String responseType, String clientKey, 
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
	
	public static BaseResult authorizationConfirm(String transactionId, String cookie) {
		
		String url = authenticateServerBaseAddress + "dialog/authorize/decision";
		
		BaseParams requestInf = new BaseParams();
		requestInf.addParam("transaction_id", transactionId);
		if(cookie != null)
			requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = post(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult authorizationDeny(String transactionId, String cookie) {
		
		String url = authenticateServerBaseAddress + "dialog/authorize/decision";
		
		BaseParams requestInf = new BaseParams();
		requestInf.addParam("transaction_id", transactionId);
		requestInf.addParam("cancel", "Deny");
		if(cookie != null)
			requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = post(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult exchangeCodeForToken(String grantType, String code, String clientKey, String clientSecret, 
			String redirectUrl, String cookie) {
		
		String url = authenticateServerBaseAddress + "tokens/exchange";
		BaseParams requestInf = new BaseParams();
				
		if(grantType != null)
			requestInf.addParam("grant_type", grantType);
		
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
	
	
	public static String parseTransactionId(BaseResult result) {
		
		String rawData = result.rawData;
		String startString = "<input name=\"transaction_id\" type=\"hidden\" value=\"";
		int startPos = rawData.indexOf(startString);
		int endPos = rawData.indexOf("\"", startPos + startString.length());
		
		String id = rawData.substring(startPos + startString.length(), endPos);

		return id;
	}
	
	public static String parseAccessToken(BaseResult result) {
		
		String location = result.getHeaderValue("Location");
		
		int startPos = location.indexOf("access_token=");
		if(startPos < 0)
			return null;
		startPos += "access_token=".length();
		
		int endPos = location.indexOf("&", startPos);
		if(endPos < 0)
			return location.substring(startPos);
		
		return location.substring(startPos, endPos);
	}
	
	public static String parseCode(BaseResult result) {
		
		String location = result.getHeaderValue("Location");
		
		int startPos = location.indexOf("code=");
		if(startPos < 0)
			return null;
		startPos += "code=".length();
		
		int endPos = location.indexOf("&", startPos);
		if(endPos < 0)
			return location.substring(startPos);
		
		return location.substring(startPos, endPos);
	}

	public static String parseReturnUrl(BaseResult result) {
		
		String location = result.getHeaderValue("Location");
		
		if(location.indexOf("#") >= 0)
			return location.substring(0, location.indexOf("#"));
		
		if(location.indexOf("?") >= 0)
			return location.substring(0, location.indexOf("?"));
		
		return location.substring(0, location.lastIndexOf("/") + 1);
	}
	
	
	public static String getAccessToken(String username, String password, String scope, String clientKey, String returnUrl) {
		
		BaseResult result0 = logInForm();
		loginUserSession(username, password, result0.getHeaderValue("Set-Cookie"));
		BaseResult result2 = authorizationDialog(RESPONSE_TYPE_TOKEN, clientKey, returnUrl, scope, null, result0.getHeaderValue("Set-Cookie"));
		BaseResult result3 = authorizationConfirm(parseTransactionId(result2), result0.getHeaderValue("Set-Cookie"));
		
		return parseAccessToken(result3);
	}
	
	public static String getCode(String username, String password, String scope, String clientKey, String returnUrl) {
		
		BaseResult result0 = logInForm();
		loginUserSession(username, password, result0.getHeaderValue("Set-Cookie"));
		BaseResult result2 = authorizationDialog(RESPONSE_TYPE_CODE, clientKey, returnUrl, scope, null, result0.getHeaderValue("Set-Cookie"));
		BaseResult result3 = authorizationConfirm(parseTransactionId(result2), result0.getHeaderValue("Set-Cookie"));
		
		return parseCode(result3);
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

