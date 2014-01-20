package com.misfit.ta.backend.aut.server.server1;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


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
        
    	System.out.println(body);
    }

}
