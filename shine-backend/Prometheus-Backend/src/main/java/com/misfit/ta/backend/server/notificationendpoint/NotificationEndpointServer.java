package com.misfit.ta.backend.server.notificationendpoint;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.base.BasicEvent;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;


@Path("/")
public class NotificationEndpointServer {
	
	protected static Logger logger = Util.setupLogger(NotificationEndpointServer.class);
	
	// event when server receive notification
	public static BasicEvent<Void> onNotificationReceived;
	public static String KeyStoreFilePath = "keys/star_misfit_server_keystore.jks";
	public static String KeyStorePassword = "importkey";
	public static String TrustStoreFilePath = "keys/truststore.ts";
	public static String TrustStorePassword = "User@123";
	
	
	// http context
	@Context
	public HttpContext context;
	
	
	// routes
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    public String doGet() {
        return "Hello world!";
    }
	
    @POST
    public String doPost(String body) {

    	HttpRequestContext request = context.getRequest();
    	logger.info("===================================================================");
    	logger.info(request.getBaseUri() );
    	logger.info("Body:\n" + body + "\n\n");
    	
    	try {
    		
    		// if subscribe url is available, try to confirm it
			JSONObject jsonObj = new JSONObject(body);
			if (!jsonObj.isNull("SubscribeURL")) {
				String subcriptionUrl = jsonObj.getString("SubscribeURL");
				OpenAPI.confirmSubcription(subcriptionUrl);
				return "Message received: " + body;
			}
			
			// if message field is available, it is considered a notification
			if (!jsonObj.isNull("Message")) {
				
				String message = jsonObj.getString("Message");

				if(onNotificationReceived != null) {
		    		onNotificationReceived.call(this, message);
		    	}
			}
			
		} catch (JSONException e) {
		}
    	
    	return "Message received: " + body;
    }

}
