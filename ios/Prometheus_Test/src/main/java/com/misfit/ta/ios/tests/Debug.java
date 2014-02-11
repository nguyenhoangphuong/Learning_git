package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.server.ServerHelper;
import com.misfit.ta.backend.server.notificationendpoint.NotificationEndpointServer;
import com.misfit.ta.base.BasicEvent;
import com.misfit.ta.utils.TextTool;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws IOException {

		NotificationEndpointServer.onNotificationReceived = new BasicEvent<Void>() {
			
			@Override
			public Void call(Object sender, Object arguments) {
		
				NotificationEndpointServer server = (NotificationEndpointServer)sender;
				String body = (String) arguments;
				
				logger.info("Sender: " + server.context.getRequest().getBaseUri());
				logger.info("Body: " + body);
				
				return null;
			}
		};
		
		ServerHelper.startNotificationEndpointServer("http://localhost:8998/");
		ServerHelper.startNotificationEndpointServer("http://localhost:8999/");
	}

}