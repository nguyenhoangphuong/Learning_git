package com.misfit.ta.backend.rest;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.misfit.ta.backend.data.AuthToken;
import com.misfit.ta.backend.data.SignInData;
import com.misfit.ta.backend.data.SyncData;

public class SignInRest extends MVPRest {

    private Logger logger = Util.setupLogger(MVPRest.class);

    public SignInRest(SignInData userData) {

        super(userData);
        apiUrl = "login/";
    }

    @Override
    public void formatRequest() {
    	
        SignInData data = (SignInData) requestObj;
        params.add("email", data.email);
        params.add("password", data.password);
    }

    @Override
    public void formatResponse() {

        logger.debug("Response content: " + contentData);
        try 
        {
            JSONObject json = (JSONObject) JSONSerializer.toJSON(contentData.toString());
        	
            // token and type 
            String token = json.getString("auth_token");
            String type = json.getString("type");
            
            if(apiUrl == "login/")
            {
            	// get objects
                String s = contentData.toString();
                int start = s.indexOf("{\"profile\":");
                int end = s.indexOf("\"last_successfully_synced\":");
                
                String objects = s.substring(start, end - 1);
	            Double timestamp = json.getDouble("last_successfully_synced");
	            
	            SyncData data = new SyncData(timestamp, objects);
	            responseObj = new AuthToken(token, type, data);
            }
            else
            {
            	SyncData data = new SyncData(0, "");
            	responseObj = new AuthToken(token, type, data);
            }
            
            if (response.getStatusCode() !=200) {
                System.out.println("\n\n\nLOG [SignInRest.formatResponse]: =========================== ERROR");
            }
        } 
        catch (Exception e) {
            responseObj = new AuthToken("", "", null);
        }

    }
}
