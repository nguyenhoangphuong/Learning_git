package com.misfit.ta.backend.server.notificationendpoint;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.ResultLogger;


@Path("/")
public class NotificationEndpointServer {
	
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    public String doGet() {
        return "Hello world!";
    }
	
    @POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void doPost(String body) {
        
    	ResultLogger logger = ResultLogger.getLogger("notification_endpoint");
    	logger.log(body);
    	System.out.println(body);
    	
    	try {
			JSONObject jsonObj = new JSONObject(body);
	    	String subcriptionUrl = jsonObj.getString("SubscribeURL");
			OpenAPI.confirmSubcription(subcriptionUrl);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }

}
