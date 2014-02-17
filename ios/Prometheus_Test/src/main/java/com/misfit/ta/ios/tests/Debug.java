package com.misfit.ta.ios.tests;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.json.JSONException;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.data.openapi.OpenAPIThirdPartyApp;
import com.misfit.ta.backend.server.ServerHelper;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws IOException, JSONException {

		String username = "thinh+1@misfit.com";
		String password = "misfit1";
		String returnUrl = "https://www.google.com.vn/";
		
		String clientKey = Settings.getParameter("MVPOpenAPIClientID");
		String clientSecret = Settings.getParameter("MVPOpenAPIClientSecret");
		String endpoint = "http://jenkins.misfitwearables.com:8999/";
		
//		ServerHelper.startNotificationEndpointServer("http://localhost:8999/");
		
//		OpenAPI.getAccessToken(username, password, OpenAPI.allScopesAsString(),  clientKey, returnUrl);
		
//		OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, OpenAPI.NOTIFICATION_RESOURCE_DEVICE);
//		OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, OpenAPI.NOTIFICATION_RESOURCE_PROFILE);
//		OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
//		OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
//		OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, OpenAPI.NOTIFICATION_RESOURCE_SLEEP);
		
		
		OpenAPIThirdPartyApp app = new OpenAPIThirdPartyApp();
		app.setName("TestCreateApp");
		String cookie = OpenAPI.logIn("thinh+1@gmail.com", "qqqqqq").cookie;
		OpenAPI.registerApp(app, cookie);
	}

}