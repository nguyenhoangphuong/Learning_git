package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.json.JSONException;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.server.ServerHelper;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws IOException, JSONException {

//		NotificationEndpointServer.onNotificationReceived = new BasicEvent<Void>() {
//			
//			@Override
//			public Void call(Object sender, Object arguments) {
//		
//				NotificationEndpointServer server = (NotificationEndpointServer)sender;
//				String body = (String) arguments;
//				
//				logger.info("Sender: " + server.context.getRequest().getBaseUri());
//				logger.info("Body: " + body);
//				
//				return null;
//			}
//		};

//		ServerHelper.startNotificationEndpointServer("http://localhost:8998/");
//		ServerHelper.startNotificationEndpointServer("http://localhost:8999/");
		MVPApi.signUp("nhhai16991@gmail.com", "qqqqqq");
	}

}