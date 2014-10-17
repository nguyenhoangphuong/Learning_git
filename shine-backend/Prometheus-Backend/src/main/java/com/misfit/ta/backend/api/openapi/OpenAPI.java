package com.misfit.ta.backend.api.openapi;

import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.RequestHelper;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.openapi.OpenAPIThirdPartyApp;

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
	
	public static String NOTIFICATION_RESOURCE_PROFILE = "profiles";
	public static String NOTIFICATION_RESOURCE_DEVICE = "devices";
	public static String NOTIFICATION_RESOURCE_GOAL = "goals";
	public static String NOTIFICATION_RESOURCE_SESSION = "sessions";
	public static String NOTIFICATION_RESOURCE_SLEEP = "sleeps";
	
	
	// fields
	public static String resourceServerBaseAddress = Settings.getValue("MVPOpenAPIResourceServerBaseAddress");
	public static Integer resourceServerPort = Settings.getValue("MVPOpenAPIResourceServerPort") == null ? null :
		Integer.parseInt(Settings.getValue("MVPOpenAPIResourceServerPort"));
	
	public static String subscribeServerBaseAddress = Settings.getValue("MVPOpenAPISubscribeServerBaseAddress");
	public static Integer subscribeServerPort = Settings.getValue("MVPOpenAPISubcribeServerPort") == null ? null :
		Integer.parseInt(Settings.getValue("MVPOpenAPISubcribeServerPort"));
	
	public static String authenticateServerBaseAddress = Settings.getValue("MVPOpenAPIAuthenticateServerBaseAddress");
	public static Integer authenticateServerPort = Settings.getValue("MVPOpenAPIAuthenticateServerPort") == null ? null :
		Integer.parseInt(Settings.getValue("MVPOpenAPIAuthenticateServerPort"));
	
	
	// dev portal
	public static BaseResult formLogIn() {
		
		String url = authenticateServerBaseAddress + "login";
		
		BaseParams requestInf = new BaseParams();
		requestInf.removeHeader("Content-Type");

		ServiceResponse response = get(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult submitLogInForm(String email, String password, String cookie) {
		
		String url = authenticateServerBaseAddress + "users/session";
		
		BaseParams requestInf = new BaseParams();
		
		requestInf.addParam("email", email);
		requestInf.addParam("password", password);
		requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = post(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		result.appendCookie(cookie);
		
		return result;
	}
	
	public static BaseResult logIn(String email, String password) {
		
		// get log in form to start new session
		BaseResult result = formLogIn();
		String cookie = result.getHeaderValue("Set-Cookie");
		
		
		// get the cookie and store it, fill log in form and submit
		return submitLogInForm(email, password, cookie);
	}
		
	public static BaseResult logOut(String cookie) {
		
		String url = authenticateServerBaseAddress + "logout";
		
		BaseParams requestInf = new BaseParams();
		requestInf.removeHeader("Content-Type");
		
		if(cookie != null)
			requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = get(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult formSignUp() {
		
		String url = authenticateServerBaseAddress + "signup";
		
		BaseParams requestInf = new BaseParams();
		requestInf.removeHeader("Content-Type");

		ServiceResponse response = get(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult submitSignUpForm(String email, String password, String cookie) {
		
		String url = authenticateServerBaseAddress + "users";
		
		BaseParams requestInf = new BaseParams();
		
		requestInf.addParam("email", email);
		requestInf.addParam("password", password);
		requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = post(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		result.appendCookie(cookie);
		
		return result;
	}
	
	public static BaseResult signUp(String email, String password) {
		
		// get sign up form to start a new session
		BaseResult result = formSignUp();
		String cookie = result.getHeaderValue("Set-Cookie");
		
		
		// store cookie, fill the form and submit
		return submitSignUpForm(email, password, cookie);
	}
	
	public static BaseResult formRegisterApp(String cookie) {
		
		String url = authenticateServerBaseAddress + "clients/regnew";
		
		BaseParams requestInf = new BaseParams();
		requestInf.removeHeader("Content-Type");
		
		requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = get(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult submitRegisterAppForm(OpenAPIThirdPartyApp app, String cookie) {
		
		String url = authenticateServerBaseAddress + "clients";
		
		BaseParams requestInf = new BaseParams();
		
		requestInf.addHeader("Cookie", cookie);
		requestInf.addParam("name", app.getName());
		
		ServiceResponse response = post(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult registerApp(OpenAPIThirdPartyApp app, String cookie) {
		
		// fill the form
		return submitRegisterAppForm(app, cookie);
	}
	
	
	// authentication and athorization	
	public static BaseResult authorizationDialog(String responseType, String clientKey, 
			String redirectUrl, String scope, String state, String cookie) {
		
		String url = authenticateServerBaseAddress + "dialog/authorize?" +
				(responseType == null ? "" : ("response_type=" + responseType)) +
				(clientKey == null ? "" : ("&client_id=" + clientKey)) +
				(scope == null ? "" : ("&scope=" + scope)) +
				(redirectUrl == null ? "" : ("&redirect_uri=" + redirectUrl)) +
				(state == null ? "" : ("&state=" + state));
		
		BaseParams requestInf = new BaseParams();
		if(cookie != null)
			requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = get(url, authenticateServerPort, requestInf);
		if (response.getStatusCode() == 302) {
		    
		}
		else if (response.getStatusCode() != 200) {
		    throw new IllegalStateException("Cant get auth dialog: error code = " + response.getStatusCode());
		}
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
		
		if(redirectUrl != null) {
			requestInf.addParam("redirect_uri", redirectUrl);
			System.out.println("LOG [OpenAPI.exchangeCodeForToken]: -------- 1");
		}
		
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
	
	
	// helpers
	public static String getAccessToken(String username, String password, String scope, String clientKey, String returnUrl) {
		
		BaseResult result = OpenAPI.logIn(username, password);
		String cookie = result.cookie;
		
		result = authorizationDialog(RESPONSE_TYPE_TOKEN, clientKey, returnUrl, scope, null, cookie);
		
//		if (result.rawData.contains("Request for permission")) {
//            result = OpenAPI.authorizationConfirm(OpenAPI.parseTransactionId(result), cookie);
//            String location = OpenAPI.parseReturnUrl(result);
//            String token = OpenAPI.parseAccessToken(result);
//        } else {
//        }
		
		if (result.rawData.contains("Request for permission")) {
            result = OpenAPI.authorizationConfirm(OpenAPI.parseTransactionId(result), cookie);
            String location = OpenAPI.parseReturnUrl(result);
            String token = OpenAPI.parseAccessToken(result);
            return parseCode(result);
            
        } else {
//            Assert.assertTrue(result.statusCode == 200 || result.statusCode == 304, "Error code is: " + result.statusCode);
            String location = OpenAPI.parseReturnUrl(result);
            return parseCode(result);
        }
	}
	
	public static String getCode(String username, String password, String scope, String clientKey, String returnUrl) {
		
		BaseResult result = OpenAPI.logIn(username, password);
		String cookie = result.cookie;
		
		result = authorizationDialog(RESPONSE_TYPE_CODE, clientKey, returnUrl, scope, null, cookie);
		
		if (result.rawData.contains("Request for permission")) {
            result = OpenAPI.authorizationConfirm(OpenAPI.parseTransactionId(result), cookie);
            String location = OpenAPI.parseReturnUrl(result);
            String token = OpenAPI.parseAccessToken(result);
            return parseCode(result);
            
        } else {
//            Assert.assertTrue(result.statusCode == 200 || result.statusCode == 304, "Error code is: " + result.statusCode);
            String location = OpenAPI.parseReturnUrl(result);
            return parseCode(result);
        }
		
		
//		BaseResult result3 = authorizationConfirm(parseTransactionId(result2), cookie);
		
	}
	
	public static List<OpenAPIThirdPartyApp> getAllApps(String cookie) {
		
		// get all apps
		String url = authenticateServerBaseAddress + "clients";

		BaseParams requestInf = new BaseParams();
		requestInf.addBasicAuthorizationHeader("misfit", "User@123");
		requestInf.addHeader("Cookie", cookie);

		ServiceResponse response = get(url, authenticateServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		List<OpenAPIThirdPartyApp> apps = OpenAPIThirdPartyApp.getAppsFromResponse(result.response);
		
		return apps;
	}
	
	public static OpenAPIThirdPartyApp getApp(String appId, String cookie) {
		
		// get all apps
		List<OpenAPIThirdPartyApp> apps = getAllApps(cookie);
		
		// find app with same id and return		
		for(OpenAPIThirdPartyApp app : apps) {
			if(app.getId().equals(appId))
				return app;
		}
		
		return null;
	}
	
	
	// resource apis using access token
	public static String allScopesAsString() {
		
		return String.format("%s,%s,%s,%s,%s,%s", 
				RESOURCE_DEVICE,
				RESOURCE_PROFILE,
				RESOURCE_SUMMARY,
				RESOURCE_GOAL,
				RESOURCE_SLEEP,
				RESOURCE_SESSION);
	}
	
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
	
	public static BaseResult getGoal(String accessToken, String userId, String objectId) {
		
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/goals/" + objectId;

		BaseParams requestInf = new BaseParams();
		if(accessToken != null)
			requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getSummary(String accessToken, String userId, String startDay, String endDate) {
		
		return getSummary(accessToken, userId, startDay, endDate, false);
	}
	
	public static BaseResult getSummary(String accessToken, String userId, String startDay, String endDate, boolean detail) {
		
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/summary?" +
				(startDay == null ? "" : ("start_date=" + startDay)) +
				(endDate == null ? "" : ("&end_date=" + endDate)) +
				(detail == false ? "" : "&detail=true");

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
	
	public static BaseResult getSession(String accessToken, String userId, String objectId) {
		
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/sessions/" + objectId;

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

	public static BaseResult getSleep(String accessToken, String userId, String objectId) {
		
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/sleeps/" + objectId;

		BaseParams requestInf = new BaseParams();
		if(accessToken != null)
			requestInf.addHeader("access_token", accessToken);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	
	// resource apis using app secret and app id
	public static BaseResult getProfile(OpenAPIThirdPartyApp app, String userId) {
		
		String url = resourceServerBaseAddress + "user/" + userId + "/profile";
		String clientId = app.getClientKey();
		String clientSecret = app.getClientSecret();

		BaseParams requestInf = new BaseParams();
		if(clientId != null)
			requestInf.addHeader("app_id", clientId);
		
		if(clientSecret != null)
			requestInf.addHeader("app_secret", clientSecret);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getDevice(OpenAPIThirdPartyApp app, String userId) {
		
		String clientId = app.getClientKey();
		String clientSecret = app.getClientSecret();
		String url = resourceServerBaseAddress + "user/" + userId + "/device";

		BaseParams requestInf = new BaseParams();
		if(clientId != null)
			requestInf.addHeader("app_id", clientId);
		
		if(clientSecret != null)
			requestInf.addHeader("app_secret", clientSecret);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getGoals(OpenAPIThirdPartyApp app, String userId, String startDay, String endDate) {
		
		String clientId = app.getClientKey();
		String clientSecret = app.getClientSecret();
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/goals?" +
				(startDay == null ? "" : ("start_date=" + startDay)) +
				(endDate == null ? "" : ("&end_date=" + endDate));

		BaseParams requestInf = new BaseParams();
		if(clientId != null)
			requestInf.addHeader("app_id", clientId);
		
		if(clientSecret != null)
			requestInf.addHeader("app_secret", clientSecret);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getGoal(OpenAPIThirdPartyApp app, String userId, String objectId) {
		
		String clientId = app.getClientKey();
		String clientSecret = app.getClientSecret();
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/goals/" + objectId;

		BaseParams requestInf = new BaseParams();
		if(clientId != null)
			requestInf.addHeader("app_id", clientId);
		
		if(clientSecret != null)
			requestInf.addHeader("app_secret", clientSecret);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getSummary(OpenAPIThirdPartyApp app, String userId, String startDay, String endDate) {
		
		return getSummary(app, userId, startDay, endDate, false);
	}

	public static BaseResult getSummary(OpenAPIThirdPartyApp app, String userId, String startDay, String endDate, boolean detail) {
		
		String clientId = app.getClientKey();
		String clientSecret = app.getClientSecret();
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/summary?" +
				(startDay == null ? "" : ("start_date=" + startDay)) +
				(endDate == null ? "" : ("&end_date=" + endDate)) +
				(detail == false ? "" : "&detail=true");

		BaseParams requestInf = new BaseParams();
		if(clientId != null)
			requestInf.addHeader("app_id", clientId);
		
		if(clientSecret != null)
			requestInf.addHeader("app_secret", clientSecret);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getSessions(OpenAPIThirdPartyApp app, String userId, String startDay, String endDate) {
		
		String clientId = app.getClientKey();
		String clientSecret = app.getClientSecret();
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/sessions?" +
				(startDay == null ? "" : ("start_date=" + startDay)) +
				(endDate == null ? "" : ("&end_date=" + endDate));

		BaseParams requestInf = new BaseParams();
		if(clientId != null)
			requestInf.addHeader("app_id", clientId);
		
		if(clientSecret != null)
			requestInf.addHeader("app_secret", clientSecret);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getSession(OpenAPIThirdPartyApp app, String userId, String objectId) {
		
		String clientId = app.getClientKey();
		String clientSecret = app.getClientSecret();
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/sessions/" + objectId;

		BaseParams requestInf = new BaseParams();
		if(clientId != null)
			requestInf.addHeader("app_id", clientId);
		
		if(clientSecret != null)
			requestInf.addHeader("app_secret", clientSecret);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static BaseResult getSleeps(OpenAPIThirdPartyApp app, String userId, String startDay, String endDate) {
		
		String clientId = app.getClientKey();
		String clientSecret = app.getClientSecret();
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/sleeps?" +
				(startDay == null ? "" : ("start_date=" + startDay)) +
				(endDate == null ? "" : ("&end_date=" + endDate));

		BaseParams requestInf = new BaseParams();
		if(clientId != null)
			requestInf.addHeader("app_id", clientId);
		
		if(clientSecret != null)
			requestInf.addHeader("app_secret", clientSecret);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}

	public static BaseResult getSleep(OpenAPIThirdPartyApp app, String userId, String objectId) {
		
		String clientId = app.getClientKey();
		String clientSecret = app.getClientSecret();
		String url = resourceServerBaseAddress + "user/" + userId + "/activity/sleeps/" + objectId;

		BaseParams requestInf = new BaseParams();
		if(clientId != null)
			requestInf.addHeader("app_id", clientId);
		
		if(clientSecret != null)
			requestInf.addHeader("app_secret", clientSecret);

		ServiceResponse response = get(url, resourceServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	
	// notification apis
	public static BaseResult subscribeNotification(String clientKey, String clientSecret, String endpoint, String resourceToSubscribe) {
		
		String url = subscribeServerBaseAddress + "subscriptions/" + resourceToSubscribe;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("app_id", clientKey);
		requestInf.addHeader("app_secret", clientSecret);
		
		requestInf.addParam("endpoint", endpoint);

		ServiceResponse response = post(url, subscribeServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}

	public static BaseResult unsubscribeNotification(String clientKey, String clientSecret, String resourceToSubscribe) {
		
		String url = subscribeServerBaseAddress + "subscriptions/" + resourceToSubscribe;

		BaseParams requestInf = new BaseParams();
		requestInf.addHeader("app_id", clientKey);
		requestInf.addHeader("app_secret", clientSecret);

		ServiceResponse response = delete(url, subscribeServerPort, requestInf);
		BaseResult result = new BaseResult(response);
		
		return result;
	}
	
	public static void confirmSubcription(String subcriptionUrl) {
		
	    int port = 443;
	    if (subcriptionUrl!= null && !subcriptionUrl.contains("https")) {
	        port = 80;
	    }
		BaseParams requestInf = new BaseParams();
		get(subcriptionUrl, port, requestInf);
		
	}
	
}

