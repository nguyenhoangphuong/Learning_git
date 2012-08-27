package com.misfit.ta.gui;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class Backend {
    
  private static String baseURL = "http://staging-api.misfitwearables.com";
  private static Logger logger = Util.setupLogger(Backend.class);

   public static String submitEmail(String email) {
       WebResource r = createWebResource("/mvp1/v1/signup");
       MultivaluedMap<String, String> params = new MultivaluedMapImpl();
       params.add("email", email);
       logger.info("Submiting email: " + email);
       String response = r.queryParam("email", email).accept(
               MediaType.APPLICATION_JSON_TYPE,
               MediaType.APPLICATION_XML_TYPE).
               header("api_key", "76801581").
               post(String.class);
       logger.info("Response: " + response);
       return response;
   }
   
   
   public static boolean isEmailExisting(String email) {
       WebResource r = createWebResource("/mvp1/v1/all_users");
       logger.info("Checking email existing: " + email);
       String response = r.queryParam("email", email).accept(
               MediaType.APPLICATION_JSON_TYPE).
               header("api_key", "76801581").
               get(String.class);
       logger.info("Response: " + response);
       if (response.indexOf(email) >=0) {
          return true; 
       }
       return false;
   }
   
   private static WebResource createWebResource(String subURL) {
       Client c = Client.create();
       return  c.resource(baseURL + "/" + subURL);
   }

}
